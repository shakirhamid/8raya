package com.raya.eightraya.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.raya.eightraya.data.local.entity.McqChoiceEntity
import com.raya.eightraya.data.local.entity.McqEntity

data class McqWithChoices(
    @Embedded val question: McqEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "questionId",
    )
    val choices: List<McqChoiceEntity>,
)
