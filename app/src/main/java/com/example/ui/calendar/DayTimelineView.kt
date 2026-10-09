package com.example.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DangerRed
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DayTimelineView(
    date: LocalDate,
    entries: List<CalendarEntry>,
    onEntryClick: (CalendarEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val now = LocalTime.now()
    val isToday = date == LocalDate.now()

    val hourStart = 8
    val hourEnd = 18 // 8 AM to 6 PM
    val hourHeightDp = 72.dp

    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)) {
            for (hour in hourStart..hourEnd) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(hourHeightDp)
                ) {
                    // Time Label & subtle horizontal gridline
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = String.format("%02d:00", hour),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.width(46.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                        )
                    }
                }
            }
        }

        // Current Time Red Indicator Line
        if (isToday && now.hour in hourStart..hourEnd) {
            val totalMinutesFromStart = (now.hour - hourStart) * 60 + now.minute
            val offsetDp = (totalMinutesFromStart.toFloat() / 60f) * hourHeightDp.value + 8f

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = offsetDp.dp)
                    .padding(start = 38.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = DangerRed,
                    modifier = Modifier.size(8.dp)
                ) {}
                HorizontalDivider(color = DangerRed, thickness = 2.dp)
            }
        }

        // Positioned Events
        val dayEntries = entries.filter { it.date == date }
        for (entry in dayEntries) {
            val startMin = (entry.startTime.hour - hourStart) * 60 + entry.startTime.minute
            val durationMin = ((entry.endTime.hour - entry.startTime.hour) * 60 + (entry.endTime.minute - entry.startTime.minute)).coerceAtLeast(35)

            val topOffsetDp = (startMin.toFloat() / 60f) * hourHeightDp.value + 8f
            val cardHeightDp = (durationMin.toFloat() / 60f) * hourHeightDp.value - 4f

            val color = Color(entry.type.colorHex)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 54.dp, end = 12.dp)
                    .offset(y = topOffsetDp.dp)
                    .height(cardHeightDp.coerceAtLeast(44f).dp)
                    .clickable { onEntryClick(entry) },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (entry.isCancelled) MaterialTheme.colorScheme.surfaceVariant else color.copy(alpha = 0.16f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = if (entry.isCancelled) MaterialTheme.colorScheme.outline else color.copy(alpha = 0.6f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(if (entry.isCancelled) DangerRed else color)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = entry.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (entry.isCancelled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                                textDecoration = if (entry.isCancelled) TextDecoration.LineThrough else null,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
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
                        Text(
                            text = "${entry.startTime.format(timeFormatter)} - ${entry.endTime.format(timeFormatter)} • ${entry.location}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (!entry.teacherName.isNullOrBlank()) {
                            Text(
                                text = "Faculty: ${entry.teacherName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = color,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
