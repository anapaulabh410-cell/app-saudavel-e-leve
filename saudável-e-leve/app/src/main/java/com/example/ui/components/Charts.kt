package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkGrayText
import com.example.ui.theme.DarkGreen
import com.example.ui.theme.GlassBorderLight
import com.example.ui.theme.LightGrayBG
import com.example.ui.theme.LightGreenAccent
import com.example.ui.theme.MustardYellow
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.PureWhite
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.SecondaryTextGray

data class ChartPoint(
    val label: String,
    val value: Float,
    val formattedValue: String
)

data class CategorySlice(
    val category: String,
    val amount: Double,
    val color: Color
)

/**
 * Interactive Glassmorphic Line Chart with Smooth Curve and Timeframe Filter
 */
@Composable
fun InteractiveLineChart(
    points: List<ChartPoint>,
    selectedTimeframe: String,
    onTimeframeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    GlassCard(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Fluxo Financeiro",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGrayText
                    )
                    Text(
                        text = "Evolução do Saldo",
                        fontSize = 12.sp,
                        color = SecondaryTextGray
                    )
                }

                // Timeframe Selector Pills
                Row(
                    modifier = Modifier
                        .background(DarkGreen.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    listOf("Mensal", "Trimestral", "Anual").forEach { tf ->
                        val isSelected = tf == selectedTimeframe
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) DarkGreen else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onTimeframeSelected(tf) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = tf,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) PureWhite else SecondaryTextGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tooltip if point selected
            selectedIndex?.let { idx ->
                if (idx in points.indices) {
                    val p = points[idx]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkGreen, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = p.label, color = MustardYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(text = p.formattedValue, color = PureWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Canvas drawing smooth curve
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                if (points.isNotEmpty()) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .pointerInput(points) {
                                detectTapGestures { offset ->
                                    val width = size.width
                                    val step = width / (points.size - 1).coerceAtLeast(1)
                                    val clickedIndex = (offset.x / step).toInt().coerceIn(0, points.size - 1)
                                    selectedIndex = clickedIndex
                                }
                            }
                    ) {
                        val width = size.width
                        val height = size.height
                        val maxVal = (points.maxOfOrNull { it.value } ?: 100f).coerceAtLeast(10f)
                        val minVal = (points.minOfOrNull { it.value } ?: 0f).coerceAtMost(0f)
                        val range = (maxVal - minVal).coerceAtLeast(1f)

                        val spacing = width / (points.size - 1).coerceAtLeast(1)

                        // Background grid lines
                        for (i in 0..3) {
                            val y = height * (i / 3f)
                            drawLine(
                                color = Color.Gray.copy(alpha = 0.15f),
                                start = Offset(0f, y),
                                end = Offset(width, y),
                                strokeWidth = 1f
                            )
                        }

                        // Path calculation
                        val coordinates = points.mapIndexed { index, point ->
                            val x = index * spacing
                            val normalizedY = (point.value - minVal) / range
                            val y = height - (normalizedY * (height - 30f) + 15f)
                            Offset(x, y)
                        }

                        val strokePath = Path().apply {
                            if (coordinates.isNotEmpty()) {
                                moveTo(coordinates.first().x, coordinates.first().y)
                                for (i in 0 until coordinates.size - 1) {
                                    val p1 = coordinates[i]
                                    val p2 = coordinates[i + 1]
                                    val cx1 = p1.x + (p2.x - p1.x) / 2f
                                    val cy1 = p1.y
                                    val cx2 = p1.x + (p2.x - p1.x) / 2f
                                    val cy2 = p2.y
                                    cubicTo(cx1, cy1, cx2, cy2, p2.x, p2.y)
                                }
                            }
                        }

                        val fillPath = Path().apply {
                            addPath(strokePath)
                            lineTo(width, height)
                            lineTo(0f, height)
                            close()
                        }

                        // Gradient fill
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MustardYellow.copy(alpha = 0.35f),
                                    MustardYellow.copy(alpha = 0.02f)
                                )
                            )
                        )

                        // Main stroke line
                        drawPath(
                            path = strokePath,
                            color = MustardYellow,
                            style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Draw points
                        coordinates.forEachIndexed { i, pt ->
                            val isSel = selectedIndex == i
                            drawCircle(
                                color = if (isSel) OrangeAccent else DarkGreen,
                                radius = if (isSel) 7.dp.toPx() else 4.dp.toPx(),
                                center = pt
                            )
                            drawCircle(
                                color = PureWhite,
                                radius = if (isSel) 4.dp.toPx() else 2.dp.toPx(),
                                center = pt
                            )
                        }
                    }
                }
            }

            // X-Axis Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                points.forEach { p ->
                    Text(
                        text = p.label,
                        fontSize = 11.sp,
                        color = SecondaryTextGray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

/**
 * Donut Chart Component for Category Allocation
 */
@Composable
fun DonutCategoryChart(
    slices: List<CategorySlice>,
    totalText: String,
    modifier: Modifier = Modifier
) {
    val totalVal = slices.sumOf { it.amount }.coerceAtLeast(1.0)

    GlassCard(modifier = modifier) {
        Column {
            Text(
                text = "Distribuição por Categoria",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGrayText
            )
            Text(
                text = "Análise de Despesas",
                fontSize = 12.sp,
                color = SecondaryTextGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Donut Canvas
                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(130.dp)) {
                        var startAngle = -90f
                        val strokeWidth = 24.dp.toPx()

                        slices.forEach { slice ->
                            val sweepAngle = ((slice.amount / totalVal) * 360f).toFloat()
                            drawArc(
                                color = slice.color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle - 2f, // gap
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                            startAngle += sweepAngle
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Total",
                            fontSize = 10.sp,
                            color = SecondaryTextGray
                        )
                        Text(
                            text = totalText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrayText
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Legend List
                Column(modifier = Modifier.weight(1f)) {
                    slices.forEach { slice ->
                        val percent = ((slice.amount / totalVal) * 100).toInt()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(slice.color, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = slice.category,
                                    fontSize = 12.sp,
                                    color = DarkGrayText,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = "$percent%",
                                fontSize = 12.sp,
                                color = SecondaryTextGray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Bar Chart Component comparing Income vs Expense
 */
@Composable
fun ComparisonBarChart(
    incomeData: List<Float>,
    expenseData: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Receitas vs Despesas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkGrayText
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(LightGreenAccent, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Receitas", fontSize = 11.sp, color = SecondaryTextGray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.size(10.dp).background(OrangeAccent, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Despesas", fontSize = 11.sp, color = SecondaryTextGray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val maxVal = (incomeData + expenseData).maxOrNull()?.coerceAtLeast(100f) ?: 100f

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                labels.forEachIndexed { i, label ->
                    val inc = incomeData.getOrNull(i) ?: 0f
                    val exp = expenseData.getOrNull(i) ?: 0f

                    val incHeightRatio = (inc / maxVal).coerceIn(0.05f, 1f)
                    val expHeightRatio = (exp / maxVal).coerceIn(0.05f, 1f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // Income Bar
                            Box(
                                modifier = Modifier
                                    .width(12.dp)
                                    .height((100 * incHeightRatio).dp)
                                    .background(LightGreenAccent, RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            )
                            // Expense Bar
                            Box(
                                modifier = Modifier
                                    .width(12.dp)
                                    .height((100 * expHeightRatio).dp)
                                    .background(OrangeAccent, RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            color = SecondaryTextGray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

data class DayCalorieData(
    val dayLabel: String,
    val consumedKcal: Int,
    val targetKcal: Int
)

/**
 * Recharts-inspired Interactive Weekly Calorie Progress Chart
 * Visualizes daily calorie intake vs target threshold with reference lines, tooltips & compliance indicators
 */
@Composable
fun WeeklyCalorieProgressChart(
    weeklyData: List<DayCalorieData>,
    modifier: Modifier = Modifier
) {
    var selectedDayIndex by remember { mutableStateOf<Int?>(null) }

    val defaultTarget = weeklyData.firstOrNull()?.targetKcal ?: 1800
    val maxVal = ((weeklyData.maxOfOrNull { maxOf(it.consumedKcal, it.targetKcal) } ?: 1800) * 1.18f).coerceAtLeast(1000f)

    val totalConsumed = weeklyData.sumOf { it.consumedKcal }
    val avgConsumed = if (weeklyData.isNotEmpty()) totalConsumed / weeklyData.size else 0
    val daysOnTarget = weeklyData.count { kotlin.math.abs(it.consumedKcal - it.targetKcal) <= 150 }

    GlassCard(modifier = modifier) {
        Column {
            // Title & Header Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Progresso Calórico Semanal",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGrayText
                    )
                    Text(
                        text = "Ingestão Real vs Meta Diária (${defaultTarget} kcal)",
                        fontSize = 12.sp,
                        color = SecondaryTextGray
                    )
                }

                // Legend
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(DarkGreen, CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Ingestão", fontSize = 10.sp, color = DarkGrayText, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.width(10.dp).height(2.dp).background(OrangeAccent))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Meta", fontSize = 10.sp, color = SecondaryTextGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightGrayBG, RoundedCornerShape(12.dp))
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Média Diária", fontSize = 10.sp, color = SecondaryTextGray)
                    Text(text = "$avgConsumed kcal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(GlassBorderLight))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Meta Atingida", fontSize = 10.sp, color = SecondaryTextGray)
                    Text(text = "$daysOnTarget de ${weeklyData.size} dias", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkGreen)
                }
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(GlassBorderLight))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Meta Definida", fontSize = 10.sp, color = SecondaryTextGray)
                    Text(text = "$defaultTarget kcal", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OrangeAccent)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Tooltip Box
            selectedDayIndex?.let { index ->
                if (index in weeklyData.indices) {
                    val day = weeklyData[index]
                    val diff = day.consumedKcal - day.targetKcal
                    val percent = (day.consumedKcal.toFloat() / day.targetKcal * 100).toInt()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkGreen, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "${day.dayLabel} - Detalhes do Dia", color = MustardYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Consumido: ${day.consumedKcal} kcal / Meta: ${day.targetKcal} kcal",
                                color = PureWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    if (kotlin.math.abs(diff) <= 150) LightGreenAccent else OrangeAccent,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "$percent%", color = DarkGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Canvas Bar Chart with Target Reference Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(weeklyData) {
                            detectTapGestures { offset ->
                                val width = size.width
                                val step = width / weeklyData.size.coerceAtLeast(1)
                                val clickedIdx = (offset.x / step).toInt().coerceIn(0, weeklyData.size - 1)
                                selectedDayIndex = clickedIdx
                            }
                        }
                ) {
                    val width = size.width
                    val height = size.height
                    val barCount = weeklyData.size.coerceAtLeast(1)
                    val sectionWidth = width / barCount
                    val barWidth = (sectionWidth * 0.45f).coerceAtMost(36.dp.toPx())

                    // Draw Horizontal Grid Lines
                    val gridSteps = 4
                    for (i in 0..gridSteps) {
                        val y = height * (i / gridSteps.toFloat())
                        drawLine(
                            color = Color.Gray.copy(alpha = 0.12f),
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1f
                        )
                    }

                    // Draw Dashed Target Reference Line (Recharts style)
                    val targetY = height - ((defaultTarget / maxVal) * height)
                    drawLine(
                        color = OrangeAccent,
                        start = Offset(0f, targetY),
                        end = Offset(width, targetY),
                        strokeWidth = 2.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                    )

                    // Draw Bars
                    weeklyData.forEachIndexed { i, dayData ->
                        val centerX = (i * sectionWidth) + (sectionWidth / 2f)
                        val barLeft = centerX - (barWidth / 2f)

                        val consumedHeight = ((dayData.consumedKcal / maxVal) * height).coerceIn(4.dp.toPx(), height)
                        val barTop = height - consumedHeight

                        val isSelected = selectedDayIndex == i

                        val isWithinMeta = kotlin.math.abs(dayData.consumedKcal - dayData.targetKcal) <= 150
                        val barColor = when {
                            isSelected -> MustardYellow
                            isWithinMeta -> DarkGreen
                            dayData.consumedKcal > dayData.targetKcal + 150 -> OrangeAccent
                            else -> LightGreenAccent
                        }

                        // Background pillar track
                        drawRoundRect(
                            color = DarkGreen.copy(alpha = 0.06f),
                            topLeft = Offset(barLeft, 0f),
                            size = Size(barWidth, height),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
                        )

                        // Active Consumed Bar
                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(barLeft, barTop),
                            size = Size(barWidth, consumedHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
                        )

                        if (isSelected) {
                            // Highlight border
                            drawRoundRect(
                                color = DarkGreen,
                                topLeft = Offset(barLeft - 2.dp.toPx(), barTop - 2.dp.toPx()),
                                size = Size(barWidth + 4.dp.toPx(), consumedHeight + 4.dp.toPx()),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                                style = Stroke(width = 2.dp.toPx())
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // X Axis Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                weeklyData.forEachIndexed { idx, dayData ->
                    val isSelected = selectedDayIndex == idx
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { selectedDayIndex = idx }
                    ) {
                        Text(
                            text = dayData.dayLabel,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) DarkGreen else SecondaryTextGray
                        )
                        Text(
                            text = "${dayData.consumedKcal}",
                            fontSize = 9.sp,
                            color = if (isSelected) DarkGreen else SecondaryTextGray.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

