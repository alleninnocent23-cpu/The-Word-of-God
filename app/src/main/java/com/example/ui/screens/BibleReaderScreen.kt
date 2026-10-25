package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.filled.Close

import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FormatColorReset
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BibleDataProvider
import com.example.model.Verse
import com.example.ui.components.AppearanceSheet
import com.example.ui.components.AudioPlayerBar
import com.example.ui.components.BookChapterPickerSheet
import com.example.ui.components.VersionPickerSheet
import com.example.ui.theme.HighlightBlue
import com.example.ui.theme.HighlightGold
import com.example.ui.theme.HighlightGreen
import com.example.ui.theme.HighlightPink
import com.example.ui.theme.HighlightPurple
import com.example.ui.theme.JesusRed
import com.example.ui.theme.getFontFamily
import com.example.viewmodel.BibleViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BibleReaderScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedBook by viewModel.selectedBook.collectAsState()
    val selectedChapter by viewModel.selectedChapter.collectAsState()
    val selectedVersion by viewModel.selectedVersion.collectAsState()
    val verses by viewModel.currentVerses.collectAsState()
    val settings by viewModel.readerSettings.collectAsState()
    val audioState by viewModel.audioState.collectAsState()
    val highlights by viewModel.allHighlights.collectAsState()
    val bookmarks by viewModel.allBookmarks.collectAsState()

    // Room Reading Progress on each Chapter and Verse
    val readVerseKeys by viewModel.readVerseKeys.collectAsState()
    val currentChapterReadCount = remember(verses, readVerseKeys) {
        verses.count { readVerseKeys.contains("${it.bookId}_${it.chapter}_${it.verseNumber}") }
    }
    val currentChapterTotal = verses.size
    val currentChapterProgress = if (currentChapterTotal > 0) currentChapterReadCount.toFloat() / currentChapterTotal else 0f
    val isChapterComplete = currentChapterTotal > 0 && currentChapterReadCount == currentChapterTotal

    // Room Search state
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    var showBookPicker by remember { mutableStateOf(false) }
    var showVersionPicker by remember { mutableStateOf(false) }
    var showAppearanceSheet by remember { mutableStateOf(false) }

    // Long-pressed / selected verse for quick highlighting
    var selectedVerseForHighlight by remember { mutableStateOf<Verse?>(null) }
    var showNoteDialogForVerse by remember { mutableStateOf<Verse?>(null) }
    var noteInputText by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    val highlightColors = listOf(
        0xFFFFD166 to HighlightGold,
        0xFF06D6A0 to HighlightGreen,
        0xFF118AB2 to HighlightBlue,
        0xFFEF476F to HighlightPink,
        0xFF9D4EDD to HighlightPurple
    )

    // Auto-scroll to current spoken verse when audio is playing
    LaunchedEffect(audioState.currentVerseIndex, audioState.isPlaying) {
        if (audioState.isPlaying && audioState.currentVerseIndex in verses.indices) {
            listState.animateScrollToItem((audioState.currentVerseIndex + 1).coerceAtLeast(0))
        }
    }

    if (showBookPicker) {
        BookChapterPickerSheet(
            currentBook = selectedBook,
            currentChapter = selectedChapter,
            readVerseKeys = readVerseKeys,
            onBookChapterSelected = { book, chapter ->
                viewModel.selectBookAndChapter(book, chapter)
            },
            onDismiss = { showBookPicker = false }
        )
    }


    if (showVersionPicker) {
        VersionPickerSheet(
            currentVersion = selectedVersion,
            onVersionSelected = { version ->
                viewModel.selectVersion(version)
            },
            onDismiss = { showVersionPicker = false }
        )
    }

    if (showAppearanceSheet) {
        AppearanceSheet(
            settings = settings,
            onSettingsChanged = { newSettings ->
                viewModel.updateSettings(newSettings)
            },
            onDismiss = { showAppearanceSheet = false }
        )
    }

    // Add note dialog
    showNoteDialogForVerse?.let { verse ->
        AlertDialog(
            onDismissRequest = { showNoteDialogForVerse = null },
            title = {
                Text(
                    text = "Add Note for ${verse.bookName} ${verse.chapter}:${verse.verseNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "\"${verse.text}\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = noteInputText,
                        onValueChange = { noteInputText = it },
                        placeholder = { Text("Write personal thoughts, prayer points, study reflections...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .testTag("reader_note_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noteInputText.isNotBlank()) {
                            viewModel.saveNote(verse, noteInputText.trim())
                            Toast.makeText(context, "Note saved to Room DB", Toast.LENGTH_SHORT).show()
                        }
                        showNoteDialogForVerse = null
                        selectedVerseForHighlight = null
                    },
                    modifier = Modifier.testTag("save_reader_note_btn")
                ) {
                    Text("Save Note")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNoteDialogForVerse = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TOP SEARCH BAR (Room Full-Text Search directly at the top of the Bible reader view)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchVerses(it) },
                    placeholder = {
                        Text(
                            text = "Search books, chapters, or keywords (e.g. Ruth, love, light)...",
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(
                                onClick = { viewModel.clearSearch() },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("top_bible_search_bar"),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Navigation Pills Row: [Book Ch | Version] and action icons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Top Pill Group: [Book Ch | Version]
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .clickable { showBookPicker = true }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("book_chapter_picker_pill")
                            ) {
                                Text(
                                    text = "${selectedBook.name} $selectedChapter",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { showVersionPicker = true }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .testTag("version_picker_pill")
                            ) {
                                Text(
                                    text = selectedVersion.code,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    // Action Icons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.toggleAudioPlayback() },
                            modifier = Modifier.size(38.dp).testTag("top_audio_btn")
                        ) {
                            Icon(
                                imageVector = if (audioState.isPlaying) Icons.Default.VolumeUp else Icons.Default.Headphones,
                                contentDescription = "Audio Reader",
                                tint = if (audioState.isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = { showAppearanceSheet = true },
                            modifier = Modifier.size(38.dp).testTag("top_appearance_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Appearance & Fonts",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // CHAPTER READING PROGRESS BAR (Live Room progress tracking on each chapter)
            if (currentChapterTotal > 0 && searchQuery.isBlank()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (isChapterComplete) Color(0xFF06D6A0).copy(alpha = 0.45f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Chapter Reading Progress",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$currentChapterReadCount/$currentChapterTotal verses (${(currentChapterProgress * 100).toInt()}%)",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isChapterComplete) Color(0xFF06D6A0) else MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            androidx.compose.material3.LinearProgressIndicator(
                                progress = { currentChapterProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = if (isChapterComplete) Color(0xFF06D6A0) else MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surface
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        TextButton(
                            onClick = {
                                if (isChapterComplete) {
                                    viewModel.clearCurrentChapterProgress()
                                } else {
                                    viewModel.markCurrentChapterComplete()
                                }
                            },
                            modifier = Modifier
                                .height(30.dp)
                                .testTag("chapter_complete_toggle_btn"),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isChapterComplete) Icons.Default.CheckCircle else Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (isChapterComplete) Color(0xFF06D6A0) else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isChapterComplete) "Done" else "Mark All",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isChapterComplete) Color(0xFF06D6A0) else MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            // SEARCH RESULTS OVERLAY (When user is searching via Room full-text search)
            if (searchQuery.isNotBlank()) {

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Found ${searchResults.size} result(s) in Room DB",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (isSearching) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            }
                        }

                        if (searchResults.isEmpty() && !isSearching) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No verses found matching \"$searchQuery\"",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(bottom = 120.dp)
                            ) {
                                itemsIndexed(searchResults) { _, verse ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable {
                                                val book = BibleDataProvider.getBook(verse.bookId)
                                                viewModel.selectBookAndChapter(book, verse.chapter)
                                                viewModel.clearSearch()
                                            },
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = "${verse.bookName} ${verse.chapter}:${verse.verseNumber}",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = verse.text,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // NORMAL SCRIPTURE TEXT LIST
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 22.dp),
                    contentPadding = PaddingValues(top = 10.dp, bottom = 140.dp)
                ) {
                    // Header (Book name small & Chapter / Title big)
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = selectedBook.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    letterSpacing = 1.sp
                                ),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (selectedBook.id == "rev" && selectedChapter == 1) {
                                    "Introduction"
                                } else {
                                    "Chapter $selectedChapter"
                                },
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontFamily = getFontFamily(settings.readerFont),
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    // Verses with Long-press selection and highlighting
                    itemsIndexed(verses, key = { _, v -> "${v.bookId}_${v.chapter}_${v.verseNumber}" }) { index, verse ->
                        val isSpokenNow = audioState.isPlaying && audioState.currentVerseIndex == index
                        val isSelected = selectedVerseForHighlight == verse

                        val highlight = highlights.firstOrNull {
                            it.bookId == verse.bookId && it.chapter == verse.chapter && it.verseNumber == verse.verseNumber
                        }
                        val highlightBg = highlight?.let { Color(it.colorHex).copy(alpha = 0.38f) }

                        val textColor = when {
                            verse.isRedLetter && settings.redLetterWords -> JesusRed
                            else -> MaterialTheme.colorScheme.onBackground
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when {
                                        isSpokenNow -> MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
                                        highlightBg != null -> highlightBg
                                        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                        else -> Color.Transparent
                                    }
                                )
                                .then(
                                    when {
                                        isSelected -> Modifier.border(
                                            width = 1.5.dp,
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        isSpokenNow -> Modifier.border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        else -> Modifier
                                    }
                                )
                                .combinedClickable(
                                    onLongClick = {
                                        selectedVerseForHighlight = verse
                                        Toast.makeText(context, "Selected ${verse.bookName} ${verse.chapter}:${verse.verseNumber}", Toast.LENGTH_SHORT).show()
                                    },
                                    onClick = {
                                        if (selectedVerseForHighlight != null) {
                                            selectedVerseForHighlight = if (selectedVerseForHighlight == verse) null else verse
                                        } else {
                                            selectedVerseForHighlight = verse
                                        }
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .testTag("verse_${verse.verseNumber}")
                        ) {
                            val isVerseRead = readVerseKeys.contains("${verse.bookId}_${verse.chapter}_${verse.verseNumber}")

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                // Verse read check indicator/button
                                IconButton(
                                    onClick = { viewModel.toggleVerseRead(verse) },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(end = 4.dp)
                                        .testTag("verse_read_check_${verse.verseNumber}")
                                ) {
                                    Icon(
                                        imageVector = if (isVerseRead) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                                        contentDescription = if (isVerseRead) "Mark unread" else "Mark read",
                                        tint = if (isVerseRead) Color(0xFF06D6A0) else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                                        modifier = Modifier.size(15.dp)
                                    )
                                }

                                if (settings.showVerseNumbers) {
                                    Text(
                                        text = "${verse.verseNumber}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = (settings.fontSizeSp * 0.7f).sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = if (isSpokenNow || isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                                        modifier = Modifier.padding(end = 6.dp, top = 2.dp)
                                    )
                                }

                                Text(
                                    text = verse.text,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = getFontFamily(settings.readerFont),
                                        fontSize = settings.fontSizeSp.sp,
                                        lineHeight = (settings.fontSizeSp * settings.lineHeightMultiplier).sp,
                                        letterSpacing = settings.letterSpacingSp.sp
                                    ),
                                    color = textColor,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // End-of-Chapter Reading Progress Summary Card
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isChapterComplete) Color(0xFF142921) else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isChapterComplete) Color(0xFF06D6A0).copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = if (isChapterComplete) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = if (isChapterComplete) Color(0xFF06D6A0) else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isChapterComplete) "${selectedBook.name} Chapter $selectedChapter Completed!" else "Reading ${selectedBook.name} Chapter $selectedChapter",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isChapterComplete) Color(0xFF06D6A0) else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "$currentChapterReadCount of $currentChapterTotal verses read (${(currentChapterProgress * 100).toInt()}%)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    if (!isChapterComplete) {
                                        Button(
                                            onClick = { viewModel.markCurrentChapterComplete() },
                                            modifier = Modifier.testTag("mark_chapter_done_bottom_btn")
                                        ) {
                                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Mark Chapter as Read")
                                        }
                                    } else {
                                        Button(
                                            onClick = {
                                                val nextCh = selectedChapter + 1
                                                if (nextCh <= selectedBook.totalChapters) {
                                                    viewModel.selectBookAndChapter(selectedBook, nextCh)
                                                }
                                            },
                                            enabled = selectedChapter < selectedBook.totalChapters,
                                            modifier = Modifier.testTag("next_chapter_btn")
                                        ) {
                                            Text("Continue to Next Chapter")
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }


        // FLOATING QUICK HIGHLIGHT & SELECTION TOOLBAR
        AnimatedVisibility(
            visible = selectedVerseForHighlight != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp, start = 16.dp, end = 16.dp)
        ) {
            selectedVerseForHighlight?.let { verse ->
                val currentHighlight = highlights.firstOrNull {
                    it.bookId == verse.bookId && it.chapter == verse.chapter && it.verseNumber == verse.verseNumber
                }
                val isBookmarked = bookmarks.any {
                    it.bookId == verse.bookId && it.chapter == verse.chapter && it.verseNumber == verse.verseNumber
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${verse.bookName} ${verse.chapter}:${verse.verseNumber}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        noteInputText = ""
                                        showNoteDialogForVerse = verse
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EditNote,
                                        contentDescription = "Add Note",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.toggleBookmark(verse)
                                        Toast.makeText(
                                            context,
                                            if (isBookmarked) "Bookmark removed" else "Verse bookmarked",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Bookmark",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                val isSelectedVerseRead = readVerseKeys.contains("${verse.bookId}_${verse.chapter}_${verse.verseNumber}")
                                IconButton(
                                    onClick = {
                                        viewModel.toggleVerseRead(verse)
                                        Toast.makeText(
                                            context,
                                            if (isSelectedVerseRead) "Marked unread" else "Marked as read ✓",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    modifier = Modifier.size(32.dp).testTag("toolbar_toggle_read_btn")
                                ) {
                                    Icon(
                                        imageVector = if (isSelectedVerseRead) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                                        contentDescription = "Toggle Read Status",
                                        tint = if (isSelectedVerseRead) Color(0xFF06D6A0) else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }


                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText(
                                            "Bible Verse",
                                            "\"${verse.text}\" — ${verse.bookName} ${verse.chapter}:${verse.verseNumber}"
                                        )
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Copied verse", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy text",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val shareIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(
                                                Intent.EXTRA_TEXT,
                                                "\"${verse.text}\"\n— ${verse.bookName} ${verse.chapter}:${verse.verseNumber} (WORD OF GOD)"
                                            )
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "Share Scripture"))
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { selectedVerseForHighlight = null },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // COLOR HIGHLIGHT PALETTE: Quick click to persist highlight to Room DB
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            highlightColors.forEach { (colorHex, displayColor) ->
                                val isColorActive = currentHighlight?.colorHex == colorHex
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(displayColor)
                                        .border(
                                            width = if (isColorActive) 2.5.dp else 1.dp,
                                            color = if (isColorActive) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.5f),
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            viewModel.saveHighlight(verse, colorHex)
                                            Toast.makeText(context, "Saved highlight to Room", Toast.LENGTH_SHORT).show()
                                            selectedVerseForHighlight = null
                                        }
                                        .testTag("quick_highlight_color_${colorHex}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isColorActive) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Active color",
                                            tint = Color.Black,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            // Clear highlight button
                            IconButton(
                                onClick = {
                                    viewModel.removeHighlight(verse)
                                    Toast.makeText(context, "Highlight removed from Room", Toast.LENGTH_SHORT).show()
                                    selectedVerseForHighlight = null
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), CircleShape)
                                    .testTag("clear_highlight_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatColorReset,
                                    contentDescription = "Remove highlight",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // TRANSPARENT FLOATING PLAY CONTROLS
        AudioPlayerBar(
            audioState = audioState,
            onTogglePlay = { viewModel.toggleAudioPlayback() },
            onPrevChapter = { viewModel.previousChapter() },
            onNextChapter = { viewModel.nextChapter() },
            onPrevVerse = { viewModel.ttsManager.previousVerse() },
            onNextVerse = { viewModel.ttsManager.nextVerse() },
            onSpeedChange = { viewModel.setAudioSpeed(it) },
            onVoiceStyleChange = { viewModel.setVoiceNarratorStyle(it) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        )
    }
}
