package com.electrocoder

import androidx.lifecycle.MutableLiveData
import androidx.room.TypeConverter
import com.electrocoder.grader.entities.Predmet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

class Converters {


    @TypeConverter
    fun convertToUnixTimestamp(date: Date): Long = date.time

    @TypeConverter
    fun convertToDate(timestamp: Long): Date = Date(timestamp)


    @TypeConverter
    fun convertFromArrayListToString(ocjene: ArrayList<Int>): String {
        var ocjeneString = ""

        for (ocjena in ocjene) ocjeneString += ",$ocjena"

        return ocjeneString
    }

    @TypeConverter
    fun convertStringToArrayList(ocjene: String): ArrayList<Int> {

        val ocjeneArray = ArrayList<Int>()

        val niz = ocjene.split(",").dropLastWhile { it.isEmpty() }.toTypedArray()

        for(ocjena in niz) {
            if(!ocjena.isEmpty())
                ocjeneArray.add(ocjena.toInt())
        }
        return ocjeneArray
    }

    @TypeConverter
    fun convertFromLiveDataToBoolean(boolean: MutableLiveData<Boolean>): Boolean? = boolean.value

    @TypeConverter
    fun convertFromBooleanToLiveData(boolean: Boolean): MutableLiveData<Boolean> = MutableLiveData(boolean)


}