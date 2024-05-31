package com.example.myform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myform.formDataUtils.FormRepository
import com.example.myform.formDataUtils.FormViewModel
import com.example.myform.formDataUtils.FormViewModelFactory
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FormUnitTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: FormRepository

    private lateinit var viewModel: FormViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        val viewModelFactory = FormViewModelFactory(repository)
        viewModel = viewModelFactory.create(FormViewModel::class.java)
    }

    @Test
    fun validateForm_emptyFields_returnsFalse(): Unit = runBlocking {
        val name = ""
        val age = ""
        val dob = ""
        val address = ""

        assert(!viewModel.validateForm(name, age, dob, address))
    }

    @Test
    fun validateForm_filledFields_returnsTrue(): Unit = runBlocking {
        val name = "John"
        val age = "30"
        val dob = "01 Jan, 1990"
        val address = "123 Street"


        assert(viewModel.validateForm(name, age, dob, address))
    }
}