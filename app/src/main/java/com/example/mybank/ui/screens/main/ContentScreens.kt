package com.example.mybank.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybank.ui.theme.BankColors

// Modèle pour les éléments du menu
data class MenuItem(val title: String, val icon: ImageVector)

// Fonction pour obtenir la liste des éléments du menu
@Composable
fun getSidebarMenuItems(): List<MenuItem> {
    return listOf(
        MenuItem("Liste Compte", Icons.AutoMirrored.Filled.List),
        MenuItem("Modifier", Icons.Default.Edit),
        MenuItem("Ajouter", Icons.Default.Add),
        MenuItem("Statistique", Icons.Default.BarChart)

    )
}

// Fonction pour obtenir les écrans de contenu
@Composable
fun getContentScreens(): List<@Composable () -> Unit> {
    return listOf(
        { DashboardScreen() },
        { AccountsScreen() },
        { TransactionsScreen() },
        { TransfersScreen() },
        { SettingsScreen() }
    )
}

// Écrans pour chaque section
@Composable
fun DashboardScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Dashboard,
                contentDescription = "Dashboard",
                modifier = Modifier.size(64.dp),
                tint = BankColors.primaryColor
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tableau de bord",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Bienvenue sur votre espace bancaire",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AccountsScreen() {
    ContentScreenTemplate(
        icon = Icons.Default.AccountBalance,
        title = "Comptes",
        description = "Gérez vos différents comptes bancaires"
    )
}

@Composable
fun TransactionsScreen() {
    ContentScreenTemplate(
        icon = Icons.Default.Payment,
        title = "Transactions",
        description = "Consultez l'historique de vos transactions"
    )
}

@Composable
fun TransfersScreen() {
    ContentScreenTemplate(
        icon = Icons.Default.Send,
        title = "Virements",
        description = "Effectuez des virements entre vos comptes ou vers d'autres bénéficiaires"
    )
}

@Composable
fun SettingsScreen() {
    ContentScreenTemplate(
        icon = Icons.Default.Settings,
        title = "Paramètres",
        description = "Configurez vos préférences et vos options de sécurité"
    )
}

@Composable
fun ContentScreenTemplate(
    icon: ImageVector,
    title: String,
    description: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(64.dp),
                tint = BankColors.primaryColor
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}