package com.electrocoder.grader.entities

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/**
 * Entity predmeta.
 */


@Entity(tableName = "tabela_predmeta")
@Parcelize
data class Predmet(
    @ColumnInfo(name = "predmet_id")
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0,

    var imePredmeta: String = "",
    //var ocjene: ArrayList<Int> = arrayListOf(),
    //var prosjek: Double = 0.00,

    var zakazanTest: @RawValue MutableLiveData<Boolean> = MutableLiveData(false)
) : Parcelable {

    //@PrimaryKey(autoGenerate = true)
    //var id:Int = 0
    /*constructor():this(null,"", arrayListOf())

    init {
        prosjek = calculateProsjekPredmeta(ocjene)

    }

    fun calculateProsjekPredmeta(ocjene: ArrayList<Int>): Double {
        var prosjek = 0.00
        val brojOcjena: Int = ocjene.size
        if(brojOcjena > 0) {
            for (ocjena in ocjene) {
                prosjek += ocjena;
            }
            return prosjek / brojOcjena.toDouble()
        } else return prosjek
    }*/

}