package com.example.myform.formDataUtils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FormRepository(private val formDao: FormDao) {
    suspend fun insert(data: FormData) = withContext(Dispatchers.IO) {
        formDao.insert(data)
    }

    suspend fun getAllUsers(): List<FormData> = withContext(Dispatchers.IO) {
        formDao.getAllUsers()
    }
}