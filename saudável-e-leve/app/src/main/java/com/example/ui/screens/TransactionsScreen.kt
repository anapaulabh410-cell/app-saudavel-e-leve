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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ui.MainViewModel
import com.example.ui.components.GlassCard
import com.example.ui.theme.DarkGrayText
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.GlassBorderLight
import com.example.ui.theme.LightGrayBG
import com.example.ui.theme.MustardYellow
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SecondaryTextGray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val meals by viewModel.filteredMeals.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedMealType by viewModel.selectedMealTypeFilter.collectAsStateWithLifecycle()

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
    val mealTypeOptions = listOf("TODOS", "CAFÉ DA MANHÃ", "ALMOÇO", "JANTAR", "LANCHE", "DETOX")

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGrayBG)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Header Title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Diário Alimentar",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrayText
                        )
                        Text(
                            text = "${meals.size} refeições cadastradas",
                            fontSize = 14.sp,
                            color = SecondaryTextGray
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = { viewModel.showFoodScannerDialog.value = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Scanner IA", tint = MustardYellow, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Escanear IA 📸", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { viewModel.showAddMealDialog.value = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LightGrayBG)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar", tint = DarkGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "+", color = DarkGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = { Text("Buscar refeição por alimento, tipo ou notas...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar", tint = SecondaryTextGray)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = PureWhite,
                        unfocusedContainerColor = PureWhite,
                        focusedBorderColor = DarkGreen,
                        unfocusedBorderColor = GlassBorderLight
                    )
                )
            }

            // Meal Type Filter Chips
            item {
                Column {
                    Text(text = "Tipo de Refeição", fontSize = 12.sp, color = SecondaryTextGray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(mealTypeOptions) { type ->
                            val isSel = type == selectedMealType
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isSel) DarkGreen else PureWhite,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { viewModel.selectedMealTypeFilter.value = type }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = type,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) PureWhite else DarkGrayText
                                )
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Meals List
            if (meals.isEmpty()) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Nenhuma refeição encontrada",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkGrayText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tente alterar os filtros ou adicione uma nova refeição.",
                                fontSize = 12.sp,
                                color = SecondaryTextGray
                            )
                        }
                    }
                }
            } else {
                items(meals, key = { it.id }) { meal ->
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Food Thumbnail Image or Icon
                            if (meal.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = meal.imageUrl,
                                    contentDescription = meal.foodName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(DarkGreen.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RestaurantMenu,
                                        contentDescription = meal.mealType,
                                        tint = DarkGreen,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = meal.mealType,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkGreen
                                    )
                                    Text(
                                        text = "${meal.calories} kcal",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OrangeAccent
                                    )
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = meal.foodName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkGrayText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "Prot: ${meal.proteinGrams.toInt()}g • Carb: ${meal.carbsGrams.toInt()}g • Gord: ${meal.fatGrams.toInt()}g • Fibra: ${meal.fiberGrams.toInt()}g",
                                    fontSize = 11.sp,
                                    color = SecondaryTextGray
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = dateFormat.format(Date(meal.dateTimestamp)),
                                        fontSize = 10.sp,
                                        color = SecondaryTextGray
                                    )

                                    IconButton(
                                        onClick = { viewModel.deleteMeal(meal) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Excluir",
                                            tint = SecondaryTextGray,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { viewModel.showAddMealDialog.value = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            containerColor = MustardYellow,
            contentColor = DarkGreen
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Nova Refeição", modifier = Modifier.size(28.dp))
        }
    }
}

