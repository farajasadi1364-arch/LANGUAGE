package com.languageos.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.languageos.app.ui.home.HomeScreen
import com.languageos.app.ui.learn.LessonListScreen
import com.languageos.app.ui.learn.LessonPlayerScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object LessonList : Screen("lessons/{courseId}") {
        fun createRoute(courseId: String) = "lessons/$courseId"
    }
    data object LessonPlayer : Screen("lesson/{lessonId}") {
        fun createRoute(lessonId: String) = "lesson/$lessonId"
    }
}

@Composable
fun LanguageOSNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                onStartLearning = { courseId ->
                    navController.navigate(Screen.LessonList.createRoute(courseId))
                }
            )
        }

        composable(Screen.LessonList.route) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId").orEmpty()
            LessonListScreen(
                courseId = courseId,
                onLessonSelected = { lessonId ->
                    navController.navigate(Screen.LessonPlayer.createRoute(lessonId))
                }
            )
        }

        composable(Screen.LessonPlayer.route) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId").orEmpty()
            LessonPlayerScreen(lessonId = lessonId, onFinished = { navController.popBackStack() })
        }
    }
}
