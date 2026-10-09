package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.*
import com.example.ui.components.MetricBadge
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.CampusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: CampusViewModel,
    onBack: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val hierarchy by viewModel.institutionHierarchy.collectAsState()
    val attendance by viewModel.attendance.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val opportunities by viewModel.opportunities.collectAsState()

    val totalClasses = attendance.sumOf { it.totalClasses }
    val totalPresent = attendance.sumOf { it.presentCount }
    val overallPct = if (totalClasses == 0) 100.0 else (totalPresent.toDouble() / totalClasses) * 100.0
    val completedTasks = tasks.count { it.isCompleted }

    var showEditHierarchyDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student Identity & Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.navigateToSubScreen(CampusViewModel.SubScreen.SETTINGS) }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Profile Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = PrimaryBlue,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = user.name.take(2).uppercase(),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${user.usnOrRoll} • ${user.email}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PrimaryBlue.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "Role: ${user.role.name.replace("_", " ")}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = user.bio,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Universal Institution Hierarchy Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Institution & Academic System",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            TextButton(onClick = { showEditHierarchyDialog = true }) {
                                Text("Configure", fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HierarchyItemRow(label = "Institution", value = hierarchy.institutionName)
                        HierarchyItemRow(label = "Campus", value = hierarchy.campus)
                        HierarchyItemRow(label = "Affiliation", value = hierarchy.universityOrBoard)
                        HierarchyItemRow(label = "Department", value = hierarchy.department)
                        HierarchyItemRow(label = "Program & Branch", value = "${hierarchy.program} - ${hierarchy.courseOrBranch}")
                        HierarchyItemRow(label = "Term & Batch", value = "Semester ${hierarchy.semesterOrTerm} (${hierarchy.sectionOrBatch}) • Year ${hierarchy.academicYear}")
                        HierarchyItemRow(label = "Grading Scheme", value = hierarchy.gradingSystem.label)
                        HierarchyItemRow(label = "Min Attendance Threshold", value = "${hierarchy.minAttendanceThreshold.toInt()}% Mandatory")
                        HierarchyItemRow(label = "Jurisdiction", value = "${hierarchy.stateOrRegion}, ${hierarchy.country}")
                    }
                }
            }

            // Key Statistics Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Attendance", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("${String.format("%.1f", overallPct)}%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = if (overallPct >= hierarchy.minAttendanceThreshold) SuccessGreen else DangerRed)
                            Text("Threshold: ${hierarchy.minAttendanceThreshold.toInt()}%", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Study Tasks", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$completedTasks / ${tasks.size}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                            Text("Tasks Completed", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            // Technical Skills & Research Focus
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Technical Skills", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            user.skills.forEach { skill ->
                                Surface(shape = RoundedCornerShape(8.dp), color = PrimaryBlue.copy(alpha = 0.12f)) {
                                    Text(text = skill, color = PrimaryBlue, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Research & Study Interests", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            user.studyInterests.forEach { interest ->
                                Surface(shape = RoundedCornerShape(8.dp), color = SecondaryTeal.copy(alpha = 0.12f)) {
                                    Text(text = interest, color = SecondaryTeal, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Configure Institution Dialog
    if (showEditHierarchyDialog) {
        var instName by remember { mutableStateOf(hierarchy.institutionName) }
        var deptName by remember { mutableStateOf(hierarchy.department) }
        var branchName by remember { mutableStateOf(hierarchy.courseOrBranch) }
        var semStr by remember { mutableStateOf(hierarchy.semesterOrTerm.toString()) }
        var sectionStr by remember { mutableStateOf(hierarchy.sectionOrBatch) }
        var thresholdStr by remember { mutableStateOf(hierarchy.minAttendanceThreshold.toInt().toString()) }

        AlertDialog(
            onDismissRequest = { showEditHierarchyDialog = false },
            title = { Text("Configure Institution Hierarchy") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = instName,
                        onValueChange = { instName = it },
                        label = { Text("Institution Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = deptName,
                        onValueChange = { deptName = it },
                        label = { Text("Department") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = branchName,
                        onValueChange = { branchName = it },
                        label = { Text("Course / Branch") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = semStr,
                            onValueChange = { semStr = it },
                            label = { Text("Term/Sem") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = sectionStr,
                            onValueChange = { sectionStr = it },
                            label = { Text("Batch/Section") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = thresholdStr,
                        onValueChange = { thresholdStr = it },
                        label = { Text("Min Attendance % (Default 75%)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateInstitutionHierarchy(
                            hierarchy.copy(
                                institutionName = instName,
                                department = deptName,
                                courseOrBranch = branchName,
                                semesterOrTerm = semStr.toIntOrNull() ?: hierarchy.semesterOrTerm,
                                sectionOrBatch = sectionStr,
                                minAttendanceThreshold = thresholdStr.toDoubleOrNull() ?: hierarchy.minAttendanceThreshold
                            )
                        )
                        showEditHierarchyDialog = false
                    }
                ) {
                    Text("Save Hierarchy")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditHierarchyDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun HierarchyItemRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings & Preferences") },
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            val settingsList = listOf(
                "Account & College Registration",
                "Universal Hierarchy & Academic Calendar Config",
                "Privacy, Security & Safe Community Controls",
                "Notifications, Quiet Hours & Schedule Alerts",
                "Appearance, Palette & Dark Theme",
                "Language & Regional Preferences",
                "Data Export, Offline Cache & Storage Management",
                "Platform Role & Authorization Audit",
                "About CampusHub (Production v2.1)",
                "Help, Documentation & Student Support",
                "Sign Out"
            )

            items(settingsList) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (item == "Sign Out") FontWeight.Bold else FontWeight.Normal,
                            color = if (item == "Sign Out") DangerRed else MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScannerScreen(
    viewModel: CampusViewModel,
    onBack: () -> Unit
) {
    var scannedText by remember { mutableStateOf("Binary Search Tree Inorder Traversal:\nvoid inorder(Node root) {\n    if (root != null) {\n        inorder(root.left);\n        System.out.print(root.val + \" \");\n        inorder(root.right);\n    }\n}\nProperties: Inorder traversal of BST gives strictly sorted keys.") }
    var noteTitle by remember { mutableStateOf("Scanned: BST Inorder Traversal Snippet") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note Scanner") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Default.DocumentScanner, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Optical Note Reader & Digitizer", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Convert classroom whiteboard or handwritten notebook snapshots into clean text notes", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            OutlinedTextField(
                value = noteTitle,
                onValueChange = { noteTitle = it },
                label = { Text("Extracted Note Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = scannedText,
                onValueChange = { scannedText = it },
                label = { Text("Extracted Note Content (Editable)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Button(
                onClick = {
                    viewModel.addNote(
                        title = noteTitle,
                        subjectId = "sub_dsa",
                        module = 4,
                        desc = "Scanned classroom notes via Note Scanner",
                        content = scannedText
                    )
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Note to DSA Module 4")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamModeScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exam Mode ⚡") },
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
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DangerRed.copy(alpha = 0.12f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Sprint Prep: Data Structures IA-1", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DangerRed)
                        Text("Exam Date: Oct 14, 2026 (6 Days Remaining) • 50 Marks", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            item {
                Text("Sprint Checklist & Schedule", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }

            val checklist = listOf(
                "Day 1: Master Module 1 (Dynamic Memory, Sparse Matrix & Stack primitives)" to true,
                "Day 2: Infix to Postfix conversions & evaluation step-by-step" to false,
                "Day 3: Circular Queue & Priority Queue pointer implementations" to false,
                "Day 4: Solve 2023 & 2024 VTU previous internal question papers" to false,
                "Day 5: Full Formula cheatsheet & diagrammatic speed run" to false
            )

            items(checklist) { (itemText, completed) ->
                var isChecked by remember { mutableStateOf(completed) }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = itemText, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    viewModel: CampusViewModel,
    onBack: () -> Unit
) {
    val messages by viewModel.chatMessages.collectAsState()
    var inputMsg by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Sneha Patel (Class Rep)")
                        Text("Online • Library Discussion Room", style = MaterialTheme.typography.labelSmall, color = SuccessGreen)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(messages) { msg ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (msg.isFromMe) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = 14.dp,
                                topEnd = 14.dp,
                                bottomStart = if (msg.isFromMe) 14.dp else 2.dp,
                                bottomEnd = if (msg.isFromMe) 2.dp else 14.dp
                            ),
                            color = if (msg.isFromMe) PrimaryBlue else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = msg.content,
                                    color = if (msg.isFromMe) Color.White else MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = msg.timestamp,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (msg.isFromMe) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Message Input bar
            Surface(
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputMsg,
                        onValueChange = { inputMsg = it },
                        placeholder = { Text("Type a study message...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (inputMsg.isNotBlank()) {
                                viewModel.sendChat(inputMsg)
                                inputMsg = ""
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = PrimaryBlue)
                    }
                }
            }
        }
    }
}
