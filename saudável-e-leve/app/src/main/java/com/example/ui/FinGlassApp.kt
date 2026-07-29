package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddMealDialog
import com.example.ui.components.AnamnesisProfileDialog
import com.example.ui.components.FoodScannerDialog
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GoalsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.MustardYellow
import com.example.ui.theme.PureWhite

import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.IconButton
import com.example.ui.screens.LoginScreen

enum class NavDestination(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    DASHBOARD("dashboard", "Início", Icons.Default.Dashboard),
    TRANSACTIONS("transactions", "Refeições", Icons.Default.RestaurantMenu),
    GOALS("goals", "Anamnese", Icons.Default.AssignmentInd),
    ANALYTICS("analytics", "Detox", Icons.Default.Spa),
    SETTINGS("settings", "Ajustes", Icons.Default.Settings)
}

@Composable
fun FinGlassApp(viewModel: MainViewModel) {
    val loggedInUser by viewModel.loggedInUser.collectAsStateWithLifecycle()

    // If user is not logged in, display the Login & Password Screen connected to Room DB
    if (loggedInUser == null) {
        LoginScreen(viewModel = viewModel)
        return
    }

    var currentDestination by remember { mutableStateOf(NavDestination.DASHBOARD) }

    val showAddMealDialog by viewModel.showAddMealDialog.collectAsStateWithLifecycle()
    val showAnamnesisDialog by viewModel.showAnamnesisDialog.collectAsStateWithLifecycle()
    val showFoodScannerDialog by viewModel.showFoodScannerDialog.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            Surface(
                color = DarkGreen,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 900.dp)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Saudável e Leve 🌿",
                                color = MustardYellow,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Olá, ${loggedInUser?.fullName ?: userProfile?.name ?: "Ana Paula"}",
                                color = PureWhite.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Logout / Switch Account Button
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(PureWhite.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                                    .clickable { viewModel.logout() }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .testTag("logout_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ExitToApp,
                                        contentDescription = "Sair da Conta",
                                        tint = PureWhite,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Sair",
                                        color = PureWhite,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = DarkGreen,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    NavigationBar(
                        containerColor = DarkGreen,
                        contentColor = PureWhite,
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .widthIn(max = 900.dp)
                            .navigationBarsPadding()
                    ) {
                        NavDestination.entries.forEach { dest ->
                            val isSelected = currentDestination == dest
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { currentDestination = dest },
                                icon = {
                                    Icon(
                                        imageVector = dest.icon,
                                        contentDescription = dest.title,
                                        tint = if (isSelected) DarkGreen else PureWhite.copy(alpha = 0.7f),
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = dest.title,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) MustardYellow else PureWhite.copy(alpha = 0.8f)
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = MustardYellow
                                ),
                                modifier = Modifier.testTag("nav_item_${dest.route}")
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 900.dp)
            ) {
                when (currentDestination) {
                    NavDestination.DASHBOARD -> DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToTransactions = { currentDestination = NavDestination.TRANSACTIONS },
                        onNavigateToGoals = { currentDestination = NavDestination.GOALS }
                    )
                    NavDestination.TRANSACTIONS -> TransactionsScreen(viewModel = viewModel)
                    NavDestination.GOALS -> GoalsScreen(viewModel = viewModel)
                    NavDestination.ANALYTICS -> AnalyticsScreen(viewModel = viewModel)
                    NavDestination.SETTINGS -> SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Modal Dialogs
    if (showAddMealDialog) {
        AddMealDialog(
            onDismiss = { viewModel.showAddMealDialog.value = false },
            onSave = { mealType, foodName, calories, protein, carbs, fat, fiber, nutrientsText, imageUrl, notes ->
                viewModel.addMeal(mealType, foodName, calories, protein, carbs, fat, fiber, nutrientsText, imageUrl, notes)
                viewModel.showAddMealDialog.value = false
            }
        )
    }

    if (showAnamnesisDialog) {
        AnamnesisProfileDialog(
            currentProfile = userProfile ?: com.example.data.UserProfileEntity(),
            onDismiss = { viewModel.showAnamnesisDialog.value = false },
            onSave = { updatedProfile ->
                viewModel.updateProfile(updatedProfile)
                viewModel.showAnamnesisDialog.value = false
            }
        )
    }

    if (showFoodScannerDialog) {
        FoodScannerDialog(
            userProfile = userProfile,
            onDismiss = { viewModel.showFoodScannerDialog.value = false },
            onSaveMealToDiary = { mealType, foodName, calories, protein, carbs, fat, fiber, nutrientsText, imageUrl, notes ->
                viewModel.addMeal(mealType, foodName, calories, protein, carbs, fat, fiber, nutrientsText, imageUrl, notes)
                viewModel.showFoodScannerDialog.value = false
            }
        )
    }
}

