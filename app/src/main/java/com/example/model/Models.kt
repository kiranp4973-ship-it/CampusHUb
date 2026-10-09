package com.example.model

enum class UserRole {
    STUDENT,
    TEACHER,
    CLASS_REPRESENTATIVE,
    DEPARTMENT_ADMIN,
    COLLEGE_ADMIN
}

data class UserProfile(
    val id: String,
    val name: String,
    val usnOrRoll: String,
    val email: String,
    val avatarUrl: String? = null,
    val college: String,
    val course: String,
    val semester: Int,
    val section: String,
    val role: UserRole = UserRole.STUDENT,
    val bio: String = "Computer Science sophomore @ BIT | Enthusiastic about Algorithms & Mobile Systems",
    val skills: List<String> = listOf("Kotlin", "Java", "Data Structures", "Jetpack Compose", "Git"),
    val studyInterests: List<String> = listOf("System Design", "Cloud Native", "Competitive Programming"),
    val status: FriendStatus = FriendStatus.AVAILABLE,
    val customStatusNote: String = "Studying for OS Internal 1"
)

enum class FriendStatus(val label: String, val emoji: String) {
    IN_CLASS("In Class", "🏫"),
    LIBRARY("Library", "📚"),
    CANTEEN("Canteen", "☕"),
    LAB("In Lab", "💻"),
    GOING_HOME("Going Home", "🚌"),
    AVAILABLE("Available to Study", "🟢"),
    BUSY("Busy / DND", "🔴")
}

data class Subject(
    val id: String,
    val code: String,
    val name: String,
    val shortName: String,
    val teacherName: String,
    val room: String,
    val credits: Int,
    val colorHex: Long = 0xFF2563EB
)

data class AttendanceRecord(
    val subjectId: String,
    val totalClasses: Int,
    val presentCount: Int,
    val requiredPercentage: Double = 75.0,
    val recentLog: List<AttendanceLogEntry> = emptyList()
) {
    val absentCount: Int get() = (totalClasses - presentCount).coerceAtLeast(0)
    val percentage: Double
        get() = if (totalClasses == 0) 100.0 else (presentCount.toDouble() / totalClasses.toDouble()) * 100.0

    // Attendance predictor helpers
    fun percentageIfAttendNext(additionalClasses: Int): Double {
        val newTotal = totalClasses + additionalClasses
        val newPresent = presentCount + additionalClasses
        return if (newTotal == 0) 100.0 else (newPresent.toDouble() / newTotal) * 100.0
    }

    fun canBunkNextClasses(): Int {
        // Find max b such that (presentCount) / (totalClasses + b) >= requiredPercentage / 100
        val req = requiredPercentage / 100.0
        val maxTotal = (presentCount / req).toInt()
        val bunks = maxTotal - totalClasses
        return bunks.coerceAtLeast(0)
    }

    fun classesNeededToReachRequired(): Int {
        if (percentage >= requiredPercentage) return 0
        val req = requiredPercentage / 100.0
        // (presentCount + x) / (totalClasses + x) >= req
        // presentCount + x >= req * totalClasses + req * x
        // x * (1 - req) >= req * totalClasses - presentCount
        // x >= (req * totalClasses - presentCount) / (1 - req)
        val needed = Math.ceil((req * totalClasses - presentCount) / (1.0 - req)).toInt()
        return needed.coerceAtLeast(0)
    }
}

data class AttendanceLogEntry(
    val id: String,
    val date: String,
    val time: String,
    val isPresent: Boolean
)

data class TimetableSlot(
    val id: String,
    val dayOfWeek: Int, // 1 = Monday ... 6 = Saturday (or 7 = Sunday)
    val startTime: String, // "09:00"
    val endTime: String,   // "10:00"
    val subjectId: String,
    val room: String,
    val type: String = "Lecture", // Lecture, Lab, Tutorial, Seminar
    val isCancelled: Boolean = false,
    val section: String = "A",
    val semester: Int = 3,
    val isOfficial: Boolean = true,
    val notes: String = ""
)

enum class TopicStatus {
    NOT_STARTED,
    STUDYING,
    COMPLETED
}

data class SyllabusTopic(
    val id: String,
    val title: String,
    val status: TopicStatus = TopicStatus.NOT_STARTED
)

data class SyllabusModule(
    val id: String,
    val moduleNumber: Int,
    val title: String,
    val topics: List<SyllabusTopic>
)

data class SubjectSyllabus(
    val subjectId: String,
    val modules: List<SyllabusModule>
)

data class NoteItem(
    val id: String,
    val title: String,
    val subjectId: String,
    val moduleNumber: Int,
    val uploadedBy: String,
    val date: String,
    val description: String,
    val fileType: String, // "PDF", "IMAGE", "DOC", "TEXT"
    val usefulCount: Int = 12,
    val notUsefulCount: Int = 1,
    val isBookmarked: Boolean = false,
    val isUserVotedUseful: Boolean? = null,
    val contentText: String? = null
)

enum class ExamType {
    INTERNAL_1,
    INTERNAL_2,
    INTERNAL_3,
    PRACTICAL,
    SEMESTER
}

data class ExamItem(
    val id: String,
    val title: String,
    val type: ExamType,
    val subjectId: String,
    val date: String, // e.g. "2026-10-14"
    val time: String, // e.g. "09:30 AM"
    val room: String,
    val syllabusSummary: String,
    val maxMarks: Int = 50,
    val instructions: String = "Bring college ID, admit card & blue/black pen. Calculators allowed."
)

data class AssignmentItem(
    val id: String,
    val title: String,
    val subjectId: String,
    val description: String,
    val dueDate: String,
    val status: AssignmentStatus = AssignmentStatus.IN_PROGRESS
)

enum class AssignmentStatus(val label: String) {
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    LATE("Late")
}

data class LabProgram(
    val id: String,
    val labNumber: Int,
    val subjectId: String,
    val title: String,
    val problemStatement: String,
    val language: String = "Java",
    val code: String,
    val explanation: String,
    val expectedOutput: String,
    val difficulty: String = "Medium",
    val isCompleted: Boolean = false
)

data class BookItem(
    val id: String,
    val title: String,
    val author: String,
    val edition: String,
    val subjectId: String,
    val description: String,
    val type: String, // "Textbook", "Reference", "Programming", "Previous Papers"
    val isAvailableInLibrary: Boolean = true
)

data class QuestionPaper(
    val id: String,
    val subjectId: String,
    val examType: String, // "Semester End Exam", "Internal 1"
    val year: Int,
    val semester: Int,
    val frequentlyAskedTopics: List<String> = emptyList(),
    val fileUrl: String = ""
)

data class StudyTask(
    val id: String,
    val title: String,
    val subjectId: String,
    val allocatedMinutes: Int = 30,
    val isCompleted: Boolean = false,
    val date: String = "Today"
)

data class Announcement(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val authorName: String,
    val authorRole: String, // "Department HOD", "Class Representative", "Exam Cell"
    val scope: String = "Department", // Class, Section, Department, College
    val isRead: Boolean = false
)

data class StudyGroup(
    val id: String,
    val name: String,
    val subjectId: String,
    val memberCount: Int,
    val description: String,
    val latestMessage: String,
    val latestMessageTime: String
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val content: String,
    val timestamp: String,
    val isFromMe: Boolean = false,
    val attachmentType: String? = null // "IMAGE", "PDF", "AUDIO", "LOCATION"
)

data class LostAndFoundItem(
    val id: String,
    val isLost: Boolean, // true = LOST, false = FOUND
    val title: String,
    val category: String, // "ID card", "Calculator", "Book", "Bag", "USB drive", "Other"
    val location: String,
    val date: String,
    val description: String,
    val contactMethod: String,
    val postedByName: String
)

data class ProjectTeamPost(
    val id: String,
    val projectTitle: String,
    val requiredSkills: List<String>,
    val membersNeeded: Int,
    val description: String,
    val postedByName: String,
    val courseSection: String
)

data class ExpenseItem(
    val id: String,
    val title: String,
    val amount: Double,
    val paidBy: String,
    val splitBetween: List<String>,
    val date: String
)

data class ExpenseGroup(
    val id: String,
    val title: String,
    val members: List<String>,
    val expenses: List<ExpenseItem>
)

data class AcademicEvent(
    val id: String,
    val title: String,
    val date: String, // "2026-10-15"
    val type: AcademicEventType,
    val time: String? = null,
    val endTime: String? = null,
    val details: String = "",
    val location: String = "",
    val subjectId: String? = null,
    val reminderMinutes: Int = 15,
    val isRecurringWeekly: Boolean = false,
    val isUserCreated: Boolean = false
)

enum class AcademicEventType(val label: String, val colorHex: Long) {
    CLASS("Class Lecture", 0xFF2563EB),
    LAB("Laboratory Session", 0xFF0D9488),
    INTERNAL("Internal Exam", 0xFFE11D48),
    SEMESTER_EXAM("Semester Exam", 0xFF9333EA),
    HOLIDAY("College Holiday", 0xFF16A34A),
    ASSIGNMENT("Assignment Due", 0xFFEA580C),
    LAB_SUBMISSION("Lab Submission", 0xFF0284C7),
    COLLEGE_EVENT("Campus Event", 0xFF8B5CF6),
    PERSONAL_TASK("Personal Study Goal", 0xFF64748B)
}
