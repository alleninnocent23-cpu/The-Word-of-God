package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BibleDao {
    @Query("SELECT * FROM bible_verses WHERE version = :version AND (text LIKE '%' || :query || '%' OR bookName LIKE '%' || :query || '%') ORDER BY id ASC LIMIT 50")
    suspend fun searchVerses(query: String, version: String): List<BibleVerseEntity>

    @Query("SELECT * FROM bible_verses WHERE bookId = :bookId AND chapter = :chapter AND version = :version ORDER BY verseNumber ASC")
    suspend fun getVersesForChapter(bookId: String, chapter: Int, version: String): List<BibleVerseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllVerses(verses: List<BibleVerseEntity>)

    @Query("SELECT COUNT(*) FROM bible_verses")
    suspend fun getVerseCount(): Int

    @Query("SELECT * FROM highlights WHERE bookId = :bookId AND chapter = :chapter")
    fun getHighlightsForChapter(bookId: String, chapter: Int): Flow<List<HighlightEntity>>

    @Query("SELECT * FROM highlights ORDER BY timestamp DESC")
    fun getAllHighlights(): Flow<List<HighlightEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHighlight(highlight: HighlightEntity)

    @Query("DELETE FROM highlights WHERE bookId = :bookId AND chapter = :chapter AND verseNumber = :verseNumber")
    suspend fun removeHighlight(bookId: String, chapter: Int, verseNumber: Int)

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE bookId = :bookId AND chapter = :chapter")
    fun getNotesForChapter(bookId: String, chapter: Int): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Long)

    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE bookId = :bookId AND chapter = :chapter AND verseNumber = :verseNumber")
    suspend fun removeBookmark(bookId: String, chapter: Int, verseNumber: Int)

    @Query("SELECT * FROM plan_progress")
    fun getAllPlanProgress(): Flow<List<PlanProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlanProgress(progress: PlanProgressEntity)

    @Query("SELECT * FROM custom_plans ORDER BY createdAt DESC")
    fun getAllCustomPlans(): Flow<List<CustomPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCustomPlan(plan: CustomPlanEntity)

    @Query("SELECT * FROM verse_read_progress")
    fun getAllReadVerses(): Flow<List<VerseReadProgressEntity>>

    @Query("SELECT * FROM verse_read_progress WHERE bookId = :bookId AND chapter = :chapter")
    fun getReadVersesForChapter(bookId: String, chapter: Int): Flow<List<VerseReadProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markVerseRead(progress: VerseReadProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markVersesRead(progressList: List<VerseReadProgressEntity>)

    @Query("DELETE FROM verse_read_progress WHERE bookId = :bookId AND chapter = :chapter AND verseNumber = :verseNumber")
    suspend fun markVerseUnread(bookId: String, chapter: Int, verseNumber: Int)

    @Query("DELETE FROM verse_read_progress WHERE bookId = :bookId AND chapter = :chapter")
    suspend fun clearChapterRead(bookId: String, chapter: Int)

    @Query("SELECT COUNT(*) FROM verse_read_progress")
    fun getReadVerseCount(): Flow<Int>
}

