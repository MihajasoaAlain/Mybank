package com.example.mybank.ui.screens.main

import ClientBancaire
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun AddEditClientScreen(
    clientToEdit: ClientBancaire? = null,
    onSave: (ClientBancaire) -> Unit,
    onCancel: () -> Unit
) {
    // Couleurs style iOS
    val iosPrimaryColor = Color(0xFF007AFF)
    val iosSurfaceColor = Color(0xFFF2F5F9)
    val iosGreen = Color(0xFF34C759)
    val headerColor = Color(0xFFF8F8F8)

    val isEditMode = clientToEdit != null
    val title = if (isEditMode) "Modifier un client" else "Ajouter un client"

    var numCompte by remember { mutableStateOf(clientToEdit?.numCompte ?: "") }
    var nom by remember { mutableStateOf(clientToEdit?.nom ?: "") }
    var solde by remember { mutableStateOf((clientToEdit?.solde ?: 0.0).toString()) }

    // État pour l'alerte de succès
    var showSuccessAlert by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    // État pour validation des erreurs
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Si l'alerte est visible, la masquer après un délai
    LaunchedEffect(showSuccessAlert) {
        if (showSuccessAlert) {
            delay(3000) // Cacher l'alerte après 3 secondes
            showSuccessAlert = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(iosSurfaceColor)
                .padding(16.dp)
        ) {
            // En-tête élégant
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Card principale contenant le formulaire
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // En-tête du formulaire
                    Text(
                        text = "Informations du client",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = iosPrimaryColor,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Champ de numéro de compte
                    OutlinedTextField(
                        value = numCompte,
                        onValueChange = { numCompte = it },
                        label = { Text("Numéro de compte") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        enabled = !isEditMode, // Désactivé en mode édition
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = iosPrimaryColor,
                            focusedLabelColor = iosPrimaryColor,
                            unfocusedBorderColor = Color.LightGray,
                            disabledBorderColor = Color.LightGray.copy(alpha = 0.5f),
                            disabledTextColor = Color.Gray
                        ),
                        isError = hasError && numCompte.isBlank()
                    )

                    // Champ de nom
                    OutlinedTextField(
                        value = nom,
                        onValueChange = { nom = it },
                        label = { Text("Nom du client") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = iosPrimaryColor,
                            focusedLabelColor = iosPrimaryColor,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        isError = hasError && nom.isBlank()
                    )

                    // Champ de solde
                    OutlinedTextField(
                        value = solde,
                        onValueChange = {
                            // Validation pour n'accepter que des nombres
                            if (it.isEmpty() || it.matches(Regex("^-?\\d*\\.?\\d*$"))) {
                                solde = it
                            }
                        },
                        label = { Text("Solde") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = iosPrimaryColor,
                            focusedLabelColor = iosPrimaryColor,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        trailingIcon = {
                            Text(
                                text = "AR",
                                color = Color.Gray,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        },
                        isError = hasError && (solde.isBlank() || solde.toDoubleOrNull() == null)
                    )

                    // Message d'erreur si nécessaire
                    if (hasError) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            // Boutons d'action dans une card séparée style iOS
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onCancel,
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
                            // Validation avant sauvegarde
                            if (numCompte.isBlank() || nom.isBlank() || solde.isBlank() || solde.toDoubleOrNull() == null) {
                                hasError = true
                                errorMessage = "Veuillez remplir correctement tous les champs"
                            } else {
                                hasError = false
                                val client = ClientBancaire(
                                    numCompte = numCompte,
                                    nom = nom,
                                    solde = solde.toDoubleOrNull() ?: 0.0
                                )

                                // Appeler onSave avec le client
                                onSave(client)

                                // Afficher le message de succès
                                successMessage = if (isEditMode) {
                                    "Client modifié avec succès"
                                } else {
                                    "Client ajouté avec succès"
                                }
                                showSuccessAlert = true
                            }
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

            // Message d'information
            if (isEditMode) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = iosGreen.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Vous modifiez les informations du client. Le numéro de compte ne peut pas être changé.",
                            fontSize = 14.sp,
                            color = iosGreen.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Alerte de succès (style iOS)
        AnimatedVisibility(
            visible = showSuccessAlert,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = iosGreen.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .widthIn(max = 320.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = successMessage,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}