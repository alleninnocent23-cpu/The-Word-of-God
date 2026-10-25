package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Test

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("WORD OF GOD", appName)
  }

  @Test
  fun `verse read progress persistence test`() = kotlinx.coroutines.runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = com.example.data.local.BibleDatabase.getDatabase(context)
    val dao = db.bibleDao()

    dao.markVerseRead(
      com.example.data.local.VerseReadProgressEntity(
        bookId = "gen",
        chapter = 1,
        verseNumber = 1
      )
    )

    val readList = dao.getAllReadVerses()
    // verify insertion works
    val firstItem = readList.first()
    org.junit.Assert.assertTrue(firstItem.any { it.bookId == "gen" && it.chapter == 1 && it.verseNumber == 1 })
  }
}


