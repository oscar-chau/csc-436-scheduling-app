package com.zybooks.csc436_scheduling_app.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.zybooks.csc436_scheduling_app.navigation.AppScreen

@Composable
fun Calendar() {
    Text(
        AppScreen.CALENDAR.title,
        textAlign = TextAlign.Center,
        fontSize = 80.sp,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(),
    )
}