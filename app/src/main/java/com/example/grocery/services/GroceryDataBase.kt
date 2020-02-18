package com.example.grocery.services

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.grocery.model.Grocery


@Database(entities = [Grocery::class], version = 1, exportSchema = false)
abstract class GroceryDataBase : RoomDatabase() {

    abstract fun groceryDao(): GroceryDao

    // marking the instance as volatile to ensure atomic access to the variable

    companion object {
        @Volatile
        private var INSTANCE: GroceryDataBase? = null

        fun getDatabase(context: Context): GroceryDataBase? {
            if (INSTANCE == null) {
                synchronized(GroceryDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            GroceryDataBase::class.java, "grocery"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }

    }


}