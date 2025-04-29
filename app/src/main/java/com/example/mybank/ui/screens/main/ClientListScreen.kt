import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import com.example.mybank.api.data.dto.StatsResponse
import com.example.mybank.api.repository.RemoteClientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



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
                                repository.supprimerClient(clientToDelete!!.first)
                                // Rafraîchir la liste après suppression
                                clients = repository.getClients()
                                stats = repository.getStats()
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
                                repository.modifierClient(clientToEdit!!.first, updatedClient)
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

@Composable
fun ClientTableRow(
    client: ClientBancaire,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    iosGreen: Color,
    iosYellow: Color,
    iosRed: Color,
    iosPrimaryColor: Color,
    isOddRow: Boolean
) {
    val statusColor = when(client.getCategorieSolde()) {
        "insuffisant" -> iosRed
        "moyen" -> iosYellow
        else -> iosGreen
    }

    val rowBackground = if (isOddRow) {
        Color.White
    } else {
        Color(0xFFFAFAFA)
    }

    // État pour gérer l'ouverture du menu déroulant
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(rowBackground)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Design responsive avec poids adaptés
        Text(
            text = client.numCompte,
            fontSize = 14.sp,
            modifier = Modifier.weight(0.8f)
        )
        Text(
            text = client.nom,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.weight(1.2f)
        )
        Text(
            text = String.format("%.2f €", client.solde),
            fontSize = 14.sp,
            color = if (client.solde < 0) iosRed else Color.Black,
            modifier = Modifier.weight(0.8f)
        )

        // Catégorie avec style badge
        Box(
            modifier = Modifier
                .weight(0.8f),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = client.getCategorieSolde(),
                fontSize = 12.sp,
                color = statusColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(statusColor.copy(alpha = 0.1f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        // Menu d'actions déroulant
        Box {
            IconButton(
                onClick = { expanded = true },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = "Options",
                    tint = iosPrimaryColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true)
            ) {
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = null,
                                tint = iosPrimaryColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Modifier")
                        }
                    },
                    onClick = {
                        expanded = false
                        onEdit()
                    }
                )
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = null,
                                tint = iosRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Supprimer")
                        }
                    },
                    onClick = {
                        expanded = false
                        onDelete()
                    }
                )
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    client: ClientBancaire,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    iosRed: Color
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Confirmer la suppression",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Êtes-vous sûr de vouloir supprimer le client suivant ?",
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Informations du client
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF5F5F5))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Nom: ",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = client.nom,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Numéro: ",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = client.numCompte,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Solde: ",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = String.format("%.2f €", client.solde),
                            fontSize = 14.sp,
                            color = if (client.solde < 0) iosRed else Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Boutons d'action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.DarkGray
                        )
                    ) {
                        Text("Annuler")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = iosRed
                        )
                    ) {
                        Text("Supprimer")
                    }
                }
            }
        }
    }
}

@Composable
fun EditClientDialog(
    client: ClientBancaire,
    onConfirm: (ClientBancaire) -> Unit,
    onDismiss: () -> Unit,
    iosPrimaryColor: Color
) {
    var nom by remember { mutableStateOf(client.nom) }
    var numCompte by remember { mutableStateOf(client.numCompte) }
    var soldeText by remember { mutableStateOf(client.solde.toString()) }

    val solde = soldeText.toDoubleOrNull() ?: client.solde

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Modifier le client",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Champs d'édition
                OutlinedTextField(
                    value = nom,
                    onValueChange = { nom = it },
                    label = { Text("Nom") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = iosPrimaryColor,
                        focusedLabelColor = iosPrimaryColor
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = numCompte,
                    onValueChange = { numCompte = it },
                    label = { Text("Numéro de compte") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = iosPrimaryColor,
                        focusedLabelColor = iosPrimaryColor
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = soldeText,
                    onValueChange = { soldeText = it },
                    label = { Text("Solde") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = iosPrimaryColor,
                        focusedLabelColor = iosPrimaryColor
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Boutons d'action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.DarkGray
                        )
                    ) {
                        Text("Annuler")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            val updatedClient = ClientBancaire(
                                nom = nom,
                                numCompte = numCompte,
                                solde = solde
                            )
                            onConfirm(updatedClient)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = iosPrimaryColor
                        )
                    ) {
                        Text("Enregistrer")
                    }
                }
            }
        }
    }
}

@Composable
fun StatistiquesIOS(
    iosPrimaryColor: Color,
    statsResponse: StatsResponse?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Statistiques",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Design statistiques responsive
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Utilisation de l'opérateur de navigation sécurisée pour accéder aux propriétés
                StatItemIOS("Solde Total", statsResponse?.total ?: 0.0, iosPrimaryColor)
                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp),
                    color = Color.LightGray
                )
                StatItemIOS("Solde Min", statsResponse?.min ?: 0.0, iosPrimaryColor)
                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp),
                    color = Color.LightGray
                )
                StatItemIOS("Solde Max", statsResponse?.max ?: 0.0, iosPrimaryColor)
            }
        }
    }
}

@Composable
fun StatItemIOS(label: String, value: Double, iosPrimaryColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = String.format("%.2f €", value),
            color = iosPrimaryColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}