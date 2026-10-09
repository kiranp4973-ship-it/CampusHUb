package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.InstitutionHierarchy
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.CampusViewModel
import kotlinx.coroutines.delay
import java.util.Locale

/**
 * Interactive Study Focus Timer (Pomodoro / Deep Work)
 */
@Composable
fun FocusTimerDialog(
    onDismiss: () -> Unit
) {
    var selectedMinutes by remember { mutableIntStateOf(25) }
    var secondsLeft by remember { mutableIntStateOf(25 * 60) }
    var isRunning by remember { mutableStateOf(false) }
    var selectedSubject by remember { mutableStateOf("Data Structures") }
    var sessionsCompleted by remember { mutableIntStateOf(2) }

    LaunchedEffect(isRunning, secondsLeft) {
        if (isRunning && secondsLeft > 0) {
            delay(1000L)
            secondsLeft -= 1
        } else if (isRunning && secondsLeft == 0) {
            isRunning = false
            sessionsCompleted += 1
            secondsLeft = selectedMinutes * 60
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("focus_timer_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Focus Timer",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mode presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(15 to "Sprint", 25 to "Pomodoro", 50 to "Deep Work").forEach { (mins, label) ->
                        val isSel = selectedMinutes == mins
                        FilterChip(
                            selected = isSel,
                            onClick = {
                                selectedMinutes = mins
                                secondsLeft = mins * 60
                                isRunning = false
                            },
                            label = { Text("$label ($mins m)") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Big Timer Dial
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    PrimaryBlue.copy(alpha = 0.08f),
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                )
                            )
                        )
                        .border(
                            width = 4.dp,
                            color = if (isRunning) PrimaryBlue else MaterialTheme.colorScheme.outlineVariant,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val minutes = secondsLeft / 60
                        val secs = secondsLeft % 60
                        Text(
                            text = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs),
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isRunning) PrimaryBlue else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isRunning) "FOCUSING" else "PAUSED",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Tag Subject
                Text(
                    text = "Subject: $selectedSubject",
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            isRunning = false
                            secondsLeft = selectedMinutes * 60
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset")
                    }

                    Button(
                        onClick = { isRunning = !isRunning },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRunning) MaterialTheme.colorScheme.error else PrimaryBlue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Close else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isRunning) "Pause" else "Start")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Daily Stat
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$sessionsCompleted study sessions logged today (${sessionsCompleted * 25} mins)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * Universal Institution Information & Hierarchy Dialog
 */
@Composable
fun MyInstitutionDialog(
    hierarchy: InstitutionHierarchy,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("institution_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = PrimaryBlue)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "My Institution",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.08f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = hierarchy.institutionName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                                Text(
                                    text = hierarchy.universityOrBoard,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "📍 ${hierarchy.campus}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Academic Hierarchy",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    val items = listOf(
                        "Country" to hierarchy.country,
                        "State / Region" to hierarchy.stateOrRegion,
                        "University / Board" to hierarchy.universityOrBoard,
                        "Department" to hierarchy.department,
                        "Program" to hierarchy.program,
                        "Branch / Major" to hierarchy.courseOrBranch,
                        "Academic Year" to hierarchy.academicYear,
                        "Term System" to hierarchy.termSystem.label,
                        "Current Semester" to "Semester ${hierarchy.semesterOrTerm}",
                        "Section / Batch" to hierarchy.sectionOrBatch,
                        "Grading Scale" to hierarchy.gradingSystem.label,
                        "Min. Attendance Req." to "${hierarchy.minAttendanceThreshold}%"
                    )

                    items(items) { (label, value) ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = value,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

/**
 * Notifications Panel Modal
 */
@Composable
fun NotificationsModal(
    viewModel: CampusViewModel,
    onDismiss: () -> Unit
) {
    var isAllRead by remember { mutableStateOf(false) }

    val notifications = remember(isAllRead) {
        listOf(
            Triple("Internal Assessment 1 Schedule Released", "Exam cell announced timetable for 3rd sem CS. Commencing Oct 14.", "10m ago"),
            Triple("DSA Module 3 Assignment Due Tomorrow", "Submit Binary Search Trees lab assignment before 11:59 PM.", "1h ago"),
            Triple("Attendance Warning Alert", "Discrete Mathematics attendance is at 74%. Need 2 classes to cross 75%.", "3h ago"),
            Triple("Campus Hackathon: BitByte 2026", "Registrations open for 36-hour inter-college hackathon. ₹1.5L prize pool.", "Yesterday"),
            Triple("Guest Lecture on Distributed Systems", "Prof. Ramanathan speaking at Sir MV Auditorium on Friday 2 PM.", "2d ago")
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.80f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("notifications_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Notifications",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    TextButton(onClick = { isAllRead = true }) {
                        Text("Mark all read", style = MaterialTheme.typography.labelMedium)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(notifications) { (title, desc, time) ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (!isAllRead) PrimaryBlue.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(if (!isAllRead) PrimaryBlue else Color.Transparent)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = time,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

/**
 * Study Goals & Weekly Target Tracker
 */
@Composable
fun StudyGoalsDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("study_goals_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEA580C).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.TrackChanges, contentDescription = null, tint = Color(0xFFEA580C))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Study Goals",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Weekly Progress Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Weekly Study Target", style = MaterialTheme.typography.labelLarge)
                            Text("18.5 / 25 hrs (74%)", fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.74f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = PrimaryBlue
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("🔥 6 Days Active Streak", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                            Text("Target: 25 hrs/wk", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Subject Breakdown",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                val subjects = listOf(
                    Triple("Data Structures & Algorithms", "8.0 hrs", 0.85f),
                    Triple("Operating Systems", "6.0 hrs", 0.65f),
                    Triple("Discrete Mathematics", "3.0 hrs", 0.50f),
                    Triple("Digital Design & CO", "1.5 hrs", 0.30f)
                )

                subjects.forEach { (sub, hours, pct) ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(sub, style = MaterialTheme.typography.bodySmall)
                            Text(hours, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { pct },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = PrimaryBlue.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Done")
                }
            }
        }
    }
}

/**
 * Campus Information & Student Services Dialog
 */
@Composable
fun CampusInfoDialog(
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.82f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("campus_info_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Campus Guide",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Facilities") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Helpline") }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Services") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    when (selectedTab) {
                        0 -> {
                            val facilities = listOf(
                                Triple("Central Library", "08:00 AM – 10:00 PM • Digital Library & Reading Hall 3rd Fl.", "📚"),
                                Triple("Main Student Canteen", "07:30 AM – 08:30 PM • South & North Indian, Cafe Corner", "☕"),
                                Triple("Sports Complex & Gym", "06:00 AM – 09:00 AM, 04:30 PM – 08:00 PM", "🏸"),
                                Triple("Computing & Innovation Lab", "Open 24/7 with Student ID access • Block C, 4th Fl.", "💻"),
                                Triple("Health Clinic & First Aid", "Doctor available 9 AM – 5 PM • Emergency: Ext 204", "🏥")
                            )
                            items(facilities) { (name, details, icon) ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(icon, style = MaterialTheme.typography.titleLarge)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                            Text(details, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }
                            }
                        }
                        1 -> {
                            val contacts = listOf(
                                Triple("Campus Security / Emergency", "+91 80 2661 5865 / Ext 100", "🚨"),
                                Triple("Exam Section Registrar", "+91 80 2661 5866", "📝"),
                                Triple("Hostel Superintendent (Boys/Girls)", "+91 80 2661 5867", "🏢"),
                                Triple("IT Helpdesk & Campus WiFi", "wifi-support@campus.edu", "📶"),
                                Triple("Student Grievance Cell", "grievance@campus.edu", "⚖️")
                            )
                            items(contacts) { (name, phone, icon) ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(icon, style = MaterialTheme.typography.titleLarge)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                            Text(phone, style = MaterialTheme.typography.bodySmall, color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
                                        }
                                    }
                                }
                            }
                        }
                        2 -> {
                            val services = listOf(
                                Triple("Bonafide Certificate Request", "Apply online with instant verification within 24 hrs", "📄"),
                                Triple("WiFi MAC Registration", "Register up to 2 devices for high-speed campus eduroam", "📱"),
                                Triple("Scholarships Assistance Desk", "Guidance on state and national merit scholarship forms", "🎓"),
                                Triple("Bus Pass & Railway Concession", "Counters open Monday – Friday, 1 PM to 3 PM", "🚌")
                            )
                            items(services) { (title, desc, icon) ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(icon, style = MaterialTheme.typography.titleLarge)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                            Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

/**
 * File Manager & Offline Documents Dialog
 */
@Composable
fun FileManagerDialog(
    onDismiss: () -> Unit
) {
    val documents = listOf(
        Triple("DSA_Module_3_BinaryTrees.pdf", "2.4 MB • Updated yesterday", "PDF"),
        Triple("OS_Process_Scheduling_Handwritten.pdf", "5.1 MB • Scanned with CampusHub", "PDF"),
        Triple("DDCO_Verilog_Lab_Manual.pdf", "1.8 MB • Downloaded", "PDF"),
        Triple("Maths_VTU_Previous_5Yr_QP.pdf", "8.6 MB • 2021-2025 Solved", "PDF"),
        Triple("Campus_Academic_Calendar_2026.pdf", "650 KB • Official VTU", "PDF")
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.80f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("file_manager_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FolderOpen, contentDescription = null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "File Manager",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = "Offline study documents & notes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(documents) { (name, info, type) ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(PrimaryBlue.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(type, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(name, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                                    Text(info, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                IconButton(onClick = { /* Open file */ }) {
                                    Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

/**
 * Help & Support Dialog
 */
@Composable
fun HelpSupportDialog(
    onDismiss: () -> Unit
) {
    var feedbackText by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("help_support_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HelpOutline, contentDescription = null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Help & Support",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "CampusHub v2.4 Universal",
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Your Education. Your Campus. Your Future.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Frequently Asked Questions",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text("• How is attendance safe bunk calculated? Tap any subject in Attendance to see the bunk predictor.", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("• How do I digitize notes? Use the Note Scanner from the drawer or quick tools to scan pages.", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("• How do I sync timetable with calendar? The academic calendar automatically aligns all class schedules.", style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(16.dp))

                if (submitted) {
                    Surface(
                        color = Color(0xFF16A34A).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "✓ Thank you! Your feedback has been sent to the CampusHub team.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF16A34A),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                } else {
                    OutlinedTextField(
                        value = feedbackText,
                        onValueChange = { feedbackText = it },
                        label = { Text("Send feedback or report an issue") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            if (feedbackText.isNotBlank()) submitted = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text("Submit Feedback")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }
}
