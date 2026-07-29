package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.UserProfileEntity
import com.example.ui.MainViewModel
import com.example.ui.components.DarkGlassCard
import com.example.ui.components.GlassCard
import com.example.ui.theme.DarkGrayText
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.GlassBorderLight
import com.example.ui.theme.LightGrayBG
import com.example.ui.theme.MustardYellow
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SecondaryTextGray

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InitialProfileFormScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onSavedSuccessfully: () -> Unit = {}
) {
    val existingProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    var name by remember(existingProfile) { mutableStateOf(existingProfile?.name ?: "Ana Paula Santos") }
    var ageText by remember(existingProfile) { mutableStateOf((existingProfile?.age ?: 32).toString()) }
    var gender by remember(existingProfile) { mutableStateOf(existingProfile?.gender ?: "Feminino") }
    var weightText by remember(existingProfile) { mutableStateOf((existingProfile?.weightKg ?: 68.5).toString()) }
    var heightText by remember(existingProfile) { mutableStateOf((existingProfile?.heightCm ?: 165.0).toString()) }
    var goal by remember(existingProfile) { mutableStateOf(existingProfile?.goal ?: "Perda de Peso e Desinflamação") }
    var healthConditions by remember(existingProfile) { mutableStateOf(existingProfile?.healthConditions ?: "Gastrite, Retenção de Líquidos") }
    var intolerances by remember(existingProfile) { mutableStateOf(existingProfile?.intolerances ?: "Lactose") }
    var allergies by remember(existingProfile) { mutableStateOf(existingProfile?.allergies ?: "Frutos do mar") }
    var calorieTargetText by remember(existingProfile) { mutableStateOf((existingProfile?.dailyCalorieTarget ?: 1800).toString()) }
    var waterTargetText by remember(existingProfile) { mutableStateOf((existingProfile?.dailyWaterTargetMl ?: 2500).toString()) }

    var saveSuccessMessage by remember { mutableStateOf(false) }

    // Predefined goal options
    val predefinedGoals = listOf(
        "Perda de Peso e Desinflamação",
        "Ganho de Massa Magra",
        "Reeducação Alimentar",
        "Melhora da Digestão & Flora Intestinal",
        "Saúde Geral & Longevidade"
    )

    val weightVal = weightText.replace(",", ".").toDoubleOrNull() ?: 0.0
    val heightVal = heightText.replace(",", ".").toDoubleOrNull() ?: 0.0
    val heightM = heightVal / 100.0
    val calculatedBmi = if (heightM > 0 && weightVal > 0) weightVal / (heightM * heightM) else 0.0
    val bmiClassification = when {
        calculatedBmi <= 0.0 -> "-"
        calculatedBmi < 18.5 -> "Abaixo do peso"
        calculatedBmi < 24.9 -> "Peso Saudável"
        calculatedBmi < 29.9 -> "Sobrepeso"
        else -> "Obesidade"
    }

    val recommendedWater = if (weightVal > 0) (weightVal * 35).toInt() else 2500

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(LightGrayBG)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Form Title
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(DarkGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AssignmentInd,
                            contentDescription = "Anamnese",
                            tint = MustardYellow,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Formulário de Anamnese & Perfil",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrayText
                        )
                        Text(
                            text = "Coleta de dados corporais, saúde e objetivos nutricionais",
                            fontSize = 13.sp,
                            color = SecondaryTextGray
                        )
                    }
                }
            }
        }

        // Hero Card - Preview of calculated BMI
        item {
            DarkGlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "Indicadores em Tempo Real",
                        color = MustardYellow,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "IMC Estimado", color = PureWhite.copy(alpha = 0.7f), fontSize = 12.sp)
                            Text(
                                text = if (calculatedBmi > 0) String.format("%.1f kg/m²", calculatedBmi) else "--",
                                color = PureWhite,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(MustardYellow, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = bmiClassification,
                                color = DarkGreen,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Água Sugerida", color = PureWhite.copy(alpha = 0.7f), fontSize = 12.sp)
                            Text(
                                text = "$recommendedWater ml/dia",
                                color = Color(0xFF64B5F6),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Section 1: Personal Physical Data
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Dados", tint = DarkGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "1. Dados Pessoais & Físicos",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrayText
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome Completo") },
                        placeholder = { Text("Ex: Maria Silva") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = ageText,
                            onValueChange = { ageText = it },
                            label = { Text("Idade (anos)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                        )

                        OutlinedTextField(
                            value = gender,
                            onValueChange = { gender = it },
                            label = { Text("Sexo") },
                            placeholder = { Text("Feminino/Masculino") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = weightText,
                            onValueChange = { weightText = it },
                            label = { Text("Peso Atual (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                        )

                        OutlinedTextField(
                            value = heightText,
                            onValueChange = { heightText = it },
                            label = { Text("Altura (cm)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                        )
                    }
                }
            }
        }

        // Section 2: Goals
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.FitnessCenter, contentDescription = "Objetivo", tint = DarkGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "2. Objetivo Nutricional Principal",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrayText
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Selecione uma opção ou digite seu objetivo customizado:",
                        fontSize = 12.sp,
                        color = SecondaryTextGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        predefinedGoals.forEach { option ->
                            val isSelected = goal.equals(option, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isSelected) DarkGreen else LightGrayBG,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) DarkGreen else GlassBorderLight,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { goal = option }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = option,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) PureWhite else DarkGrayText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = goal,
                        onValueChange = { goal = it },
                        label = { Text("Objetivo Personalizado") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )
                }
            }
        }

        // Section 3: Health & Restricted Foods
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Healing, contentDescription = "Saúde", tint = DarkGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "3. Saúde, Intolerâncias & Alergias",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrayText
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = healthConditions,
                        onValueChange = { healthConditions = it },
                        label = { Text("Problemas de Saúde / Queixas Principais") },
                        placeholder = { Text("Ex: Retenção de líquidos, Gastrite, Hipertensão") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = intolerances,
                        onValueChange = { intolerances = it },
                        label = { Text("Intolerâncias Alimentares") },
                        placeholder = { Text("Ex: Lactose, Glúten, Frutose") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = allergies,
                        onValueChange = { allergies = it },
                        label = { Text("Alergias Confirmadas") },
                        placeholder = { Text("Ex: Frutos do mar, Amendoim, Ovo") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )
                }
            }
        }

        // Section 4: Daily Nutritional Goals
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Restaurant, contentDescription = "Metas", tint = DarkGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "4. Metas Diárias de Ingestão",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrayText
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = calorieTargetText,
                            onValueChange = { calorieTargetText = it },
                            label = { Text("Calorias (kcal)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                        )

                        OutlinedTextField(
                            value = waterTargetText,
                            onValueChange = { waterTargetText = it },
                            label = { Text("Água (ml)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                        )
                    }
                }
            }
        }

        // Save Success Message Banner
        if (saveSuccessMessage) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkGreen)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Sucesso", tint = MustardYellow)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Dados do usuário e anamnese salvos com sucesso no banco de dados!",
                            color = PureWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Save Profile Button
        item {
            Button(
                onClick = {
                    val idVal = existingProfile?.id ?: 1
                    val ageVal = ageText.toIntOrNull() ?: 30
                    val wVal = weightText.replace(",", ".").toDoubleOrNull() ?: 70.0
                    val hVal = heightText.replace(",", ".").toDoubleOrNull() ?: 165.0
                    val calVal = calorieTargetText.toIntOrNull() ?: 1800
                    val waterVal = waterTargetText.toIntOrNull() ?: recommendedWater

                    val newProfile = UserProfileEntity(
                        id = idVal,
                        name = name.ifBlank { "Usuário Saudável" },
                        age = ageVal,
                        gender = gender.ifBlank { "Feminino" },
                        weightKg = wVal,
                        heightCm = hVal,
                        goal = goal.ifBlank { "Saúde e Reeducação Alimentar" },
                        healthConditions = healthConditions,
                        intolerances = intolerances,
                        allergies = allergies,
                        dailyCalorieTarget = calVal,
                        dailyWaterTargetMl = waterVal
                    )

                    viewModel.updateProfile(newProfile)
                    saveSuccessMessage = true
                    onSavedSuccessfully()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
            ) {
                Text(
                    text = "Salvar Perfil e Atualizar Banco de Dados",
                    color = MustardYellow,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}
