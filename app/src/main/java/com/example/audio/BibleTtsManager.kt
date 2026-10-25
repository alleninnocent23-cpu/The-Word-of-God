package com.example.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.model.AudioState
import com.example.model.Verse
import com.example.model.VoiceNarratorStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class BibleTtsManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private val _audioState = MutableStateFlow(AudioState())
    val audioState: StateFlow<AudioState> = _audioState.asStateFlow()

    private var activeVerses: List<Verse> = emptyList()
    private val mainScope = CoroutineScope(Dispatchers.Main)

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("BibleTtsManager", "TTS Locale US not supported, using default locale")
                tts?.setLanguage(Locale.getDefault())
            }
            isTtsReady = true
            setupUtteranceListener()
            applyVoiceParams()
        } else {
            Log.e("BibleTtsManager", "TextToSpeech init failed with status: $status")
            isTtsReady = false
        }
    }

    private fun setupUtteranceListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                utteranceId?.let { id ->
                    if (id.startsWith("verse_")) {
                        val index = id.removePrefix("verse_").toIntOrNull() ?: 0
                        mainScope.launch {
                            _audioState.value = _audioState.value.copy(
                                isPlaying = true,
                                isPaused = false,
                                currentVerseIndex = index
                            )
                        }
                    }
                }
            }

            override fun onDone(utteranceId: String?) {
                utteranceId?.let { id ->
                    val index = id.removePrefix("verse_").toIntOrNull() ?: 0
                    if (index >= activeVerses.size - 1) {
                        mainScope.launch {
                            _audioState.value = _audioState.value.copy(
                                isPlaying = false,
                                isPaused = false,
                                currentVerseIndex = 0
                            )
                        }
                    }
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                Log.e("BibleTtsManager", "TTS error on utterance: $utteranceId")
                mainScope.launch {
                    _audioState.value = _audioState.value.copy(isPlaying = false, isPaused = false)
                }
            }
        })
    }

    fun playChapter(
        verses: List<Verse>,
        bookName: String,
        chapter: Int,
        startVerseIndex: Int = 0
    ) {
        if (verses.isEmpty()) return
        activeVerses = verses

        _audioState.value = _audioState.value.copy(
            isPlaying = true,
            isPaused = false,
            currentBookName = bookName,
            currentChapter = chapter,
            currentVerseIndex = startVerseIndex,
            totalVerses = verses.size
        )

        if (!isTtsReady) {
            Log.w("BibleTtsManager", "TTS not ready yet; queuing fallback state")
            return
        }

        tts?.stop()
        applyVoiceParams()

        // Queue each verse starting from startVerseIndex
        for (i in startVerseIndex until verses.size) {
            val v = verses[i]
            val textToSpeak = "Verse ${v.verseNumber}. ${v.text}"
            val utteranceId = "verse_$i"
            val queueMode = if (i == startVerseIndex) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
            tts?.speak(textToSpeak, queueMode, null, utteranceId)
        }
    }

    fun pause() {
        if (_audioState.value.isPlaying) {
            tts?.stop()
            _audioState.value = _audioState.value.copy(
                isPlaying = false,
                isPaused = true
            )
        }
    }

    fun resume() {
        if (_audioState.value.isPaused && activeVerses.isNotEmpty()) {
            playChapter(
                verses = activeVerses,
                bookName = _audioState.value.currentBookName,
                chapter = _audioState.value.currentChapter,
                startVerseIndex = _audioState.value.currentVerseIndex
            )
        }
    }

    fun stop() {
        tts?.stop()
        _audioState.value = _audioState.value.copy(
            isPlaying = false,
            isPaused = false
        )
    }

    fun togglePlayPause(verses: List<Verse>, bookName: String, chapter: Int) {
        if (_audioState.value.isPlaying) {
            pause()
        } else if (_audioState.value.isPaused) {
            resume()
        } else {
            playChapter(verses, bookName, chapter, 0)
        }
    }

    fun seekToVerse(index: Int) {
        if (activeVerses.isNotEmpty() && index in activeVerses.indices) {
            playChapter(
                verses = activeVerses,
                bookName = _audioState.value.currentBookName,
                chapter = _audioState.value.currentChapter,
                startVerseIndex = index
            )
        }
    }

    fun nextVerse() {
        val nextIdx = _audioState.value.currentVerseIndex + 1
        if (nextIdx < activeVerses.size) {
            seekToVerse(nextIdx)
        }
    }

    fun previousVerse() {
        val prevIdx = (_audioState.value.currentVerseIndex - 1).coerceAtLeast(0)
        seekToVerse(prevIdx)
    }

    fun setNarratorStyle(style: VoiceNarratorStyle) {
        _audioState.value = _audioState.value.copy(
            narratorStyle = style,
            pitch = style.pitch,
            speed = style.speed
        )
        applyVoiceParams()
    }

    fun setSpeed(speed: Float) {
        _audioState.value = _audioState.value.copy(speed = speed)
        applyVoiceParams()
    }

    fun setPitch(pitch: Float) {
        _audioState.value = _audioState.value.copy(pitch = pitch)
        applyVoiceParams()
    }

    private fun applyVoiceParams() {
        val speed = _audioState.value.speed
        val pitch = _audioState.value.pitch
        tts?.setSpeechRate(speed)
        tts?.setPitch(pitch)
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("BibleTtsManager", "Error shutting down TTS", e)
        }
    }
}
