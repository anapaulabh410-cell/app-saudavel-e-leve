package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mealType: String, // "Café da Manhã", "Almoço", "Jantar", "Lanche", "Ceia"
    val foodName: String,
    val calories: Int,
    val proteinGrams: Double,
    val carbsGrams: Double,
    val fatGrams: Double,
    val fiberGrams: Double = 0.0,
    val nutrientsText: String = "",
    val imageUrl: String = "",
    val dateTimestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)

@Entity(tableName = "water_logs")
data class WaterLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amountMl: Int,
    val dateTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val name: String = "Ana Paula",
    val age: Int = 32,
    val gender: String = "Feminino",
    val weightKg: Double = 68.5,
    val heightCm: Double = 165.0,
    val goal: String = "Perda de Peso & Desinflamação",
    val healthConditions: String = "Hipertensão leve, Gastrite ocasional",
    val intolerances: String = "Lactose",
    val allergies: String = "Nenhuma",
    val dailyCalorieTarget: Int = 1800,
    val dailyProteinTarget: Int = 110,
    val dailyCarbsTarget: Int = 180,
    val dailyFatTarget: Int = 50,
    val dailyWaterTargetMl: Int = 2500
)

@Entity(tableName = "detox_tips")
data class DetoxTipEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val category: String, // "Chá Desinflamante", "Suco Detox", "Protocolo Reorganização", "Prato Funcional"
    val description: String,
    val benefits: String,
    val ingredients: String,
    val instructions: String,
    val imageUrl: String = ""
)

@Entity(tableName = "user_accounts")
data class UserAccountEntity(
    @PrimaryKey
    val email: String,
    val passwordHash: String,
    val fullName: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isLoggedIn: Boolean = true
)

