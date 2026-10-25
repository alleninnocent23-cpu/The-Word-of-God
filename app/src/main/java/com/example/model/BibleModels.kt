package com.example.model

enum class BibleVersion(
    val code: String,
    val title: String,
    val description: String,
    val isModern: Boolean
) {
    KJV("KJV", "King James Version", "Classic poetic translation, beloved for majesty and resonance (1611)", false),
    WEB("WEB", "World English Bible", "Clean modern public domain English translation for clear reading", true),
    BBE("BBE", "Bible in Basic English", "Simplified vocabulary of 1,000 words, ideal for clarity and study", true),
    ASV("ASV", "American Standard Version", "Faithful word-for-word scholarly literal translation", false),
    YLT("YLT", "Young's Literal Translation", "Strictly literal to the original Hebrew and Greek syntax", false)
}

enum class Testament {
    OLD, NEW
}

data class BibleBook(
    val id: String,
    val name: String,
    val testament: Testament,
    val category: String,
    val totalChapters: Int
)

data class Verse(
    val bookId: String,
    val bookName: String,
    val chapter: Int,
    val verseNumber: Int,
    val text: String,
    val isRedLetter: Boolean = false
)

data class Chapter(
    val bookId: String,
    val bookName: String,
    val chapterNumber: Int,
    val verses: List<Verse>
)

enum class AppThemeMode(
    val id: String,
    val displayName: String,
    val previewHex: Long,
    val isDark: Boolean
) {
    OBSIDIAN_DARK("obsidian", "Dark Obsidian", 0xFF0D0E11, true),
    SEPIA_PARCHMENT("sepia", "Warm Sepia", 0xFFFBF0D9, false),
    CRISP_LIGHT("light", "Crisp Ivory", 0xFFF7F8FA, false),
    MIDNIGHT_NAVY("midnight", "Midnight Navy", 0xFF0A1128, true),
    OLIVE_FOREST("forest", "Olive Forest", 0xFF101C14, true)
}

enum class ReaderFont(
    val id: String,
    val displayName: String,
    val fontResName: String?
) {
    LORA("lora", "Lora (Classic Serif)", "lora"),
    PLAYFAIR("playfair", "Playfair (Editorial Serif)", "playfair_display"),
    OPEN_SANS("open_sans", "Open Sans (Clean Modern)", "open_sans"),
    MONOSPACE("mono", "Monospace (Study)", null),
    SYSTEM_SERIF("serif", "System Serif", null)
}

data class ReaderSettings(
    val themeMode: AppThemeMode = AppThemeMode.OBSIDIAN_DARK,
    val readerFont: ReaderFont = ReaderFont.LORA,
    val fontSizeSp: Float = 18f,
    val lineHeightMultiplier: Float = 1.55f,
    val letterSpacingSp: Float = 0.2f,
    val showVerseNumbers: Boolean = true,
    val redLetterWords: Boolean = true,
    val highContrast: Boolean = false
)

enum class VoiceNarratorStyle(
    val id: String,
    val displayName: String,
    val pitch: Float,
    val speed: Float,
    val description: String
) {
    WARM("warm", "Warm & Calming", 0.95f, 0.92f, "Gentle, reverent voice for meditation and devotion"),
    SCHOLARLY("scholarly", "Scholarly & Clear", 1.0f, 1.0f, "Measured, articulate cadence for in-depth scripture study"),
    REVERENT("reverent", "Deep & Reverent", 0.85f, 0.88f, "Deep resonant tone evoking cathedral lecterns"),
    DYNAMIC("dynamic", "Dynamic & Fast", 1.05f, 1.15f, "Energetic pacing ideal for daily commute or active listening")
}

data class AudioState(
    val isPlaying: Boolean = false,
    val isPaused: Boolean = false,
    val currentVerseIndex: Int = 0,
    val currentBookName: String = "",
    val currentChapter: Int = 1,
    val totalVerses: Int = 0,
    val speed: Float = 1.0f,
    val pitch: Float = 1.0f,
    val narratorStyle: VoiceNarratorStyle = VoiceNarratorStyle.WARM
)

data class ReadingPlan(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val category: String,
    val durationDays: Int,
    val passagesPerDay: List<DailyPlanPassage>,
    val completedDays: Set<Int> = emptySet(),
    val isCustom: Boolean = false,
    val iconName: String = "book"
) {
    val progress: Float
        get() = if (durationDays > 0) completedDays.size.toFloat() / durationDays else 0f
}

data class DailyPlanPassage(
    val dayNumber: Int,
    val title: String,
    val bookId: String,
    val chapter: Int,
    val versesSummary: String,
    val reflectionThought: String
)

data class DevotionalPrayer(
    val id: String,
    val title: String,
    val subtitle: String,
    val scriptureReference: String,
    val scriptureText: String,
    val prayerText: String,
    val tags: List<String>
)

data class MemoryVerse(
    val reference: String,
    val text: String,
    val translation: String,
    val tip: String
)

data class BibleHighlight(
    val id: Long = 0,
    val bookId: String,
    val chapter: Int,
    val verseNumber: Int,
    val colorHex: Long,
    val timestamp: Long = System.currentTimeMillis()
)

data class BibleNote(
    val id: Long = 0,
    val bookId: String,
    val bookName: String,
    val chapter: Int,
    val verseNumber: Int,
    val verseText: String,
    val noteContent: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class BibleBookmark(
    val id: Long = 0,
    val bookId: String,
    val bookName: String,
    val chapter: Int,
    val verseNumber: Int,
    val previewText: String,
    val timestamp: Long = System.currentTimeMillis()
)
