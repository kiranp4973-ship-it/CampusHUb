package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.CampusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalSearchScreen(
    viewModel: CampusViewModel,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    val subjects by viewModel.subjects.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val books by viewModel.books.collectAsState()
    val labPrograms by viewModel.labPrograms.collectAsState()
    val friends by viewModel.friends.collectAsState()
    val studyGroups by viewModel.studyGroups.collectAsState()

    val filteredSubjects = remember(query, subjects) {
        if (query.isBlank()) emptyList() else subjects.filter { it.name.contains(query, ignoreCase = true) || it.code.contains(query, ignoreCase = true) }
    }
    val filteredNotes = remember(query, notes) {
        if (query.isBlank()) emptyList() else notes.filter { it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true) }
    }
    val filteredBooks = remember(query, books) {
        if (query.isBlank()) emptyList() else books.filter { it.title.contains(query, ignoreCase = true) || it.author.contains(query, ignoreCase = true) }
    }
    val filteredLabs = remember(query, labPrograms) {
        if (query.isBlank()) emptyList() else labPrograms.filter { it.title.contains(query, ignoreCase = true) }
    }
    val filteredFriends = remember(query, friends) {
        if (query.isBlank()) emptyList() else friends.filter { it.name.contains(query, ignoreCase = true) || it.usnOrRoll.contains(query, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Search subjects, notes, books, peers...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            if (query.isBlank()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(56.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Search anything across CampusHub", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Type to quickly find classroom notes, textbooks, classmates or code samples", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                if (filteredSubjects.isNotEmpty()) {
                    item { Text("Subjects", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrimaryBlue) }
                    items(filteredSubjects) { sub ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(sub.name, fontWeight = FontWeight.Bold)
                                Text("${sub.code} • ${sub.teacherName}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                if (filteredNotes.isNotEmpty()) {
                    item { Text("Notes", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrimaryBlue) }
                    items(filteredNotes) { note ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(note.title, fontWeight = FontWeight.Bold)
                                Text(note.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                if (filteredBooks.isNotEmpty()) {
                    item { Text("Textbooks & Prescribed Material", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrimaryBlue) }
                    items(filteredBooks) { b ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(b.title, fontWeight = FontWeight.Bold)
                                Text("${b.author} • ${b.edition}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                if (filteredFriends.isNotEmpty()) {
                    item { Text("Classmates", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrimaryBlue) }
                    items(filteredFriends) { friend ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(friend.name, fontWeight = FontWeight.Bold)
                                Text("${friend.usnOrRoll} • Sem ${friend.semester}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                if (filteredSubjects.isEmpty() && filteredNotes.isEmpty() && filteredBooks.isEmpty() && filteredFriends.isEmpty() && filteredLabs.isEmpty()) {
                    item {
                        Text(
                            "No matching results found for \"$query\"",
                            modifier = Modifier.padding(vertical = 24.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
