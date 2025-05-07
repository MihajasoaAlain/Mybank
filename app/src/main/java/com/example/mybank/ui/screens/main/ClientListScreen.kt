import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybank.api.data.dto.StatsResponse
import com.example.mybank.api.repository.RemoteClientRepository
import com.example.mybank.ui.client.ClientTableRow
import com.example.mybank.ui.client.StatistiquesIOS
import com.example.mybank.ui.client.dialog.DeleteConfirmationDialog
import com.example.mybank.ui.client.dialog.EditClientDialog
import kotlinx.coroutines.launch

@Composable
fun ClientListScreen(repository: RemoteClientRepository) {
    var clients by remember { mutableStateOf<List<ClientBancaire>>(emptyList()) }
    var stats by remember { mutableStateOf<StatsResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // État pour les alertes de succès
    var showSuccessMessage by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    // Couleurs style iOS
    val iosPrimaryColor = Color(0xFF007AFF)
    val iosSurfaceColor = Color(0xFFF2F5F9)
    val iosGreen = Color(0xFF34C759)
    val iosYellow = Color(0xFFFFCC00)
    val iosRed = Color(0xFFFF3B30)
    val headerColor = Color(0xFFF8F8F8)

    // État pour les modales
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var clientToDelete by remember { mutableStateOf<ClientBancaire?>(null) }
    var clientToEdit by remember { mutableStateOf<ClientBancaire?>(null) }

    // État pour le SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Fonction pour rafraîchir les données
    val refreshData = {
        scope.launch {
            isLoading = true
            error = null
            try {
                val fetchedClients = repository.getClients()
                println("DEBUG: refreshData - fetched ${fetchedClients.size} clients")
                clients = fetchedClients

                val fetchedStats = repository.getStats()
                println("DEBUG: refreshData - stats fetched: ${fetchedStats != null}")
                stats = fetchedStats

                isLoading = false
            } catch (e: Exception) {
                println("ERROR in refreshData: ${e.message}")
                e.printStackTrace()
                error = "Erreur lors du chargement des données: ${e.message}"
                isLoading = false
            }
        }
    }

    // Charger les données initiales
    LaunchedEffect(key1 = true) {
        println("DEBUG: Initial data load started")
        refreshData()
    }

    // Afficher une Snackbar lorsque le message de succès est défini
    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            snackbarHostState.showSnackbar(
                message = successMessage,
                duration = SnackbarDuration.Short
            )
            showSuccessMessage = false
        }
    }

    // Scaffold pour contenir le Snackbar
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = iosSurfaceColor
    ) { paddingValues ->
        // Afficher l'interface selon l'état (chargement, erreur, données)
        when {
            isLoading -> {
                // Afficher un indicateur de chargement
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Chargement en cours...")
                    }
                }
            }

            error != null -> {
                // Afficher le message d'erreur avec option de réessayer
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = error ?: "Une erreur s'est produite",
                            color = iosRed,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { refreshData() },
                            colors = ButtonDefaults.buttonColors(containerColor = iosPrimaryColor)
                        ) {
                            Text("Réessayer")
                        }
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(iosSurfaceColor)
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    // En-tête élégant
                    Text(
                        text = "Liste des Clients",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Tableau de clients stylisé
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // En-tête du tableau
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(headerColor, headerColor.copy(alpha = 0.9f))
                                        )
                                    )
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "N°",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = iosPrimaryColor,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.weight(0.15f)
                                )
                                Text(
                                    text = "Nom",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = iosPrimaryColor,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.weight(0.35f)
                                )
                                Text(
                                    text = "Solde",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = iosPrimaryColor,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.weight(0.25f)
                                )
                                Text(
                                    text = "Catégorie",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = iosPrimaryColor,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.weight(0.25f)
                                )
                                // Espace pour le menu d'actions
                                Spacer(modifier = Modifier.width(20.dp))
                            }

                            // Contenu du tableau
                            LazyColumn {
                                itemsIndexed(clients) { index, client ->
                                    ClientTableRow(
                                        client = client,
                                        onEdit = {
                                            println("DEBUG: Edit clicked for client: ${client.numCompte}")
                                            clientToEdit = client
                                            showEditDialog = true
                                        },
                                        onDelete = {
                                            println("DEBUG: Delete clicked for client: ${client.numCompte}")
                                            clientToDelete = client
                                            showDeleteDialog = true
                                        },
                                        iosGreen = iosGreen,
                                        iosYellow = iosYellow,
                                        iosRed = iosRed,
                                        iosPrimaryColor = iosPrimaryColor,
                                        isOddRow = index % 2 != 0
                                    )
                                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Statistiques style iOS
                    StatistiquesIOS(iosPrimaryColor, stats)

                    // Modale de confirmation de suppression
                    if (showDeleteDialog) {
                        // Capture du client à supprimer dans une variable locale non-nullable
                        val clientToDeleteNonNull = clientToDelete
                        if (clientToDeleteNonNull != null) {
                            // Stocker l'ID en dehors de la fonction de callback
                            val clientId = clientToDeleteNonNull.numCompte.toString()

                            DeleteConfirmationDialog(
                                client = clientToDeleteNonNull,
                                onConfirm = {
                                    scope.launch {
                                        try {
                                            // Utiliser directement l'ID capturé plus haut
                                            if (clientId.isNotEmpty()) {
                                                println("DEBUG: Deleting client with ID: $clientId")
                                                val success = repository.deleteClient(clientId)

                                                if (success) {
                                                    // Rafraîchir la liste après suppression
                                                    refreshData()
                                                    successMessage = "Client supprimé avec succès"
                                                    showSuccessMessage = true
                                                } else {
                                                    error = "Échec de la suppression du client"
                                                }
                                            } else {
                                                error = "ID client invalide"
                                                println("ERROR: Empty client ID for delete")
                                            }
                                        } catch (e: Exception) {
                                            println("ERROR during delete: ${e.message}")
                                            e.printStackTrace()
                                            error = "Erreur lors de la suppression: ${e.message}"
                                        }
                                    }
                                    showDeleteDialog = false
                                    clientToDelete = null
                                },
                                onDismiss = {
                                    showDeleteDialog = false
                                    clientToDelete = null
                                },
                                iosRed = iosRed
                            )
                        }
                    }

                    // Modale d'édition de client
                    if (showEditDialog) {
                        // Capture du client à éditer dans une variable locale non-nullable
                        val clientToEditNonNull = clientToEdit
                        if (clientToEditNonNull != null) {
                            // Stocker l'ID en dehors de la fonction de callback
                            val clientId = clientToEditNonNull.numCompte.toString()

                            EditClientDialog(
                                client = clientToEditNonNull,
                                onConfirm = { updatedClient ->
                                    scope.launch {
                                        try {
                                            // Utiliser directement l'ID capturé plus haut
                                            if (clientId.isNotEmpty()) {
                                                println("DEBUG: Updating client with ID: $clientId")
                                                println("DEBUG: Updated data: $updatedClient")

                                                val success = repository.updateClient(clientId, updatedClient)

                                                if (success) {
                                                    // Rafraîchir la liste après modification
                                                    refreshData()
                                                    successMessage = "Client mis à jour avec succès"
                                                    showSuccessMessage = true
                                                } else {
                                                    error = "Échec de la mise à jour du client"
                                                }
                                            } else {
                                                error = "ID client invalide"
                                                println("ERROR: Empty client ID for update")
                                            }
                                        } catch (e: Exception) {
                                            println("ERROR during update: ${e.message}")
                                            e.printStackTrace()
                                            error = "Erreur lors de la mise à jour: ${e.message}"
                                        }
                                    }
                                    showEditDialog = false
                                    clientToEdit = null
                                },
                                onDismiss = {
                                    showEditDialog = false
                                    clientToEdit = null
                                },
                                iosPrimaryColor = iosPrimaryColor
                            )
                        }
                    }
                }
            }
        }
    }
}