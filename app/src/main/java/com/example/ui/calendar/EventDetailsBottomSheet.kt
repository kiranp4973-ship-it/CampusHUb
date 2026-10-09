package com.example.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.NoteItem
import com.example.model.Subject
import com.example.model.SubjectSyllabus
import com.example.ui.components.MetricBadge
import com.example.ui.theme.DangerRed
import com.example.ui.theme.PrimaryBlue
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsBottomSheet(
    entry: CalendarEntry,
    subject: Subject?,
    syllabus: SubjectSyllabus?,
    relatedNotes: List<NoteItem>,
    canEdit: Boolean,
    onDismiss: () -> Unit,
    onToggleCancelled: () -> Unit,
    onDelete: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val color = Color(entry.type.colorHex)
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with type badge and close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = color.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = entry.type.label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = color,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (canEdit && entry.rawTimetableSlotId != null) {
                        OutlinedButton(
                            onClick = onToggleCancelled,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(if (entry.isCancelled) "Restore Class" else "Cancel Class", color = DangerRed)
                        }
                    }
                    if (canEdit && entry.rawAcademicEventId != null) {
                        IconButton(onClick = onDelete) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = DangerRed)
                        }
                    }
                }
            }

            // Title and basic status
            Column {
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (entry.isCancelled) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "⚠️ Notice: This class session has been marked cancelled by administration.",
                        style = MaterialTheme.typography.bodySmall,
                        color = DangerRed,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Time, Date & Room details
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "${entry.date.format(DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy"))} • ${entry.startTime.format(timeFormatter)} - ${entry.endTime.format(timeFormatter)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Place, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = entry.location,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (!entry.teacherName.isNullOrBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Faculty Instructor: ${entry.teacherName}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Description / notes
            if (entry.details.isNotBlank()) {
                Column {
                    Text("Session Details", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(entry.details, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Connected Intelligence: Syllabus & Notes Integration
            if (subject != null) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Text("Subject Intelligence (${subject.shortName})", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                if (syllabus != null) {
                    val modulesCount = syllabus.modules.size
                    val totalTopics = syllabus.modules.flatMap { it.topics }.size
                    Text(
                        text = "Course: ${subject.name} • $modulesCount Modules ($totalTopics Topics)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (relatedNotes.isNotEmpty()) {
                    Text(
                        text = "Available Peer Study Notes:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    relatedNotes.take(2).forEach { note ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(note.title, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                                    Text("By ${note.uploadedBy} • ${note.fileType}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text("👍 ${note.usefulCount}", style = MaterialTheme.typography.labelSmall, color = PrimaryBlue)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Done")
            }
        }
    }
}
