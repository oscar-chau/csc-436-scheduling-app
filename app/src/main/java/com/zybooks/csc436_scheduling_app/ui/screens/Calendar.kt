package com.zybooks.csc436_scheduling_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

@Composable
fun Calendar() {
    // Replace simple title with an interactive calendar widget
    CalendarWidget(
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
private fun CalendarWidget(modifier: Modifier = Modifier) {
    val todayCal = Calendar.getInstance()
    val todayYear = todayCal.get(Calendar.YEAR)
    val todayMonth = todayCal.get(Calendar.MONTH) // 0-based
    val todayDay = todayCal.get(Calendar.DAY_OF_MONTH)

    // state: current shown year/month
    val monthState = remember { mutableStateOf(Pair(todayYear, todayMonth)) }
    // selected: Triple(year, month, day) or null
    val selected = remember { mutableStateOf<Triple<Int, Int, Int>?>(null) }

    val (year, month) = monthState.value

    // compute first day of month and days in month using GregorianCalendar
    val firstOfMonthCal = GregorianCalendar(year, month, 1)
    val firstDayOfWeek = firstOfMonthCal.get(Calendar.DAY_OF_WEEK) // SUNDAY=1 .. SATURDAY=7
    // convert to index where Sunday -> 0, Monday ->1, ... Saturday ->6
    val firstWeekdayIndex = (firstDayOfWeek + 6) % 7
    val daysInMonth = firstOfMonthCal.getActualMaximum(Calendar.DAY_OF_MONTH)

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header: Month and Prev/Next
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "<",
                modifier = Modifier
                    .clickable {
                        // go to previous month
                        var y = monthState.value.first
                        var m = monthState.value.second - 1
                        if (m < 0) {
                            m = 11
                            y -= 1
                        }
                        monthState.value = Pair(y, m)
                    }
                    .padding(8.dp),
                fontSize = 20.sp
            )
            val monthName = DateFormatSymbols.getInstance(Locale.getDefault()).months[month]
            Text(
                text = "$monthName $year",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Text(
                ">",
                modifier = Modifier
                    .clickable {
                        // next month
                        var y = monthState.value.first
                        var m = monthState.value.second + 1
                        if (m > 11) {
                            m = 0
                            y += 1
                        }
                        monthState.value = Pair(y, m)
                    }
                    .padding(8.dp),
                fontSize = 20.sp
            )
        }

        // Weekday headers (Sun ... Sat)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val shortWeekdays = DateFormatSymbols.getInstance(Locale.getDefault()).shortWeekdays
            // shortWeekdays[1] == Sunday
            val ordered = listOf(1, 2, 3, 4, 5, 6, 7)
            for (idx in ordered) {
                Text(
                    text = shortWeekdays[idx],
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        // Days grid: generate list with leading blanks
        val cells = buildList<Int?> {
            repeat(firstWeekdayIndex) { add(null) } // leading empty cells
            for (day in 1..daysInMonth) add(day)
        }

        // Render rows of 7
        val rows = cells.chunked(7)
        for (week in rows) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                for (cell in week) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (cell == null) {
                            // empty slot
                            Text("", modifier = Modifier.size(36.dp))
                        } else {
                            val isToday = (year == todayYear && month == todayMonth && cell == todayDay)
                            val isSelected = selected.value?.let { (sy, sm, sd) ->
                                sy == year && sm == month && sd == cell
                            } ?: false
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
                                        selected.value = Triple(year, month, cell)
                                    },
                                shadowElevation = 0.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = cell.toString(),
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
            }
        }

        // Optional: show selected date below
        val sel = selected.value
        if (sel != null) {
            val selMonthName = DateFormatSymbols.getInstance(Locale.getDefault()).months[sel.second]
            Text(
                text = "Selected: $selMonthName ${sel.third}, ${sel.first}",
                modifier = Modifier.padding(top = 12.dp),
                fontSize = 14.sp
            )
        }
    }
}