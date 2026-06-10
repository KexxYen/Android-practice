package ci.nsu.moble.main.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import ci.nsu.moble.main.data.ServiceLocator

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
@Composable
fun AppNav() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val serviceLocator = remember {
        ServiceLocator(context.applicationContext)
    }

    val authViewModel: AuthViewModel = viewModel()

    val depositViewModel: DepositViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DepositViewModel(serviceLocator.depositRepository) as T
            }
        }
    )

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("tabs") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("tabs") {
            MainTabsScreen(
                authViewModel = authViewModel,
                depositViewModel = depositViewModel,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("tabs") { inclusive = true }
                    }
                },
                onOpenNewCalculation = {
                    navController.navigate("step1")
                },
                onOpenDetail = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }

        composable("step1") {
            Step1Screen(
                nav = navController,
                vm = depositViewModel
            )
        }

        composable("step2") {
            Step2Screen(
                nav = navController,
                vm = depositViewModel
            )
        }

        composable("result") {
            ResultScreen(
                nav = navController,
                vm = depositViewModel
            )
        }

        composable(
            route = "detail/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L

            DetailScreen(
                nav = navController,
                vm = depositViewModel,
                id = id
            )
        }
    }
}

@Composable
fun MainTabsScreen(
    authViewModel: AuthViewModel,
    depositViewModel: DepositViewModel,
    onLogout: () -> Unit,
    onOpenNewCalculation: () -> Unit,
    onOpenDetail: (Long) -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("Пользователи") },
                    icon = { Icon(Icons.Default.People, contentDescription = null) }
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("Мои расчёты") },
                    icon = { Icon(Icons.Default.History, contentDescription = null) }
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        onOpenNewCalculation()
                    },
                    label = { Text("Новый расчёт") },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = null) }
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> {
                HomeScreen(
                    viewModel = authViewModel,
                    onLogout = onLogout
                )
            }

            1 -> {
                HistoryScreenWrapper(
                    modifier = Modifier.padding(padding),
                    vm = depositViewModel,
                    onOpenDetail = onOpenDetail
                )
            }
        }
    }
}