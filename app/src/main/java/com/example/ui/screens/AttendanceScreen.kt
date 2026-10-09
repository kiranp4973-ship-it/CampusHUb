package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AttendanceRecord
import com.example.ui.components.MetricBadge
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.CampusViewModel

@Composable
fun AttendanceScreen(
    viewModel: CampusViewModel,
    modifier: Modifier = Modifier
) {
    val attendanceList by viewModel.attendance.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val subjectsMap = remember(subjects) { subjects.associateBy { it.id } }

    var selectedSubjectIdForPredictor by remember {
        mutableStateOf(attendanceList.firstOrNull()?.subjectId ?: "")
    }

    // Attendance stats
    val totalClasses = attendanceList.sumOf { it.totalClasses }
    val totalPresent = attendanceList.sumOf { it.presentCount }
    val overallPercentage = if (totalClasses == 0) 100.0 else (totalPresent.toDouble() / totalClasses) * 100.0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Overall Attendance Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Overall Semester Attendance",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${String.format("%.1f", overallPercentage)}%",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (overallPercentage >= 75.0) SuccessGreen else DangerRed
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$totalPresent Present / $totalClasses Total Sessions",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        MetricBadge(
                            label = "Required",
                            value = "75%",
                            color = PrimaryBlue
                        )
                        MetricBadge(
                            label = "Status",
                            value = if (overallPercentage >= 75.0) "Safe Zone ✅" else "Shortage Alert ⚠️",
                            color = if (overallPercentage >= 75.0) SuccessGreen else DangerRed
                        )
                    }
                }
            }
        }

        // Attendance Predictor Section
        item {
            SectionHeader(
                title = "Attendance Predictor & Bunk Calculator",
                subtitle = "Simulate how upcoming classes affect your eligibility"
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Select Subject to Calculate:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Subject Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        attendanceList.forEach { record ->
                            val sub = subjectsMap[record.subjectId]
                            val isSelected = record.subjectId == selectedSubjectIdForPredictor
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedSubjectIdForPredictor = record.subjectId },
                                label = { Text(sub?.shortName ?: "Sub") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val selectedRecord = attendanceList.find { it.subjectId == selectedSubjectIdForPredictor }
                        ?: attendanceList.firstOrNull()

                    if (selectedRecord != null) {
                        val subName = subjectsMap[selectedRecord.subjectId]?.name ?: "Subject"
                        Text(
                            text = subName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Current: ${String.format("%.1f", selectedRecord.percentage)}% (${selectedRecord.presentCount}/${selectedRecord.totalClasses})",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Calculations
                        val bunksAllowed = selectedRecord.canBunkNextClasses()
                        val classesNeeded = selectedRecord.classesNeededToReachRequired()

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Attend next 5 classes:", style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        "${String.format("%.1f", selectedRecord.percentageIfAttendNext(5))}%",
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Attend next 10 classes:", style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        "${String.format("%.1f", selectedRecord.percentageIfAttendNext(10))}%",
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                if (selectedRecord.percentage >= 75.0) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Classes you can safely miss:", style = MaterialTheme.typography.bodySmall)
                                        Text(
                                            "$bunksAllowed classes",
                                            fontWeight = FontWeight.Bold,
                                            color = if (bunksAllowed > 0) PrimaryBlue else WarningOrange
                                        )
                                    }
                                } else {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Classes needed for 75%:", style = MaterialTheme.typography.bodySmall, color = DangerRed)
                                        Text(
                                            "Attend next $classesNeeded classes consecutively",
                                            fontWeight = FontWeight.Bold,
                                            color = DangerRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Subject-wise Attendance Breakdown
        item {
            SectionHeader(
                title = "Subject-Wise Tracker",
                subtitle = "Log attendance with one tap"
            )
        }

        items(attendanceList) { record ->
            val sub = subjectsMap[record.subjectId]
            SubjectAttendanceCard(
                record = record,
                subjectName = sub?.name ?: "Subject",
                subjectCode = sub?.code ?: "",
                teacher = sub?.teacherName ?: "",
                onMarkPresent = { viewModel.markAttendance(record.subjectId, true) },
                onMarkAbsent = { viewModel.markAttendance(record.subjectId, false) }
            )
        }
    }
}

@Composable
fun SubjectAttendanceCard(
    record: AttendanceRecord,
    subjectName: String,
    subjectCode: String,
    teacher: String,
    onMarkPresent: () -> Unit,
    onMarkAbsent: () -> Unit
) {
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subjectName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$subjectCode • $teacher",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "${String.format("%.1f", record.percentage)}%",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (record.percentage >= 75.0) SuccessGreen else DangerRed
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { (record.percentage / 100.0).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = if (record.percentage >= 75.0) SuccessGreen else DangerRed,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Present: ${record.presentCount} | Absent: ${record.absentCount} | Total: ${record.totalClasses}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(
                        onClick = onMarkPresent,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("+ Present", color = SuccessGreen, fontWeight = FontWeight.Bold)
                    }
                    FilledTonalButton(
                        onClick = onMarkAbsent,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("+ Absent", color = DangerRed, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
