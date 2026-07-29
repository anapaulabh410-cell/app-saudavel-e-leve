package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.data.DetoxTipEntity
import com.example.data.MealEntity
import com.example.data.UserProfileEntity
import com.example.data.WaterLogEntity
import com.example.ui.components.CategorySlice
import com.example.ui.components.ChartPoint
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.LightGreenAccent
import com.example.ui.theme.MustardYellow
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.PurpleAccent
import com.example.data.UserAccountEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    val searchQuery = MutableStateFlow("")
    val selectedMealTypeFilter = MutableStateFlow("TODOS")
    val selectedTimeframe = MutableStateFlow("Semanal")

    // Auth state messages
    val authErrorMessage = MutableStateFlow<String?>(null)
    val authSuccessMessage = MutableStateFlow<String?>(null)

    // Modal dialog controls
    val showAddMealDialog = MutableStateFlow(false)
    val showAnamnesisDialog = MutableStateFlow(false)
    val showFoodScannerDialog = MutableStateFlow(false)

    init {
        val db = AppDatabase.getDatabase(application)
        repository = AppRepository(
            db.mealDao(),
            db.waterDao(),
            db.userProfileDao(),
            db.detoxDao(),
            db.userAccountDao()
        )

        viewModelScope.launch {
            val existingMeals = repository.allMeals.first()
            if (existingMeals.isEmpty()) {
                repository.seedInitialDataIfEmpty()
            }
        }
    }

    val loggedInUser: StateFlow<UserAccountEntity?> = repository.loggedInAccount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val rawMeals: StateFlow<List<MealEntity>> = repository.allMeals
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val waterLogs: StateFlow<List<WaterLogEntity>> = repository.allWaterLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val detoxTips: StateFlow<List<DetoxTipEntity>> = repository.allDetoxTips
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Filtered Meals
    val filteredMeals: StateFlow<List<MealEntity>> = combine(
        rawMeals,
        searchQuery,
        selectedMealTypeFilter
    ) { meals, query, mealType ->
        meals.filter { m ->
            val matchesQuery = m.foodName.contains(query, ignoreCase = true) ||
                    m.mealType.contains(query, ignoreCase = true) ||
                    m.nutrientsText.contains(query, ignoreCase = true) ||
                    m.notes.contains(query, ignoreCase = true)

            val matchesType = mealType == "TODOS" || m.mealType.equals(mealType, ignoreCase = true)

            matchesQuery && matchesType
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Today's Macro Aggregation
    val todayCalories: StateFlow<Int> = rawMeals.combine(rawMeals) { meals, _ ->
        meals.sumOf { it.calories }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1070)

    val todayProtein: StateFlow<Double> = rawMeals.combine(rawMeals) { meals, _ ->
        meals.sumOf { it.proteinGrams }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 82.0)

    val todayCarbs: StateFlow<Double> = rawMeals.combine(rawMeals) { meals, _ ->
        meals.sumOf { it.carbsGrams }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 82.0)

    val todayFat: StateFlow<Double> = rawMeals.combine(rawMeals) { meals, _ ->
        meals.sumOf { it.fatGrams }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 36.5)

    val todayFiber: StateFlow<Double> = rawMeals.combine(rawMeals) { meals, _ ->
        meals.sumOf { it.fiberGrams }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 14.7)

    // Water Hydration
    val todayWaterMl: StateFlow<Int> = waterLogs.combine(waterLogs) { logs, _ ->
        logs.sumOf { it.amountMl }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1250)

    // Calorie & Nutrients Chart Trend Data
    val calorieTrendPoints: StateFlow<List<ChartPoint>> = selectedTimeframe.combine(rawMeals) { tf, _ ->
        when (tf) {
            "Mensal" -> listOf(
                ChartPoint("Sem 1", 1750f, "1.750 kcal"),
                ChartPoint("Sem 2", 1820f, "1.820 kcal"),
                ChartPoint("Sem 3", 1690f, "1.690 kcal"),
                ChartPoint("Sem 4", 1780f, "1.780 kcal")
            )
            else -> listOf(
                ChartPoint("Seg", 1650f, "1.650 kcal"),
                ChartPoint("Ter", 1800f, "1.800 kcal"),
                ChartPoint("Qua", 1720f, "1.720 kcal"),
                ChartPoint("Qui", 1910f, "1.910 kcal"),
                ChartPoint("Sex", 1600f, "1.600 kcal"),
                ChartPoint("Sáb", 2100f, "2.100 kcal"),
                ChartPoint("Dom", 1070f, "1.070 kcal")
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Macro Category Breakdown Slices for Donut Chart
    val macroSlices: StateFlow<List<CategorySlice>> = combine(
        todayProtein,
        todayCarbs,
        todayFat
    ) { protein, carbs, fat ->
        listOf(
            CategorySlice("Proteínas (${protein.toInt()}g)", protein * 4.0, DarkGreen),
            CategorySlice("Carboidratos (${carbs.toInt()}g)", carbs * 4.0, MustardYellow),
            CategorySlice("Gorduras Boas (${fat.toInt()}g)", fat * 9.0, OrangeAccent)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // User Operations
    fun addMeal(
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
    ) {
        viewModelScope.launch {
            repository.insertMeal(
                MealEntity(
                    mealType = mealType,
                    foodName = foodName,
                    calories = calories,
                    proteinGrams = protein,
                    carbsGrams = carbs,
                    fatGrams = fat,
                    fiberGrams = fiber,
                    nutrientsText = nutrientsText,
                    imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1498837167922-ddd27525d352?w=500&auto=format&fit=crop" },
                    notes = notes
                )
            )
        }
    }

    fun deleteMeal(meal: MealEntity) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
        }
    }

    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            repository.addWaterLog(amountMl)
        }
    }

    fun resetWater() {
        viewModelScope.launch {
            repository.resetWaterLogs()
        }
    }

    fun updateProfile(profile: UserProfileEntity) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
        }
    }

    // Authentication Actions connected to Room Database
    fun login(email: String, pass: String, onSuccess: () -> Unit = {}) {
        authErrorMessage.value = null
        authSuccessMessage.value = null

        if (email.isBlank() || pass.isBlank()) {
            authErrorMessage.value = "Por favor, preencha todos os campos."
            return
        }

        viewModelScope.launch {
            val success = repository.login(email, pass)
            if (success) {
                authSuccessMessage.value = "Login realizado com sucesso!"
                onSuccess()
            } else {
                val account = repository.getAccountByEmail(email)
                if (account == null) {
                    authErrorMessage.value = "E-mail não cadastrado no banco de dados. Crie uma conta."
                } else {
                    authErrorMessage.value = "Senha incorreta. Verifique suas credenciais."
                }
            }
        }
    }

    fun register(fullName: String, email: String, pass: String, passConfirm: String, onSuccess: () -> Unit = {}) {
        authErrorMessage.value = null
        authSuccessMessage.value = null

        if (fullName.isBlank() || email.isBlank() || pass.isBlank()) {
            authErrorMessage.value = "Por favor, preencha todos os campos obrigatórios."
            return
        }

        if (!email.contains("@") || !email.contains(".")) {
            authErrorMessage.value = "Informe um endereço de e-mail válido."
            return
        }

        if (pass.length < 4) {
            authErrorMessage.value = "A senha deve ter pelo menos 4 caracteres."
            return
        }

        if (pass != passConfirm) {
            authErrorMessage.value = "As senhas não coincidem."
            return
        }

        viewModelScope.launch {
            val success = repository.registerUserAccount(email, pass, fullName)
            if (success) {
                authSuccessMessage.value = "Conta criada e armazenada com sucesso no Room DB!"
                onSuccess()
            } else {
                authErrorMessage.value = "Este e-mail já está cadastrado no sistema."
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            authErrorMessage.value = null
            authSuccessMessage.value = null
        }
    }

    fun clearAuthMessages() {
        authErrorMessage.value = null
        authSuccessMessage.value = null
    }
}

