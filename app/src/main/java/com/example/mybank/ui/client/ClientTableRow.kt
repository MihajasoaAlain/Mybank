package com.example.mybank.ui.client

import ClientBancaire
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties

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
            text = client.numCompte.toString(),
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