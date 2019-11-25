# Using Dagger For Dependency Injection(DI)

Dependency injection (DI) is a technique widely used in programming and well suited to Android development. By following the principles of DI, you lay the groundwork for a good app architecture.

Implementing dependency injection provides you with the following advantages:

	* Reusability of code.
	* Ease of refactoring.
	* Ease of testing
We'll use Dagger as the DI tool to manage dependencies

#Adding Dagger In gradle

To add Dagger to our project, open the app/build.gradle file and add the two Dagger dependencies and the kapt plugin to the top of the file.

app/build.gradle

apply plugin: 'kotlin-kapt'

...

dependencies {
    ...
    def dagger_version = "2.25.2"
    implementation "com.google.dagger:dagger:$dagger_version"
    kapt "com.google.dagger:dagger-compiler:$dagger_version"
}

Dagger is implemented using Java's annotations model. It generates code at compile-time using an annotation processor. 
Annotation processors are supported in Kotlin with the kapt compiler plugin. They are enabled by adding apply plugin: 'kotlin-kapt' to the top of the file

In the dependencies, the dagger library contains all the annotations you can use in your app and dagger-compiler is the annotation processor that will generate the code for us.


# Process
## @Inject annotation
To get Dagger know how to create instance of a class @Inject annotation used befor constructor in Kotlin.
In given Sample ItemsViewModelFactory use @Inject in it constructor to get dagger know how to create a ItemsViewModelFactory.
Its has dependency in ItemsRepository.

	```
	class ItemsViewModelFactory
	@Inject
	constructor(private val repository: ItemsRepository)
	
	```

Now Dagger knows to create instance of ItemsViewModelFactory need ItemsRepository. But still Dagger anware how to create ItemsRepository.
To get to know that We also follow the previous way that is use @Inject in construstor of ItemsRepository It has also a dependency on ItemDao.

	```

	class ItemsRepository

    	@Inject
    	constructor(private val itemDao: ItemDao)

	```

Now ItemDao is an interface which implementation is generated so Its implementation is not in my project.
To get an ItemDao I need to have a Instance of my app Databse. To get both in a package di add an dao module with annotation @Module

@Module annotated class tell Dagger how to provide the instance of an Interface or a class which is not in our project.

In the app DaoModule provide dao and db instance. Two annotation to define dependencies in Module 
	* @Provide(classes that your project doesn't own)
	* @Bind(best practice for implementing interface)

Here use @Proveide in dao module to get ItemDao and DB instance.

	```

	@Module
	class DaoModule {

    		@Provides
    		fun provideDao(db:HappyShopingDatabase):ItemDao{
        		return db.itemDao()
    		}

    		@Provides
    		fun provideDB(context: Context): HappyShopingDatabase{
        		return HappyShopingDatabase.getDatabase(context)
    		}
	}

	```

As mentioned before @Inject used in constructor of a class to infrom Dagger how to create its instance.

But what will we do when classes are initialized by system such as activity or fragment. For example any initialization code in 
activity needs to go to the onCreate method. So we can't use constructor injection (@Inject constructor()). Here we use field injection.

	```
	class ItemListFragment : Fragment() {


    		@Inject
    		lateinit var itemsViewModelFactory: ItemsViewModelFactory

		...

		override fun onAttach(context: Context) {
        		super.onAttach(context)

           	}

		...
	}

	```
To tell the dagger which object we need to be injected we need to create dagger graph and use it to inject activity or fragment.

We want Dagger to create the graph of dependencies of our project, manage them for us and be able 
to get dependencies from the graph. To make Dagger do it, we need to create an interface and annotate it with @Component. 
Dagger will create a Container. Inside di package create AppComponent.kt 

	```
	interface AppComponent {
   
    		fun inject(fragment: ItemListFragment)
	}

	```

Since Dagger has to create an instance of ItemsViewModelFactory internally, it also needs to satisfy 
ItemsViewModelFactory's dependencies (i.e. UserManager). If during this recursive process of finding
dependencies of the objects it needs to provide Dagger doesn't know how to provide a particular dependency, 
it will fail at compile time saying there's a dependency that cannot satisfy.

The dependencies which we provide through DaoModule the application graph need to know that. For that,
we iinclude AppComponent with modules parameter inside the @Component annotation as follows:

	```

	@Component(modules = [DaoModule::class])
	interface AppComponent {

    		fun inject(fragment: ItemListFragment)
	}

	```

In DaoModule to get Database Instance we need Context. Context is provided by the Android system and therefore 
constructed outside of the graph. Since Context is already available at the time we'll be creating an instance of 
the graph, we can pass it in. The way to pass it in is with a Component Factory and using the @BindsInstance annotation.


	```

	@Component(modules = [DaoModule::class])
	interface AppComponent {

		@Component.Factory
    		interface Factory{
        		fun create(@BindsInstance context: Context): AppComponent
    		}

    		fun inject(fragment: ItemListFragment)
	}

	```

We're declaring an interface called Factory annotated with @Component.Factory. Inside, there's a method that returns the 
component type (i.e. AppComponent) and has a parameter of type Context annotated with @BindsInstance.

@BindsInstance tells Dagger that it needs to add that instance in the graph and whenever Context is required, provide that instance.


> Use @BindsInstance for objects that are constructed outside of the graph (e.g. instances of Context).

#Injecting the graph in our Fragment

As we want to have dagger graph as long as the app is running that is why we create dagger graph in our Application class.

Add an instance of the dagger graph to our custom Application MyApplication

	```

	open class MyApplication: Application() {

    		val appComponent:AppComponent by lazy{
        		DaggerAppComponent.factory().create(applicationContext)
    		}
	}

	```

We can use this instance of the graph in MainActivity to make Dagger inject the fields annotated with @Inject.
We have to call the AppComponent's method that takes ItemListFragment as a parameter.

In this app get AppComponent in MainActivity and used it in ItemListFragment to Inject the fields annotated with
@Inject.

	```

	class MainActivity : AppCompatActivity() {
    		lateinit var appComponent: AppComponent


    		override fun onCreate(savedInstanceState: Bundle?) {
        		appComponent = (application as MyApplication).
            				appComponent
        		super.onCreate(savedInstanceState)
        		setContentView(R.layout.activity_main)

    		}
	}

	class ItemListFragment : Fragment() {


   		@Inject
    		lateinit var itemsViewModelFactory: ItemsViewModelFactory

		...

    		override fun onAttach(context: Context) {
        		super.onAttach(context)
        		(activity as MainActivity).appComponent.inject(this)

           	}

   		...

	}

	```



> When using Activities, inject Dagger in the Activity's onCreate method before calling super.onCreate 
> to avoid issues with fragment restoration. In super.onCreate, an Activity during the restore phase will 
> attach fragments that might want to access activity bindings.

> Dagger-injected fields cannot be private. They need to have at least package-private visibility.


# Scope

Every time we request for an instance in dagger graph it create a new instance. But some cases we need 
to have a unique instance of a dependency in container.

You can use scope annotations to limit the lifetime of an object to the lifetime of its component. 
This means that the same instance of a dependency is used every time that type needs to be provided.

To have a unique instance of a ItemsRepository when you ask for the repository in ApplicationGraph, 
use the same scope annotation for the @Component interface and ItemsRepository. You can use the 
@Singleton annotation that already comes with the javax.inject package that Dagger uses:

	```
	...
	@Singleton
	@Component(modules = [DaoModule::class])
	interface AppComponent {
		...
	}

	@Singleton
	class ItemsRepository

    		@Inject
    		constructor(private val itemDao: ItemDao){

    		...
    	}

	```

Now we can build and run the project. 
