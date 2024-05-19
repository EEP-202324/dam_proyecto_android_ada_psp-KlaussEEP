package com.example.internsviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

@Serializable
data class Intern(
    val id: Int,
    var name: String,
    var surname: String,
    var amount: Double,
    var boss: String
)

interface InternApiService {
    @GET("interns")
    fun getInterns(): Call<List<Intern>>

    @POST("interns")
    fun createIntern(@Body intern: Intern): Call<Void>

    @DELETE("interns/{id}")
    fun deleteIntern(@Path("id") internId: Int): Call<Void>
}

fun createInternApiService(): InternApiService {
    val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(InternApiService::class.java)
}

object ApiService {
    val instance: InternApiService by lazy { createInternApiService() }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InternsApp()
        }
    }
}

@Composable
fun InternsList(interns: List<Intern>) {
    Column {
        interns.forEach { intern ->
            Text(
                text = "ID: ${intern.id}, Name: ${intern.name}, Surname: ${intern.surname}, Salary: ${intern.amount}, Boss: ${intern.boss}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun InternsApp() {
    var intern by remember { mutableStateOf(Intern(0, "", "", 0.0, "")) }
    val internsList = remember { mutableStateListOf<Intern>() }
    var showInternsList by remember { mutableStateOf(false) }
    var deleteId by remember { mutableStateOf("") }
    var isDeleteDialogOpen by remember { mutableStateOf(false) }

    fun fetchInterns() {
        val internApiService = createInternApiService()

        val call = internApiService.getInterns()
        call.enqueue(object : Callback<List<Intern>> {
            override fun onResponse(call: Call<List<Intern>>, response: Response<List<Intern>>) {
                if (response.isSuccessful) {
                    val interns = response.body()
                    internsList.clear()
                    interns?.let {
                        internsList.addAll(it)
                    }
                    showInternsList = true
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<List<Intern>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "InternsViewer",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = intern.name,
                onValueChange = { intern = intern.copy(name = it) },
                label = { Text("Name") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = intern.surname,
                onValueChange = { intern = intern.copy(surname = it) },
                label = { Text("Surname") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = intern.amount.toString(),
                onValueChange = { intern = intern.copy(amount = it.toDoubleOrNull() ?: 0.0) },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = intern.boss,
                onValueChange = { intern = intern.copy(boss = it) },
                label = { Text("Boss") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.heightIn(min = 48.dp)
            ) {
                Button(onClick = {
                    val newIntern = Intern(0, "Nuevo Intern", "Posición", 0.0, "Otra información")
                    ApiService.instance.createIntern(newIntern).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                // La creación fue exitosa
                            } else {
                                // La solicitud no fue exitosa
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            // Error de red u otro error ocurrió, manejarlo aquí
                        }
                    })
                }) {
                    Text("Create")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    fetchInterns()
                    showInternsList = true
                }) {
                    Text("View all")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    isDeleteDialogOpen = true
                }) {
                    Text("Delete")
                }
            }
        }
    }

    if (showInternsList) {
        AlertDialog(
            onDismissRequest = { showInternsList = false },
            title = { Text("All Interns") },
            text = {
                InternsList(interns = internsList)
            },
            confirmButton = {
                Button(onClick = { showInternsList = false }) {
                    Text("Close")
                }
            }
        )
    }

    if (isDeleteDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDeleteDialogOpen = false },
            title = { Text("Delete Intern") },
            text = {
                TextField(
                    value = deleteId,
                    onValueChange = { deleteId = it },
                    label = { Text("Enter ID to delete") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                Button(onClick = {
                    val idToDelete = deleteId.toIntOrNull()
                    if (idToDelete != null) {
                        ApiService.instance.deleteIntern(idToDelete)
                        deleteId = ""
                        isDeleteDialogOpen = false
                    }
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { isDeleteDialogOpen = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview
@Composable
fun PreviewInternsApp() {
    InternsApp()
}
