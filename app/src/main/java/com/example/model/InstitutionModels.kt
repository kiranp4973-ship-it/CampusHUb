package com.example.model

enum class DiscoveryScope(val label: String, val emoji: String) {
    LOCAL("Campus / Local", "🏫"),
    NATIONAL("National Opportunities", "🇮🇳"),
    GLOBAL("Global & International", "🌍")
}

data class InstitutionHierarchy(
    val country: String = "India",
    val stateOrRegion: String = "Karnataka",
    val universityOrBoard: String = "Visvesvaraya Technological University (VTU)",
    val institutionName: String = "Bangalore Institute of Technology",
    val campus: String = "Main Campus (K.R. Road, Bengaluru)",
    val department: String = "Department of Computer Science & Engineering",
    val program: String = "Bachelor of Engineering (B.E.)",
    val courseOrBranch: String = "Computer Science and Engineering",
    val academicYear: String = "2026-2027",
    val termSystem: TermSystem = TermSystem.SEMESTER,
    val semesterOrTerm: Int = 3,
    val sectionOrBatch: String = "Section A",
    val gradingSystem: GradingSystem = GradingSystem.CGPA_10_POINT,
    val minAttendanceThreshold: Double = 75.0
)

enum class TermSystem(val label: String) {
    SEMESTER("Semester System (1-8)"),
    TRIMESTER("Trimester System (1-12)"),
    ANNUAL("Annual Academic Year"),
    CUSTOM_TERM("Modular / Quarter Term")
}

enum class GradingSystem(val label: String) {
    CGPA_10_POINT("10.0 CGPA Scale"),
    GPA_4_POINT("4.0 GPA Scale (US / International)"),
    PERCENTAGE("Percentage (0-100%)"),
    LETTER_GRADE("Letter Grades (A+, A, B, etc.)")
}

data class OpportunityItem(
    val id: String,
    val title: String,
    val organization: String,
    val scope: DiscoveryScope,
    val category: String, // "Scholarship", "Internship", "Hackathon", "Certification", "Research", "Competition"
    val deadline: String,
    val stipendOrAward: String,
    val eligibility: String,
    val description: String,
    val officialUrl: String,
    val isSaved: Boolean = false,
    val verifiedBadge: Boolean = true
)

data class ProjectItem(
    val id: String,
    val title: String,
    val tagLine: String,
    val category: String,
    val requiredSkills: List<String>,
    val membersNeeded: Int,
    val currentMembers: List<String>,
    val milestones: List<ProjectMilestone>,
    val githubUrl: String? = null,
    val createdByName: String,
    val department: String
)

data class ProjectMilestone(
    val id: String,
    val title: String,
    val dueDate: String,
    val isCompleted: Boolean = false
)

data class CampusClubItem(
    val id: String,
    val name: String,
    val category: String, // "Technical", "Cultural", "Sports", "Robotics", "E-Cell"
    val memberCount: Int,
    val description: String,
    val leadName: String,
    val upcomingEvent: String,
    val isJoined: Boolean = false
)
