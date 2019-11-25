package com.example.happyshoping.di

import android.content.Context
import com.example.happyshoping.db.HappyShopingDatabase
import com.example.happyshoping.db.dao.ItemDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaoModule {

    @Provides
    fun provideDao(db:HappyShopingDatabase):ItemDao{
        return db.itemDao()
    }

    @Singleton
    @Provides
    fun provideDB(context: Context): HappyShopingDatabase{
        return HappyShopingDatabase.getDatabase(context)
    }
}