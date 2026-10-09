package com.example.ui.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DiscoveryScope
import com.example.model.InstitutionHierarchy
import com.example.model.UserProfile
import com.example.ui.theme.*
import com.example.viewmodel.CampusViewModel

/**
 * ReadEra inspired left navigation drawer content with:
 * 1. Compact header with avatar, name, hierarchy, profile and settings shortcuts.
 * 2. Clean expandable accordion sections (Home, Academics, Productivity, Campus Life, Community, Career, Tools, Preferences).
 * 3. Bottom pinned area with About, Feedback, Help Center and Version Info.
 */
@Composable
fun AppNavigationDrawerContent(
    user: UserProfile,
    hierarchy: InstitutionHierarchy,
    currentTab: CampusViewModel.MainTab,
    currentStudyTab: CampusViewModel.StudySectionTab,
    currentCommunityTab: CampusViewModel.CommunitySectionTab,
    onNavigateHome: () -> Unit,
    onNavigateCalendar: (CampusViewModel.CalendarViewMode?) -> Unit,
    onNavigateStudyTab: (CampusViewModel.StudySectionTab) -> Unit,
    onNavigateCommunityTab: (CampusViewModel.CommunitySectionTab, DiscoveryScope?) -> Unit,
    onNavigateProfile: () -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigateExamMode: () -> Unit,
    onNavigateScanner: () -> Unit,
    onNavigateChat: () -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Track expanded state for each accordion section
    var expandedHome by remember { mutableStateOf(true) }
    var expandedAcademics by remember { mutableStateOf(true) }
    var expandedProductivity by remember { mutableStateOf(false) }
    var expandedCampusLife by remember { mutableStateOf(false) }
    var expandedCommunity by remember { mutableStateOf(false) }
    var expandedCareer by remember { mutableStateOf(false) }
    var expandedTools by remember { mutableStateOf(false) }
    var expandedPreferences by remember { mutableStateOf(false) }

    ModalDrawerSheet(
        modifier = modifier.widthIn(max = 330.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerContentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // 1. DRAWER HEADER (ReadEra style compact identity)
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    // CampusHub Brand Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PrimaryBlue,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "CampusHub",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                                Text(
                                    text = "Your Education. Your Future.",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Header Actions (Quick Profile & Settings icons)
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            IconButton(onClick = onNavigateProfile, modifier = Modifier.size(34.dp)) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profile",
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            IconButton(onClick = onNavigateSettings, modifier = Modifier.size(34.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Student Profile Card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable(onClick = onNavigateProfile)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = PrimaryBlue.copy(alpha = 0.15f),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = user.name.take(2).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${hierarchy.institutionName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${hierarchy.courseOrBranch} • Sem ${hierarchy.semesterOrTerm} (${hierarchy.sectionOrBatch})",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = BrandCoral,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // 2. SCROLLABLE ACCORDION MENU SECTIONS
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp)
            ) {
                // SECTION: HOME
                DrawerSectionHeader(
                    title = "HOME",
                    isExpanded = expandedHome,
                    onToggle = { expandedHome = !expandedHome }
                )
                AnimatedVisibility(visible = expandedHome) {
                    Column {
                        DrawerNavigationItem(
                            icon = Icons.Default.Dashboard,
                            label = "Dashboard",
                            isSelected = currentTab == CampusViewModel.MainTab.HOME,
                            onClick = onNavigateHome
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Person,
                            label = "My Profile",
                            isSelected = currentTab == CampusViewModel.MainTab.PROFILE,
                            onClick = onNavigateProfile
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.AccountBalance,
                            label = "My Institution & Hierarchy",
                            isSelected = false,
                            badge = hierarchy.termSystem.name.take(3),
                            onClick = onNavigateProfile
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Notifications,
                            label = "Notifications & Alerts",
                            isSelected = false,
                            badge = "3",
                            badgeColor = DangerRed,
                            onClick = { onShowToast("3 new schedule and assignment notifications") }
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))

                // SECTION: ACADEMICS
                DrawerSectionHeader(
                    title = "ACADEMICS",
                    isExpanded = expandedAcademics,
                    onToggle = { expandedAcademics = !expandedAcademics }
                )
                AnimatedVisibility(visible = expandedAcademics) {
                    Column {
                        DrawerNavigationItem(
                            icon = Icons.Default.CalendarMonth,
                            label = "Academic Calendar",
                            isSelected = currentTab == CampusViewModel.MainTab.CALENDAR,
                            onClick = { onNavigateCalendar(null) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Schedule,
                            label = "Class Timetable (Week View)",
                            isSelected = currentTab == CampusViewModel.MainTab.CALENDAR,
                            onClick = { onNavigateCalendar(CampusViewModel.CalendarViewMode.WEEK) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.CheckCircle,
                            label = "Attendance Tracker & Predictor",
                            isSelected = currentTab == CampusViewModel.MainTab.STUDY && currentStudyTab == CampusViewModel.StudySectionTab.ATTENDANCE,
                            badge = "78%",
                            badgeColor = SuccessGreen,
                            onClick = { onNavigateStudyTab(CampusViewModel.StudySectionTab.ATTENDANCE) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.MenuBook,
                            label = "Courses & Syllabus",
                            isSelected = currentTab == CampusViewModel.MainTab.STUDY && currentStudyTab == CampusViewModel.StudySectionTab.COURSES,
                            onClick = { onNavigateStudyTab(CampusViewModel.StudySectionTab.COURSES) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Description,
                            label = "Notes & Documents",
                            isSelected = currentTab == CampusViewModel.MainTab.STUDY && currentStudyTab == CampusViewModel.StudySectionTab.NOTES,
                            onClick = { onNavigateStudyTab(CampusViewModel.StudySectionTab.NOTES) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Assignment,
                            label = "Assignments & Submissions",
                            isSelected = currentTab == CampusViewModel.MainTab.STUDY && currentStudyTab == CampusViewModel.StudySectionTab.ASSIGNMENTS,
                            badge = "Due Soon",
                            badgeColor = BrandCoral,
                            onClick = { onNavigateStudyTab(CampusViewModel.StudySectionTab.ASSIGNMENTS) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.AssignmentLate,
                            label = "Examinations & Exam Mode",
                            isSelected = currentTab == CampusViewModel.MainTab.STUDY && currentStudyTab == CampusViewModel.StudySectionTab.EXAMS,
                            onClick = onNavigateExamMode
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Computer,
                            label = "Practical Labs & Coding",
                            isSelected = currentTab == CampusViewModel.MainTab.STUDY && currentStudyTab == CampusViewModel.StudySectionTab.LABS,
                            onClick = { onNavigateStudyTab(CampusViewModel.StudySectionTab.LABS) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.MenuBook,
                            label = "Textbooks & Library",
                            isSelected = currentTab == CampusViewModel.MainTab.STUDY && currentStudyTab == CampusViewModel.StudySectionTab.BOOKS,
                            onClick = { onNavigateStudyTab(CampusViewModel.StudySectionTab.BOOKS) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Article,
                            label = "Previous Question Papers",
                            isSelected = currentTab == CampusViewModel.MainTab.STUDY && currentStudyTab == CampusViewModel.StudySectionTab.PAPERS,
                            onClick = { onNavigateStudyTab(CampusViewModel.StudySectionTab.PAPERS) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.CameraAlt,
                            label = "Optical Note Scanner (OCR)",
                            isSelected = false,
                            onClick = onNavigateScanner
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))

                // SECTION: PRODUCTIVITY (Notion inspiration)
                DrawerSectionHeader(
                    title = "PRODUCTIVITY",
                    isExpanded = expandedProductivity,
                    onToggle = { expandedProductivity = !expandedProductivity }
                )
                AnimatedVisibility(visible = expandedProductivity) {
                    Column {
                        DrawerNavigationItem(
                            icon = Icons.Default.TaskAlt,
                            label = "My Tasks & Study Planner",
                            isSelected = currentTab == CampusViewModel.MainTab.STUDY && currentStudyTab == CampusViewModel.StudySectionTab.TASKS,
                            onClick = { onNavigateStudyTab(CampusViewModel.StudySectionTab.TASKS) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Timer,
                            label = "Focus Session & Study Sprint",
                            isSelected = false,
                            onClick = onNavigateExamMode
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Bookmark,
                            label = "Saved Items & Bookmarks",
                            isSelected = false,
                            onClick = { onShowToast("Saved bookmarks: 2 opportunities & 1 note") }
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))

                // SECTION: CAMPUS LIFE
                DrawerSectionHeader(
                    title = "CAMPUS LIFE",
                    isExpanded = expandedCampusLife,
                    onToggle = { expandedCampusLife = !expandedCampusLife }
                )
                AnimatedVisibility(visible = expandedCampusLife) {
                    Column {
                        DrawerNavigationItem(
                            icon = Icons.Default.Campaign,
                            label = "Class Pulse & Bulletins",
                            isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && currentCommunityTab == CampusViewModel.CommunitySectionTab.PULSE,
                            onClick = { onNavigateCommunityTab(CampusViewModel.CommunitySectionTab.PULSE, null) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Festival,
                            label = "Clubs & Societies",
                            isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && currentCommunityTab == CampusViewModel.CommunitySectionTab.CLUBS,
                            onClick = { onNavigateCommunityTab(CampusViewModel.CommunitySectionTab.CLUBS, null) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.FindInPage,
                            label = "Lost & Found Registry",
                            isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && currentCommunityTab == CampusViewModel.CommunitySectionTab.LOST_FOUND,
                            onClick = { onNavigateCommunityTab(CampusViewModel.CommunitySectionTab.LOST_FOUND, null) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.AttachMoney,
                            label = "Expense Splitter",
                            isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && currentCommunityTab == CampusViewModel.CommunitySectionTab.EXPENSES,
                            onClick = { onNavigateCommunityTab(CampusViewModel.CommunitySectionTab.EXPENSES, null) }
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))

                // SECTION: COMMUNITY (Teams inspiration)
                DrawerSectionHeader(
                    title = "COMMUNITY",
                    isExpanded = expandedCommunity,
                    onToggle = { expandedCommunity = !expandedCommunity }
                )
                AnimatedVisibility(visible = expandedCommunity) {
                    Column {
                        DrawerNavigationItem(
                            icon = Icons.Default.Groups,
                            label = "Classmates & Where Are They?",
                            isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && currentCommunityTab == CampusViewModel.CommunitySectionTab.FRIENDS,
                            onClick = { onNavigateCommunityTab(CampusViewModel.CommunitySectionTab.FRIENDS, null) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Chat,
                            label = "Study Messages & Chat",
                            isSelected = false,
                            badge = "Active",
                            badgeColor = SuccessGreen,
                            onClick = onNavigateChat
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Forum,
                            label = "Study Groups & Squads",
                            isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && currentCommunityTab == CampusViewModel.CommunitySectionTab.STUDY_GROUPS,
                            onClick = { onNavigateCommunityTab(CampusViewModel.CommunitySectionTab.STUDY_GROUPS, null) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.GroupWork,
                            label = "Project Teams & Teammate Finder",
                            isSelected = currentTab == CampusViewModel.MainTab.COMMUNITY && currentCommunityTab == CampusViewModel.CommunitySectionTab.PROJECTS,
                            onClick = { onNavigateCommunityTab(CampusViewModel.CommunitySectionTab.PROJECTS, null) }
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))

                // SECTION: CAREER & OPPORTUNITIES
                DrawerSectionHeader(
                    title = "CAREER & OPPORTUNITIES",
                    isExpanded = expandedCareer,
                    onToggle = { expandedCareer = !expandedCareer }
                )
                AnimatedVisibility(visible = expandedCareer) {
                    Column {
                        DrawerNavigationItem(
                            icon = Icons.Default.School,
                            label = "Local Campus Fellowships",
                            isSelected = false,
                            onClick = { onNavigateCommunityTab(CampusViewModel.CommunitySectionTab.OPPORTUNITIES, DiscoveryScope.LOCAL) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.EmojiEvents,
                            label = "National Hackathons & Grants",
                            isSelected = false,
                            badge = "SIH 2026",
                            badgeColor = BrandCoral,
                            onClick = { onNavigateCommunityTab(CampusViewModel.CommunitySectionTab.OPPORTUNITIES, DiscoveryScope.NATIONAL) }
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Public,
                            label = "Global Internships & GSoC",
                            isSelected = false,
                            badge = "DAAD / GSoC",
                            badgeColor = BrandViolet,
                            onClick = { onNavigateCommunityTab(CampusViewModel.CommunitySectionTab.OPPORTUNITIES, DiscoveryScope.GLOBAL) }
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))

                // SECTION: TOOLS & PREFERENCES
                DrawerSectionHeader(
                    title = "TOOLS & PREFERENCES",
                    isExpanded = expandedPreferences,
                    onToggle = { expandedPreferences = !expandedPreferences }
                )
                AnimatedVisibility(visible = expandedPreferences) {
                    Column {
                        DrawerNavigationItem(
                            icon = Icons.Default.Search,
                            label = "Global Campus Search",
                            isSelected = false,
                            onClick = onNavigateSearch
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Palette,
                            label = "Appearance & Theme",
                            isSelected = false,
                            onClick = onNavigateSettings
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Security,
                            label = "Privacy & Student Permissions",
                            isSelected = false,
                            onClick = onNavigateSettings
                        )
                        DrawerNavigationItem(
                            icon = Icons.Default.Tune,
                            label = "Institution Settings",
                            isSelected = false,
                            onClick = onNavigateProfile
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // 3. BOTTOM FOOTER AREA (About, Help, Version, Sign Out)
            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CampusHub v2.2.0 • Production",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Feedback",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryBlue,
                            modifier = Modifier.clickable { onShowToast("Thank you for your feedback! CampusHub support notified.") }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Role: ${user.role.name}",
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onShowToast("Signed out. Using local offline profile.") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = null,
                                tint = DangerRed,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Sign Out",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = DangerRed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerSectionHeader(
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val rotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "accordion")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.sp
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(18.dp)
                .rotate(rotation)
        )
    }
}

@Composable
private fun DrawerNavigationItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    badge: String? = null,
    badgeColor: Color = PrimaryBlue,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) PrimaryBlue.copy(alpha = 0.12f) else Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (badge != null) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = badgeColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        fontWeight = FontWeight.Bold,
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
