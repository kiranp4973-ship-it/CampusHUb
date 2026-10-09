package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.CampusHubTheme
import com.example.ui.theme.PrimaryBlue
import com.example.viewmodel.CampusViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusHubTheme {
                val viewModel: CampusViewModel = viewModel()
                CampusHubApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusHubApp(
    viewModel: CampusViewModel
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val currentSubScreen by viewModel.currentSubScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val hierarchy by viewModel.institutionHierarchy.collectAsState()

    // Navigation Drawer State
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Drawer Feature Dialog States
    var showInstitutionDialog by remember { mutableStateOf(false) }
    var showNotificationsModal by remember { mutableStateOf(false) }
    var showFocusTimerDialog by remember { mutableStateOf(false) }
    var showStudyGoalsDialog by remember { mutableStateOf(false) }
    var showCampusInfoDialog by remember { mutableStateOf(false) }
    var showFileManagerDialog by remember { mutableStateOf(false) }
    var showHelpSupportDialog by remember { mutableStateOf(false) }

    // System Back Handler: first close drawer if open, else handle sub-screen
    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    if (currentSubScreen != CampusViewModel.SubScreen.NONE && !drawerState.isOpen) {
        BackHandler {
            viewModel.closeSubScreen()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentSubScreen == CampusViewModel.SubScreen.NONE,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.widthIn(max = 340.dp),
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerTonalElevation = 2.dp
            ) {
                CampusHubDrawerContent(
                    user = currentUser,
                    hierarchy = hierarchy,
                    viewModel = viewModel,
                    onCloseDrawer = {
                        coroutineScope.launch { drawerState.close() }
                    },
                    onOpenInstitutionDialog = { showInstitutionDialog = true },
                    onOpenNotificationsModal = { showNotificationsModal = true },
                    onOpenFocusTimer = { showFocusTimerDialog = true },
                    onOpenStudyGoals = { showStudyGoalsDialog = true },
                    onOpenCampusInfo = { showCampusInfoDialog = true },
                    onOpenFileManager = { showFileManagerDialog = true },
                    onOpenHelpSupport = { showHelpSupportDialog = true }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (currentSubScreen == CampusViewModel.SubScreen.NONE) {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        drawerState.open()
                                    }
                                },
                                modifier = Modifier.testTag("hamburger_menu_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Open Navigation Menu",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        title = {
                            Column {
                                Text(
                                    text = "CampusHub",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                                Text(
                                    text = "${hierarchy.institutionName} • ${hierarchy.courseOrBranch}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { viewModel.navigateToSubScreen(CampusViewModel.SubScreen.SEARCH) }) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "Global Search")
                            }
                            IconButton(onClick = { showNotificationsModal = true }) {
                                Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
                            }
                            IconButton(onClick = { viewModel.navigateToSubScreen(CampusViewModel.SubScreen.SETTINGS) }) {
                                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                            }
                            IconButton(onClick = { viewModel.setTab(CampusViewModel.MainTab.PROFILE) }) {
                                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Profile", tint = PrimaryBlue)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )
                }
            },
            bottomBar = {
                if (currentSubScreen == CampusViewModel.SubScreen.NONE) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        val items = listOf(
                            NavigationItem(CampusViewModel.MainTab.HOME, "Home", Icons.Default.Home),
                            NavigationItem(CampusViewModel.MainTab.CALENDAR, "Calendar", Icons.Default.CalendarMonth),
                            NavigationItem(CampusViewModel.MainTab.STUDY, "Study", Icons.Default.MenuBook),
                            NavigationItem(CampusViewModel.MainTab.COMMUNITY, "Community", Icons.Default.Groups),
                            NavigationItem(CampusViewModel.MainTab.PROFILE, "Profile", Icons.Default.Person)
                        )

                        items.forEach { item ->
                            val isSelected = currentTab == item.tab
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.setTab(item.tab) },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PrimaryBlue,
                                    selectedTextColor = PrimaryBlue,
                                    indicatorColor = PrimaryBlue.copy(alpha = 0.15f)
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentSubScreen) {
                    CampusViewModel.SubScreen.SETTINGS -> {
                        SettingsScreen(
                            onBack = { viewModel.closeSubScreen() }
                        )
                    }
                    CampusViewModel.SubScreen.SEARCH -> {
                        GlobalSearchScreen(
                            viewModel = viewModel,
                            onBack = { viewModel.closeSubScreen() }
                        )
                    }
                    CampusViewModel.SubScreen.NOTE_SCANNER -> {
                        NoteScannerScreen(
                            viewModel = viewModel,
                            onBack = { viewModel.closeSubScreen() }
                        )
                    }
                    CampusViewModel.SubScreen.EXAM_MODE -> {
                        ExamModeScreen(
                            onBack = { viewModel.closeSubScreen() }
                        )
                    }
                    CampusViewModel.SubScreen.CHAT_DETAIL -> {
                        ChatDetailScreen(
                            viewModel = viewModel,
                            onBack = { viewModel.closeSubScreen() }
                        )
                    }
                    CampusViewModel.SubScreen.LOST_AND_FOUND -> {
                        viewModel.closeSubScreen()
                    }
                    CampusViewModel.SubScreen.INSTITUTION_SELECTOR,
                    CampusViewModel.SubScreen.OPPORTUNITIES_HUB -> {
                        viewModel.closeSubScreen()
                    }
                    CampusViewModel.SubScreen.NONE -> {
                        when (currentTab) {
                            CampusViewModel.MainTab.HOME -> HomeScreen(viewModel = viewModel)
                            CampusViewModel.MainTab.CALENDAR -> CalendarScreen(viewModel = viewModel)
                            CampusViewModel.MainTab.STUDY -> StudyHubScreen(viewModel = viewModel)
                            CampusViewModel.MainTab.COMMUNITY -> CommunityScreen(viewModel = viewModel)
                            CampusViewModel.MainTab.PROFILE -> ProfileScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.setTab(CampusViewModel.MainTab.HOME) }
                            )
                        }
                    }
                    else -> {
                        viewModel.closeSubScreen()
                    }
                }
            }
        }
    }

    // Interactive Dialogs triggered from Drawer
    if (showInstitutionDialog) {
        MyInstitutionDialog(
            hierarchy = hierarchy,
            onDismiss = { showInstitutionDialog = false }
        )
    }

    if (showNotificationsModal) {
        NotificationsModal(
            viewModel = viewModel,
            onDismiss = { showNotificationsModal = false }
        )
    }

    if (showFocusTimerDialog) {
        FocusTimerDialog(
            onDismiss = { showFocusTimerDialog = false }
        )
    }

    if (showStudyGoalsDialog) {
        StudyGoalsDialog(
            onDismiss = { showStudyGoalsDialog = false }
        )
    }

    if (showCampusInfoDialog) {
        CampusInfoDialog(
            onDismiss = { showCampusInfoDialog = false }
        )
    }

    if (showFileManagerDialog) {
        FileManagerDialog(
            onDismiss = { showFileManagerDialog = false }
        )
    }

    if (showHelpSupportDialog) {
        HelpSupportDialog(
            onDismiss = { showHelpSupportDialog = false }
        )
    }
}

private data class NavigationItem(
    val tab: CampusViewModel.MainTab,
    val label: String,
    val icon: ImageVector
)
