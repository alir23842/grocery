package com.example.grocery.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Grocery {

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "description")
    var description: String = ""

    @ColumnInfo(name = "name")
    var name: String = ""

    @ColumnInfo(name = "price")
    var price: Double = 0.0


    @ColumnInfo(name = "unit")
    var unit: String = ""

    override fun toString(): String {
        return "Name: $name\nUnit: $unit\nPrice: $price\nDescription: $description"
    }
}