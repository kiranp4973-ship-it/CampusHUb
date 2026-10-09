package com.example.ui.calendar

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.UserRole
import com.example.ui.components.SectionHeader
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.CampusViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun TimetableCalendarScreen(
    viewModel: CampusViewModel,
    modifier: Modifier = Modifier
) {
    val timetable by viewModel.timetable.collectAsState()
    val events by viewModel.calendarEvents.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val syllabi by viewModel.syllabi.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val user by viewModel.currentUser.collectAsState()
    val viewMode by viewModel.calendarViewMode.collectAsState()
    val activeFilter by viewModel.calendarFilter.collectAsState()

    val subjectsMap = remember(subjects) { subjects.associateBy { it.id } }
    val syllabiMap = remember(syllabi) { syllabi.associateBy { it.subjectId } }

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    var showEventDetails by remember { mutableStateOf<CalendarEntry?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    // Can student edit official schedule? Only CR or Admin / Teachers can edit official schedule. Students can add personal events.
    val canManageOfficialTimetable = user.role == UserRole.CLASS_REPRESENTATIVE ||
            user.role == UserRole.TEACHER ||
            user.role == UserRole.DEPARTMENT_ADMIN

    // Week boundaries (Monday to Sunday)
    val weekStart = remember(selectedDate) {
        selectedDate.with(DayOfWeek.MONDAY)
    }
    val weekEnd = remember(weekStart) {
        weekStart.plusDays(6)
    }

    // Date range according to viewMode
    val (queryStart, queryEnd) = remember(viewMode, selectedDate, currentYearMonth) {
        when (viewMode) {
            CampusViewModel.CalendarViewMode.DAY -> selectedDate to selectedDate
            CampusViewModel.CalendarViewMode.WEEK -> weekStart to weekEnd
            CampusViewModel.CalendarViewMode.MONTH -> currentYearMonth.atDay(1) to currentYearMonth.atEndOfMonth()
            CampusViewModel.CalendarViewMode.AGENDA -> selectedDate to selectedDate.plusDays(21)
        }
    }

    // Unified Calendar Entries
    val allEntries = remember(queryStart, queryEnd, timetable, events, subjectsMap, canManageOfficialTimetable) {
        CalendarUtils.buildEntries(
            startDate = queryStart,
            endDate = queryEnd,
            timetable = timetable,
            events = events,
            subjectsMap = subjectsMap,
            canEditOfficialTimetable = canManageOfficialTimetable
        )
    }

    // Filtered entries
    val filteredEntries = remember(allEntries, activeFilter) {
        when (activeFilter) {
            CampusViewModel.CalendarEventTypeFilter.ALL -> allEntries
            CampusViewModel.CalendarEventTypeFilter.CLASSES_ONLY -> allEntries.filter {
                it.type == CalendarEntryType.LECTURE || it.type == CalendarEntryType.LAB || it.type == CalendarEntryType.TUTORIAL
            }
            CampusViewModel.CalendarEventTypeFilter.EXAMS_ONLY -> allEntries.filter {
                it.type == CalendarEntryType.INTERNAL_EXAM || it.type == CalendarEntryType.SEMESTER_EXAM
            }
            CampusViewModel.CalendarEventTypeFilter.ASSIGNMENTS_ONLY -> allEntries.filter {
                it.type == CalendarEntryType.ASSIGNMENT || it.type == CalendarEntryType.LAB_SUBMISSION
            }
            CampusViewModel.CalendarEventTypeFilter.HOLIDAYS_ONLY -> allEntries.filter {
                it.type == CalendarEntryType.HOLIDAY
            }
            CampusViewModel.CalendarEventTypeFilter.PERSONAL_ONLY -> allEntries.filter {
                it.type == CalendarEntryType.PERSONAL_TASK
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Top Navigation Header: Date title + Today button + Previous/Next + Add Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = when (viewMode) {
                        CampusViewModel.CalendarViewMode.DAY -> selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMM d"))
                        CampusViewModel.CalendarViewMode.WEEK -> "${weekStart.format(DateTimeFormatter.ofPattern("MMM d"))} - ${weekEnd.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))}"
                        CampusViewModel.CalendarViewMode.MONTH -> currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
                        CampusViewModel.CalendarViewMode.AGENDA -> "Upcoming Agenda (${selectedDate.format(DateTimeFormatter.ofPattern("MMM d"))})"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Section ${user.section} • Semester ${user.semester}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                // Today button
                FilledTonalButton(
                    onClick = {
                        val today = LocalDate.now()
                        selectedDate = today
                        currentYearMonth = YearMonth.now()
                    },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Today", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }

                // Prev Arrow
                IconButton(
                    onClick = {
                        when (viewMode) {
                            CampusViewModel.CalendarViewMode.DAY -> selectedDate = selectedDate.minusDays(1)
                            CampusViewModel.CalendarViewMode.WEEK -> selectedDate = selectedDate.minusWeeks(1)
                            CampusViewModel.CalendarViewMode.MONTH -> currentYearMonth = currentYearMonth.minusMonths(1)
                            CampusViewModel.CalendarViewMode.AGENDA -> selectedDate = selectedDate.minusWeeks(1)
                        }
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                }

                // Next Arrow
                IconButton(
                    onClick = {
                        when (viewMode) {
                            CampusViewModel.CalendarViewMode.DAY -> selectedDate = selectedDate.plusDays(1)
                            CampusViewModel.CalendarViewMode.WEEK -> selectedDate = selectedDate.plusWeeks(1)
                            CampusViewModel.CalendarViewMode.MONTH -> currentYearMonth = currentYearMonth.plusMonths(1)
                            CampusViewModel.CalendarViewMode.AGENDA -> selectedDate = selectedDate.plusWeeks(1)
                        }
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                }

                // Add Event / Class Button
                IconButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add Entry", tint = PrimaryBlue)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // View Mode Switcher: Day | Week | Month | Agenda
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            CampusViewModel.CalendarViewMode.values().forEachIndexed { index, mode ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = 4),
                    onClick = { viewModel.setCalendarViewMode(mode) },
                    selected = viewMode == mode
                ) {
                    Text(mode.name.lowercase().replaceFirstChar { it.uppercase() })
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Event Type Filters (Scrollable Chips)
        ScrollableTabRow(
            selectedTabIndex = activeFilter.ordinal,
            edgePadding = 0.dp,
            divider = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            CampusViewModel.CalendarEventTypeFilter.values().forEach { filter ->
                Tab(
                    selected = activeFilter == filter,
                    onClick = { viewModel.setCalendarFilter(filter) },
                    text = {
                        Text(
                            text = when (filter) {
                                CampusViewModel.CalendarEventTypeFilter.ALL -> "All (${allEntries.size})"
                                CampusViewModel.CalendarEventTypeFilter.CLASSES_ONLY -> "Classes"
                                CampusViewModel.CalendarEventTypeFilter.EXAMS_ONLY -> "Exams"
                                CampusViewModel.CalendarEventTypeFilter.ASSIGNMENTS_ONLY -> "Deadlines"
                                CampusViewModel.CalendarEventTypeFilter.HOLIDAYS_ONLY -> "Holidays"
                                CampusViewModel.CalendarEventTypeFilter.PERSONAL_ONLY -> "Personal"
                            },
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar Content Container
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (viewMode) {
                CampusViewModel.CalendarViewMode.DAY -> {
                    DayTimelineView(
                        date = selectedDate,
                        entries = filteredEntries,
                        onEntryClick = { showEventDetails = it }
                    )
                }
                CampusViewModel.CalendarViewMode.WEEK -> {
                    WeekGridView(
                        weekStart = weekStart,
                        entries = filteredEntries,
                        selectedDate = selectedDate,
                        onDateSelect = { selectedDate = it },
                        onEntryClick = { showEventDetails = it }
                    )
                }
                CampusViewModel.CalendarViewMode.MONTH -> {
                    MonthCalendarView(
                        currentMonth = currentYearMonth,
                        selectedDate = selectedDate,
                        entries = filteredEntries,
                        onDateSelect = {
                            selectedDate = it
                            // switch to day on tap in month view
                            viewModel.setCalendarViewMode(CampusViewModel.CalendarViewMode.DAY)
                        },
                        onEntryClick = { showEventDetails = it }
                    )
                }
                CampusViewModel.CalendarViewMode.AGENDA -> {
                    AgendaListView(
                        entries = filteredEntries,
                        onEntryClick = { showEventDetails = it }
                    )
                }
            }
        }
    }

    // Event Details BottomSheet
    showEventDetails?.let { entry ->
        val sub = entry.subjectId?.let { subjectsMap[it] }
        val syl = entry.subjectId?.let { syllabiMap[it] }
        val relatedNotes = remember(notes, entry.subjectId) {
            if (entry.subjectId != null) notes.filter { it.subjectId == entry.subjectId } else emptyList()
        }

        EventDetailsBottomSheet(
            entry = entry,
            subject = sub,
            syllabus = syl,
            relatedNotes = relatedNotes,
            canEdit = entry.isEditable || canManageOfficialTimetable,
            onDismiss = { showEventDetails = null },
            onToggleCancelled = {
                entry.rawTimetableSlotId?.let { slotId ->
                    viewModel.toggleClassCancelled(slotId)
                }
                showEventDetails = null
            },
            onDelete = {
                entry.rawAcademicEventId?.let { eventId ->
                    viewModel.deleteAcademicEvent(eventId)
                }
                entry.rawTimetableSlotId?.let { slotId ->
                    viewModel.deleteTimetableSlot(slotId)
                }
                showEventDetails = null
            }
        )
    }

    // Add / Edit Dialog
    if (showAddDialog) {
        AddEditEventDialog(
            subjects = subjects,
            defaultDate = selectedDate,
            canManageTimetable = canManageOfficialTimetable,
            onDismiss = { showAddDialog = false },
            onSaveEvent = { newEvent ->
                viewModel.addAcademicEvent(newEvent)
            },
            onSaveTimetableSlot = { newSlot ->
                viewModel.addTimetableSlot(newSlot)
            }
        )
    }
}
