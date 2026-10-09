package com.example.ui.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.model.AcademicEvent
import com.example.model.AcademicEventType
import com.example.model.Subject
import com.example.model.TimetableSlot
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventDialog(
    subjects: List<Subject>,
    defaultDate: LocalDate,
    canManageTimetable: Boolean,
    onDismiss: () -> Unit,
    onSaveEvent: (AcademicEvent) -> Unit,
    onSaveTimetableSlot: (TimetableSlot) -> Unit
) {
    var entryCategory by remember { mutableStateOf(if (canManageTimetable) "Class Timetable Slot" else "Academic Event / Task") }
    var title by remember { mutableStateOf("") }
    var selectedSubjectId by remember { mutableStateOf(subjects.firstOrNull()?.id ?: "") }
    var selectedDateStr by remember { mutableStateOf(defaultDate.toString()) }
    var startTimeStr by remember { mutableStateOf("09:00") }
    var endTimeStr by remember { mutableStateOf("10:00") }
    var locationStr by remember { mutableStateOf("LH-204") }
    var detailsStr by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(AcademicEventType.PERSONAL_TASK) }
    var selectedDayOfWeek by remember { mutableIntStateOf(defaultDate.dayOfWeek.value) }

    val dayNames = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add to Academic Calendar") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (canManageTimetable) {
                    Text("Entry Category:", style = MaterialTheme.typography.labelMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = entryCategory == "Class Timetable Slot",
                            onClick = { entryCategory = "Class Timetable Slot" },
                            label = { Text("Timetable Slot") }
                        )
                        FilterChip(
                            selected = entryCategory == "Academic Event / Task",
                            onClick = { entryCategory = "Academic Event / Task" },
                            label = { Text("Event / Task") }
                        )
                    }
                }

                if (entryCategory == "Class Timetable Slot") {
                    Text("Day of Week:", style = MaterialTheme.typography.labelMedium)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        (1..6).forEach { d ->
                            FilterChip(
                                selected = selectedDayOfWeek == d,
                                onClick = { selectedDayOfWeek = d },
                                label = { Text(dayNames[d - 1].take(3)) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = locationStr,
                        onValueChange = { locationStr = it },
                        label = { Text("Room / Lab Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Event Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = selectedDateStr,
                        onValueChange = { selectedDateStr = it },
                        label = { Text("Date (YYYY-MM-DD)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Event Type:", style = MaterialTheme.typography.labelMedium)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(
                            AcademicEventType.PERSONAL_TASK,
                            AcademicEventType.ASSIGNMENT,
                            AcademicEventType.INTERNAL
                        ).forEach { t ->
                            FilterChip(
                                selected = selectedType == t,
                                onClick = { selectedType = t },
                                label = { Text(t.label.split(" ").first()) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = locationStr,
                        onValueChange = { locationStr = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Associated Subject
                Text("Associated Subject:", style = MaterialTheme.typography.labelMedium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    subjects.take(4).forEach { sub ->
                        FilterChip(
                            selected = selectedSubjectId == sub.id,
                            onClick = { selectedSubjectId = sub.id },
                            label = { Text(sub.shortName) }
                        )
                    }
                }

                // Time Pickers
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = startTimeStr,
                        onValueChange = { startTimeStr = it },
                        label = { Text("Start Time") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = endTimeStr,
                        onValueChange = { endTimeStr = it },
                        label = { Text("End Time") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = detailsStr,
                    onValueChange = { detailsStr = it },
                    label = { Text("Description / Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (entryCategory == "Class Timetable Slot") {
                        val newSlot = TimetableSlot(
                            id = "slot_${System.currentTimeMillis()}",
                            dayOfWeek = selectedDayOfWeek,
                            startTime = startTimeStr,
                            endTime = endTimeStr,
                            subjectId = selectedSubjectId,
                            room = locationStr,
                            type = "Lecture",
                            isOfficial = true,
                            notes = detailsStr
                        )
                        onSaveTimetableSlot(newSlot)
                    } else {
                        val newEvent = AcademicEvent(
                            id = "ev_${System.currentTimeMillis()}",
                            title = if (title.isNotBlank()) title else "Personal Study Goal",
                            date = selectedDateStr,
                            type = selectedType,
                            time = startTimeStr,
                            endTime = endTimeStr,
                            details = detailsStr,
                            location = locationStr,
                            subjectId = selectedSubjectId,
                            isUserCreated = true
                        )
                        onSaveEvent(newEvent)
                    }
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
