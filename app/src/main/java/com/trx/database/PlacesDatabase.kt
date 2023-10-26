package com.trx.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.trx.models.PlaceModel

@Database([PlaceModel::class], version = 1)
abstract class PlacesDatabase : RoomDatabase(){

    abstract fun contactDao() : PlacesDao

}