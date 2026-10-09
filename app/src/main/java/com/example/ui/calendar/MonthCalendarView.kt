package com.example.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrimaryBlue
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MonthCalendarView(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    entries: List<CalendarEntry>,
    onDateSelect: (LocalDate) -> Unit,
    onEntryClick: (CalendarEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOfWeekIndex = firstDayOfMonth.dayOfWeek.value - 1 // 0 for Monday ... 6 for Sunday
    val daysInMonth = currentMonth.lengthOfMonth()

    val totalCells = ((firstDayOfWeekIndex + daysInMonth + 6) / 7) * 7
    val today = LocalDate.now()

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 8.dp)) {
        // Week Header (Mon Tue Wed Thu Fri Sat Sun)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf("M", "T", "W", "T", "F", "S", "S").forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(36.dp)
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            items(totalCells) { index ->
                val dayOffset = index - firstDayOfWeekIndex + 1
                if (dayOffset in 1..daysInMonth) {
                    val date = currentMonth.atDay(dayOffset)
                    val isToday = date == today
                    val isSelected = date == selectedDate
                    val dayEntries = entries.filter { it.date == date }

                    Card(
                        modifier = Modifier
                            .aspectRatio(0.85f)
                            .padding(2.dp)
                            .clickable { onDateSelect(date) },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                isSelected -> PrimaryBlue.copy(alpha = 0.12f)
                                isToday -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                                else -> MaterialTheme.colorScheme.surface
                            }
                        ),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryBlue) else null
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (isToday) PrimaryBlue else Color.Transparent,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "$dayOffset",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isToday) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            // Miniature event chips/dots for this day
                            dayEntries.take(3).forEach { entry ->
                                val color = Color(entry.type.colorHex)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 1.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(color.copy(alpha = 0.2f))
                                        .padding(horizontal = 2.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = entry.title,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                        color = color,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            if (dayEntries.size > 3) {
                                Text(
                                    text = "+${dayEntries.size - 3} more",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    // Empty Cell outside month bounds
                    Box(modifier = Modifier.aspectRatio(0.85f).padding(2.dp))
                }
            }
        }
    }
}
