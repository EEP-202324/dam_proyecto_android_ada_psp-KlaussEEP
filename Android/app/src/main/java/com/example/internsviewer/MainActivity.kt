package com.example.internsviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel

@Serializable
data class Intern(
    val Id: Int,
    val Name: String,
    val Surname: String,
    val amount: Double,
    val boss: String
)

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
                text = "ID: ${intern.Id}, Name: ${intern.Name}, Surname: ${intern.Surname}, Amount: ${intern.amount}, Boss: ${intern.boss}",
                style = MaterialTheme.typography.body1,
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


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
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
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = intern.Id.toString(),
                onValueChange = { intern = intern.copy(Id = it.toIntOrNull() ?: 0) },
                label = { Text("ID") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = intern.Name,
                onValueChange = { intern = intern.copy(Name = it) },
                label = { Text("Name") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = intern.Surname,
                onValueChange = { intern = intern.copy(Surname = it) },
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
                    internsList.add(intern.copy())
                    intern = Intern(0, "", "", 0.0, "")
                }) {
                    Text("Create")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
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
                        internsList.removeAll { it.Id == idToDelete }
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
