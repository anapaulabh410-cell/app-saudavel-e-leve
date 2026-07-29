package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val mealDao: MealDao,
    private val waterDao: WaterDao,
    private val userProfileDao: UserProfileDao,
    private val detoxDao: DetoxDao,
    private val userAccountDao: UserAccountDao
) {
    val allMeals: Flow<List<MealEntity>> = mealDao.getAllMeals()
    val allWaterLogs: Flow<List<WaterLogEntity>> = waterDao.getAllWaterLogs()
    val userProfile: Flow<UserProfileEntity?> = userProfileDao.getProfile()
    val allDetoxTips: Flow<List<DetoxTipEntity>> = detoxDao.getAllDetoxTips()
    val loggedInAccount: Flow<UserAccountEntity?> = userAccountDao.getLoggedInAccount()

    suspend fun getAccountByEmail(email: String): UserAccountEntity? {
        return userAccountDao.getAccountByEmail(email)
    }

    suspend fun registerUserAccount(email: String, password: String, fullName: String): Boolean {
        val existing = userAccountDao.getAccountByEmail(email)
        if (existing != null) return false // Already registered

        // Logout existing sessions first
        userAccountDao.logoutAllAccounts()

        val newAccount = UserAccountEntity(
            email = email.trim(),
            passwordHash = password, // Simple secure store for app database
            fullName = fullName.trim(),
            isLoggedIn = true
        )
        userAccountDao.insertOrUpdateAccount(newAccount)

        // Sync name with UserProfile
        userProfileDao.insertOrUpdateProfile(
            UserProfileEntity(
                id = 1,
                name = fullName.trim()
            )
        )
        return true
    }

    suspend fun login(email: String, password: String): Boolean {
        val account = userAccountDao.getAccountByEmail(email.trim()) ?: return false
        if (account.passwordHash == password) {
            userAccountDao.logoutAllAccounts()
            userAccountDao.insertOrUpdateAccount(account.copy(isLoggedIn = true))
            return true
        }
        return false
    }

    suspend fun logout() {
        userAccountDao.logoutAllAccounts()
    }

    suspend fun insertMeal(meal: MealEntity) {
        mealDao.insertMeal(meal)
    }

    suspend fun deleteMeal(meal: MealEntity) {
        mealDao.deleteMeal(meal)
    }

    suspend fun addWaterLog(amountMl: Int) {
        waterDao.insertWaterLog(WaterLogEntity(amountMl = amountMl))
    }

    suspend fun resetWaterLogs() {
        waterDao.clearWaterLogs()
    }

    suspend fun saveUserProfile(profile: UserProfileEntity) {
        userProfileDao.insertOrUpdateProfile(profile)
    }

    suspend fun seedInitialDataIfEmpty() {
        // Seed default user account if empty
        if (userAccountDao.getAccountCount() == 0) {
            userAccountDao.insertOrUpdateAccount(
                UserAccountEntity(
                    email = "anapaula@saudavel.com",
                    passwordHash = "123456",
                    fullName = "Ana Paula",
                    isLoggedIn = true
                )
            )
        }

        // Seed default User Profile
        userProfileDao.insertOrUpdateProfile(
            UserProfileEntity(
                id = 1,
                name = "Ana Paula",
                age = 32,
                gender = "Feminino",
                weightKg = 68.5,
                heightCm = 165.0,
                goal = "Perda de Peso & Desinflamação",
                healthConditions = "Gastrite leve, Hipertensão arterial leve",
                intolerances = "Lactose",
                allergies = "Amendoim",
                dailyCalorieTarget = 1800,
                dailyProteinTarget = 110,
                dailyCarbsTarget = 180,
                dailyFatTarget = 50,
                dailyWaterTargetMl = 2500
            )
        )

        // Seed Detox & Desinflamação Protocols
        val initialDetoxTips = listOf(
            DetoxTipEntity(
                title = "Chá Anti-inflamatório de Cúrcuma e Gengibre",
                category = "Chá Desinflamante",
                description = "Bebida termogênica e antioxidante ideal para tomar ao acordar ou pós-refeições pesadas.",
                benefits = "Reduz o inchaço abdominal, estimula a digestão e combate radicais livres.",
                ingredients = "1 colher de chá de cúrcuma em pó, 1 pedaço pequeno de gengibre ralado, pitada de pimenta preta, 300ml de água morna e suco de 1/2 limão.",
                instructions = "Ferva a água com o gengibre por 5 minutos. Desligue o fogo, adicione a cúrcuma e pimenta. Coe e finalize com o limão fresco.",
                imageUrl = "https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=500&auto=format&fit=crop"
            ),
            DetoxTipEntity(
                title = "Suco Verde Detox 'Pós-Chutar o Balde'",
                category = "Suco Detox",
                description = "Infusão concentrada de clorofila e fibras funcionais para acelerar a eliminação de toxinas.",
                benefits = "Alivia a retenção de líquidos, reativa o trânsito intestinal e alcaliniza o PH sanguíneo.",
                ingredients = "2 folhas de couve manteiga, 1 fatia de abacaxi, 1/2 pepino com casca, 200ml de água de coco e hortelã a gosto.",
                instructions = "Bata todos os ingredientes no liquidificador com pedra de gelo. Consuma preferencialmente sem coar para preservar as fibras.",
                imageUrl = "https://images.unsplash.com/photo-1610970881699-44a5587cabec?w=500&auto=format&fit=crop"
            ),
            DetoxTipEntity(
                title = "Protocolo de Hidratação Intensa 24 Horas",
                category = "Protocolo Reorganização",
                description = "Guia prático de reorganização nutricional para dias posteriores a exageros alimentares.",
                benefits = "Elimina o excesso de sódio retido, alivia a azia e restaura os níveis de energia celular.",
                ingredients = "3 Litros de água aromatizada com rodelas de limão siciliano, pepino e folhas de manjericão fresco.",
                instructions = "Beba 500ml ao acordar em jejum e fracione 300ml a cada 2 horas ao longo do dia. Evite ultraprocessados nas próximas 24h.",
                imageUrl = "https://images.unsplash.com/photo-1548839140-29a749e1cf4e?w=500&auto=format&fit=crop"
            ),
            DetoxTipEntity(
                title = "Salada Funcional Anti-inflamatória com Abacate",
                category = "Prato Funcional",
                description = "Refeição densa em micronutrientes, gorduras boas (Ômega 9) e fibras solúveis.",
                benefits = "Sensação prolongada de saciedade sem pesar no estômago, protegendo a mucosa gástrica.",
                ingredients = "Mix de folhas verdes (rúcula/espinafre), 1/2 abacate em cubos, tomates cereja, sementes de girassol e azeite extravirgem.",
                instructions = "Monte a base com as folhas lavadas, adicione o abacate e tomates. Tempere com azeite de oliva, gotas de limão e pitada de sal rosa.",
                imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=500&auto=format&fit=crop"
            )
        )

        initialDetoxTips.forEach { detoxDao.insertDetoxTip(it) }

        // Seed initial today's meals
        val initialMeals = listOf(
            MealEntity(
                mealType = "Café da Manhã",
                foodName = "Omelete de Espinafre com Queijo Minas + Café com Canela",
                calories = 320,
                proteinGrams = 24.0,
                carbsGrams = 8.0,
                fatGrams = 18.0,
                fiberGrams = 3.2,
                nutrientsText = "Vitamina A, Ferro, Cálcio, Colina",
                imageUrl = "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=500&auto=format&fit=crop",
                notes = "Preparo rápido sem óleo refinado, utilizando azeite extravirgem."
            ),
            MealEntity(
                mealType = "Almoço",
                foodName = "Filet de Frango Grelhado com Arroz Integral e Abóbora Assada",
                calories = 540,
                proteinGrams = 42.0,
                carbsGrams = 52.0,
                fatGrams = 12.0,
                fiberGrams = 6.5,
                nutrientsText = "Complexo B, Zinco, Betacaroteno, Magnesio",
                imageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&auto=format&fit=crop",
                notes = "Refeição bem equilibrada, rica em fibras e baixo teor de sódio."
            ),
            MealEntity(
                mealType = "Lanche",
                foodName = "Iogurte Natural Zero Lactose com Frutas Vermixas e Chia",
                calories = 210,
                proteinGrams = 16.0,
                carbsGrams = 22.0,
                fatGrams = 4.5,
                fiberGrams = 5.0,
                nutrientsText = "Probióticos, Antioxidantes, Ômega 3, Cálcio",
                imageUrl = "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=500&auto=format&fit=crop",
                notes = "Opção sem lactose ideal para a digestão e saúde intestinal."
            )
        )

        initialMeals.forEach { mealDao.insertMeal(it) }

        // Seed initial water logs
        waterDao.insertWaterLog(WaterLogEntity(amountMl = 500))
        waterDao.insertWaterLog(WaterLogEntity(amountMl = 500))
        waterDao.insertWaterLog(WaterLogEntity(amountMl = 250))
    }
}

