package com.example.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DangerRed
import com.example.ui.theme.PrimaryBlue
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun WeekGridView(
    weekStart: LocalDate,
    entries: List<CalendarEntry>,
    selectedDate: LocalDate,
    onDateSelect: (LocalDate) -> Unit,
    onEntryClick: (CalendarEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysOfWeek = (0..6).map { weekStart.plusDays(it.toLong()) }
    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()

    val hourStart = 8
    val hourEnd = 18
    val hourHeightDp = 64.dp
    val dayColumnWidth = 120.dp
    val timeColWidth = 48.dp

    val now = LocalTime.now()
    val today = LocalDate.now()

    Column(modifier = modifier.fillMaxSize()) {
        // Week Days Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Spacer(modifier = Modifier.width(timeColWidth))
            Row(
                modifier = Modifier
                    .horizontalScroll(horizontalScroll)
            ) {
                daysOfWeek.forEach { day ->
                    val isToday = day == today
                    val isSelected = day == selectedDate
                    Column(
                        modifier = Modifier
                            .width(dayColumnWidth)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onDateSelect(day) }
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = day.dayOfWeek.name.take(3),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isToday) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Surface(
                            shape = CircleShape,
                            color = when {
                                isSelected -> PrimaryBlue
                                isToday -> PrimaryBlue.copy(alpha = 0.15f)
                                else -> Color.Transparent
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${day.dayOfMonth}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        isSelected -> Color.White
                                        isToday -> PrimaryBlue
                                        else -> MaterialTheme.colorScheme.onSurface
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

        // Time Grid + Events Body
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Fixed Time Column
            Column(
                modifier = Modifier
                    .width(timeColWidth)
                    .verticalScroll(verticalScroll)
                    .padding(top = 4.dp)
            ) {
                for (hour in hourStart..hourEnd) {
                    Box(
                        modifier = Modifier
                            .width(timeColWidth)
                            .height(hourHeightDp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = String.format("%02d:00", hour),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Horizontally Scrollable 7-day grid
            Box(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(verticalScroll)
                    .horizontalScroll(horizontalScroll)
            ) {
                // Background hour lines & day dividers
                Row {
                    daysOfWeek.forEach { day ->
                        val isToday = day == today
                        Box(
                            modifier = Modifier
                                .width(dayColumnWidth)
                                .background(if (isToday) PrimaryBlue.copy(alpha = 0.03f) else Color.Transparent)
                        ) {
                            Column {
                                for (hour in hourStart..hourEnd) {
                                    Box(
                                        modifier = Modifier
                                            .width(dayColumnWidth)
                                            .height(hourHeightDp)
                                    ) {
                                        HorizontalDivider(
                                            modifier = Modifier.align(Alignment.TopStart),
                                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                                        )
                                    }
                                }
                            }
                            // Vertical border
                            VerticalDivider(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                            )
                        }
                    }
                }

                // Current Time Line
                if (today in daysOfWeek && now.hour in hourStart..hourEnd) {
                    val dayIndex = daysOfWeek.indexOf(today)
                    val totalMinutesFromStart = (now.hour - hourStart) * 60 + now.minute
                    val topOffsetDp = (totalMinutesFromStart.toFloat() / 60f) * hourHeightDp.value
                    val leftOffsetDp = (dayIndex * dayColumnWidth.value)

                    Row(
                        modifier = Modifier
                            .offset(x = leftOffsetDp.dp, y = topOffsetDp.dp)
                            .width(dayColumnWidth),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(shape = CircleShape, color = DangerRed, modifier = Modifier.size(6.dp)) {}
                        HorizontalDivider(color = DangerRed, thickness = 2.dp)
                    }
                }

                // Render Week Events
                entries.forEach { entry ->
                    val dayIndex = daysOfWeek.indexOf(entry.date)
                    if (dayIndex >= 0) {
                        val startMin = (entry.startTime.hour - hourStart) * 60 + entry.startTime.minute
                        val durationMin = ((entry.endTime.hour - entry.startTime.hour) * 60 + (entry.endTime.minute - entry.startTime.minute)).coerceAtLeast(30)

                        val topOffset = (startMin.toFloat() / 60f) * hourHeightDp.value
                        val height = ((durationMin.toFloat() / 60f) * hourHeightDp.value - 4f).coerceAtLeast(38f)
                        val leftOffset = (dayIndex * dayColumnWidth.value) + 4f
                        val color = Color(entry.type.colorHex)

                        Card(
                            modifier = Modifier
                                .offset(x = leftOffset.dp, y = topOffset.dp)
                                .width((dayColumnWidth.value - 8f).dp)
                                .height(height.dp)
                                .clickable { onEntryClick(entry) },
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (entry.isCancelled) MaterialTheme.colorScheme.surfaceVariant else color.copy(alpha = 0.18f)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (entry.isCancelled) MaterialTheme.colorScheme.outline else color.copy(alpha = 0.6f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 6.dp, vertical = 4.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = entry.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (entry.isCancelled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                                    textDecoration = if (entry.isCancelled) TextDecoration.LineThrough else null,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${entry.startTime} • ${entry.location}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
}
