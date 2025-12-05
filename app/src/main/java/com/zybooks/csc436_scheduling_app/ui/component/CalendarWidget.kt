package com.zybooks.csc436_scheduling_app.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarWidget(
    modifier: Modifier = Modifier,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    var selectedDate by remember { mutableStateOf(today) }
    var currentMonth by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }

    Surface(
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "<",
                    modifier = Modifier.clickable { currentMonth = currentMonth.minusMonths(1) }
                        .padding(8.dp),
                    fontSize = 20.sp
                )
                Text(
                    text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Text(
                    ">",
                    modifier = Modifier.clickable { currentMonth = currentMonth.plusMonths(1) }
                        .padding(8.dp),
                    fontSize = 20.sp
                )
            }

            val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
            val daysOfWeek = remember {
                val weekDays = mutableListOf<java.time.DayOfWeek>()
                var currentDay = firstDayOfWeek
                for (i in 0..6) {
                    weekDays.add(currentDay)
                    currentDay = currentDay.plus(1)
                }
                weekDays
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (day in daysOfWeek) {
                    Text(
                        text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            val daysInMonth = currentMonth.lengthOfMonth()
            val firstDayOfMonth = currentMonth.withDayOfMonth(1).dayOfWeek
            val firstDayOfMonthIndex = (firstDayOfMonth.value - firstDayOfWeek.value + 7) % 7

            val cells = buildList<LocalDate?> {
                repeat(firstDayOfMonthIndex) { add(null) }
                for (day in 1..daysInMonth) {
                    add(currentMonth.withDayOfMonth(day))
                }
            }

            val rows = cells.chunked(7)
            for (week in rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (date in week) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (date == null) {
                                Text("", modifier = Modifier.size(36.dp))
                            } else {
                                val isToday = date == today
                                val isSelected = date == selectedDate
                                val bgColor = when {
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.surface
                                }
                                val contentColor = when {
                                    isSelected -> MaterialTheme.colorScheme.onPrimary
                                    isToday -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.onSurface
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = bgColor,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clickable {
                                            selectedDate = date
                                            onDateSelected(date)
                                        },
                                    shadowElevation = 0.dp
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = date.dayOfMonth.toString(),
                                            color = contentColor,
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    repeat(7 - week.size) {
                        Box(modifier = Modifier.weight(1f).padding(4.dp)) {
                            Text("", modifier = Modifier.size(36.dp))
                        }
                    }
                }
            }
        }
    }
}