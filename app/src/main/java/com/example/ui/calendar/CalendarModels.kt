package com.example.ui.calendar

import com.example.model.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Unified item rendered inside the Interactive Academic Calendar.
 * Combines Timetable lectures/labs with academic events (exams, assignments, holidays, events).
 */
data class CalendarEntry(
    val id: String,
    val title: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val location: String,
    val type: CalendarEntryType,
    val subjectId: String? = null,
    val teacherName: String? = null,
    val details: String = "",
    val isCancelled: Boolean = false,
    val isEditable: Boolean = false,
    val rawTimetableSlotId: String? = null,
    val rawAcademicEventId: String? = null
)

enum class CalendarEntryType(val label: String, val colorHex: Long) {
    LECTURE("Lecture", 0xFF2563EB),        // Primary Blue
    LAB("Lab Session", 0xFF0D9488),        // Teal
    TUTORIAL("Tutorial", 0xFF6366F1),      // Indigo
    INTERNAL_EXAM("Internal Exam", 0xFFE11D48), // Rose / Red
    SEMESTER_EXAM("Semester Exam", 0xFF9333EA), // Purple
    ASSIGNMENT("Assignment", 0xFFEA580C),   // Orange
    LAB_SUBMISSION("Lab Submission", 0xFF0284C7), // Sky Blue
    HOLIDAY("Holiday", 0xFF16A34A),        // Green
    CAMPUS_EVENT("Campus Event", 0xFF8B5CF6), // Violet
    PERSONAL_TASK("Personal Goal", 0xFF64748B) // Slate
}

object CalendarUtils {
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun parseTime(timeStr: String?, defaultTime: LocalTime): LocalTime {
        if (timeStr.isNullOrBlank()) return defaultTime
        return try {
            val clean = timeStr.trim().uppercase()
            if (clean.contains("AM") || clean.contains("PM")) {
                val isPm = clean.contains("PM")
                val parts = clean.replace("AM", "").replace("PM", "").trim().split(":")
                var hour = parts[0].trim().toInt()
                val min = if (parts.size > 1) parts[1].trim().toInt() else 0
                if (isPm && hour < 12) hour += 12
                if (!isPm && hour == 12) hour = 0
                LocalTime.of(hour.coerceIn(0, 23), min.coerceIn(0, 59))
            } else {
                val parts = clean.split(":")
                LocalTime.of(parts[0].trim().toInt().coerceIn(0, 23), if (parts.size > 1) parts[1].trim().toInt().coerceIn(0, 59) else 0)
            }
        } catch (e: Exception) {
            defaultTime
        }
    }

    /**
     * Resolves unified CalendarEntry list for a specific date range.
     * Generates recurring timetable slots for the weekdays inside this range (excluding holidays),
     * and maps academic events.
     */
    fun buildEntries(
        startDate: LocalDate,
        endDate: LocalDate,
        timetable: List<TimetableSlot>,
        events: List<AcademicEvent>,
        subjectsMap: Map<String, Subject>,
        canEditOfficialTimetable: Boolean = false
    ): List<CalendarEntry> {
        val result = mutableListOf<CalendarEntry>()

        // Find holiday dates to omit or mark regular timetable classes
        val holidayDates = events
            .filter { it.type == AcademicEventType.HOLIDAY }
            .mapNotNull {
                try { LocalDate.parse(it.date) } catch (e: Exception) { null }
            }.toSet()

        // 1. Generate Timetable slots for each day in range
        var curr = startDate
        while (!curr.isAfter(endDate)) {
            val isHoliday = holidayDates.contains(curr)
            if (!isHoliday && curr.dayOfWeek != DayOfWeek.SUNDAY) {
                // DayOfWeek value: 1 = Monday ... 6 = Saturday
                val dayNum = curr.dayOfWeek.value
                val slotsForDay = timetable.filter { it.dayOfWeek == dayNum }
                for (slot in slotsForDay) {
                    val sub = subjectsMap[slot.subjectId]
                    val start = parseTime(slot.startTime, LocalTime.of(9, 0))
                    val end = parseTime(slot.endTime, start.plusHours(1))
                    val entryType = when (slot.type.lowercase()) {
                        "lab" -> CalendarEntryType.LAB
                        "tutorial" -> CalendarEntryType.TUTORIAL
                        else -> CalendarEntryType.LECTURE
                    }
                    result.add(
                        CalendarEntry(
                            id = "slot_${slot.id}_$curr",
                            title = sub?.name ?: "Scheduled Class",
                            date = curr,
                            startTime = start,
                            endTime = end,
                            location = "Room ${slot.room}",
                            type = entryType,
                            subjectId = slot.subjectId,
                            teacherName = sub?.teacherName,
                            details = if (slot.notes.isNotEmpty()) slot.notes else "Regular ${slot.type} • Sem ${slot.semester} (${slot.section})",
                            isCancelled = slot.isCancelled,
                            isEditable = canEditOfficialTimetable,
                            rawTimetableSlotId = slot.id
                        )
                    )
                }
            }
            curr = curr.plusDays(1)
        }

        // 2. Add Academic Events
        for (event in events) {
            val eventDate = try {
                LocalDate.parse(event.date)
            } catch (e: Exception) {
                null
            } ?: continue

            if (eventDate.isBefore(startDate) || eventDate.isAfter(endDate)) continue

            val (start, end) = when (event.type) {
                AcademicEventType.HOLIDAY -> {
                    LocalTime.of(0, 0) to LocalTime.of(23, 59)
                }
                AcademicEventType.INTERNAL, AcademicEventType.SEMESTER_EXAM -> {
                    val s = parseTime(event.time, LocalTime.of(9, 30))
                    val e = parseTime(event.endTime, s.plusMinutes(90))
                    s to e
                }
                AcademicEventType.ASSIGNMENT, AcademicEventType.LAB_SUBMISSION -> {
                    val s = parseTime(event.time, LocalTime.of(17, 0))
                    s to s.plusMinutes(30)
                }
                else -> {
                    val s = parseTime(event.time, LocalTime.of(10, 0))
                    val e = parseTime(event.endTime, s.plusHours(1))
                    s to e
                }
            }

            val entryType = when (event.type) {
                AcademicEventType.INTERNAL -> CalendarEntryType.INTERNAL_EXAM
                AcademicEventType.SEMESTER_EXAM -> CalendarEntryType.SEMESTER_EXAM
                AcademicEventType.ASSIGNMENT -> CalendarEntryType.ASSIGNMENT
                AcademicEventType.LAB_SUBMISSION -> CalendarEntryType.LAB_SUBMISSION
                AcademicEventType.HOLIDAY -> CalendarEntryType.HOLIDAY
                AcademicEventType.COLLEGE_EVENT -> CalendarEntryType.CAMPUS_EVENT
                AcademicEventType.PERSONAL_TASK -> CalendarEntryType.PERSONAL_TASK
                AcademicEventType.LAB -> CalendarEntryType.LAB
                AcademicEventType.CLASS -> CalendarEntryType.LECTURE
            }

            val sub = event.subjectId?.let { subjectsMap[it] }

            result.add(
                CalendarEntry(
                    id = "event_${event.id}",
                    title = event.title,
                    date = eventDate,
                    startTime = start,
                    endTime = end,
                    location = if (event.location.isNotEmpty()) event.location else "Campus",
                    type = entryType,
                    subjectId = event.subjectId,
                    teacherName = sub?.teacherName,
                    details = event.details,
                    isCancelled = false,
                    isEditable = event.isUserCreated || canEditOfficialTimetable,
                    rawAcademicEventId = event.id
                )
            )
        }

        return result.sortedWith(compareBy({ it.date }, { it.startTime }))
    }
}
