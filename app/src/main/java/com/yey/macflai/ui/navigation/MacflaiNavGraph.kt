package com.yey.macflai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yey.macflai.ui.screens.AxisDetailScreen
import com.yey.macflai.ui.screens.HomeScreen
import com.yey.macflai.ui.screens.LoginScreen
import com.yey.macflai.ui.screens.QuizScreen
import com.yey.macflai.viewmodel.AuthUiState
import com.yey.macflai.viewmodel.AuthViewModel
import com.yey.macflai.viewmodel.SinclairViewModel

@Composable
fun MacflaiNavGraph(
    viewModel: SinclairViewModel,
    authViewModel: AuthViewModel,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "login"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onNavigateToAxis = { axisId ->
                    navController.navigate("eje_detail/$axisId")
                }
            )
        }
        composable("eje_detail/{axisId}") { backStackEntry ->
            val axisId = backStackEntry.arguments?.getString("axisId") ?: "Palabras y Significado"
            AxisDetailScreen(
                axisId = axisId,
                onStartJourney = {
                    navController.navigate("quiz/$axisId")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("quiz/{axisId}") { backStackEntry ->
            val axisId = backStackEntry.arguments?.getString("axisId") ?: "Palabras y Significado"
            QuizScreen(
                viewModel = viewModel,
                axisId = axisId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
