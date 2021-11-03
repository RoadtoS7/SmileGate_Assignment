package com.smilegate.assignment.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "category",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("parentCategoryId")
    )]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val parentCategoryId: Int?,
    val categoryName: String
)
