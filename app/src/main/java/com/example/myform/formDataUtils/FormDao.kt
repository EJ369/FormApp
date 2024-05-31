package com.example.myform.formDataUtils

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FormDao {
    @Insert
    suspend fun insert(data: FormData)

    @Delete
    fun delete(data: FormData)

    @Query("SELECT * FROM FormTable")
    suspend fun getAllUsers(): List<FormData>
}