package com.example.mybank.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.graphics.Paint

/**
 * Enum pour définir le type d'arrondi des barres
 */
enum class BarType {
    CIRCULAR_TYPE,  // Barres avec forme circulaire
    TOP_CURVED      // Barres avec coins arrondis en haut
}

/**
 * Composant pour afficher un graphique en barres personnalisable
 *
 * @param graphBarData Liste des valeurs normalisées (entre 0 et 1) pour les hauteurs des barres
 * @param xAxisScaleData Liste des valeurs à afficher sur l'axe X
 * @param barData_ Liste des valeurs réelles des données (utilisées pour l'échelle Y)
 * @param height Hauteur du graphique
 * @param roundType Type d'arrondi pour les barres
 * @param barWidth Largeur des barres
 * @param barColor Couleur des barres
 * @param barArrangement Disposition horizontale des barres
 */
@Composable
fun BarGraph(
    graphBarData: List<Float>,
    xAxisScaleData: List<String>,
    barData_: List<Int>,
    height: Dp = 300.dp,
    roundType: BarType = BarType.TOP_CURVED,
    barWidth: Dp = 40.dp,
    barColor: Color = Color(0xFF6E53E5),
    barArrangement: Arrangement.Horizontal = Arrangement.SpaceEvenly
) {
    // Validation des entrées
    if (graphBarData.isEmpty() || xAxisScaleData.isEmpty() || barData_.isEmpty() ||
        graphBarData.size != xAxisScaleData.size || graphBarData.size != barData_.size) {
        Text(
            text = "Données invalides pour le graphique",
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    val barData by remember {
        mutableStateOf(barData_.toList())
    }

    // Configuration de l'écran
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp.dp

    // Dimensions
    val xAxisScaleHeight = 40.dp
    val yAxisScaleSpacing by remember { mutableStateOf(100f) }
    val yAxisTextWidth by remember { mutableStateOf(80.dp) }

    // Forme des barres selon le type choisi
    val barShape = when (roundType) {
        BarType.CIRCULAR_TYPE -> CircleShape
        BarType.TOP_CURVED -> RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
    }

    // Configuration pour le texte et les lignes
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = Color.Black.hashCode()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    // Coordonnées Y pour les lignes pointillées
    val yCoordinates = remember { mutableListOf<Float>() }
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    val lineHeightXAxis = 10.dp
    val horizontalLineHeight = 2.dp

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        // Couche 1: Échelle Y et lignes pointillées horizontales
        Column(
            modifier = Modifier
                .padding(top = xAxisScaleHeight, end = 3.dp)
                .height(height)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxSize()
            ) {
                // Nettoyage des coordonnées précédentes
                yCoordinates.clear()

                // Calcul de l'échelle Y
                val maxValue = barData.maxOrNull() ?: 0
                val minValue = barData.minOrNull() ?: 0
                val yAxisScaleText = (maxValue - minValue).toFloat() / 3f

                // Dessin du texte de l'échelle Y
                (0..3).forEach { i ->
                    val yValue = minValue + (yAxisScaleText * i).toInt()
                    val yPos = size.height - yAxisScaleSpacing - i * size.height / 3f

                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            yValue.toString(),
                            30f,
                            yPos,
                            textPaint
                        )
                    }
                    yCoordinates.add(yPos)
                }

                // Dessin des lignes pointillées horizontales
                (1..3).forEach {
                    drawLine(
                        start = Offset(x = yAxisScaleSpacing + 30f, y = yCoordinates[it]),
                        end = Offset(x = size.width, y = yCoordinates[it]),
                        color = Color.Gray,
                        strokeWidth = 2f,
                        pathEffect = pathEffect
                    )
                }
            }
        }

        // Couche 2: Graphique avec barres et échelle X
        Box(
            modifier = Modifier
                .padding(start = 50.dp)
                .width(width - yAxisTextWidth)
                .height(height + xAxisScaleHeight),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Barres du graphique
            Row(
                modifier = Modifier.width(width - yAxisTextWidth),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = barArrangement
            ) {
                graphBarData.forEachIndexed { index, value ->
                    var animationTriggered by remember { mutableStateOf(false) }
                    val graphBarHeight by animateFloatAsState(
                        targetValue = if (animationTriggered) value else 0f,
                        animationSpec = tween(
                            durationMillis = 1000,
                            delayMillis = 0
                        )
                    )

                    LaunchedEffect(key1 = true) {
                        animationTriggered = true
                    }

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Barre individuelle
                        Box(
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .clip(barShape)
                                .width(barWidth)
                                .height(height - 10.dp)
                                .background(Color.Transparent),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(barShape)
                                    .fillMaxWidth()
                                    .fillMaxHeight(graphBarHeight)
                                    .background(barColor)
                            )
                        }

                        // Échelle X et partie inférieure du graphique
                        Column(
                            modifier = Modifier.height(xAxisScaleHeight),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Petite ligne verticale joignant la ligne horizontale de l'axe X
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            bottomStart = 2.dp,
                                            bottomEnd = 2.dp
                                        )
                                    )
                                    .width(horizontalLineHeight)
                                    .height(lineHeightXAxis)
                                    .background(color = Color.Gray)
                            )

                            // Texte de l'échelle X
                            Text(
                                modifier = Modifier.padding(bottom = 3.dp),
                                text = xAxisScaleData[index],
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // Ligne horizontale sur l'axe X du graphique
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = xAxisScaleHeight + 3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .fillMaxWidth()
                        .height(horizontalLineHeight)
                        .background(Color.Gray)
                )
            }
        }
    }
}

/**
 * Exemple d'utilisation du composant BarGraph
 */
@Composable
fun BarChartExample() {
    // Données réelles
    val barData = listOf(1500, 2300, 1800, 2800, 2000, 2650)

    // Étiquettes de l'axe X
    val xAxisLabels = listOf("Jan", "Fév", "Mar", "Avr", "Mai", "Juin")

    // Normalisation des données pour la hauteur des barres (entre 0 et 1)
    val maxValue = barData.maxOrNull()?.toFloat() ?: 1f
    val normalizedData = barData.map { it / maxValue }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Revenus Mensuels",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        BarGraph(
            graphBarData = normalizedData,
            xAxisScaleData = xAxisLabels,
            barData_ = barData,
            height = 250.dp,
            roundType = BarType.TOP_CURVED,
            barWidth = 30.dp,
            barColor = Color(0xFF6200EE)
        )
    }
}