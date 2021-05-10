package com.electrocoder.grader.entities

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity predmeta.
 */


@Entity(tableName = "tabela_predmeta")
data class Predmet(
    @PrimaryKey(autoGenerate = true) var id:Int? = null,
    var imePredmeta: String,
    var ocjene: ArrayList<Int> = arrayListOf(),
    var prosjek: Double = 0.00,
    var zakazanTest: MutableLiveData<Boolean> = MutableLiveData(false)) {

    //@PrimaryKey(autoGenerate = true)
    //var id:Int = 0
    constructor():this(null,"", arrayListOf())

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
    }

}