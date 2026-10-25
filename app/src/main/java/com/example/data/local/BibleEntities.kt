package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bible_verses",
    indices = [
        Index(value = ["bookId", "chapter", "version"]),
        Index(value = ["bookName"]),
        Index(value = ["text"])
    ]
)
data class BibleVerseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val bookName: String,
    val testament: String,
    val chapter: Int,
    val verseNumber: Int,
    val version: String,
    val text: String,
    val isRedLetter: Boolean = false
)

@Entity(tableName = "highlights")
data class HighlightEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val chapter: Int,
    val verseNumber: Int,
    val colorHex: Long,
    val timestamp: Long
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val bookName: String,
    val chapter: Int,
    val verseNumber: Int,
    val verseText: String,
    val noteContent: String,
    val timestamp: Long
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val bookName: String,
    val chapter: Int,
    val verseNumber: Int,
    val previewText: String,
    val timestamp: Long
)

@Entity(tableName = "plan_progress")
data class PlanProgressEntity(
    @PrimaryKey val planId: String,
    val completedDaysCsv: String,
    val lastActiveTimestamp: Long
)

@Entity(tableName = "custom_plans")
data class CustomPlanEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val durationDays: Int,
    val targetPassage: String,
    val createdAt: Long
)

@Entity(
    tableName = "verse_read_progress",
    indices = [
        Index(value = ["bookId", "chapter"]),
        Index(value = ["bookId", "chapter", "verseNumber"], unique = true)
    ]
)
data class VerseReadProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val chapter: Int,
    val verseNumber: Int,
    val readAt: Long = System.currentTimeMillis()
)

