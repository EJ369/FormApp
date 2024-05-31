package com.example.myform.formDataUtils

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FormTable")
data class FormData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String?,
    val age: Int?,
    val dob: String?,
    val address: String?
)
