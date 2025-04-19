package com.example.mybank.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybank.ui.theme.BankColors
import com.example.mybank.ui.theme.MyBankTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(onNavigateToMain: () -> Unit) {
    // State
    var backendAddress by remember { mutableStateOf("") }

    // Gradient pour le fond
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            BankColors.backgroundColor,
            BankColors.lightBlue.copy(alpha = 0.3f),
            BankColors.backgroundColor
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AdminContentCard(
                backendAddress = backendAddress,
                onBackendAddressChange = { backendAddress = it },
                onStartClick = onNavigateToMain
            )
        }

        // Élément décoratif en bas de l'écran
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BankColors.backgroundColor.copy(alpha = 0f),
                            BankColors.primaryColor.copy(alpha = 0.1f)
                        )
                    )
                )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminContentCard(
    backendAddress: String,
    onBackendAddressChange: (String) -> Unit,
    onStartClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icône et titre
            Icon(
                imageVector = Icons.Filled.AdminPanelSettings,
                contentDescription = "Admin Icon",
                tint = BankColors.primaryColor,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "BANK ADMIN",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BankColors.primaryColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Configuration du système",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Champ de saisie pour l'adresse du backend avec icône
            OutlinedTextField(
                value = backendAddress,
                onValueChange = onBackendAddressChange,
                label = { Text("Adresse du backend") },
                placeholder = { Text("https://api.mybank.com") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    cursorColor = BankColors.accentColor,
                    focusedIndicatorColor = BankColors.accentColor,
                    unfocusedIndicatorColor = BankColors.primaryColor.copy(alpha = 0.5f),
                    focusedLabelColor = BankColors.accentColor,
                    unfocusedLabelColor = BankColors.primaryColor.copy(alpha = 0.7f)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Link,
                        contentDescription = "URL Icon",
                        tint = BankColors.accentColor
                    )
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Bouton pour commencer avec icône
            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .size(width = 220.dp, height = 56.dp)
                    .clip(RoundedCornerShape(28.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BankColors.accentColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Start",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "COMMENCER",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
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