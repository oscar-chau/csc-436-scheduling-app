package com.zybooks.csc436_scheduling_app.ui.components

import android.graphics.Color.rgb
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ClassCard(
    schoolClass: SchoolClass,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = schoolClass.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0, 0, 0)
                )

                Box(modifier = Modifier.
                            background(
                                color = Color(rgb(216, 236, 255)), // light blue
                                shape = RoundedCornerShape(12.dp)
                            )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Class",
                        color = Color(rgb(38, 101, 232)), // darker blue text
                        fontSize = 12.sp,
                    )
                }

            }

            schoolClass.location?.let {
                Text(
                    text = "$it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            val timeFormat = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
            val start = timeFormat.format(schoolClass.startTime)
            val end = timeFormat.format(schoolClass.endTime)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Time",
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "$start - $end",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}