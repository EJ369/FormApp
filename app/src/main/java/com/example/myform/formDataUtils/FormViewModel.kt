package com.example.myform.formDataUtils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FormViewModel(private val repository: FormRepository) : ViewModel() {

    private val _insertionResult = MutableLiveData<Boolean>()
    val insertionResult: LiveData<Boolean>
        get() = _insertionResult

    fun insertData(formData: FormData) {
        viewModelScope.launch {
            try {
                repository.insert(formData)
                _insertionResult.value = true
            } catch (e: Exception) {
                _insertionResult.value = false
            }
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            repository.getAllUsers()
        }
    }

    fun validateForm(name: String, age: String, dob: String, address: String) : Boolean {
        val isNameValid = name.isNotEmpty()
        val isAgeValid = age.isNotEmpty()
        val isDobValid = dob.isNotEmpty()
        val isAddressValid = address.isNotEmpty()

        return isNameValid && isAgeValid && isDobValid && isAddressValid
    }
}