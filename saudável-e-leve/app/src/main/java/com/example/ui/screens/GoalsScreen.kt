package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.DarkGlassCard
import com.example.ui.components.GlassCard
import com.example.ui.theme.DarkGrayText
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.LightGrayBG
import com.example.ui.theme.MustardYellow
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SecondaryTextGray

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    var isEditingForm by remember { mutableStateOf(false) }

    if (isEditingForm) {
        Column(modifier = modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Editar Formulário",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGrayText
                )
                Button(
                    onClick = { isEditingForm = false },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Ver Resumo", color = PureWhite, fontSize = 12.sp)
                }
            }

            InitialProfileFormScreen(
                viewModel = viewModel,
                modifier = Modifier.weight(1f),
                onSavedSuccessfully = { isEditingForm = false }
            )
        }
        return
    }

    val profile = userProfile ?: com.example.data.UserProfileEntity(
        id = 1,
        name = "Ana Paula Santos",
        age = 32,
        gender = "Feminino",
        weightKg = 68.5,
        heightCm = 165.0,
        goal = "Perda de Peso e Desinflamação",
        healthConditions = "Gastrite, Retenção de Líquidos",
        allergies = "Frutos do mar",
        intolerances = "Lactose",
        dailyCalorieTarget = 1800,
        dailyProteinTarget = 110,
        dailyCarbsTarget = 180,
        dailyFatTarget = 50,
        dailyWaterTargetMl = 2500
    )

    // Calculate BMI
    val heightM = profile.heightCm / 100.0
    val bmi = if (heightM > 0) profile.weightKg / (heightM * heightM) else 0.0
    val bmiClassification = when {
        bmi < 18.5 -> "Abaixo do peso"
        bmi < 24.9 -> "Peso Saudável"
        bmi < 29.9 -> "Sobrepeso"
        else -> "Obesidade"
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Perfil & Anamnese",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGrayText
                    )
                    Text(
                        text = "Saúde, restrições e metas nutricionais personalizadas",
                        fontSize = 14.sp,
                        color = SecondaryTextGray
                    )
                }

                Button(
                    onClick = { isEditingForm = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = MustardYellow)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Editar Formulário", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Hero Profile Card (Dark Glass)
        item {
            DarkGlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(DarkGreen, CircleShape)
                                .padding(2.dp)
                                .background(MustardYellow, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil",
                                tint = DarkGreen,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = profile.name,
                                color = PureWhite,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${profile.age} anos • ${profile.gender} • ${profile.heightCm.toInt()} cm",
                                color = PureWhite.copy(alpha = 0.8f),
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .background(MustardYellow, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Objetivo: ${profile.goal}",
                                    color = DarkGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Physical metrics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Peso Atual", color = PureWhite.copy(alpha = 0.7f), fontSize = 11.sp)
                            Text(text = "${profile.weightKg} kg", color = PureWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "IMC Calculado", color = PureWhite.copy(alpha = 0.7f), fontSize = 11.sp)
                            Text(text = String.format("%.1f", bmi), color = MustardYellow, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Classificação", color = PureWhite.copy(alpha = 0.7f), fontSize = 11.sp)
                            Text(text = bmiClassification, color = PureWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Health Anamnesis Details Card
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Healing, contentDescription = "Saúde", tint = DarkGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Anamnese & Condições de Saúde",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrayText
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Problemas de Saúde / Queixas:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        profile.healthConditions.split(",").map { it.trim() }.filter { it.isNotBlank() }.forEach { issue ->
                            Box(
                                modifier = Modifier
                                    .background(OrangeAccent.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(text = issue, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OrangeAccent)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Intolerâncias Alimentares:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        profile.intolerances.split(",").map { it.trim() }.filter { it.isNotBlank() }.forEach { intol ->
                            Box(
                                modifier = Modifier
                                    .background(MustardYellow.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(text = intol, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Alergias Confirmadas:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkGrayText)
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        profile.allergies.split(",").map { it.trim() }.filter { it.isNotBlank() }.forEach { alg ->
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFE53935).copy(alpha = 0.12f), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(text = alg, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE53935))
                            }
                        }
                    }
                }
            }
        }

        // Daily Nutritional Targets Summary Card
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Restaurant, contentDescription = "Metas", tint = DarkGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Metas Nutricionais Diárias",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrayText
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Meta Calórica", fontSize = 12.sp, color = SecondaryTextGray)
                            Text(text = "${profile.dailyCalorieTarget} kcal", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                        }
                        Column {
                            Text(text = "Meta de Proteínas", fontSize = 12.sp, color = SecondaryTextGray)
                            Text(text = "${profile.dailyProteinTarget} g", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                        }
                        Column {
                            Text(text = "Meta de Carboidratos", fontSize = 12.sp, color = SecondaryTextGray)
                            Text(text = "${profile.dailyCarbsTarget} g", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MustardYellow)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Meta de Gorduras", fontSize = 12.sp, color = SecondaryTextGray)
                            Text(text = "${profile.dailyFatTarget} g", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OrangeAccent)
                        }
                        Column {
                            Text(text = "Meta de Água", fontSize = 12.sp, color = SecondaryTextGray)
                            Text(text = "${profile.dailyWaterTargetMl} ml", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2196F3))
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

