package com.example.myform

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDefaults.dateFormatter
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.myform.formDataUtils.FormData
import com.example.myform.formDataUtils.FormDatabase
import com.example.myform.formDataUtils.FormRepository
import com.example.myform.formDataUtils.FormViewModel
import com.example.myform.formDataUtils.FormViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: FormViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val formDatabase = Room.databaseBuilder(this, FormDatabase::class.java, "FormDatabase").build()
        val repository = FormRepository(formDatabase.formDao())
        val viewModelFactory = FormViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[FormViewModel::class.java]
        
        viewModel.insertionResult.observe(this) {
            if (it) {
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Data failed to save", Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            MyApp(viewModel)
        }
    }
}

@Composable
fun MyApp(viewModel: FormViewModel? = null) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        FormScreen(viewModel)
    }
}

private fun validateInput(name: String, age: String, dob: String?, address: String): Boolean {
    val nameIsValid = name.isNotEmpty()
    val ageIsValid = age.isNotEmpty() && age.toIntOrNull() != null
    val dobIsValid = dob?.isNotEmpty()
    val addressIsValid = address.isNotEmpty()

    return nameIsValid && ageIsValid && dobIsValid == true && addressIsValid
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(viewModel: FormViewModel?) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var displayDate by remember { mutableStateOf("Select Date") }
    var saveDate by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    var address by remember { mutableStateOf("") }

    Log.d("DOB Before", saveDate)

    Column(modifier = Modifier
        .padding(20.dp)
        .fillMaxWidth()) {
        Text("Personal\nInformation", style = MaterialTheme.typography.headlineMedium, color = colorResource(R.color.orange), fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)
        Text("Let us know about yourself", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 5.dp))

        PickerCard {
            Text("Name", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp, start = 15.dp))
            ModifiedTextFiled(value = name, onValueChange = { name = it }, keyboardType = KeyboardType.Text)
        }

        PickerCard {
            Text("Age", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp, start = 15.dp))
            ModifiedTextFiled(value = age, onValueChange = { age = it }, keyboardType = KeyboardType.Number)
        }

        PickerCard {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)) {
                Text("Date of Birth", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = 15.dp))
                OutlinedButton(onClick = { openDialog.value = true }, modifier = Modifier
                    .padding(top = 10.dp, start = 30.dp, end = 10.dp, bottom = 10.dp)
                    .fillMaxWidth()) {
                    Text(displayDate, color = colorResource(R.color.orange))
                }
            }

            if (openDialog.value) {
                val datePickerState = rememberDatePickerState()
                val confirmEnabled = remember { derivedStateOf { true } }
                DatePickerDialog(onDismissRequest = { openDialog.value = false }, confirmButton = {
                    TextButton(onClick = {
                        openDialog.value = false
                        var displaydate = "Select Date"
                        var savedate = ""
                        if (datePickerState.selectedDateMillis != null) {
                            displaydate = dateFormatter(selectedDateSkeleton = "dd MMM, yyyy").formatDate(datePickerState.selectedDateMillis, locale = CalendarLocale.ENGLISH).toString()
                            savedate = dateFormatter(selectedDateSkeleton = "yyyy-MM-dd").formatDate(datePickerState.selectedDateMillis, locale = CalendarLocale.ROOT).toString()
                        }
                        Log.d("DOB Between", saveDate)
                        displayDate = displaydate
                        saveDate = savedate
                    }, enabled = confirmEnabled.value) {
                        Text("OK")
                    }
                }, dismissButton = {
                    TextButton(onClick = {
                        openDialog.value = false
                    }) {
                        Text("Cancel")
                    }
                }, colors = DatePickerDefaults.colors(containerColor = colorResource(R.color.light_orange))) {
                    DatePicker(state = datePickerState)
                }
            }
        }

        PickerCard {
            Text("Address", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp, start = 15.dp))
            ModifiedTextFiled(value = address, onValueChange = { address = it }, keyboardType = KeyboardType.Text)
        }

        val context = LocalContext.current
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp), horizontalArrangement = Arrangement.End) {
            Button(onClick = {
                Log.d("DOB After", saveDate)
                if (viewModel?.validateForm(name, age, saveDate, address) == true) {
                    val data = FormData(0, name = name, age = age.toInt(), dob = saveDate, address = address)
                    viewModel.insertData(data)
                } else {
                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier
                .width(150.dp)
                .height(50.dp), shape = MaterialTheme.shapes.medium, elevation = ButtonDefaults.buttonElevation(5.dp), colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.orange))) {
                Text("SAVE", style = MaterialTheme.typography.labelMedium, fontSize = 20.sp)
            }
        }

    }
}

@Composable
fun PickerCard(content: @Composable () -> Unit) {
    Card(modifier = Modifier
        .wrapContentSize()
        .padding(top = 18.dp)
        .border(1.dp, color = colorResource(R.color.dark_orange), RoundedCornerShape(10.dp)),
         elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
         colors = CardDefaults.cardColors(containerColor = colorResource(R.color.light_orange))) {
        content()
    }
}

@Composable
fun ModifiedTextFiled(value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType) {
    TextField(value = value,
              onValueChange = { newValue -> onValueChange(newValue) },
              textStyle = TextStyle(fontSize = 18.sp, fontFamily = FontFamily.Serif),
              maxLines = 2,
              modifier = Modifier
                  .fillMaxWidth()
                  .wrapContentHeight(),
              keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
              colors = TextFieldDefaults.colors(focusedContainerColor = colorResource(R.color.light_orange), unfocusedContainerColor = colorResource(R.color.light_orange), focusedLabelColor = colorResource(R.color.black)))
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}