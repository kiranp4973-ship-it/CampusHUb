package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.*
import com.example.ui.components.EmptyStateCard
import com.example.ui.components.MetricBadge
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import com.example.viewmodel.CampusViewModel

@Composable
fun CommunityScreen(
    viewModel: CampusViewModel,
    modifier: Modifier = Modifier
) {
    val communityTab by viewModel.communityTab.collectAsState()
    val friends by viewModel.friends.collectAsState()
    val studyGroups by viewModel.studyGroups.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val lostAndFound by viewModel.lostAndFound.collectAsState()
    val projectTeams by viewModel.projectTeams.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    val clubs by viewModel.campusClubs.collectAsState()
    val opportunities by viewModel.opportunities.collectAsState()
    val discoveryScope by viewModel.discoveryScope.collectAsState()
    val user by viewModel.currentUser.collectAsState()

    var showStatusDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        ScrollableTabRow(
            selectedTabIndex = communityTab.ordinal,
            edgePadding = 0.dp,
            divider = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = communityTab == CampusViewModel.CommunitySectionTab.PULSE,
                onClick = { viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.PULSE) },
                text = { Text("Pulse") }
            )
            Tab(
                selected = communityTab == CampusViewModel.CommunitySectionTab.OPPORTUNITIES,
                onClick = { viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.OPPORTUNITIES) },
                text = { Text("Opportunities") }
            )
            Tab(
                selected = communityTab == CampusViewModel.CommunitySectionTab.FRIENDS,
                onClick = { viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.FRIENDS) },
                text = { Text("Classmates") }
            )
            Tab(
                selected = communityTab == CampusViewModel.CommunitySectionTab.STUDY_GROUPS,
                onClick = { viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.STUDY_GROUPS) },
                text = { Text("Groups") }
            )
            Tab(
                selected = communityTab == CampusViewModel.CommunitySectionTab.PROJECTS,
                onClick = { viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.PROJECTS) },
                text = { Text("Projects") }
            )
            Tab(
                selected = communityTab == CampusViewModel.CommunitySectionTab.CLUBS,
                onClick = { viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.CLUBS) },
                text = { Text("Clubs") }
            )
            Tab(
                selected = communityTab == CampusViewModel.CommunitySectionTab.LOST_FOUND,
                onClick = { viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.LOST_FOUND) },
                text = { Text("Lost & Found") }
            )
            Tab(
                selected = communityTab == CampusViewModel.CommunitySectionTab.EXPENSES,
                onClick = { viewModel.setCommunityTab(CampusViewModel.CommunitySectionTab.EXPENSES) },
                text = { Text("Split") }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (communityTab) {
            CampusViewModel.CommunitySectionTab.PULSE -> {
                ClassPulseView(
                    announcements = announcements,
                    friends = friends,
                    onChatOpen = { viewModel.navigateToSubScreen(CampusViewModel.SubScreen.CHAT_DETAIL) }
                )
            }
            CampusViewModel.CommunitySectionTab.OPPORTUNITIES -> {
                OpportunitiesDiscoveryView(
                    opportunities = opportunities,
                    selectedScope = discoveryScope,
                    onScopeSelect = { viewModel.setDiscoveryScope(it) },
                    onToggleSaved = { viewModel.toggleOpportunitySaved(it) }
                )
            }
            CampusViewModel.CommunitySectionTab.FRIENDS -> {
                FriendsListView(
                    friends = friends,
                    myStatus = user.status,
                    myStatusNote = user.customStatusNote,
                    onChangeStatusClick = { showStatusDialog = true },
                    onChatOpen = { viewModel.navigateToSubScreen(CampusViewModel.SubScreen.CHAT_DETAIL) }
                )
            }
            CampusViewModel.CommunitySectionTab.STUDY_GROUPS -> {
                StudyGroupsView(
                    groups = studyGroups,
                    onGroupClick = { viewModel.navigateToSubScreen(CampusViewModel.SubScreen.CHAT_DETAIL) }
                )
            }
            CampusViewModel.CommunitySectionTab.PROJECTS -> {
                ProjectTeamsView(teams = projectTeams)
            }
            CampusViewModel.CommunitySectionTab.CLUBS -> {
                CampusClubsView(clubs = clubs, onToggleJoin = { viewModel.toggleClubJoined(it) })
            }
            CampusViewModel.CommunitySectionTab.LOST_FOUND -> {
                LostAndFoundView(
                    items = lostAndFound,
                    onPostItem = { viewModel.navigateToSubScreen(CampusViewModel.SubScreen.LOST_AND_FOUND) }
                )
            }
            CampusViewModel.CommunitySectionTab.EXPENSES -> {
                ExpenseSplitView(
                    group = expenses,
                    onAddExpense = { viewModel.addExpense("Canteen snacks", 180.0, "Kiran") }
                )
            }
        }
    }

    if (showStatusDialog) {
        var selectedStatus by remember { mutableStateOf(user.status) }
        var statusNote by remember { mutableStateOf(user.customStatusNote) }

        AlertDialog(
            onDismissRequest = { showStatusDialog = false },
            title = { Text("Update Campus Activity Status") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Where are you on campus right now? (Manual & Privacy friendly)", style = MaterialTheme.typography.bodySmall)
                    FriendStatus.values().forEach { st ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedStatus = st }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedStatus == st,
                                onClick = { selectedStatus = st }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${st.emoji} ${st.label}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    OutlinedTextField(
                        value = statusNote,
                        onValueChange = { statusNote = it },
                        label = { Text("Custom Note") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateStatus(selectedStatus, statusNote)
                        showStatusDialog = false
                    }
                ) {
                    Text("Save Status")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStatusDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun OpportunitiesDiscoveryView(
    opportunities: List<OpportunityItem>,
    selectedScope: DiscoveryScope,
    onScopeSelect: (DiscoveryScope) -> Unit,
    onToggleSaved: (String) -> Unit
) {
    val filtered = remember(opportunities, selectedScope) {
        opportunities.filter { it.scope == selectedScope }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            // Three Scope Selector (Local / National / Global)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DiscoveryScope.values().forEach { scope ->
                    FilterChip(
                        selected = selectedScope == scope,
                        onClick = { onScopeSelect(scope) },
                        label = { Text("${scope.emoji} ${scope.label}") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        if (filtered.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = Icons.Default.Public,
                    title = "No Opportunities Found",
                    description = "Check back soon for new grants, scholarships, and internships."
                )
            }
        } else {
            items(filtered) { opp ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = BrandViolet.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = opp.category,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandViolet,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            IconButton(onClick = { onToggleSaved(opp.id) }, modifier = Modifier.size(32.dp)) {
                                Icon(
                                    imageVector = if (opp.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Save",
                                    tint = if (opp.isSaved) BrandCoral else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = opp.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(text = "By ${opp.organization} • Deadline: ${opp.deadline}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "Award / Benefit: ${opp.stipendOrAward}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "Eligibility: ${opp.eligibility}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = opp.description, style = MaterialTheme.typography.bodySmall)

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Verified Opportunity", style = MaterialTheme.typography.labelSmall, color = PrimaryBlue)
                            }
                            FilledTonalButton(
                                onClick = {},
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text("Apply Official")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CampusClubsView(clubs: List<CampusClubItem>, onToggleJoin: (String) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        items(clubs) { club ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = club.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        MetricBadge(label = "Members", value = "${club.memberCount}", color = PrimaryBlue)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${club.category} Society • Lead: ${club.leadName}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = club.description, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Next Event: ${club.upcomingEvent}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryBlue,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { onToggleJoin(club.id) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (club.isJoined) MaterialTheme.colorScheme.surfaceVariant else PrimaryBlue,
                                contentColor = if (club.isJoined) MaterialTheme.colorScheme.onSurfaceVariant else Color.White
                            )
                        ) {
                            Text(if (club.isJoined) "Joined Society ✓" else "+ Join Club")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClassPulseView(
    announcements: List<Announcement>,
    friends: List<UserProfile>,
    onChatOpen: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryBlue.copy(alpha = 0.12f))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Campaign, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Class Pulse • Active Campus Updates", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("3 peer notes uploaded • Internal exam in 6 days • Lab records due this week", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        item {
            SectionHeader(title = "Official Institutional Bulletins")
        }

        items(announcements) { anc ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PrimaryBlue.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = anc.scope,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(text = anc.date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = anc.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = anc.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Posted by ${anc.authorName} (${anc.authorRole})", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun FriendsListView(
    friends: List<UserProfile>,
    myStatus: FriendStatus,
    myStatusNote: String,
    onChangeStatusClick: () -> Unit,
    onChatOpen: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onChangeStatusClick),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = myStatus.emoji, style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "My Status: ${myStatus.label}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        Text(text = "\"$myStatusNote\"", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    OutlinedButton(onClick = onChangeStatusClick, shape = RoundedCornerShape(8.dp)) {
                        Text("Update")
                    }
                }
            }
        }

        item {
            SectionHeader(
                title = "Classmates & Where Are They?",
                subtitle = "Self-reported manual location & study availability"
            )
        }

        items(friends) { friend ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = PrimaryBlue.copy(alpha = 0.15f), modifier = Modifier.size(44.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = friend.name.take(2).uppercase(),
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = friend.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            if (friend.role == UserRole.CLASS_REPRESENTATIVE) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(shape = RoundedCornerShape(4.dp), color = AccentPurple.copy(alpha = 0.15f)) {
                                    Text("CR", color = AccentPurple, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                }
                            }
                        }
                        Text(text = "${friend.usnOrRoll} • Sem ${friend.semester} (${friend.section})", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "${friend.status.emoji} ${friend.status.label}: ${friend.customStatusNote}", style = MaterialTheme.typography.labelSmall, color = PrimaryBlue, fontWeight = FontWeight.Medium)
                    }

                    IconButton(onClick = onChatOpen) {
                        Icon(imageVector = Icons.Default.ChatBubbleOutline, contentDescription = "Chat", tint = PrimaryBlue)
                    }
                }
            }
        }
    }
}

@Composable
fun StudyGroupsView(groups: List<StudyGroup>, onGroupClick: () -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        items(groups) { grp ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onGroupClick),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = grp.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        MetricBadge(label = "Members", value = "${grp.memberCount}", color = PrimaryBlue)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = grp.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = grp.latestMessage, style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
                            Text(text = grp.latestMessageTime, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectTeamsView(teams: List<ProjectTeamPost>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        items(teams) { team ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = team.projectTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        MetricBadge(label = "Needed", value = "${team.membersNeeded}", color = WarningOrange)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = team.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Required Skills:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        team.requiredSkills.forEach { skill ->
                            Surface(shape = RoundedCornerShape(6.dp), color = PrimaryBlue.copy(alpha = 0.12f)) {
                                Text(text = skill, color = PrimaryBlue, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Posted by ${team.postedByName} (${team.courseSection})", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Button(onClick = {}, shape = RoundedCornerShape(8.dp), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
                            Text("Request to Join")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LostAndFoundView(items: List<LostAndFoundItem>, onPostItem: () -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Campus Lost & Found", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Button(onClick = onPostItem, shape = RoundedCornerShape(10.dp)) {
                    Text("+ Post Item")
                }
            }
        }

        items(items) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (item.isLost) DangerRed.copy(alpha = 0.15f) else SuccessGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (item.isLost) "LOST" else "FOUND",
                                fontWeight = FontWeight.Bold,
                                color = if (item.isLost) DangerRed else SuccessGreen,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Text(text = item.date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "Location: ${item.location}", style = MaterialTheme.typography.bodySmall, color = PrimaryBlue, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = item.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Contact: ${item.contactMethod}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseSplitView(group: ExpenseGroup, onAddExpense: () -> Unit) {
    val totalExpense = group.expenses.sumOf { it.amount }
    val perPerson = totalExpense / group.members.size

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = group.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Members: ${group.members.joinToString(", ")}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Spent", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("₹${String.format("%.0f", totalExpense)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Split Per Person", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("₹${String.format("%.0f", perPerson)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = SuccessGreen)
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Logged Expenses", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                FilledTonalButton(onClick = onAddExpense, shape = RoundedCornerShape(8.dp)) {
                    Text("+ Add Expense")
                }
            }
        }

        items(group.expenses) { exp ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = exp.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Text(text = "Paid by ${exp.paidBy} • ${exp.date}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(text = "₹${String.format("%.0f", exp.amount)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                }
            }
        }
    }
}
