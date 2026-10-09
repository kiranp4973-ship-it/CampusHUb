package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CampusRepository
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CampusViewModel(
    private val repository: CampusRepository = CampusRepository()
) : ViewModel() {

    // Main bottom navigation destinations (Google Calendar, Notion, Teams, Moodle inspiration)
    enum class MainTab {
        HOME,
        CALENDAR,
        STUDY,
        COMMUNITY,
        PROFILE
    }

    // Modal / Sub-views
    enum class SubScreen {
        NONE,
        SETTINGS,
        SEARCH,
        NOTE_SCANNER,
        EXAM_MODE,
        CHAT_DETAIL,
        EXPENSES,
        PROJECT_FINDER,
        LOST_AND_FOUND,
        INSTITUTION_SELECTOR,
        OPPORTUNITIES_HUB
    }

    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _currentSubScreen = MutableStateFlow(SubScreen.NONE)
    val currentSubScreen: StateFlow<SubScreen> = _currentSubScreen.asStateFlow()

    // Study section sub-tabs (Notion / Moodle style workspace)
    enum class StudySectionTab {
        COURSES,
        ATTENDANCE,
        NOTES,
        ASSIGNMENTS,
        EXAMS,
        LABS,
        TASKS,
        BOOKS,
        PAPERS
    }

    private val _studyTab = MutableStateFlow(StudySectionTab.COURSES)
    val studyTab: StateFlow<StudySectionTab> = _studyTab.asStateFlow()

    // Community section sub-tabs (Microsoft Teams style collaboration)
    enum class CommunitySectionTab {
        PULSE,
        FRIENDS,
        STUDY_GROUPS,
        PROJECTS,
        CLUBS,
        OPPORTUNITIES,
        LOST_FOUND,
        EXPENSES
    }

    private val _communityTab = MutableStateFlow(CommunitySectionTab.PULSE)
    val communityTab: StateFlow<CommunitySectionTab> = _communityTab.asStateFlow()

    // Discovery scope filter for opportunities
    private val _discoveryScope = MutableStateFlow(DiscoveryScope.LOCAL)
    val discoveryScope: StateFlow<DiscoveryScope> = _discoveryScope.asStateFlow()

    fun setDiscoveryScope(scope: DiscoveryScope) {
        _discoveryScope.value = scope
    }

    // Data streams from repository
    val currentUser = repository.currentUser
    val institutionHierarchy = repository.institutionHierarchy
    val opportunities = repository.opportunities
    val campusClubs = repository.campusClubs
    val subjects = repository.subjects
    val attendance = repository.attendance
    val timetable = repository.timetable
    val tasks = repository.tasks
    val syllabi = repository.syllabi
    val notes = repository.notes
    val exams = repository.exams
    val assignments = repository.assignments
    val labPrograms = repository.labPrograms
    val books = repository.books
    val questionPapers = repository.questionPapers
    val announcements = repository.announcements
    val friends = repository.friends
    val studyGroups = repository.studyGroups
    val chatMessages = repository.chatMessages
    val lostAndFound = repository.lostAndFound
    val projectTeams = repository.projectTeams
    val expenses = repository.expenses
    val calendarEvents = repository.calendarEvents

    // Global Search State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setTab(tab: MainTab) {
        _currentTab.value = tab
        _currentSubScreen.value = SubScreen.NONE
    }

    fun navigateToSubScreen(subScreen: SubScreen) {
        _currentSubScreen.value = subScreen
    }

    fun closeSubScreen() {
        _currentSubScreen.value = SubScreen.NONE
    }

    fun setStudyTab(tab: StudySectionTab) {
        _studyTab.value = tab
    }

    fun setCommunityTab(tab: CommunitySectionTab) {
        _communityTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Actions
    fun markAttendance(subjectId: String, isPresent: Boolean) {
        repository.markAttendance(subjectId, isPresent)
    }

    fun toggleTask(taskId: String) {
        repository.toggleTaskCompletion(taskId)
    }

    fun addTask(title: String, subjectId: String, minutes: Int) {
        repository.addTask(title, subjectId, minutes)
    }

    fun updateTopicStatus(subjectId: String, moduleId: String, topicId: String, status: TopicStatus) {
        repository.updateTopicStatus(subjectId, moduleId, topicId, status)
    }

    fun voteNote(noteId: String, isUseful: Boolean) {
        repository.voteNoteUseful(noteId, isUseful)
    }

    fun addNote(title: String, subjectId: String, module: Int, desc: String, content: String) {
        repository.addNote(title, subjectId, module, desc, content)
    }

    fun sendChat(text: String) {
        repository.sendChatMessage(text)
    }

    fun updateStatus(status: FriendStatus, note: String) {
        repository.updateUserStatus(status, note)
    }

    fun addLostAndFound(isLost: Boolean, title: String, cat: String, loc: String, desc: String, contact: String) {
        repository.addLostAndFoundItem(isLost, title, cat, loc, desc, contact)
    }

    fun addExpense(title: String, amount: Double, paidBy: String) {
        repository.addExpense(title, amount, paidBy)
    }

    fun markAnnouncementRead(id: String) {
        repository.markAnnouncementRead(id)
    }

    // Calendar View Options
    enum class CalendarViewMode {
        DAY,
        WEEK,
        MONTH,
        AGENDA
    }

    private val _calendarViewMode = MutableStateFlow(CalendarViewMode.WEEK)
    val calendarViewMode: StateFlow<CalendarViewMode> = _calendarViewMode.asStateFlow()

    enum class CalendarEventTypeFilter {
        ALL,
        CLASSES_ONLY,
        EXAMS_ONLY,
        ASSIGNMENTS_ONLY,
        HOLIDAYS_ONLY,
        PERSONAL_ONLY
    }

    private val _calendarFilter = MutableStateFlow(CalendarEventTypeFilter.ALL)
    val calendarFilter: StateFlow<CalendarEventTypeFilter> = _calendarFilter.asStateFlow()

    fun setCalendarViewMode(mode: CalendarViewMode) {
        _calendarViewMode.value = mode
    }

    fun setCalendarFilter(filter: CalendarEventTypeFilter) {
        _calendarFilter.value = filter
    }

    fun addAcademicEvent(event: AcademicEvent) {
        repository.addAcademicEvent(event)
    }

    fun updateAcademicEvent(event: AcademicEvent) {
        repository.updateAcademicEvent(event)
    }

    fun deleteAcademicEvent(eventId: String) {
        repository.deleteAcademicEvent(eventId)
    }

    fun addTimetableSlot(slot: TimetableSlot) {
        repository.addTimetableSlot(slot)
    }

    fun updateTimetableSlot(slot: TimetableSlot) {
        repository.updateTimetableSlot(slot)
    }

    fun deleteTimetableSlot(slotId: String) {
        repository.deleteTimetableSlot(slotId)
    }

    fun toggleClassCancelled(slotId: String) {
        repository.toggleClassCancelled(slotId)
    }

    fun updateInstitutionHierarchy(hierarchy: InstitutionHierarchy) {
        repository.updateInstitutionHierarchy(hierarchy)
    }

    fun toggleOpportunitySaved(id: String) {
        repository.toggleOpportunitySaved(id)
    }

    fun toggleClubJoined(id: String) {
        repository.toggleClubJoined(id)
    }
}
