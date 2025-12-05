package com.zybooks.csc436_scheduling_app.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(
    title: String,
    classes: List<SchoolClass> = emptyList(),
    onDismiss: () -> Unit,
    onSubmit: (
        name: String,
        location: String,
        startDate: Date?,
        endDate: Date?,
        startTime: Date?,
        endTime: Date?,
        days: List<String>,
        classId: Long?
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var startDateStr by remember { mutableStateOf("") }
    var endDateStr by remember { mutableStateOf("") }
    var startTimeStr by remember { mutableStateOf("") }
    var endTimeStr by remember { mutableStateOf("") }

    val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val selectedDays = remember { mutableStateListOf<String>() }

    val isClass = title.contains("Class", ignoreCase = true)
    val isTask = title.contains("Task", ignoreCase = true)
    val isReminder = title.contains("Reminder", ignoreCase = true)

    var selectedClassId by remember { mutableStateOf<Long?>(null) }
    var classMenuExpanded by remember { mutableStateOf(false) }

    // DATE PARSERS
    val dateFormatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun parseDate(dateStr: String): Date? =
        try { dateFormatter.parse(dateStr) } catch (_: Exception) { null }

    fun parseTime(timeStr: String): Date? =
        try { timeFormatter.parse(timeStr) } catch (_: Exception) { null }

    // Combine a date ("MM/DD/YYYY") with a time ("HH:mm")
    fun combineDateAndTime(dateOnly: Date?, timeOnly: Date?): Date? {
        if (dateOnly == null || timeOnly == null) return null

        val calDate = Calendar.getInstance().apply { time = dateOnly }
        val calTime = Calendar.getInstance().apply { time = timeOnly }

        return Calendar.getInstance().apply {
            set(Calendar.YEAR, calDate.get(Calendar.YEAR))
            set(Calendar.MONTH, calDate.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, calTime.get(Calendar.MINUTE))
            set(Calendar.SECOND, 0)
        }.time
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                // ------------------ NAME ------------------
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text(
                            when {
                                isTask -> "Task Name"
                                isReminder -> "Reminder Title"
                                else -> "Class Name"
                            }
                        )
                    }
                )

                // ------------------ LOCATION ------------------
                if (isClass || isReminder) {
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") }
                    )
                }

                // ------------------ TASK CLASS PICKER ------------------
                if (isTask && classes.isNotEmpty()) {

                    ExposedDropdownMenuBox(
                        expanded = classMenuExpanded,
                        onExpandedChange = { classMenuExpanded = !classMenuExpanded }
                    ) {
                        val selectedClass =
                            classes.find { it.id.toLong() == selectedClassId }

                        OutlinedTextField(
                            value = selectedClass?.name ?: "Select Class",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Class") },
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = classMenuExpanded,
                            onDismissRequest = { classMenuExpanded = false }
                        ) {
                            classes.forEach { schoolClass ->
                                DropdownMenuItem(
                                    text = { Text(schoolClass.name) },
                                    onClick = {
                                        selectedClassId = schoolClass.id.toLong()
                                        classMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // ------------------ DATE FIELDS ------------------
                if (isClass) {
                    OutlinedTextField(
                        value = startDateStr,
                        onValueChange = { startDateStr = it },
                        label = { Text("Start Date") },
                        placeholder = { Text("MM/DD/YYYY") }
                    )

                    OutlinedTextField(
                        value = endDateStr,
                        onValueChange = { endDateStr = it },
                        label = { Text("End Date") },
                        placeholder = { Text("MM/DD/YYYY") }
                    )
                }

                if (isTask || isReminder) {
                    OutlinedTextField(
                        value = startDateStr,
                        onValueChange = { startDateStr = it },
                        label = { Text(if (isTask) "Due Date" else "Date") },
                        placeholder = { Text("MM/DD/YYYY") }
                    )
                }

                // ------------------ TIME FIELDS ------------------
                if (isClass) {
                    OutlinedTextField(
                        value = startTimeStr,
                        onValueChange = { startTimeStr = it },
                        label = { Text("Start Time") },
                        placeholder = { Text("HH:MM") }
                    )

                    OutlinedTextField(
                        value = endTimeStr,
                        onValueChange = { endTimeStr = it },
                        label = { Text("End Time") },
                        placeholder = { Text("HH:MM") }
                    )
                }

                if (isTask || isReminder) {
                    OutlinedTextField(
                        value = startTimeStr,
                        onValueChange = { startTimeStr = it },
                        label = { Text(if (isTask) "Due Time" else "Time") },
                        placeholder = { Text("HH:MM") }
                    )
                }

                // ------------------ DAYS (CLASS ONLY) ------------------
                if (isClass) {
                    Text("Days", style = MaterialTheme.typography.labelLarge)

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        dayNames.forEach { day ->
                            val isSelected = selectedDays.contains(day)

                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) selectedDays.remove(day)
                                    else selectedDays.add(day)
                                },
                                label = { Text(day) },
                                shape = RoundedCornerShape(8.dp),
                                colors = FilterChipDefaults.filterChipColors()
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {

                val parsedStartDate = parseDate(startDateStr)
                val parsedEndDate = parseDate(endDateStr)

                val parsedStartTime = parseTime(startTimeStr)
                val parsedEndTime = parseTime(endTimeStr)

                val combinedStart = combineDateAndTime(parsedStartDate, parsedStartTime)
                val combinedEnd = combineDateAndTime(parsedEndDate, parsedEndTime)

                val dayNameMapping = mapOf(
                    "Mon" to "Monday",
                    "Tue" to "Tuesday",
                    "Wed" to "Wednesday",
                    "Thu" to "Thursday",
                    "Fri" to "Friday",
                    "Sat" to "Saturday",
                    "Sun" to "Sunday"
                )

                onSubmit(
                    name,
                    location,
                    parsedStartDate,
                    parsedEndDate,
                    combinedStart,
                    combinedEnd,
                    selectedDays.mapNotNull { dayNameMapping[it] },
                    selectedClassId
                )

                onDismiss()
            }) { Text("Add") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
