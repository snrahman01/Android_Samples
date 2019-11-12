# Custom View
This example showing how to create custom view, following some rules:
* Extend View class
* Provide a constructor which takes attributes and parameters from xml 
* Override following methods
    * onMessure()
    * onDraw()

## Description
* Extend a View class. Name It ClockView
* Define custom attributes in <declare-styleable> resource elment. Add      <declare-styleable> into res/values/attrs.xm. When a View created from an     XML layout, all of the attributes in the XML tag are read from the resource bundle and passed into the view's constructor as an AttributeSet.
* Change onMesure() method and calculate clock diameter.
* Change onDraw() method and draw a clock view.
