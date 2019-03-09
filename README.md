# SoleAdapter
Data Binding based RecyclerView Adapter

Only RecyclerView adapter that you will ever need again. You don't even have to write a ViewHolder!

* Based on [**Android Data Binding**](https://developer.android.com/topic/libraries/data-binding/index.html)
* Works with [**Androidx**](https://developer.android.com/jetpack/androidx)
* Written in [**Kotlin**](http://kotlinlang.org)
* No need to write the adapter
* No need to write the viewholders
* No need to modify your model classes
* No need to notify the adapter when data set changes
* Supports custom empty list/ list end item views
* Supports multiple item view types
* Supports optional callbacks
* Support custom callbacks
* Super easy API
* Tiny size: **~37.2 KB**
* Minimum Android SDK: **15**

## Setup

### Gradle

```gradle
apply plugin: 'kotlin-kapt'

android {
    ...
    dataBinding.enabled true 
}

dependencies {
        implementation 'com.github.nikb7:SoleAdapter:v0.3.0'
}
```
## Usage

Create your item layouts with `<layout>` as root:

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>
       <variable
                name="obj"
                type="com.nikb7.soleadapter.TextModel"/>
       <!--All your models must extend interface StableId-->
        <variable
                name="lis"
                type="com.nikb7.soleadapter.OnRecyclerItemClickListener"/>
    </data>
    
    <Button
        andorid:id="@+id/btnId"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@{obj.text}"
        android:onClick="@{(v)->lis.onRecyclerInnerItemClick(v,obj)}"
        />
        <!--pass the reference of the view and obj to identify the view click in callback-->
        
</layout>
```

**It is important for all the item types to have the same variable name**, in this case "obj", "lis". 
This name is passed to the adapter builder as BR.variableName, in this case BR.obj, BR.lis:
Kotlin code
```
val adapter = SoleAdapter(viewMap = mapOf(TextModel::class to R.layout.text_cell), //pass a mapping of your data class and xml files
            listener = this) //Extend your Activity/Fragment with OnRecyclerItemClickListener and pass it's reference for callbacks.
adapter.apply {
            recyclerView.adapter = this //assign the adpter to the recycler view
            submitList(getListOfItems()) //submit the list of items here
        }
data class TextModel(val text: String) : StableId {
    override fun getStableId(): String = text
}
```
Callbacks/Listener
```
//Extend your Activity/Fragment with OnRecyclerItemClickListener

//This gets called when the view inside recyclerview'c cell gets clicked
override fun onRecyclerInnerItemClick(view: View, obj: StableId) {
        when {
            view.id == R.id.btnId && obj is TextModel -> {
                Toast.makeText(this, "List inner item: ${obj.text}", Toast.LENGTH_SHORT).show()
            }
        }
    }

//This gets called when the recyclerview'c cell gets clicked
override fun onRecyclerItemClick(obj: StableId) {
        when {
            obj is TextModel -> Toast.makeText(this, obj.text, Toast.LENGTH_SHORT).show()
        }
    }
    
//This gets called when the recyclerview'c cell gets long pressed
override fun onRecyclerItemLongPressed(obj: StableId) {

    }
 ```
Advance usuage
```
open class RecyclerAdapter(viewMap: MutableMap<Class<out StableId>, Int>,
                           listener: OnRecyclerItemClickListener? = null,
                           emptyListModel: StableId? = EmptyListModel(), //set your custom empty-list model
                           listEndModel: StableId? = ListEndModel(), //set your custom list-end model
                           hasStableIds:Boolean = true) : //set hasStableIds as true to notify recylerview that all the items in list has unique id
        SoleAdapter(
                viewMap = viewMap,
                listener = listener
        ) {

    init {
        setHasStabeleIds(hasStableIds)
        if (emptyListModel != null)
            setEmptyListView(EmptyListModel(), R.layout.empty_list_cell) // assign the xml resource of empty-list view
        if (listEndModel != null)
            setListEndView(ListEndModel(), R.layout.list_end_cell)  // assign the xml resource of list-end view
    }

}
```
EmptyListModel is displayed when your there are no items in the list.<br>
ListEndModel is appened at the end of the list<br>
To disable any of these you can pass it's value as null

* Same can be achieved without extending the SoleAdapter, but I encorouge you to extend the SoleAdapter so that it is more customised to your use case and becomes easier to use.<br>
You can also extend SoleAdapter use-case like

* Adpater with Movable Cells 
* Requirement to display multiple cells based on some conditions from a single data class

### Utilities

* AutoGridLayoutManager
* HorizontalDivider

## License
```MIT License```

