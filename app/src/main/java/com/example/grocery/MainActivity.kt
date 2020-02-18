package com.example.grocery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.grocery.helpers.Functions
import com.example.grocery.model.Grocery
import com.example.grocery.services.GroceryDao
import com.example.grocery.services.GroceryDataBase
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    private var mLocalDataBase: GroceryDataBase? = null
    private var mGroceryDao: GroceryDao? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        AM_sync_btn.setOnClickListener(this)
        AM_view_btn.setOnClickListener(this)
        AM_delete_btn.setOnClickListener(this)

        database = FirebaseDatabase.getInstance()
        myRef = database.reference
        mLocalDataBase = GroceryDataBase.getDatabase(this)
        mGroceryDao = mLocalDataBase?.groceryDao()

    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.AM_sync_btn -> {
                syncData()
            }

            R.id.AM_view_btn -> {
                viewData()
            }

            R.id.AM_delete_btn -> {
                deleteData()
            }


        }
    }

    private fun syncData() {

        if (!Functions.isInternetAvailable()) {

            proBarVisible(true)

            myRef.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {

                    val list: ArrayList<Grocery> = ArrayList()

                    for (childDataSnapshot in p0.children.iterator()) {

                        list.add(childDataSnapshot.getValue(Grocery::class.java)!!)
                    }


                    GlobalScope.launch(Dispatchers.IO) {

                        for (data in list) {
                            mGroceryDao?.insert(data)
                        }
                    }


                    proBarVisible(false)
                    Toast.makeText(
                        this@MainActivity, "Data fetched Successful!", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.d("mytag", " Data: ${p0.message}")
                    proBarVisible(false)
                }
            })
        } else {
            Toast.makeText(
                this, "Make Sure You Hava Active Network!", Toast.LENGTH_SHORT
            ).show()
        }


    }

    private fun viewData() {

        var list: List<Grocery> = ArrayList()

        GlobalScope.launch(Dispatchers.Main) {

            async(Dispatchers.IO) {
                list = mGroceryDao?.getAll()!!

            }.await()

            if (list.isEmpty()) {

                Toast.makeText(
                    this@MainActivity, "Please Sync Data! ", Toast.LENGTH_SHORT
                ).show()

            } else {
                startActivity(Intent(this@MainActivity, GroceryList::class.java))
            }

        }


    }

    private fun deleteData() {

        GlobalScope.launch(Dispatchers.IO) {
            mGroceryDao?.deleteAll()
        }

    }

    private fun proBarVisible(boolean: Boolean) {

        if (boolean) {
            this.AM_sync_btn.visibility = View.INVISIBLE
            this.AM_sync_pro_bar.visibility = View.VISIBLE
        } else {
            this.AM_sync_btn.visibility = View.VISIBLE
            this.AM_sync_pro_bar.visibility = View.INVISIBLE
        }

    }


}
