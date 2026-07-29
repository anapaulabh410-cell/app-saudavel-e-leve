package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.MealEntity
import com.example.ui.MainViewModel
import com.example.ui.components.DarkGlassCard
import com.example.ui.components.DonutCategoryChart
import com.example.ui.components.GlassCard
import com.example.ui.components.InteractiveLineChart
import com.example.ui.theme.DarkGrayText
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.GlassBorderLight
import com.example.ui.theme.LightGrayBG
import com.example.ui.theme.LightGreenAccent
import com.example.ui.theme.MustardYellow
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SecondaryTextGray

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onNavigateToTransactions: () -> Unit,
    onNavigateToGoals: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val todayCalories by viewModel.todayCalories.collectAsStateWithLifecycle()
    val todayProtein by viewModel.todayProtein.collectAsStateWithLifecycle()
    val todayCarbs by viewModel.todayCarbs.collectAsStateWithLifecycle()
    val todayFat by viewModel.todayFat.collectAsStateWithLifecycle()
    val todayFiber by viewModel.todayFiber.collectAsStateWithLifecycle()
    val todayWaterMl by viewModel.todayWaterMl.collectAsStateWithLifecycle()
    val meals by viewModel.rawMeals.collectAsStateWithLifecycle()
    val macroSlices by viewModel.macroSlices.collectAsStateWithLifecycle()
    val calorieTrendPoints by viewModel.calorieTrendPoints.collectAsStateWithLifecycle()
    val timeframe by viewModel.selectedTimeframe.collectAsStateWithLifecycle()

    val calorieTarget = userProfile?.dailyCalorieTarget ?: 1800
    val waterTarget = userProfile?.dailyWaterTargetMl ?: 2500
    val proteinTarget = userProfile?.dailyProteinTarget ?: 110
    val carbsTarget = userProfile?.dailyCarbsTarget ?: 180
    val fatTarget = userProfile?.dailyFatTarget ?: 50

    val calProgress = (todayCalories.toFloat() / calorieTarget.toFloat()).coerceIn(0f, 1f)
    val waterProgress = (todayWaterMl.toFloat() / waterTarget.toFloat()).coerceIn(0f, 1f)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightGrayBG)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Top Navigation / User Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(DarkGreen, CircleShape)
                            .border(2.dp, MustardYellow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile?.name?.take(2)?.uppercase() ?: "AP",
                            color = PureWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Olá, ${userProfile?.name ?: "Ana Paula"} 👋",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrayText
                        )
                        Text(
                            text = "Saudável e Leve • ${userProfile?.goal ?: "Alimentação Equilibrada"}",
                            fontSize = 12.sp,
                            color = SecondaryTextGray
                        )
                    }
                }

                // Action Buttons (Scanner IA & + Refeição)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Button(
                        onClick = { viewModel.showFoodScannerDialog.value = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Escanear Prato",
                            tint = MustardYellow,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Escanear IA 📸", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PureWhite)
                    }

                    Button(
                        onClick = { viewModel.showAddMealDialog.value = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LightGrayBG)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Nova Refeição",
                            tint = DarkGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "+", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                    }
                }
            }
        }

        // Hero AI Food Scanner Quick Banner Card
        item {
            androidx.compose.material3.Card(
                shape = RoundedCornerShape(20.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = DarkGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.showFoodScannerDialog.value = true }
                    .border(1.dp, MustardYellow, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(MustardYellow, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Scanner IA",
                                tint = DarkGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Escanear Prato com IA",
                                    color = PureWhite,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .background(MustardYellow, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "NOVO 📸",
                                        color = DarkGreen,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Descubra calorias, macros e se o prato está de acordo com sua dieta",
                                color = PureWhite.copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Abrir Scanner",
                        tint = MustardYellow,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Hero Calorie Summary Card (Dark Glass)
        item {
            DarkGlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(OrangeAccent.copy(alpha = 0.2f), CircleShape)
                                    .border(1.dp, OrangeAccent.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Calorias",
                                    tint = OrangeAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Balanço Calórico Diário",
                                color = PureWhite.copy(alpha = 0.85f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(MustardYellow, RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${(calProgress * 100).toInt()}% da meta",
                                color = DarkGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "$todayCalories kcal",
                                color = PureWhite,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Meta diária: $calorieTarget kcal • Restam ${(calorieTarget - todayCalories).coerceAtLeast(0)} kcal",
                                color = PureWhite.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { calProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = MustardYellow,
                        trackColor = PureWhite.copy(alpha = 0.2f)
                    )
                }
            }
        }

        // Water Hydration Tracker Card (Consumo de Água por Dia)
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(Color(0xFF2196F3).copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalDrink,
                                    contentDescription = "Água",
                                    tint = Color(0xFF2196F3),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Consumo de Água Hoje",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkGrayText
                                )
                                Text(
                                    text = "$todayWaterMl ml de $waterTarget ml (${(waterProgress * 100).toInt()}%)",
                                    fontSize = 12.sp,
                                    color = SecondaryTextGray
                                )
                            }
                        }

                        IconButton(onClick = { viewModel.resetWater() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reiniciar Água",
                                tint = SecondaryTextGray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { waterProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF2196F3),
                        trackColor = Color(0xFF2196F3).copy(alpha = 0.15f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.addWater(250) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("+ 250ml", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                        }

                        OutlinedButton(
                            onClick = { viewModel.addWater(500) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("+ 500ml", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                        }
                    }
                }
            }
        }

        // Macronutrients Overview Cards
        item {
            Column {
                Text(
                    text = "Macronutrientes Principais",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGrayText
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Proteínas Card
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column {
                            Text(text = "Proteínas", fontSize = 12.sp, color = SecondaryTextGray, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "${todayProtein.toInt()}g / ${proteinTarget}g", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { (todayProtein.toFloat() / proteinTarget.toFloat()).coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = DarkGreen,
                                trackColor = DarkGreen.copy(alpha = 0.15f)
                            )
                        }
                    }

                    // Carboidratos Card
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column {
                            Text(text = "Carbos", fontSize = 12.sp, color = SecondaryTextGray, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "${todayCarbs.toInt()}g / ${carbsTarget}g", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MustardYellow)
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { (todayCarbs.toFloat() / carbsTarget.toFloat()).coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = MustardYellow,
                                trackColor = MustardYellow.copy(alpha = 0.2f)
                            )
                        }
                    }

                    // Gorduras Card
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column {
                            Text(text = "Gorduras", fontSize = 12.sp, color = SecondaryTextGray, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "${todayFat.toInt()}g / ${fatTarget}g", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OrangeAccent)
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { (todayFat.toFloat() / fatTarget.toFloat()).coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = OrangeAccent,
                                trackColor = OrangeAccent.copy(alpha = 0.2f)
                            )
                        }
                    }
                }
            }
        }

        // Calorie Trend Line Chart
        item {
            InteractiveLineChart(
                points = calorieTrendPoints,
                selectedTimeframe = timeframe,
                onTimeframeSelected = { viewModel.selectedTimeframe.value = it },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Macro Distribution Donut Chart
        item {
            DonutCategoryChart(
                slices = macroSlices,
                totalText = "$todayCalories kcal",
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Today's Meals Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Refeições Registradas Hoje",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGrayText
                )

                Row(
                    modifier = Modifier.clickable { onNavigateToTransactions() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ver Diário",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Ver",
                        tint = DarkGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Meals List
        items(meals) { meal ->
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Image or Food Icon
                    if (meal.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = meal.imageUrl,
                            contentDescription = meal.foodName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .background(DarkGreen.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestaurantMenu,
                                contentDescription = meal.mealType,
                                tint = DarkGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = meal.mealType,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkGreen
                            )
                            Text(
                                text = "${meal.calories} kcal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OrangeAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = meal.foodName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrayText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "P: ${meal.proteinGrams.toInt()}g • C: ${meal.carbsGrams.toInt()}g • G: ${meal.fatGrams.toInt()}g",
                            fontSize = 11.sp,
                            color = SecondaryTextGray
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

