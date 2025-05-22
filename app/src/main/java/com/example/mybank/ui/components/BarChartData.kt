package com.example.mybank.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

@Composable
fun TotalMaxMinBarChart(
    totalValue: Double = 0.0,
    maxValue: Double = 0.0,
    minValue: Double = 0.0,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }

    // Animation for bars growing
    val totalAnimation by animateFloatAsState(
        targetValue = if (animationPlayed) totalValue.toFloat() else 0f,
        animationSpec = tween(durationMillis = 1200),
        label = "total_animation"
    )

    val maxAnimation by animateFloatAsState(
        targetValue = if (animationPlayed) maxValue.toFloat() else 0f,
        animationSpec = tween(durationMillis = 1200, delayMillis = 200),
        label = "max_animation"
    )

    val minAnimation by animateFloatAsState(
        targetValue = if (animationPlayed) minValue.toFloat() else 0f,
        animationSpec = tween(durationMillis = 1200, delayMillis = 400),
        label = "min_animation"
    )

    LaunchedEffect(totalValue, maxValue, minValue) {
        animationPlayed = false
        kotlinx.coroutines.delay(100)
        animationPlayed = true
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(500.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(40.dp)
        ) {
            // Chart Title
            Text(
                text = "Solde Statistique",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Total, Maximum & Minimum solde",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Custom Bar Chart Canvas
            BarChartCanvas(
                totalValue = totalAnimation,
                maxValue = maxAnimation,
                minValue = minAnimation,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Values Summary Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ValueSummaryCard(
                    title = "TOTAL",
                    value = totalValue,
                    color = Color(0xFF1976D2),
                    icon = "💰"
                )
                ValueSummaryCard(
                    title = "MAX",
                    value = maxValue,
                    color = Color(0xFF388E3C),
                    icon = "📈"
                )
                ValueSummaryCard(
                    title = "MIN",
                    value = minValue,
                    color = Color(0xFFF57C00),
                    icon = "📉"
                )
            }
        }
    }
}

@Composable
private fun BarChartCanvas(
    totalValue: Float,
    maxValue: Float,
    minValue: Float,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    Canvas(modifier = modifier) {
        drawBarChart(
            totalValue = totalValue,
            maxValue = maxValue,
            minValue = minValue,
            canvasWidth = size.width,
            canvasHeight = size.height,
            density = density
        )
    }
}

private fun DrawScope.drawBarChart(
    totalValue: Float,
    maxValue: Float,
    minValue: Float,
    canvasWidth: Float,
    canvasHeight: Float,
    density: androidx.compose.ui.unit.Density
) {
    val values = listOf(totalValue, maxValue, minValue)
    val labels = listOf("Total", "Max", "Min")
    val colors = listOf(
        Color(0xFF1976D2), // Blue
        Color(0xFF388E3C), // Green
        Color(0xFFF57C00)  // Orange
    )

    val maxDataValue = kotlin.math.max(kotlin.math.max(totalValue, maxValue), kotlin.math.max(maxValue, minValue))
    if (maxDataValue <= 0) return

    // Chart area settings
    val chartAreaHeight = canvasHeight * 0.7f
    val chartAreaBottom = canvasHeight * 0.8f
    val chartAreaTop = chartAreaBottom - chartAreaHeight

    // Bar settings
    val numberOfBars = 3
    val totalBarAreaWidth = canvasWidth * 0.8f
    val barWidth = totalBarAreaWidth / (numberOfBars * 1.5f)
    val barSpacing = barWidth * 0.5f
    val startX = (canvasWidth - totalBarAreaWidth) / 2

    // Draw grid lines
    for (i in 0..5) {
        val y = chartAreaBottom - (i * chartAreaHeight / 5)
        drawLine(
            color = Color.Gray.copy(alpha = 0.2f),
            start = Offset(startX - 20f, y),
            end = Offset(startX + totalBarAreaWidth + 20f, y),
            strokeWidth = 1.dp.toPx()
        )

        // Draw y-axis labels
        val labelValue = (maxDataValue * i / 5).toInt()
        drawContext.canvas.nativeCanvas.drawText(
            "${labelValue}AR",
            startX - 40f,
            y + with(density) { 4.sp.toPx() },
            android.graphics.Paint().apply {
                color = Color.Gray.toArgb()
                textSize = with(density) { 10.sp.toPx() }
                textAlign = android.graphics.Paint.Align.RIGHT
            }
        )
    }

    // Draw bars
    values.forEachIndexed { index, value ->
        val barHeight = (value / maxDataValue) * chartAreaHeight
        val barLeft = startX + (index * (barWidth + barSpacing))
        val barTop = chartAreaBottom - barHeight

        // Draw bar shadow
        drawRoundRect(
            color = Color.Black.copy(alpha = 0.1f),
            topLeft = Offset(barLeft + 4f, barTop + 4f),
            size = Size(barWidth, barHeight),
            cornerRadius = CornerRadius(8.dp.toPx())
        )

        // Draw main bar
        drawRoundRect(
            color = colors[index],
            topLeft = Offset(barLeft, barTop),
            size = Size(barWidth, barHeight),
            cornerRadius = CornerRadius(8.dp.toPx())
        )

        // Draw value label on top of bar
        if (value > 0) {
            drawContext.canvas.nativeCanvas.drawText(
                "${value.toInt()}AR",
                barLeft + barWidth / 2,
                barTop - with(density) { 8.dp.toPx() },
                android.graphics.Paint().apply {
                    color = colors[index].toArgb()
                    textSize = with(density) { 12.sp.toPx() }
                    textAlign = android.graphics.Paint.Align.CENTER
                    isFakeBoldText = true
                }
            )
        }

        // Draw label below bar
        drawContext.canvas.nativeCanvas.drawText(
            labels[index],
            barLeft + barWidth / 2,
            chartAreaBottom + with(density) { 20.dp.toPx() },
            android.graphics.Paint().apply {
                color = Color.DarkGray.toArgb()
                textSize = with(density) { 14.sp.toPx() }
                textAlign = android.graphics.Paint.Align.CENTER
                isFakeBoldText = true
            }
        )
    }
}

@Composable
private fun ValueSummaryCard(
    title: String,
    value: Double,
    color: Color,
    icon: String
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = icon,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = color,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "$${String.format("%.0f", value)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Example usage screen
@Composable
fun DataVisualizationScreen() {
    var totalValue by remember { mutableDoubleStateOf(1200.0) }
    var maxValue by remember { mutableDoubleStateOf(180.0) }
    var minValue by remember { mutableDoubleStateOf(35.0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        Text(
            text = "Financial Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Controls Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Adjust Values",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                // Interactive Sliders
                SliderWithLabel(
                    label = "Total",
                    value = totalValue,
                    onValueChange = { totalValue = it },
                    valueRange = 500.0..2500.0,
                    color = Color(0xFF1976D2)
                )

                SliderWithLabel(
                    label = "Max",
                    value = maxValue,
                    onValueChange = { maxValue = it },
                    valueRange = 50.0..500.0,
                    color = Color(0xFF388E3C)
                )

                SliderWithLabel(
                    label = "Min",
                    value = minValue,
                    onValueChange = { minValue = it },
                    valueRange = 10.0..200.0,
                    color = Color(0xFFF57C00)
                )

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            totalValue = (800..2000).random().toDouble()
                            maxValue = (100..400).random().toDouble()
                            minValue = (15..100).random().toDouble()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                    ) {
                        Text("🎲 Random")
                    }

                    OutlinedButton(
                        onClick = {
                            totalValue = 1200.0
                            maxValue = 180.0
                            minValue = 35.0
                        }
                    ) {
                        Text("🔄 Reset")
                    }
                }
            }
        }

        // Main Chart
        TotalMaxMinBarChart(
            totalValue = totalValue,
            maxValue = maxValue,
            minValue = minValue
        )
    }
}

@Composable
private fun SliderWithLabel(
    label: String,
    value: Double,
    onValueChange: (Double) -> Unit,
    valueRange: ClosedFloatingPointRange<Double>,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = color
            )
            Text(
                text = "$${String.format("%.0f", value)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toDouble()) },
            valueRange = valueRange.start.toFloat()..valueRange.endInclusive.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color.copy(alpha = 0.7f)
            )
        )
    }
}