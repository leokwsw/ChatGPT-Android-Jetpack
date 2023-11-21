package dev.leonardpark.chatgpt.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.leonardpark.chatgpt.model.Screen
import dev.leonardpark.chatgpt.ui.screen.AudioScreen
import dev.leonardpark.chatgpt.ui.screen.ChatScreen
import dev.leonardpark.chatgpt.ui.screen.ChatsScreen
import dev.leonardpark.chatgpt.ui.screen.PicturesScreen
import dev.leonardpark.chatgpt.ui.screen.SettingsScreen
import dev.leonardpark.chatgpt.ui.screen.SetupScreen
import dev.leonardpark.chatgpt.ui.theme.OpenAITheme
import dev.leonardpark.chatgpt.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            OpenAITheme {
                Surface(modifier = Modifier.imePadding()) {
                    val openSetup by viewModel.shouldOpenSetup.collectAsStateWithLifecycle(initialValue = false)

                    Crossfade(targetState = openSetup, label = "Setup") {
                        if (it) {
                            SetupScreen()
                        } else {
                            val navController = rememberNavController()

                            NavHost(
                                navController = navController,
                                startDestination = Screen.Chats.route
                            ) {
                                composable(
                                    route = Screen.Chats.route,
                                    enterTransition = { fadeIn() },
                                    exitTransition = { fadeOut() }
                                ) {
                                    ChatsScreen(navController = navController)
                                }

                                composable(
                                    route = "${Screen.Chat.route}/{id}",
                                    arguments = listOf(
                                        navArgument("id") {
                                            type = NavType.LongType
                                        }
                                    ),
                                    enterTransition = { fadeIn() },
                                    exitTransition = { fadeOut() }
                                ) {
                                    ChatScreen(navController = navController)
                                }

                                composable(
                                    route = Screen.Pictures.route,
                                    enterTransition = { fadeIn() },
                                    exitTransition = { fadeOut() }
                                ) {
                                    PicturesScreen(navController = navController)
                                }
                                
                                composable(
                                    route = Screen.Audio.route,
                                    enterTransition = { fadeIn() },
                                    exitTransition = { fadeOut() }
                                ) {
                                    AudioScreen(navController = navController)
                                }

                                composable(
                                    route = Screen.Settings.route,
                                    enterTransition = { fadeIn() },
                                    exitTransition = { fadeOut() }
                                ) {
                                    SettingsScreen(navController = navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}