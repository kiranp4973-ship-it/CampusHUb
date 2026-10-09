package com.example.ui.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ui.components.EmptyStateCard
import com.example.ui.theme.DangerRed
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AgendaListView(
    entries: List<CalendarEntry>,
    onEntryClick: (CalendarEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    if (entries.isEmpty()) {
        EmptyStateCard(
            icon = Icons.Default.EventAvailable,
            title = "No Upcoming Academic Events",
            description = "You're all caught up! Enjoy your free time or prepare for next week.",
            modifier = modifier.padding(16.dp)
        )
        return
    }

    val groupedByDate = entries.groupBy { it.date }
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
    val today = LocalDate.now()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        groupedByDate.forEach { (date, dayEntries) ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = when {
                            date == today -> "Today • ${date.format(dateFormatter)}"
                            date == today.plusDays(1) -> "Tomorrow • ${date.format(dateFormatter)}"
                            else -> date.format(dateFormatter)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (date == today) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "${dayEntries.size} items",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            items(dayEntries) { entry ->
                val color = Color(entry.type.colorHex)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEntryClick(entry) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = color.copy(alpha = 0.15f),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = when (entry.type) {
                                        CalendarEntryType.INTERNAL_EXAM, CalendarEntryType.SEMESTER_EXAM -> Icons.Default.AssignmentLate
                                        CalendarEntryType.LAB -> Icons.Default.Computer
                                        CalendarEntryType.ASSIGNMENT -> Icons.Default.Assignment
                                        CalendarEntryType.HOLIDAY -> Icons.Default.Celebration
                                        CalendarEntryType.CAMPUS_EVENT -> Icons.Default.Festival
                                        CalendarEntryType.PERSONAL_TASK -> Icons.Default.TaskAlt
                                        else -> Icons.Default.Class
                                    },
                                    contentDescription = null,
                                    tint = color,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = entry.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = if (entry.isCancelled) TextDecoration.LineThrough else null
                                )
                                if (entry.isCancelled) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(shape = RoundedCornerShape(4.dp), color = DangerRed.copy(alpha = 0.2f)) {
                                        Text(
                                            text = "CANCELLED",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = DangerRed,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${entry.startTime.format(timeFormatter)} - ${entry.endTime.format(timeFormatter)} • ${entry.location}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (!entry.teacherName.isNullOrBlank()) {
                                Text(
                                    text = "Faculty: ${entry.teacherName}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = color
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = color.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = entry.type.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = color,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
