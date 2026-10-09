package com.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ui.calendar.TimetableCalendarScreen
import com.example.viewmodel.CampusViewModel

@Composable
fun CalendarScreen(
    viewModel: CampusViewModel,
    modifier: Modifier = Modifier
) {
    TimetableCalendarScreen(
        viewModel = viewModel,
        modifier = modifier
    )
}
