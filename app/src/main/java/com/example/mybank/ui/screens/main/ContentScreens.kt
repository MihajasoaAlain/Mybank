package com.example.mybank.ui.screens.main

import ClientBancaire
import ClientListScreen
import ClientRepository
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mybank.repository.RemoteClientRepository
import com.example.mybank.ui.client.AddEditClientScreen
import com.example.mybank.ui.client.StatistiquesScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Modèle pour les éléments du menu
data class MenuItem(val title: String, val icon: ImageVector)

// Fonction pour obtenir la liste des éléments du menu
@Composable
fun getSidebarMenuItems(): List<MenuItem> {
    return listOf(
        MenuItem("Liste Compte", Icons.AutoMirrored.Filled.List),
        MenuItem("Ajouter", Icons.Default.Add),
        MenuItem("Statistique", Icons.Default.BarChart)
    )
}

// Fonction pour obtenir les écrans de contenu
@Composable
fun getContentScreens(): List<@Composable () -> Unit> {
    val repository = remember { RemoteClientRepository() }
    var selectedScreen by remember { mutableStateOf(0) }

    return listOf(
        { ClientListScreen(repository = repository) },
        { AddEditClientScreen(
            onSave = { client ->
                CoroutineScope(Dispatchers.IO).launch {
                    repository.ajouterClient(client)
                }
                selectedScreen = 0
            },
            onCancel = { selectedScreen = 0 }
        ) },
        { StatistiquesScreen(repository = repository) }
    )
}
