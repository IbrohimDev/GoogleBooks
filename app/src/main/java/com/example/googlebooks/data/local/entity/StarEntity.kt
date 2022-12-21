package com.example.googlebooks.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class StarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val bookId: String,
    val author: String,
    val title: String?,
    val imageLink: String?,
    val infoLink: String?
)
