package com.zybooks.csc436_scheduling_app.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "assignment",
    foreignKeys = [
        ForeignKey(
            entity = SchoolClass::class,
            parentColumns = ["id"],
            childColumns = ["classId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Assignment(
    val title: String,
    val date: Date,
    val time: Date,
    val classId: Int?,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)