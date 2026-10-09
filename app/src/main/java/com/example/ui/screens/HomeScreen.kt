package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.*
import com.example.ui.components.MetricBadge
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.CampusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CampusViewModel,
    modifier: Modifier = Modifier
) {
    val user by viewModel.currentUser.collectAsState()
    val hierarchy by viewModel.institutionHierarchy.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val attendance by viewModel.attendance.collectAsState()
    val timetable by viewModel.timetable.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val exams by viewModel.exams.collectAsState()
    val assignments by viewModel.assignments.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val opportunities by viewModel.opportunities.collectAsState()

    var showAddTaskDialog by remember { mutableStateOf(false) }

    // Today's classes filter
    val todaySlots = remember(timetable) { timetable.filter { it.dayOfWeek == 1 } }
    val subjectsMap = remember(subjects) { subjects.associateBy { it.id } }

    // Overall attendance calculation
    val totalClasses = attendance.sumOf { it.totalClasses }
    val totalPresent = attendance.sumOf { it.presentCount }
    val overallPercentage = if (totalClasses == 0) 100.0 else (totalPresent.toDouble() / totalClasses) * 100.0
    val warningSubjects = attendance.filter { it.percentage < hierarchy.minAttendanceThreshold }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header Banner with Universal Hierarchy indicator
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(PrimaryBlue, Color(0xFF1E3A8A))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Welcome, ${user.name.substringBefore(" ")} 👋",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${hierarchy.institutionName} • ${hierarchy.courseOrBranch}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.85f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Term: Sem ${hierarchy.semesterOrTerm} (${hierarchy.sectionOrBatch})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BrandCoralLight,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .clickable {
                                        viewModel.setTab(CampusViewModel.MainTab.PROFILE)
                                    }
                            ) {
                                Text(
                                    text = user.name.take(2).uppercase(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Status pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.15f),
                            modifier = Modifier.clickable {
                                viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.FRIENDS)
                                viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = user.status.emoji, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = "Status: ${user.status.label} • \"${user.customStatusNote}\"",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Actions Row (Home Shortcuts)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickActionButton(
                        icon = Icons.Default.CalendarMonth,
                        label = "Schedule",
                        tint = PrimaryBlue,
                        onClick = { viewModel.setTab(CampusViewModel.MainTab.CALENDAR) }
                    )
                    QuickActionButton(
                        icon = Icons.Default.CheckCircle,
                        label = "Attendance",
                        tint = SecondaryTeal,
                        onClick = {
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.ATTENDANCE)
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                        }
                    )
                    QuickActionButton(
                        icon = Icons.Default.EditNote,
                        label = "Workspace",
                        tint = BrandCoral,
                        onClick = {
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.COURSES)
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                        }
                    )
                    QuickActionButton(
                        icon = Icons.Default.Public,
                        label = "Discovery",
                        tint = BrandViolet,
                        onClick = {
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.OPPORTUNITIES)
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                        }
                    )
                    QuickActionButton(
                        icon = Icons.Default.Timer,
                        label = "Exam Mode",
                        tint = DangerRed,
                        onClick = { viewModel.navigateToSubScreen(CampusViewModel.SubScreen.EXAM_MODE) }
                    )
                }
            }
        }

        // Today's Classes Section
        item {
            SectionHeader(
                title = "Today's Schedule & Classes",
                subtitle = "Active Timetable",
                actionText = "Calendar",
                onActionClick = { viewModel.setTab(CampusViewModel.MainTab.CALENDAR) }
            )
        }

        items(todaySlots) { slot ->
            val subject = subjectsMap[slot.subjectId]
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.width(64.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = slot.startTime,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = slot.endTime,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = subject?.name ?: "Subject Class",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = subject?.teacherName ?: "Faculty",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Room ${slot.room}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Single-tap Present
                    IconButton(
                        onClick = {
                            slot.subjectId.let { id -> viewModel.markAttendance(id, true) }
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Present",
                            tint = SuccessGreen
                        )
                    }
                }
            }
        }

        // Attendance Summary Dashboard Card
        item {
            SectionHeader(
                title = "Attendance Status",
                actionText = "Analytics",
                onActionClick = {
                    viewModel.setStudyTab(CampusViewModel.StudySectionTab.ATTENDANCE)
                    viewModel.setTab(CampusViewModel.MainTab.STUDY)
                }
            )

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
                                text = "Overall Attendance",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${String.format("%.1f", overallPercentage)}%",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (overallPercentage >= hierarchy.minAttendanceThreshold) SuccessGreen else DangerRed
                            )
                        }

                        MetricBadge(
                            label = "Min Req",
                            value = "${hierarchy.minAttendanceThreshold.toInt()}%",
                            color = PrimaryBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { (overallPercentage / 100.0).toFloat().coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = if (overallPercentage >= hierarchy.minAttendanceThreshold) SuccessGreen else DangerRed,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    if (warningSubjects.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DangerRed.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = DangerRed,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Alert: ${warningSubjects.joinToString { subjectsMap[it.subjectId]?.shortName ?: "" }} is below threshold!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DangerRed,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Upcoming Deadlines & Internals
        item {
            SectionHeader(
                title = "Deadlines & Assessments",
                subtitle = "Examinations and course submissions"
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(exams.take(3)) { exam ->
                    Card(
                        modifier = Modifier.width(260.dp),
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
                                        text = "Exam in 6 days",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = DangerRed,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = exam.date,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = exam.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = exam.syllabusSummary,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                items(assignments.take(2)) { assignment ->
                    Card(
                        modifier = Modifier.width(260.dp),
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
                                        text = assignment.dueDate,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandCoral,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = assignment.status.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = assignment.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = assignment.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // Daily Study Tasks & Goals (Notion style)
        item {
            SectionHeader(
                title = "Study Goals & Action Plan",
                actionText = "+ Task",
                onActionClick = { showAddTaskDialog = true }
            )
        }

        items(tasks) { task ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleTask(task.id) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (task.isCompleted)
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { viewModel.toggleTask(task.id) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.SemiBold,
                            color = if (task.isCompleted)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = subjectsMap[task.subjectId]?.shortName ?: "",
                                style = MaterialTheme.typography.labelSmall,
                                color = PrimaryBlue
                            )
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${task.allocatedMinutes} mins",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Opportunities & Scholarships Discovery Preview
        item {
            SectionHeader(
                title = "Verified Opportunities & Fellowships",
                actionText = "Explore Hub",
                onActionClick = {
                    viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.OPPORTUNITIES)
                    viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                }
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    opportunities.take(2).forEach { opp ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = BrandViolet.copy(alpha = 0.12f),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(opp.scope.emoji, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(opp.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("${opp.organization} • Deadline: ${opp.deadline}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = { viewModel.toggleOpportunitySaved(opp.id) }) {
                                Icon(
                                    imageVector = if (opp.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Save",
                                    tint = if (opp.isSaved) BrandCoral else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Task Dialog
    if (showAddTaskDialog) {
        var taskTitle by remember { mutableStateOf("") }
        var selectedSubId by remember { mutableStateOf(subjects.firstOrNull()?.id ?: "") }
        var minutesStr by remember { mutableStateOf("30") }

        AlertDialog(
            onDismissRequest = { showAddTaskDialog = false },
            title = { Text("Add Study Goal") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        label = { Text("Task Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = minutesStr,
                        onValueChange = { minutesStr = it },
                        label = { Text("Allocated Minutes") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (taskTitle.isNotBlank()) {
                            viewModel.addTask(taskTitle, selectedSubId, minutesStr.toIntOrNull() ?: 30)
                            showAddTaskDialog = false
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTaskDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = tint.copy(alpha = 0.12f),
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
