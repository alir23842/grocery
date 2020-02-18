package com.example.grocery.services

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grocery.model.Grocery


@Dao
interface GroceryDao {

    @Query("SELECT * FROM Grocery")
    fun getAll(): List<Grocery>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(grocery: Grocery)


    @Query("DELETE FROM Grocery")
    fun deleteAll()


}