package com.example.grocery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocery.adapter.GroceryListAdaptor
import com.example.grocery.model.Grocery
import com.example.grocery.services.GroceryDao
import com.example.grocery.services.GroceryDataBase
import kotlinx.android.synthetic.main.activity_grocery_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class GroceryList : AppCompatActivity() {

    private var mLocalDataBase: GroceryDataBase? = null
    private var mGroceryDao: GroceryDao? = null
    private var mList: ArrayList<Grocery> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grocery_list)

        mLocalDataBase = GroceryDataBase.getDatabase(this)
        mGroceryDao = mLocalDataBase?.groceryDao()

        this.AGL_rv.layoutManager = LinearLayoutManager(this)

        GlobalScope.launch(Dispatchers.Main) {

            async(Dispatchers.IO) {
                mGroceryDao?.getAll()?.let { mList.addAll(it) }
            }.await()

            AGL_rv.adapter = GroceryListAdaptor(mList, this@GroceryList)

        }

    }
}
