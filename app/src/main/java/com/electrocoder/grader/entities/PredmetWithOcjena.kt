package com.electrocoder.grader.entities

import android.os.Parcelable
import android.util.Log
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.electrocoder.grader.util.PredmetUtilFunctions
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PredmetWithOcjena(
    @Embedded var predmet: Predmet = Predmet(),

    @Relation(
        parentColumn = "predmet_id",
        entityColumn = "predmet_id"
    )
    var ocjene: MutableList<Ocjena> = mutableListOf(),

    @Ignore var prosjek: Double = PredmetUtilFunctions.calculateProsjek(ocjene)
) : Parcelable {

    init {
        //this.prosjek = PredmetUtilFunctions.calculateProsjek(ocjene)
        Log.d("TAG", "PREDMET WITH OCJENA INIT: PROSJEK - ${this.prosjek}")
    }
}
