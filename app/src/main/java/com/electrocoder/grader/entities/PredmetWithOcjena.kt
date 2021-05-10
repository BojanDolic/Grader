package com.electrocoder.grader.entities

import androidx.room.Embedded
import androidx.room.Relation


data class PredmetWithOcjena(
    @Embedded val predmet: Predmet,
    @Relation(
        parentColumn = "id",
        entityColumn = "ocjenaId"
    )
    val ocjene: List<Ocjena>
)
