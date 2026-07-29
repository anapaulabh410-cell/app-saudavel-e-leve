package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.ComparisonBarChart
import com.example.ui.components.DarkGlassCard
import com.example.ui.components.DayCalorieData
import com.example.ui.components.DonutCategoryChart
import com.example.ui.components.GlassCard
import com.example.ui.components.WeeklyCalorieProgressChart
import com.example.ui.theme.DarkGrayText
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.LightGrayBG
import com.example.ui.theme.LightGreenAccent
import com.example.ui.theme.MustardYellow
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SecondaryTextGray

@Composable
fun AnalyticsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val detoxTips by viewModel.detoxTips.collectAsStateWithLifecycle()
    val macroSlices by viewModel.macroSlices.collectAsStateWithLifecycle()
    val todayCalories by viewModel.todayCalories.collectAsStateWithLifecycle()

    var selectedCategoryFilter by remember { mutableStateOf("TODOS") }
    val categories = listOf("TODOS", "SHOT MATINAL", "INFUSÃO", "REFEIÇÃO LEVE", "DESINFLAMAÇÃO")

    val filteredTips = if (selectedCategoryFilter == "TODOS") {
        detoxTips
    } else {
        detoxTips.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightGrayBG)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Header Title
        item {
            Column {
                Text(
                    text = "Detox & Desinflamação",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGrayText
                )
                Text(
                    text = "Protocolos para momentos pós-excesso e recuperação do organismo",
                    fontSize = 14.sp,
                    color = SecondaryTextGray
                )
            }
        }

        // Hero SOS Detox Card (Dark Glass)
        item {
            DarkGlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(LightGreenAccent.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = "Detox",
                                tint = LightGreenAccent,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Chutou o balde na alimentação?",
                                color = PureWhite,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Guia SOS de Recuperação em 24h",
                                color = MustardYellow,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "1. Beba 500ml de água morna com limão e Cúrcuma logo ao acordar.\n" +
                                "2. Evite açúcar, glúten e laticínios no dia de recuperação.\n" +
                                "3. Consuma chás diuréticos (Cavalinha, Hibisco ou Gengibre).\n" +
                                "4. Priorize caldos vegetais, folhas escuras e proteínas magras.",
                        color = PureWhite.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Weekly Calorie Progress Chart: Daily Intake vs Target Goal
        item {
            val userProfileState by viewModel.userProfile.collectAsStateWithLifecycle()
            val userTargetKcal = userProfileState?.dailyCalorieTarget ?: 1800
            val sampleWeeklyData = listOf(
                DayCalorieData("Seg", 1750, userTargetKcal),
                DayCalorieData("Ter", 1820, userTargetKcal),
                DayCalorieData("Qua", 2050, userTargetKcal),
                DayCalorieData("Qui", 1680, userTargetKcal),
                DayCalorieData("Sex", 1790, userTargetKcal),
                DayCalorieData("Sáb", 2150, userTargetKcal),
                DayCalorieData("Dom", todayCalories.coerceAtLeast(1450), userTargetKcal)
            )

            WeeklyCalorieProgressChart(
                weeklyData = sampleWeeklyData,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Category Filter Chips
        item {
            Column {
                Text(
                    text = "Categorias de Protocolo Detox",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGrayText
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        val isSel = cat == selectedCategoryFilter
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSel) DarkGreen else PureWhite,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedCategoryFilter = cat }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) PureWhite else DarkGrayText
                            )
                        }
                    }
                }
            }
        }

        // Detox Tips List Items
        items(filteredTips) { tip ->
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(DarkGreen.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (tip.category) {
                                "SHOT MATINAL" -> Icons.Default.WaterDrop
                                "INFUSÃO" -> Icons.Default.LocalDrink
                                "REFEIÇÃO LEVE" -> Icons.Default.Spa
                                else -> Icons.Default.CleaningServices
                            },
                            contentDescription = tip.category,
                            tint = DarkGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = tip.category,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkGreen
                            )
                            if (tip.benefits.isNotBlank()) {
                                Text(
                                    text = tip.benefits,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OrangeAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = tip.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrayText
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = tip.description,
                            fontSize = 12.sp,
                            color = SecondaryTextGray,
                            lineHeight = 16.sp
                        )

                        if (tip.ingredients.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(MustardYellow.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Ingredientes: ${tip.ingredients}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = DarkGrayText
                                )
                            }
                        }
                    }
                }
            }
        }

        // Macro Donut Distribution
        item {
            DonutCategoryChart(
                slices = macroSlices,
                totalText = "$todayCalories kcal",
                modifier = Modifier.fillMaxWidth()
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

