package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import com.example.data.UserProfileEntity
import com.example.ui.theme.DarkGrayText
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.GlassBorderLight
import com.example.ui.theme.LightGrayBG
import com.example.ui.theme.MustardYellow
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SecondaryTextGray

/**
 * Add Meal Dialog
 */
@Composable
fun AddMealDialog(
    onDismiss: () -> Unit,
    onSave: (
        mealType: String,
        foodName: String,
        calories: Int,
        protein: Double,
        carbs: Double,
        fat: Double,
        fiber: Double,
        nutrientsText: String,
        imageUrl: String,
        notes: String
    ) -> Unit
) {
    var mealType by remember { mutableStateOf("Almoço") }
    var foodName by remember { mutableStateOf("") }
    var caloriesText by remember { mutableStateOf("") }
    var proteinText by remember { mutableStateOf("") }
    var carbsText by remember { mutableStateOf("") }
    var fatText by remember { mutableStateOf("") }
    var fiberText by remember { mutableStateOf("") }
    var nutrientsText by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val mealTypes = listOf("Café da Manhã", "Almoço", "Jantar", "Lanche", "Ceia")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = PureWhite,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorderLight, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Registrar Refeição",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar", tint = SecondaryTextGray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Meal Type Selector Chips
                Text(text = "Tipo de Refeição", fontSize = 12.sp, color = SecondaryTextGray, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    mealTypes.take(3).forEach { mt ->
                        val isSel = mealType == mt
                        Box(
                            modifier = Modifier
                                .background(if (isSel) DarkGreen else LightGrayBG, RoundedCornerShape(8.dp))
                                .clickable { mealType = mt }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = mt, fontSize = 12.sp, color = if (isSel) PureWhite else DarkGrayText)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    mealTypes.drop(3).forEach { mt ->
                        val isSel = mealType == mt
                        Box(
                            modifier = Modifier
                                .background(if (isSel) DarkGreen else LightGrayBG, RoundedCornerShape(8.dp))
                                .clickable { mealType = mt }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = mt, fontSize = 12.sp, color = if (isSel) PureWhite else DarkGrayText)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Food Name Input
                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = { Text("Nome do Alimento / Prato") },
                    placeholder = { Text("Ex: Omelete de espinafre com aveia") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkGreen,
                        unfocusedBorderColor = GlassBorderLight
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Calories and Protein Inputs
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = caloriesText,
                        onValueChange = { caloriesText = it },
                        label = { Text("Calorias (kcal)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )

                    OutlinedTextField(
                        value = proteinText,
                        onValueChange = { proteinText = it },
                        label = { Text("Proteínas (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Carbs, Fat, Fiber Inputs
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = carbsText,
                        onValueChange = { carbsText = it },
                        label = { Text("Carbos (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )

                    OutlinedTextField(
                        value = fatText,
                        onValueChange = { fatText = it },
                        label = { Text("Gorduras (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )

                    OutlinedTextField(
                        value = fiberText,
                        onValueChange = { fiberText = it },
                        label = { Text("Fibras (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Nutrients / Vitamins Input
                OutlinedTextField(
                    value = nutrientsText,
                    onValueChange = { nutrientsText = it },
                    label = { Text("Vitaminas & Nutrientes") },
                    placeholder = { Text("Ex: Vitamina C, Ferro, Magnésio, Ômega 3") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Image URL Input
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("Link da Imagem (URL do HTML)") },
                    placeholder = { Text("https://...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Notes / Observations Input
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Observação / Modo de Preparo") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val caloriesVal = caloriesText.toIntOrNull() ?: 0
                        val proteinVal = proteinText.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val carbsVal = carbsText.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val fatVal = fatText.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val fiberVal = fiberText.replace(",", ".").toDoubleOrNull() ?: 0.0

                        if (foodName.isNotBlank() && caloriesVal > 0) {
                            onSave(
                                mealType,
                                foodName,
                                caloriesVal,
                                proteinVal,
                                carbsVal,
                                fatVal,
                                fiberVal,
                                nutrientsText,
                                imageUrl,
                                notes
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
                ) {
                    Text(text = "Salvar Refeição", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PureWhite)
                }
            }
        }
    }
}

/**
 * Anamnesis / User Profile Dialog
 */
@Composable
fun AnamnesisProfileDialog(
    currentProfile: UserProfileEntity,
    onDismiss: () -> Unit,
    onSave: (UserProfileEntity) -> Unit
) {
    var name by remember { mutableStateOf(currentProfile.name) }
    var ageText by remember { mutableStateOf(currentProfile.age.toString()) }
    var gender by remember { mutableStateOf(currentProfile.gender) }
    var weightText by remember { mutableStateOf(currentProfile.weightKg.toString()) }
    var heightText by remember { mutableStateOf(currentProfile.heightCm.toString()) }
    var goal by remember { mutableStateOf(currentProfile.goal) }
    var healthConditions by remember { mutableStateOf(currentProfile.healthConditions) }
    var intolerances by remember { mutableStateOf(currentProfile.intolerances) }
    var allergies by remember { mutableStateOf(currentProfile.allergies) }
    var calorieTargetText by remember { mutableStateOf(currentProfile.dailyCalorieTarget.toString()) }
    var waterTargetText by remember { mutableStateOf(currentProfile.dailyWaterTargetMl.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = PureWhite,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, GlassBorderLight, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Anamnese & Perfil",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar", tint = SecondaryTextGray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { weightText = it },
                        label = { Text("Peso (kg)") },
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

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = goal,
                    onValueChange = { goal = it },
                    label = { Text("Objetivo Nutricional") },
                    placeholder = { Text("Ex: Perda de Peso & Desinflamação") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = healthConditions,
                    onValueChange = { healthConditions = it },
                    label = { Text("Problemas de Saúde / Condições") },
                    placeholder = { Text("Ex: Hipertensão, Gastrite, Diabetes") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = intolerances,
                    onValueChange = { intolerances = it },
                    label = { Text("Intolerâncias Alimentares") },
                    placeholder = { Text("Ex: Lactose, Glúten") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = allergies,
                    onValueChange = { allergies = it },
                    label = { Text("Alergias Alimentares") },
                    placeholder = { Text("Ex: Amendoim, Frutos do mar, Ovo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = calorieTargetText,
                        onValueChange = { calorieTargetText = it },
                        label = { Text("Meta Calorias (kcal)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )

                    OutlinedTextField(
                        value = waterTargetText,
                        onValueChange = { waterTargetText = it },
                        label = { Text("Meta Água (ml)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = DarkGreen)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val ageVal = ageText.toIntOrNull() ?: currentProfile.age
                        val weightVal = weightText.replace(",", ".").toDoubleOrNull() ?: currentProfile.weightKg
                        val heightVal = heightText.replace(",", ".").toDoubleOrNull() ?: currentProfile.heightCm
                        val calVal = calorieTargetText.toIntOrNull() ?: currentProfile.dailyCalorieTarget
                        val waterVal = waterTargetText.toIntOrNull() ?: currentProfile.dailyWaterTargetMl

                        onSave(
                            currentProfile.copy(
                                name = name.ifBlank { currentProfile.name },
                                age = ageVal,
                                gender = gender.ifBlank { currentProfile.gender },
                                weightKg = weightVal,
                                heightCm = heightVal,
                                goal = goal.ifBlank { currentProfile.goal },
                                healthConditions = healthConditions,
                                intolerances = intolerances,
                                allergies = allergies,
                                dailyCalorieTarget = calVal,
                                dailyWaterTargetMl = waterVal
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MustardYellow)
                ) {
                    Text(text = "Salvar Anamnese", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                }
            }
        }
    }
}

