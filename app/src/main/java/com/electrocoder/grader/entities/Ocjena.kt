package com.electrocoder.grader.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "tabela_ocjena", foreignKeys = [
    ForeignKey(entity = Predmet::class,
    parentColumns = arrayOf("predmet_id"),
    childColumns = arrayOf("predmet_id"),
    onDelete = CASCADE)
    ]
)
@Parcelize
data class Ocjena(

    @PrimaryKey(autoGenerate = true)
    var ocjenaId: Long = 0,

    @ColumnInfo(name = "predmet_id", index = true)
    var predmetId: Long = 0,

    val ocjena: Int = 0,

    val datumOcjene: Date = Date()
) : Parcelable
