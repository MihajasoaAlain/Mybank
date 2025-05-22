package com.example.mybank.ui.screens.admin

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybank.ui.theme.BankColors
import com.example.mybank.ui.theme.MyBankTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(onNavigateToMain: () -> Unit) {
    // Animation d'entrée
    val animatedScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    // Animation de rotation pour l'icône
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Gradient sophistiqué pour le fond
    val gradientBrush = Brush.radialGradient(
        colors = listOf(
            BankColors.primaryColor.copy(alpha = 0.1f),
            BankColors.lightBlue.copy(alpha = 0.05f),
            BankColors.backgroundColor,
            BankColors.accentColor.copy(alpha = 0.08f)
        ),
        radius = 1200f,
        center = Offset(500f, 300f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        // Éléments décoratifs animés
        FloatingDecorations()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AdminContentCard(
                onStartClick = onNavigateToMain,
                animatedScale = animatedScale,
                rotationAngle = rotationAngle
            )
        }
    }
}

@Composable
private fun FloatingDecorations() {
    // Cercles décoratifs flottants
    Box(
        modifier = Modifier
            .offset(x = 50.dp, y = 100.dp)
            .size(80.dp)
            .background(
                BankColors.primaryColor.copy(alpha = 0.1f),
                CircleShape
            )
    )

    Box(
        modifier = Modifier
            .offset(x = 300.dp, y = 150.dp)
            .size(60.dp)
            .background(
                BankColors.accentColor.copy(alpha = 0.08f),
                CircleShape
            )
    )

    Box(
        modifier = Modifier
            .offset(x = 80.dp, y = 600.dp)
            .size(120.dp)
            .background(
                BankColors.lightBlue.copy(alpha = 0.06f),
                CircleShape
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminContentCard(
    onStartClick: () -> Unit,
    animatedScale: Float,
    rotationAngle: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(animatedScale),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Badge sécurisé
            Surface(
                modifier = Modifier
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                color = BankColors.primaryColor.copy(alpha = 0.1f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Secure",
                        tint = BankColors.primaryColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ACCÈS SÉCURISÉ",
                        style = MaterialTheme.typography.labelMedium,
                        color = BankColors.primaryColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Icône principale avec animation
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                BankColors.primaryColor.copy(alpha = 0.2f),
                                BankColors.primaryColor.copy(alpha = 0.05f)
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AdminPanelSettings,
                    contentDescription = "Admin Icon",
                    tint = BankColors.primaryColor,
                    modifier = Modifier
                        .size(56.dp)
                        .rotate(rotationAngle * 0.1f) // Rotation très lente
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Titre principal
            Text(
                text = "ADMINISTRATION",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = BankColors.primaryColor,
                textAlign = TextAlign.Center,
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "PANEL BANCAIRE",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Divider décoratif
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(3.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                BankColors.accentColor,
                                Color.Transparent
                            )
                        ),
                        RoundedCornerShape(2.dp)
                    )
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Bouton principal stylisé
            ElevatedButton(
                onClick = onStartClick,
                modifier = Modifier
                    .size(width = 260.dp, height = 64.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = BankColors.accentColor,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    hoveredElevation = 10.dp
                ),
                shape = RoundedCornerShape(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Start",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "ACCÉDER AU SYSTÈME",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Informations supplémentaires
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Interface d'administration sécurisée",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Gestion complète du système bancaire",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminScreenPreview() {
    MyBankTheme {
        AdminScreen(onNavigateToMain = {})
    }
}