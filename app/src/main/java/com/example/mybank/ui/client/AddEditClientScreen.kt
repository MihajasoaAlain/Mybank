package com.example.mybank.ui.client
// Correction de l'import
import ClientBancaire
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AddEditClientScreen(
    clientToEdit: ClientBancaire? = null,
    onSave: (ClientBancaire) -> Unit,
    onCancel: () -> Unit
) {
    val isEditMode = clientToEdit != null
    val title = if (isEditMode) "Modifier un client" else "Ajouter un client"

    var numCompte by remember { mutableStateOf(clientToEdit?.numCompte ?: "") }
    var nom by remember { mutableStateOf(clientToEdit?.nom ?: "") }
    var solde by remember { mutableStateOf((clientToEdit?.solde ?: 0.0).toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Formulaire
        TextField(
            value = numCompte,
            onValueChange = { numCompte = it },
            label = { Text("Numéro de compte") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            enabled = !isEditMode // Désactivé en mode édition
        )

        TextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom du client") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        TextField(
            value = solde,
            onValueChange = {
                // Validation pour n'accepter que des nombres
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                    solde = it
                }
            },
            label = { Text("Solde") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Boutons d'action
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Annuler")
            }

            Button(
                onClick = {
                    // Validation avant sauvegarde
                    if (numCompte.isNotBlank() && nom.isNotBlank() && solde.isNotBlank()) {
                        val client = ClientBancaire(
                            numCompte = numCompte,
                            nom = nom,
                            solde = solde.toDoubleOrNull() ?: 0.0
                        )
                        onSave(client)
                    }
                }
            ) {
                Text("Enregistrer")
            }
        }
    }
}