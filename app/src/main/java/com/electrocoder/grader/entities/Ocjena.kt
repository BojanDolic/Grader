package com.electrocoder.grader.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tabela_ocjena")
data class Ocjena(
    @PrimaryKey val ocjenaId: Long,
    val ocjena: Int,
    val datumOcjene: Date = Date()
)
