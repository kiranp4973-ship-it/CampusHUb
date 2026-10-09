package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DiscoveryScope
import com.example.model.InstitutionHierarchy
import com.example.model.UserProfile
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.CampusViewModel

/**
 * Data structure for drawer navigation item
 */
data class DrawerItemData(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val badge: String? = null,
    val badgeColor: Color = PrimaryBlue,
    val isSelected: Boolean = false,
    val onClick: () -> Unit
)

/**
 * Data structure for drawer expandable section
 */
data class DrawerSectionData(
    val key: String,
    val title: String,
    val icon: ImageVector,
    val iconTint: Color,
    val items: List<DrawerItemData>
)

/**
 * CampusHub Professional Left Navigation Drawer Content (ReadEra inspired)
 */
@Composable
fun CampusHubDrawerContent(
    user: UserProfile,
    hierarchy: InstitutionHierarchy,
    viewModel: CampusViewModel,
    onCloseDrawer: () -> Unit,
    onOpenInstitutionDialog: () -> Unit,
    onOpenNotificationsModal: () -> Unit,
    onOpenFocusTimer: () -> Unit,
    onOpenStudyGoals: () -> Unit,
    onOpenCampusInfo: () -> Unit,
    onOpenFileManager: () -> Unit,
    onOpenHelpSupport: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val studyTab by viewModel.studyTab.collectAsState()
    val communityTab by viewModel.communityTab.collectAsState()
    val currentSubScreen by viewModel.currentSubScreen.collectAsState()

    // Track expansion state for each section. Default HOME, ACADEMICS, PRODUCTIVITY to expanded.
    val expandedSections = remember {
        mutableStateMapOf(
            "HOME" to true,
            "ACADEMICS" to true,
            "PRODUCTIVITY" to true,
            "CAMPUS_LIFE" to false,
            "COMMUNITY" to false,
            "CAREER" to false,
            "TOOLS" to false
        )
    }

    // Build the sections according to specifications
    val sections = remember(currentTab, studyTab, communityTab, currentSubScreen) {
        listOf(
            DrawerSectionData(
                key = "HOME",
                title = "HOME",
                icon = Icons.Default.Dashboard,
                iconTint = PrimaryBlue,
                items = listOf(
                    DrawerItemData(
                        id = "drawer_dashboard",
                        title = "Dashboard",
                        icon = Icons.Default.Home,
                        isSelected = currentTab == CampusViewModel.MainTab.HOME && currentSubScreen == CampusViewModel.SubScreen.NONE,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.HOME)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_my_profile",
                        title = "My Profile",
                        icon = Icons.Default.Person,
                        isSelected = currentTab == CampusViewModel.MainTab.PROFILE,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.PROFILE)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_my_institution",
                        title = "My Institution",
                        icon = Icons.Default.AccountBalance,
                        badge = hierarchy.stateOrRegion,
                        onClick = {
                            onOpenInstitutionDialog()
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_notifications",
                        title = "Notifications",
                        icon = Icons.Default.Notifications,
                        badge = "5 New",
                        badgeColor = Color(0xFFE11D48),
                        onClick = {
                            onOpenNotificationsModal()
                            onCloseDrawer()
                        }
                    )
                )
            ),
            DrawerSectionData(
                key = "ACADEMICS",
                title = "ACADEMICS",
                icon = Icons.Default.School,
                iconTint = Color(0xFF2563EB),
                items = listOf(
                    DrawerItemData(
                        id = "drawer_academic_calendar",
                        title = "Academic Calendar",
                        icon = Icons.Default.CalendarMonth,
                        isSelected = currentTab == CampusViewModel.MainTab.CALENDAR,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.CALENDAR)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_class_timetable",
                        title = "Class Timetable",
                        icon = Icons.Default.Schedule,
                        isSelected = currentTab == CampusViewModel.MainTab.CALENDAR,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.CALENDAR)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_attendance",
                        title = "Attendance",
                        icon = Icons.Default.FactCheck,
                        badge = "84%",
                        badgeColor = Color(0xFF16A34A),
                        isSelected = currentTab == CampusViewModel.MainTab.STUDY && studyTab == CampusViewModel.StudySectionTab.ATTENDANCE,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.ATTENDANCE)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_courses_subjects",
                        title = "Courses & Subjects",
                        icon = Icons.Default.MenuBook,
                        isSelected = currentTab == CampusViewModel.MainTab.STUDY && studyTab == CampusViewModel.StudySectionTab.COURSES,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.COURSES)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_syllabus",
                        title = "Syllabus",
                        icon = Icons.Default.ListAlt,
                        isSelected = currentTab == CampusViewModel.MainTab.STUDY && studyTab == CampusViewModel.StudySectionTab.COURSES,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.COURSES)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_notes_documents",
                        title = "Notes & Documents",
                        icon = Icons.Default.Description,
                        isSelected = currentTab == CampusViewModel.MainTab.STUDY && studyTab == CampusViewModel.StudySectionTab.NOTES,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.NOTES)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_books_reading",
                        title = "Books & Reading",
                        icon = Icons.Default.AutoStories,
                        isSelected = currentTab == CampusViewModel.MainTab.STUDY && studyTab == CampusViewModel.StudySectionTab.BOOKS,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.BOOKS)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_question_papers",
                        title = "Question Papers",
                        icon = Icons.Default.Quiz,
                        badge = "Solved",
                        isSelected = currentTab == CampusViewModel.MainTab.STUDY && studyTab == CampusViewModel.StudySectionTab.PAPERS,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.PAPERS)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_assignments",
                        title = "Assignments",
                        icon = Icons.Default.Assignment,
                        badge = "1 Due",
                        badgeColor = Color(0xFFEA580C),
                        isSelected = currentTab == CampusViewModel.MainTab.STUDY && studyTab == CampusViewModel.StudySectionTab.ASSIGNMENTS,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.ASSIGNMENTS)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_examinations_results",
                        title = "Examinations & Results",
                        icon = Icons.Default.Grade,
                        isSelected = currentTab == CampusViewModel.MainTab.STUDY && studyTab == CampusViewModel.StudySectionTab.EXAMS,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.EXAMS)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_practical_labs",
                        title = "Practical Labs",
                        icon = Icons.Default.Terminal,
                        isSelected = currentTab == CampusViewModel.MainTab.STUDY && studyTab == CampusViewModel.StudySectionTab.LABS,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.LABS)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_study_planner",
                        title = "Study Planner",
                        icon = Icons.Default.EventNote,
                        isSelected = currentTab == CampusViewModel.MainTab.STUDY && studyTab == CampusViewModel.StudySectionTab.TASKS,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.TASKS)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_flashcards_revision",
                        title = "Flashcards & Revision",
                        icon = Icons.Default.Style,
                        badge = "Sprint",
                        badgeColor = Color(0xFF8B5CF6),
                        isSelected = currentSubScreen == CampusViewModel.SubScreen.EXAM_MODE,
                        onClick = {
                            viewModel.navigateToSubScreen(CampusViewModel.SubScreen.EXAM_MODE)
                            onCloseDrawer()
                        }
                    )
                )
            ),
            DrawerSectionData(
                key = "PRODUCTIVITY",
                title = "PRODUCTIVITY",
                icon = Icons.Default.Lightbulb,
                iconTint = Color(0xFFEA580C),
                items = listOf(
                    DrawerItemData(
                        id = "drawer_my_tasks",
                        title = "My Tasks",
                        icon = Icons.Default.CheckCircle,
                        badge = "4 Left",
                        isSelected = currentTab == CampusViewModel.MainTab.STUDY && studyTab == CampusViewModel.StudySectionTab.TASKS,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.TASKS)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_focus_timer",
                        title = "Focus Timer",
                        icon = Icons.Default.Timer,
                        badge = "Pomodoro",
                        badgeColor = Color(0xFF0D9488),
                        onClick = {
                            onOpenFocusTimer()
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_study_goals",
                        title = "Study Goals",
                        icon = Icons.Default.TrackChanges,
                        badge = "74%",
                        onClick = {
                            onOpenStudyGoals()
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_bookmarks",
                        title = "Bookmarks",
                        icon = Icons.Default.Bookmark,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.NOTES)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_recent_activity",
                        title = "Recent Activity",
                        icon = Icons.Default.History,
                        onClick = {
                            onOpenNotificationsModal()
                            onCloseDrawer()
                        }
                    )
                )
            ),
            DrawerSectionData(
                key = "CAMPUS_LIFE",
                title = "CAMPUS LIFE",
                icon = Icons.Default.Festival,
                iconTint = Color(0xFF8B5CF6),
                items = listOf(
                    DrawerItemData(
                        id = "drawer_announcements",
                        title = "Announcements",
                        icon = Icons.Default.Campaign,
                        badge = "Campus",
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.PULSE,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.PULSE)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_college_events",
                        title = "College Events",
                        icon = Icons.Default.Festival,
                        isSelected = currentTab == CampusViewModel.MainTab.CALENDAR,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.CALENDAR)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_clubs_communities",
                        title = "Clubs & Communities",
                        icon = Icons.Default.Diversity3,
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.CLUBS,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.CLUBS)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_lost_and_found",
                        title = "Lost & Found",
                        icon = Icons.Default.Search,
                        badge = "Active",
                        badgeColor = Color(0xFFD97706),
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.LOST_FOUND,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.LOST_FOUND)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_campus_information",
                        title = "Campus Information",
                        icon = Icons.Default.Info,
                        onClick = {
                            onOpenCampusInfo()
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_student_services",
                        title = "Student Services",
                        icon = Icons.Default.SupportAgent,
                        onClick = {
                            onOpenCampusInfo()
                            onCloseDrawer()
                        }
                    )
                )
            ),
            DrawerSectionData(
                key = "COMMUNITY",
                title = "COMMUNITY",
                icon = Icons.Default.Groups,
                iconTint = Color(0xFF0284C7),
                items = listOf(
                    DrawerItemData(
                        id = "drawer_classmates",
                        title = "Classmates",
                        icon = Icons.Default.People,
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.FRIENDS,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.FRIENDS)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_friends",
                        title = "Friends",
                        icon = Icons.Default.Favorite,
                        badge = "Online",
                        badgeColor = Color(0xFF16A34A),
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.FRIENDS,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.FRIENDS)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_messages",
                        title = "Messages",
                        icon = Icons.Default.Forum,
                        badge = "3",
                        badgeColor = PrimaryBlue,
                        isSelected = currentSubScreen == CampusViewModel.SubScreen.CHAT_DETAIL,
                        onClick = {
                            viewModel.navigateToSubScreen(CampusViewModel.SubScreen.CHAT_DETAIL)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_study_groups",
                        title = "Study Groups",
                        icon = Icons.Default.GroupWork,
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.STUDY_GROUPS,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.STUDY_GROUPS)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_discussion_forums",
                        title = "Discussion Forums",
                        icon = Icons.Default.QuestionAnswer,
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.PULSE,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.PULSE)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_project_teams",
                        title = "Project Teams",
                        icon = Icons.Default.Hub,
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.PROJECTS,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.PROJECTS)
                            onCloseDrawer()
                        }
                    )
                )
            ),
            DrawerSectionData(
                key = "CAREER",
                title = "CAREER & OPPORTUNITIES",
                icon = Icons.Default.Work,
                iconTint = Color(0xFF059669),
                items = listOf(
                    DrawerItemData(
                        id = "drawer_scholarships",
                        title = "Scholarships",
                        icon = Icons.Default.CardGiftcard,
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.OPPORTUNITIES,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.OPPORTUNITIES)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_internships",
                        title = "Internships",
                        icon = Icons.Default.Work,
                        badge = "New",
                        badgeColor = Color(0xFF059669),
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.OPPORTUNITIES,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.OPPORTUNITIES)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_placements",
                        title = "Placements",
                        icon = Icons.Default.BusinessCenter,
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.OPPORTUNITIES,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.OPPORTUNITIES)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_competitions_hackathons",
                        title = "Competitions & Hackathons",
                        icon = Icons.Default.EmojiEvents,
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.OPPORTUNITIES,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.OPPORTUNITIES)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_certifications",
                        title = "Certifications",
                        icon = Icons.Default.Verified,
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.OPPORTUNITIES,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.OPPORTUNITIES)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_global_opportunities",
                        title = "Global Opportunities",
                        icon = Icons.Default.Public,
                        badge = "Global",
                        badgeColor = PrimaryBlue,
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.OPPORTUNITIES,
                        onClick = {
                            viewModel.setDiscoveryScope(DiscoveryScope.GLOBAL)
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.OPPORTUNITIES)
                            onCloseDrawer()
                        }
                    )
                )
            ),
            DrawerSectionData(
                key = "TOOLS",
                title = "TOOLS",
                icon = Icons.Default.Build,
                iconTint = Color(0xFF64748B),
                items = listOf(
                    DrawerItemData(
                        id = "drawer_global_search",
                        title = "Global Search",
                        icon = Icons.Default.Search,
                        isSelected = currentSubScreen == CampusViewModel.SubScreen.SEARCH,
                        onClick = {
                            viewModel.navigateToSubScreen(CampusViewModel.SubScreen.SEARCH)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_file_manager",
                        title = "File Manager",
                        icon = Icons.Default.FolderOpen,
                        onClick = {
                            onOpenFileManager()
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_shared_resources",
                        title = "Shared Resources",
                        icon = Icons.Default.Share,
                        isSelected = currentTab == CampusViewModel.MainTab.STUDY && studyTab == CampusViewModel.StudySectionTab.NOTES,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.STUDY)
                            viewModel.setStudyTab(CampusViewModel.StudySectionTab.NOTES)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_expense_splitter",
                        title = "Expense Splitter",
                        icon = Icons.Default.AttachMoney,
                        isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && communityTab == CampusViewModel.CommunitySectionTab.EXPENSES,
                        onClick = {
                            viewModel.setTab(CampusViewModel.MainTab.COMMUNITY)
                            viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.EXPENSES)
                            onCloseDrawer()
                        }
                    ),
                    DrawerItemData(
                        id = "drawer_help_support",
                        title = "Help & Support",
                        icon = Icons.Default.HelpOutline,
                        onClick = {
                            onOpenHelpSupport()
                            onCloseDrawer()
                        }
                    )
                )
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .widthIn(max = 340.dp)
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // --- DRAWER HEADER ---
        DrawerHeaderSection(
            user = user,
            hierarchy = hierarchy,
            onProfileClick = {
                viewModel.setTab(CampusViewModel.MainTab.PROFILE)
                onCloseDrawer()
            },
            onSettingsClick = {
                viewModel.navigateToSubScreen(CampusViewModel.SubScreen.SETTINGS)
                onCloseDrawer()
            }
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            thickness = 1.dp
        )

        // --- EXPANDABLE LIST OF SECTIONS ---
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            sections.forEach { section ->
                val isExpanded = expandedSections[section.key] ?: false

                item(key = "section_${section.key}") {
                    DrawerSectionHeader(
                        title = section.title,
                        icon = section.icon,
                        iconTint = section.iconTint,
                        itemCount = section.items.size,
                        isExpanded = isExpanded,
                        onToggle = {
                            expandedSections[section.key] = !isExpanded
                        }
                    )
                }

                item(key = "items_${section.key}") {
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            section.items.forEach { item ->
                                DrawerItemRow(item = item)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }

            // Bottom Footer
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "CampusHub Universal • v2.4",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Your Education. Your Campus. Your Future.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

/**
 * Drawer Header matching ReadEra-style with profile avatar, details, and shortcuts
 */
@Composable
private fun DrawerHeaderSection(
    user: UserProfile,
    hierarchy: InstitutionHierarchy,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // App Branding Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(PrimaryBlue, Color(0xFF0284C7))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "CampusHub Logo",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "CampusHub",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryBlue
                        )
                        Text(
                            text = "Universal Student Platform",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Quick Settings Shortcut
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("drawer_settings_shortcut")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings Shortcut",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Student Profile Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProfileClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Avatar with status dot
                Box(
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(PrimaryBlue, Color(0xFF6366F1))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    // Online presence badge
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF16A34A))
                            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = user.usnOrRoll,
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${hierarchy.courseOrBranch} • Sem ${hierarchy.semesterOrTerm}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // College Badge & Profile Shortcut Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProfileClick() }
                    .testTag("drawer_profile_shortcut"),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🏫 ${hierarchy.institutionName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "View Profile",
                        modifier = Modifier.size(16.dp),
                        tint = PrimaryBlue
                    )
                }
            }
        }
    }
}

/**
 * Expandable section header with category label, icon, badge, and rotating chevron
 */
@Composable
private fun DrawerSectionHeader(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    itemCount: Int,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation_$title"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .testTag("section_header_$title"),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = 0.8.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Item count indicator
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "$itemCount",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(rotationAngle),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Individual drawer item row with rounded active pill, icon, title, and optional badge
 */
@Composable
private fun DrawerItemRow(
    item: DrawerItemData
) {
    val containerColor = if (item.isSelected) {
        PrimaryBlue.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    val contentColor = if (item.isSelected) {
        PrimaryBlue
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = item.onClick)
            .testTag(item.id),
        color = containerColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (item.isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (item.badge != null) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = item.badgeColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = item.badge,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = item.badgeColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
