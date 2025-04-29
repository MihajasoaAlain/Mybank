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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ClientListScreen(repository: RemoteClientRepository) {
    // Suppression de la déclaration en double du repository
    var clients by remember { mutableStateOf<List<ClientBancaire>>(emptyList()) }
    var stats by remember { mutableStateOf<StatsResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
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
    var clientToDelete by remember { mutableStateOf<Pair<Int, ClientBancaire>?>(null) }
    var clientToEdit by remember { mutableStateOf<Pair<Int, ClientBancaire>?>(null) }

    LaunchedEffect(key1 = true) {
        try {
            clients = repository.getClients()
            print(clients)
            isLoading = false
            stats = repository.getStats()
        } catch (e: Exception) {
            error = "Erreur lors du chargement des clients: ${e.message}"
            isLoading = false
        }
    }

    // Afficher l'interface selon l'état (chargement, erreur, données)
    when {
        isLoading -> {
            // Afficher un indicateur de chargement
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        error != null -> {
            // Afficher le message d'erreur
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = error!!, color = Color.Red)
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(iosSurfaceColor)
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
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // En-tête du tableau avec design responsive
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
                            // Design responsive avec poids adaptés aux différentes largeurs d'écran
                            Text(
                                text = "N°",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = iosPrimaryColor,
                                modifier = Modifier.weight(0.8f),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = "Nom",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = iosPrimaryColor,
                                modifier = Modifier.weight(1.2f),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = "Solde",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = iosPrimaryColor,
                                modifier = Modifier.weight(0.8f),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = "Catégorie",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = iosPrimaryColor,
                                modifier = Modifier.weight(0.8f),
                                textAlign = TextAlign.Start
                            )
                            // Espace pour le menu d'actions
                            Spacer(modifier = Modifier.width(40.dp))
                        }

                        // Divider élégant
                        Divider(
                            color = iosPrimaryColor.copy(alpha = 0.1f),
                            thickness = 1.dp
                        )

                        // Contenu du tableau avec LazyColumn pour une meilleure performance
                        LazyColumn {
                            itemsIndexed(clients) { index, client ->
                                ClientTableRow(
                                    client = client,
                                    onEdit = {
                                        clientToEdit = Pair(index, client)
                                        showEditDialog = true
                                    },
                                    onDelete = {
                                        clientToDelete = Pair(index, client)
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
                if (showDeleteDialog && clientToDelete != null) {
                    DeleteConfirmationDialog(
                        client = clientToDelete!!.second,
                        onConfirm = {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    // Utiliser le numCompte du client à supprimer au lieu de l'index
                                    val clientId = clientToDelete!!.second.numCompte.toInt()  // ou .id selon votre modèle
                                    repository.supprimerClient(clientId)

                                    // Rafraîchir la liste après suppression
                                    clients = repository.getClients()
                                    stats = repository.getStats()
                                } catch (e: Exception) {
                                    // Gérer les erreurs
                                    println("Erreur lors de la suppression: ${e.message}")
                                    // Optionnel: mettre à jour l'UI pour montrer l'erreur
                                    withContext(Dispatchers.Main) {
                                        error = "Erreur lors de la suppression: ${e.message}"
                                    }
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

                // Modale d'édition de client
                if (showEditDialog && clientToEdit != null) {
                    EditClientDialog(
                        client = clientToEdit!!.second,
                        onConfirm = { updatedClient ->
                            CoroutineScope(Dispatchers.IO).launch {
                                repository.modifierClient(clientToEdit!!.second.numCompte.toInt(), updatedClient)
                                // Rafraîchir la liste après modification
                                clients = repository.getClients()
                                stats = repository.getStats()
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