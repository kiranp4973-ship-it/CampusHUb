package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.*
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.MetricBadge
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.CampusViewModel

@Composable
fun StudyHubScreen(
    viewModel: CampusViewModel,
    modifier: Modifier = Modifier
) {
    val currentSubTab by viewModel.studyTab.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val syllabi by viewModel.syllabi.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val exams by viewModel.exams.collectAsState()
    val assignments by viewModel.assignments.collectAsState()
    val labPrograms by viewModel.labPrograms.collectAsState()
    val books by viewModel.books.collectAsState()
    val questionPapers by viewModel.questionPapers.collectAsState()
    val tasks by viewModel.tasks.collectAsState()

    var showUploadNoteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Top Horizontal Tabs for Study Hub (Notion & Moodle style)
        ScrollableTabRow(
            selectedTabIndex = currentSubTab.ordinal,
            edgePadding = 0.dp,
            divider = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = currentSubTab == CampusViewModel.StudySectionTab.COURSES,
                onClick = { viewModel.setStudyTab(CampusViewModel.StudySectionTab.COURSES) },
                text = { Text("Courses") }
            )
            Tab(
                selected = currentSubTab == CampusViewModel.StudySectionTab.ATTENDANCE,
                onClick = { viewModel.setStudyTab(CampusViewModel.StudySectionTab.ATTENDANCE) },
                text = { Text("Attendance") }
            )
            Tab(
                selected = currentSubTab == CampusViewModel.StudySectionTab.NOTES,
                onClick = { viewModel.setStudyTab(CampusViewModel.StudySectionTab.NOTES) },
                text = { Text("Notes") }
            )
            Tab(
                selected = currentSubTab == CampusViewModel.StudySectionTab.ASSIGNMENTS,
                onClick = { viewModel.setStudyTab(CampusViewModel.StudySectionTab.ASSIGNMENTS) },
                text = { Text("Assignments") }
            )
            Tab(
                selected = currentSubTab == CampusViewModel.StudySectionTab.EXAMS,
                onClick = { viewModel.setStudyTab(CampusViewModel.StudySectionTab.EXAMS) },
                text = { Text("Exams") }
            )
            Tab(
                selected = currentSubTab == CampusViewModel.StudySectionTab.LABS,
                onClick = { viewModel.setStudyTab(CampusViewModel.StudySectionTab.LABS) },
                text = { Text("Labs & Code") }
            )
            Tab(
                selected = currentSubTab == CampusViewModel.StudySectionTab.TASKS,
                onClick = { viewModel.setStudyTab(CampusViewModel.StudySectionTab.TASKS) },
                text = { Text("Planner") }
            )
            Tab(
                selected = currentSubTab == CampusViewModel.StudySectionTab.BOOKS,
                onClick = { viewModel.setStudyTab(CampusViewModel.StudySectionTab.BOOKS) },
                text = { Text("Books") }
            )
            Tab(
                selected = currentSubTab == CampusViewModel.StudySectionTab.PAPERS,
                onClick = { viewModel.setStudyTab(CampusViewModel.StudySectionTab.PAPERS) },
                text = { Text("Papers") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Content Area based on selected Tab
        when (currentSubTab) {
            CampusViewModel.StudySectionTab.COURSES -> {
                SyllabusView(
                    syllabi = syllabi,
                    subjects = subjects,
                    onUpdateStatus = { subId, modId, topId, status ->
                        viewModel.updateTopicStatus(subId, modId, topId, status)
                    }
                )
            }
            CampusViewModel.StudySectionTab.ATTENDANCE -> {
                AttendanceScreen(viewModel = viewModel)
            }
            CampusViewModel.StudySectionTab.NOTES -> {
                NotesView(
                    notes = notes,
                    subjects = subjects,
                    onVote = { id, useful -> viewModel.voteNote(id, useful) },
                    onUploadClick = { showUploadNoteDialog = true }
                )
            }
            CampusViewModel.StudySectionTab.ASSIGNMENTS -> {
                AssignmentsView(assignments = assignments, subjects = subjects)
            }
            CampusViewModel.StudySectionTab.EXAMS -> {
                ExamsView(
                    exams = exams,
                    subjects = subjects,
                    onExamModeClick = { viewModel.navigateToSubScreen(CampusViewModel.SubScreen.EXAM_MODE) }
                )
            }
            CampusViewModel.StudySectionTab.LABS -> {
                LabProgramsView(programs = labPrograms, subjects = subjects)
            }
            CampusViewModel.StudySectionTab.TASKS -> {
                TasksPlannerView(tasks = tasks, subjects = subjects, onToggle = { viewModel.toggleTask(it) })
            }
            CampusViewModel.StudySectionTab.BOOKS -> {
                BooksView(books = books, subjects = subjects)
            }
            CampusViewModel.StudySectionTab.PAPERS -> {
                QuestionPapersView(papers = questionPapers, subjects = subjects)
            }
        }
    }

    if (showUploadNoteDialog) {
        var noteTitle by remember { mutableStateOf("") }
        var noteDesc by remember { mutableStateOf("") }
        var noteContent by remember { mutableStateOf("") }
        var selectedSubjectId by remember { mutableStateOf(subjects.firstOrNull()?.id ?: "") }
        var moduleNum by remember { mutableStateOf("1") }

        AlertDialog(
            onDismissRequest = { showUploadNoteDialog = false },
            title = { Text("Upload Study Note") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = noteTitle,
                        onValueChange = { noteTitle = it },
                        label = { Text("Note Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = noteDesc,
                        onValueChange = { noteDesc = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = noteContent,
                        onValueChange = { noteContent = it },
                        label = { Text("Content / Key Takeaways") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noteTitle.isNotBlank()) {
                            viewModel.addNote(
                                noteTitle,
                                selectedSubjectId,
                                moduleNum.toIntOrNull() ?: 1,
                                noteDesc,
                                noteContent
                            )
                            showUploadNoteDialog = false
                        }
                    }
                ) {
                    Text("Upload")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUploadNoteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SyllabusView(
    syllabi: List<SubjectSyllabus>,
    subjects: List<Subject>,
    onUpdateStatus: (String, String, String, TopicStatus) -> Unit
) {
    val subjectsMap = remember(subjects) { subjects.associateBy { it.id } }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        items(syllabi) { syllabus ->
            val sub = subjectsMap[syllabus.subjectId]
            val allTopics = syllabus.modules.flatMap { it.topics }
            val completedCount = allTopics.count { it.status == TopicStatus.COMPLETED }
            val completionPct = if (allTopics.isEmpty()) 0 else (completedCount * 100) / allTopics.size

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
                        Column {
                            Text(
                                text = sub?.name ?: "Subject",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Course: ${sub?.shortName} • ${sub?.credits} Credits • Faculty: ${sub?.teacherName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "$completionPct%",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { (completionPct / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = PrimaryBlue
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    syllabus.modules.forEach { module ->
                        Text(
                            text = "Module ${module.moduleNumber}: ${module.title}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        module.topics.forEach { topic ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = topic.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    TopicStatusChip(
                                        label = "Done",
                                        isSelected = topic.status == TopicStatus.COMPLETED,
                                        activeColor = SuccessGreen,
                                        onClick = {
                                            onUpdateStatus(syllabus.subjectId, module.id, topic.id, TopicStatus.COMPLETED)
                                        }
                                    )
                                    TopicStatusChip(
                                        label = "Studying",
                                        isSelected = topic.status == TopicStatus.STUDYING,
                                        activeColor = WarningOrange,
                                        onClick = {
                                            onUpdateStatus(syllabus.subjectId, module.id, topic.id, TopicStatus.STUDYING)
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TopicStatusChip(
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (isSelected) activeColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) activeColor else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun AssignmentsView(assignments: List<AssignmentItem>, subjects: List<Subject>) {
    val subjectsMap = remember(subjects) { subjects.associateBy { it.id } }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        items(assignments) { item ->
            val sub = subjectsMap[item.subjectId]
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BrandCoral.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "${sub?.shortName} • ${item.dueDate}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BrandCoral,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        MetricBadge(label = item.status.label, value = "", color = PrimaryBlue)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = item.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun TasksPlannerView(tasks: List<StudyTask>, subjects: List<Subject>, onToggle: (String) -> Unit) {
    val subjectsMap = remember(subjects) { subjects.associateBy { it.id } }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        items(tasks) { task ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle(task.id) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (task.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = task.isCompleted, onCheckedChange = { onToggle(task.id) })
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(task.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        Text("${subjectsMap[task.subjectId]?.shortName} • ${task.allocatedMinutes} mins target", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun NotesView(
    notes: List<NoteItem>,
    subjects: List<Subject>,
    onVote: (String, Boolean) -> Unit,
    onUploadClick: () -> Unit
) {
    val subjectsMap = remember(subjects) { subjects.associateBy { it.id } }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${notes.size} Peer Notes Available",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = onUploadClick,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Upload Note")
                }
            }
        }

        items(notes) { note ->
            val sub = subjectsMap[note.subjectId]
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PrimaryBlue.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "${sub?.shortName} • Mod ${note.moduleNumber}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = note.fileType,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = note.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (note.contentText != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = note.contentText,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "By ${note.uploadedBy} • ${note.date}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Useful / Not Useful votes
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledTonalButton(
                                onClick = { onVote(note.id, true) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("👍 ${note.usefulCount}", style = MaterialTheme.typography.labelSmall)
                            }
                            FilledTonalButton(
                                onClick = { onVote(note.id, false) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("👎 ${note.notUsefulCount}", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExamsView(
    exams: List<ExamItem>,
    subjects: List<Subject>,
    onExamModeClick: () -> Unit
) {
    val subjectsMap = remember(subjects) { subjects.associateBy { it.id } }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onExamModeClick),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DangerRed.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = DangerRed,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Launch Exam Mode ⚡",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DangerRed
                        )
                        Text(
                            text = "Generate day-by-day sprint checklist for upcoming internals",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        items(exams) { exam ->
            val sub = subjectsMap[exam.subjectId]
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = DangerRed.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = exam.type.name.replace("_", " "),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = DangerRed,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "${exam.date} • ${exam.time}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = exam.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Syllabus: ${exam.syllabusSummary}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Room: ${exam.room} • Max Marks: ${exam.maxMarks}",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryBlue
                    )
                }
            }
        }
    }
}

@Composable
fun LabProgramsView(programs: List<LabProgram>, subjects: List<Subject>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        items(programs) { prog ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Lab Ex #${prog.labNumber} • ${prog.difficulty}",
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (prog.isCompleted) "Completed ✅" else "Pending ⏳",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (prog.isCompleted) SuccessGreen else WarningOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = prog.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = prog.problemStatement,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = prog.code,
                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Expected Output: ${prog.expectedOutput}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun BooksView(books: List<BookItem>, subjects: List<Subject>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        items(books) { book ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = book.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${book.author} • ${book.edition}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${book.type} • Prescribed",
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryBlue
                        )
                    }
                    MetricBadge(
                        label = "Library",
                        value = if (book.isAvailableInLibrary) "In Stock" else "Issued",
                        color = if (book.isAvailableInLibrary) SuccessGreen else DangerRed
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionPapersView(papers: List<QuestionPaper>, subjects: List<Subject>) {
    val subjectsMap = remember(subjects) { subjects.associateBy { it.id } }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        items(papers) { paper ->
            val sub = subjectsMap[paper.subjectId]
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${sub?.name ?: "Subject"} (${paper.year})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = paper.examType,
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Frequently Appearing Topics:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    paper.frequentlyAskedTopics.forEach { topic ->
                        Text(
                            text = "• $topic",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
