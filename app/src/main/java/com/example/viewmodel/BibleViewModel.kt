package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.BibleTtsManager
import com.example.data.BibleDataProvider
import com.example.data.local.BibleDatabase
import com.example.data.local.BibleVerseEntity
import com.example.data.local.BookmarkEntity
import com.example.data.local.CustomPlanEntity
import com.example.data.local.HighlightEntity
import com.example.data.local.NoteEntity
import com.example.data.local.PlanProgressEntity
import com.example.data.local.VerseReadProgressEntity
import com.example.model.AppThemeMode

import com.example.model.BibleBook
import com.example.model.BibleBookmark
import com.example.model.BibleHighlight
import com.example.model.BibleNote
import com.example.model.BibleVersion
import com.example.model.ReaderFont
import com.example.model.ReaderSettings
import com.example.model.ReadingPlan
import com.example.model.Verse
import com.example.model.VoiceNarratorStyle
import com.example.notification.BibleNotificationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BibleViewModel(application: Application) : AndroidViewModel(application) {

    private val db = BibleDatabase.getDatabase(application)
    private val dao = db.bibleDao()
    val ttsManager = BibleTtsManager(application)

    private val prefs = application.getSharedPreferences("word_of_god_prefs", Context.MODE_PRIVATE)

    // Onboarding flow state
    private val _isOnboardingCompleted = MutableStateFlow(prefs.getBoolean("onboarding_completed", false))
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted.asStateFlow()

    // Current Scripture Reader State
    private val _selectedBook = MutableStateFlow(BibleDataProvider.getBook("rev")) // Revelation
    val selectedBook: StateFlow<BibleBook> = _selectedBook.asStateFlow()

    private val _selectedChapter = MutableStateFlow(1)
    val selectedChapter: StateFlow<Int> = _selectedChapter.asStateFlow()

    private val _selectedVersion = MutableStateFlow(BibleVersion.KJV)
    val selectedVersion: StateFlow<BibleVersion> = _selectedVersion.asStateFlow()

    private val _currentVerses = MutableStateFlow<List<Verse>>(emptyList())
    val currentVerses: StateFlow<List<Verse>> = _currentVerses.asStateFlow()

    // Full Customization Settings
    private val _readerSettings = MutableStateFlow(loadSettings())
    val readerSettings: StateFlow<ReaderSettings> = _readerSettings.asStateFlow()

    // Audio / Voice State
    val audioState = ttsManager.audioState

    // Plans
    private val _customPlans = MutableStateFlow<List<ReadingPlan>>(emptyList())
    private val _planProgressMap = MutableStateFlow<Map<String, Set<Int>>>(
        mapOf("plan_peace_anxiety_14" to setOf(1))
    )
    val planProgressMap: StateFlow<Map<String, Set<Int>>> = _planProgressMap.asStateFlow()

    private val _allPlans = MutableStateFlow<List<ReadingPlan>>(BibleDataProvider.curatedReadingPlans)
    val allPlans: StateFlow<List<ReadingPlan>> = _allPlans.asStateFlow()

    // User Profile & Streak
    private val _userName = MutableStateFlow(prefs.getString("user_name", "Allen") ?: "Allen")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _readingStreak = MutableStateFlow(prefs.getInt("streak", 1))
    val readingStreak: StateFlow<Int> = _readingStreak.asStateFlow()

    // Highlights, Notes, Bookmarks
    val allHighlights: StateFlow<List<HighlightEntity>> = dao.getAllHighlights()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotes: StateFlow<List<NoteEntity>> = dao.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookmarks: StateFlow<List<BookmarkEntity>> = dao.getAllBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Room Reading Progress on each Chapter and Verse
    val readVerseKeys: StateFlow<Set<String>> = dao.getAllReadVerses()
        .map { list -> list.map { "${it.bookId}_${it.chapter}_${it.verseNumber}" }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val totalReadCount: StateFlow<Int> = dao.getReadVerseCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Room Full-Text Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Verse>>(emptyList())
    val searchResults: StateFlow<List<Verse>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    init {
        loadVerses()
        loadPersistedCustomPlans()
        seedRoomDatabase()
        scheduleNotifications()
        observeAudioNarratorProgress()
    }


    fun completeOnboarding(name: String) {
        val trimmed = if (name.isNotBlank()) name.trim() else "Beloved Reader"
        _userName.value = trimmed
        _isOnboardingCompleted.value = true
        prefs.edit()
            .putString("user_name", trimmed)
            .putBoolean("onboarding_completed", true)
            .apply()
    }

    fun resetOnboarding() {
        _isOnboardingCompleted.value = false
        prefs.edit().putBoolean("onboarding_completed", false).apply()
    }

    private fun scheduleNotifications() {
        BibleNotificationManager.scheduleTwoHourNotifications(getApplication())
    }

    fun triggerTestNotification() {
        BibleNotificationManager.sendScriptureNotification(getApplication())
    }

    private fun seedRoomDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val count = dao.getVerseCount()
                if (count == 0) {
                    val entities = mutableListOf<BibleVerseEntity>()
                    BibleDataProvider.authenticScriptures.forEach { (key, versionMap) ->
                        val parts = key.split("_")
                        if (parts.size == 2) {
                            val bookId = parts[0]
                            val chapter = parts[1].toIntOrNull() ?: 1
                            val book = BibleDataProvider.getBook(bookId)

                            versionMap.forEach { (version, verses) ->
                                verses.forEachIndexed { idx, text ->
                                    entities.add(
                                        BibleVerseEntity(
                                            bookId = book.id,
                                            bookName = book.name,
                                            testament = book.testament.name,
                                            chapter = chapter,
                                            verseNumber = idx + 1,
                                            version = version.code,
                                            text = text,
                                            isRedLetter = (book.category == "Gospels" && idx in 2..8)
                                        )
                                    )
                                }
                            }
                        }
                    }
                    dao.insertAllVerses(entities)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadSettings(): ReaderSettings {
        val themeStr = prefs.getString("theme", AppThemeMode.OBSIDIAN_DARK.name) ?: AppThemeMode.OBSIDIAN_DARK.name
        val fontStr = prefs.getString("font", ReaderFont.LORA.name) ?: ReaderFont.LORA.name
        val fontSize = prefs.getFloat("font_size", 19f)
        val lineHeight = prefs.getFloat("line_height", 1.55f)
        val redLetter = prefs.getBoolean("red_letter", true)
        val showNumbers = prefs.getBoolean("show_numbers", true)

        val theme = runCatching { AppThemeMode.valueOf(themeStr) }.getOrDefault(AppThemeMode.OBSIDIAN_DARK)
        val font = runCatching { ReaderFont.valueOf(fontStr) }.getOrDefault(ReaderFont.LORA)

        return ReaderSettings(
            themeMode = theme,
            readerFont = font,
            fontSizeSp = fontSize,
            lineHeightMultiplier = lineHeight,
            redLetterWords = redLetter,
            showVerseNumbers = showNumbers
        )
    }

    private fun persistSettings(settings: ReaderSettings) {
        prefs.edit()
            .putString("theme", settings.themeMode.name)
            .putString("font", settings.readerFont.name)
            .putFloat("font_size", settings.fontSizeSp)
            .putFloat("line_height", settings.lineHeightMultiplier)
            .putBoolean("red_letter", settings.redLetterWords)
            .putBoolean("show_numbers", settings.showVerseNumbers)
            .apply()
    }

    fun updateSettings(newSettings: ReaderSettings) {
        _readerSettings.value = newSettings
        persistSettings(newSettings)
    }

    fun setThemeMode(theme: AppThemeMode) {
        updateSettings(_readerSettings.value.copy(themeMode = theme))
    }

    fun setReaderFont(font: ReaderFont) {
        updateSettings(_readerSettings.value.copy(readerFont = font))
    }

    fun setFontSize(sizeSp: Float) {
        updateSettings(_readerSettings.value.copy(fontSizeSp = sizeSp))
    }

    fun setLineHeight(multiplier: Float) {
        updateSettings(_readerSettings.value.copy(lineHeightMultiplier = multiplier))
    }

    fun toggleRedLetter(enabled: Boolean) {
        updateSettings(_readerSettings.value.copy(redLetterWords = enabled))
    }

    fun toggleVerseNumbers(enabled: Boolean) {
        updateSettings(_readerSettings.value.copy(showVerseNumbers = enabled))
    }

    fun loadVerses() {
        val verses = BibleDataProvider.getVerses(
            bookId = _selectedBook.value.id,
            chapter = _selectedChapter.value,
            version = _selectedVersion.value
        )
        _currentVerses.value = verses
    }

    fun selectBookAndChapter(book: BibleBook, chapter: Int) {
        ttsManager.stop()
        _selectedBook.value = book
        _selectedChapter.value = chapter.coerceIn(1, book.totalChapters)
        loadVerses()
    }

    fun selectVersion(version: BibleVersion) {
        _selectedVersion.value = version
        loadVerses()
        if (ttsManager.audioState.value.isPlaying) {
            ttsManager.playChapter(
                verses = _currentVerses.value,
                bookName = _selectedBook.value.name,
                chapter = _selectedChapter.value,
                startVerseIndex = ttsManager.audioState.value.currentVerseIndex
            )
        }
    }

    fun nextChapter() {
        ttsManager.stop()
        val currentCh = _selectedChapter.value
        val total = _selectedBook.value.totalChapters
        if (currentCh < total) {
            _selectedChapter.value = currentCh + 1
            loadVerses()
        } else {
            val currentIdx = BibleDataProvider.books.indexOfFirst { it.id == _selectedBook.value.id }
            if (currentIdx >= 0 && currentIdx < BibleDataProvider.books.size - 1) {
                val nextBook = BibleDataProvider.books[currentIdx + 1]
                _selectedBook.value = nextBook
                _selectedChapter.value = 1
                loadVerses()
            }
        }
    }

    fun previousChapter() {
        ttsManager.stop()
        val currentCh = _selectedChapter.value
        if (currentCh > 1) {
            _selectedChapter.value = currentCh - 1
            loadVerses()
        } else {
            val currentIdx = BibleDataProvider.books.indexOfFirst { it.id == _selectedBook.value.id }
            if (currentIdx > 0) {
                val prevBook = BibleDataProvider.books[currentIdx - 1]
                _selectedBook.value = prevBook
                _selectedChapter.value = prevBook.totalChapters
                loadVerses()
            }
        }
    }

    // Audio Playback
    fun toggleAudioPlayback() {
        ttsManager.togglePlayPause(
            verses = _currentVerses.value,
            bookName = _selectedBook.value.name,
            chapter = _selectedChapter.value
        )
    }

    fun playFromVerse(verseIndex: Int) {
        ttsManager.playChapter(
            verses = _currentVerses.value,
            bookName = _selectedBook.value.name,
            chapter = _selectedChapter.value,
            startVerseIndex = verseIndex
        )
    }

    fun setAudioSpeed(speed: Float) {
        ttsManager.setSpeed(speed)
    }

    fun setVoiceNarratorStyle(style: VoiceNarratorStyle) {
        ttsManager.setNarratorStyle(style)
    }

    // Highlights, Notes & Bookmarks
    fun saveHighlight(verse: Verse, colorHex: Long) {
        viewModelScope.launch {
            dao.saveHighlight(
                HighlightEntity(
                    bookId = verse.bookId,
                    chapter = verse.chapter,
                    verseNumber = verse.verseNumber,
                    colorHex = colorHex,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun removeHighlight(verse: Verse) {
        viewModelScope.launch {
            dao.removeHighlight(verse.bookId, verse.chapter, verse.verseNumber)
        }
    }

    fun saveNote(verse: Verse, content: String) {
        viewModelScope.launch {
            dao.saveNote(
                NoteEntity(
                    bookId = verse.bookId,
                    bookName = verse.bookName,
                    chapter = verse.chapter,
                    verseNumber = verse.verseNumber,
                    verseText = verse.text,
                    noteContent = content,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            dao.deleteNote(id)
        }
    }

    fun toggleBookmark(verse: Verse) {
        viewModelScope.launch {
            val exists = allBookmarks.value.any {
                it.bookId == verse.bookId && it.chapter == verse.chapter && it.verseNumber == verse.verseNumber
            }
            if (exists) {
                dao.removeBookmark(verse.bookId, verse.chapter, verse.verseNumber)
            } else {
                dao.saveBookmark(
                    BookmarkEntity(
                        bookId = verse.bookId,
                        bookName = verse.bookName,
                        chapter = verse.chapter,
                        verseNumber = verse.verseNumber,
                        previewText = verse.text,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    // Reading Plans
    fun togglePlanDay(planId: String, dayNumber: Int) {
        val currentSet = _planProgressMap.value[planId]?.toMutableSet() ?: mutableSetOf()
        if (currentSet.contains(dayNumber)) {
            currentSet.remove(dayNumber)
        } else {
            currentSet.add(dayNumber)
            incrementStreak()
        }
        val updated = _planProgressMap.value.toMutableMap()
        updated[planId] = currentSet
        _planProgressMap.value = updated

        viewModelScope.launch {
            dao.savePlanProgress(
                PlanProgressEntity(
                    planId = planId,
                    completedDaysCsv = currentSet.joinToString(","),
                    lastActiveTimestamp = System.currentTimeMillis()
                )
            )
        }
    }

    private fun incrementStreak() {
        val newStreak = _readingStreak.value + 1
        _readingStreak.value = newStreak
        prefs.edit().putInt("streak", newStreak).apply()
    }

    fun createPersonalizedPlan(title: String, targetScope: String, durationDays: Int) {
        val newPlan = BibleDataProvider.createPersonalizedPlan(title, targetScope, durationDays)
        _customPlans.value = listOf(newPlan) + _customPlans.value
        refreshAllPlansList()

        viewModelScope.launch {
            dao.saveCustomPlan(
                CustomPlanEntity(
                    id = newPlan.id,
                    title = newPlan.title,
                    subtitle = newPlan.subtitle,
                    durationDays = newPlan.durationDays,
                    targetPassage = targetScope,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }

    private fun loadPersistedCustomPlans() {
        viewModelScope.launch {
            dao.getAllCustomPlans().collect { entities ->
                val reconstructed = entities.map { entity ->
                    BibleDataProvider.createPersonalizedPlan(
                        title = entity.title,
                        targetScope = entity.targetPassage,
                        durationDays = entity.durationDays
                    ).copy(id = entity.id, subtitle = entity.subtitle)
                }
                _customPlans.value = reconstructed
                refreshAllPlansList()
            }
        }

        viewModelScope.launch {
            dao.getAllPlanProgress().collect { progressList ->
                val map = progressList.associate { entity ->
                    val days = entity.completedDaysCsv.split(",")
                        .mapNotNull { it.trim().toIntOrNull() }
                        .toSet()
                    entity.planId to days
                }
                if (map.isNotEmpty()) {
                    _planProgressMap.value = map
                }
            }
        }
    }

    private fun refreshAllPlansList() {
        _allPlans.value = _customPlans.value + BibleDataProvider.curatedReadingPlans
    }

    fun setUserName(name: String) {
        _userName.value = name
        prefs.edit().putString("user_name", name).apply()
    }

    // Room Full-Text & Fast Search
    fun searchVerses(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _isSearching.value = true
            val cleanQuery = query.trim()
            val roomResults = dao.searchVerses(cleanQuery, _selectedVersion.value.code)

            val mappedResults = if (roomResults.isNotEmpty()) {
                roomResults.map { entity ->
                    Verse(
                        bookId = entity.bookId,
                        bookName = entity.bookName,
                        chapter = entity.chapter,
                        verseNumber = entity.verseNumber,
                        text = entity.text,
                        isRedLetter = entity.isRedLetter
                    )
                }
            } else {
                // In-memory fallback across all authentic scriptures
                val fallbackList = mutableListOf<Verse>()
                val lower = cleanQuery.lowercase()
                BibleDataProvider.authenticScriptures.forEach { (key, versionMap) ->
                    val parts = key.split("_")
                    if (parts.size == 2) {
                        val bId = parts[0]
                        val ch = parts[1].toIntOrNull() ?: 1
                        val book = BibleDataProvider.getBook(bId)
                        val texts = versionMap[_selectedVersion.value] ?: versionMap[BibleVersion.KJV] ?: emptyList()
                        texts.forEachIndexed { idx, txt ->
                            if (txt.lowercase().contains(lower) || book.name.lowercase().contains(lower)) {
                                fallbackList.add(
                                    Verse(
                                        bookId = book.id,
                                        bookName = book.name,
                                        chapter = ch,
                                        verseNumber = idx + 1,
                                        text = txt
                                    )
                                )
                            }
                        }
                    }
                }
                fallbackList
            }

            _searchResults.value = mappedResults
            _isSearching.value = false
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    private fun observeAudioNarratorProgress() {
        viewModelScope.launch {
            audioState.collect { state ->
                if (state.isPlaying && state.currentVerseIndex in _currentVerses.value.indices) {
                    val verse = _currentVerses.value[state.currentVerseIndex]
                    markVerseRead(verse)
                }
            }
        }
    }

    fun toggleVerseRead(verse: Verse) {
        viewModelScope.launch(Dispatchers.IO) {
            val key = "${verse.bookId}_${verse.chapter}_${verse.verseNumber}"
            if (readVerseKeys.value.contains(key)) {
                dao.markVerseUnread(verse.bookId, verse.chapter, verse.verseNumber)
            } else {
                dao.markVerseRead(
                    VerseReadProgressEntity(
                        bookId = verse.bookId,
                        chapter = verse.chapter,
                        verseNumber = verse.verseNumber
                    )
                )
            }
        }
    }

    fun markVerseRead(verse: Verse) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.markVerseRead(
                VerseReadProgressEntity(
                    bookId = verse.bookId,
                    chapter = verse.chapter,
                    verseNumber = verse.verseNumber
                )
            )
        }
    }

    fun markVerseUnread(verse: Verse) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.markVerseUnread(verse.bookId, verse.chapter, verse.verseNumber)
        }
    }

    fun markCurrentChapterComplete() {
        viewModelScope.launch(Dispatchers.IO) {
            val verses = _currentVerses.value
            if (verses.isNotEmpty()) {
                val entities = verses.map { v ->
                    VerseReadProgressEntity(
                        bookId = v.bookId,
                        chapter = v.chapter,
                        verseNumber = v.verseNumber
                    )
                }
                dao.markVersesRead(entities)
            }
        }
    }

    fun clearCurrentChapterProgress() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearChapterRead(_selectedBook.value.id, _selectedChapter.value)
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}

