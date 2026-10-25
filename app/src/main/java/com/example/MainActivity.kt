package com.example

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.BibleDataProvider
import com.example.ui.screens.BibleReaderScreen
import com.example.ui.screens.PlansScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.TodayScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.ScriptureBibleTheme
import com.example.viewmodel.BibleViewModel

enum class AppNavTab(
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTag: String
) {
    TODAY("Today", Icons.Filled.WbSunny, Icons.Outlined.WbSunny, "tab_today"),
    BIBLE("Bible", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "tab_bible"),
    GUIDES("Guides", Icons.Filled.Explore, Icons.Outlined.Explore, "tab_guides"),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person, "tab_profile")
}

class MainActivity : ComponentActivity() {

    private val viewModel: BibleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settings by viewModel.readerSettings.collectAsState()
            val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsState()
            val userName by viewModel.userName.collectAsState()

            // Request runtime notification permission on Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val notificationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { /* notification permission result */ }
                )
                LaunchedEffect(Unit) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            ScriptureBibleTheme(themeMode = settings.themeMode) {
                var currentTab by rememberSaveable { mutableStateOf(AppNavTab.TODAY) }

                if (!isOnboardingCompleted) {
                    // WELCOME & ONBOARDING FLOW
                    // Shows app name "WORD OF GOD", image of the Bible, "Get Started" button,
                    // text input for name, and "Enter" button to continue straight to the Bible!
                    WelcomeScreen(
                        initialName = userName,
                        onContinueToBible = { enteredName ->
                            viewModel.completeOnboarding(enteredName)
                            currentTab = AppNavTab.BIBLE
                        }
                    )
                } else {
                    BackHandler(enabled = currentTab != AppNavTab.TODAY) {
                        currentTab = AppNavTab.TODAY
                    }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        contentWindowInsets = WindowInsets.safeDrawing,
                        bottomBar = {
                            NavigationBar(
                                modifier = Modifier
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                                    .testTag("main_navigation_bar"),
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 6.dp
                            ) {
                                AppNavTab.entries.forEach { tab ->
                                    val isSelected = currentTab == tab
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { currentTab = tab },
                                        icon = {
                                            Icon(
                                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                                contentDescription = tab.title
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = tab.title,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                            indicatorColor = MaterialTheme.colorScheme.primary,
                                            selectedTextColor = MaterialTheme.colorScheme.primary,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        modifier = Modifier.testTag(tab.testTag)
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        when (currentTab) {
                            AppNavTab.TODAY -> {
                                TodayScreen(
                                    viewModel = viewModel,
                                    onNavigateToBible = { bookId, chapter ->
                                        val book = BibleDataProvider.getBook(bookId)
                                        viewModel.selectBookAndChapter(book, chapter)
                                        currentTab = AppNavTab.BIBLE
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            AppNavTab.BIBLE -> {
                                BibleReaderScreen(
                                    viewModel = viewModel,
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            AppNavTab.GUIDES -> {
                                PlansScreen(
                                    viewModel = viewModel,
                                    onNavigateToBible = { bookId, chapter ->
                                        val book = BibleDataProvider.getBook(bookId)
                                        viewModel.selectBookAndChapter(book, chapter)
                                        currentTab = AppNavTab.BIBLE
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                            AppNavTab.PROFILE -> {
                                ProfileScreen(
                                    viewModel = viewModel,
                                    onNavigateToBible = { bookId, chapter ->
                                        val book = BibleDataProvider.getBook(bookId)
                                        viewModel.selectBookAndChapter(book, chapter)
                                        currentTab = AppNavTab.BIBLE
                                    },
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
