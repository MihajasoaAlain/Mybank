package com.example.mybank.ui.client

import ClientBancaire
import ClientRepository
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatistiquesScreen(repository: ClientRepository) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Statistiques des Clients",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Affichage des statistiques textuelles
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Résumé des comptes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                StatsRow("Nombre de clients", repository.clients.size.toString())
                StatsRow("Solde total", String.format("%.2f €", repository.getSoldeTotal()))
                StatsRow("Solde minimal", String.format("%.2f €", repository.getSoldeMinimal()))
                StatsRow("Solde maximal", String.format("%.2f €", repository.getSoldeMaximal()))
                StatsRow("Solde moyen", String.format("%.2f €", repository.getSoldeTotal() / repository.clients.size))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visualisation graphique (histogramme)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Répartition des soldes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Composant graphique simple avec Compose
                BarChart(repository.clients)
            }
        }
    }
}

@Composable
fun StatsRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = value, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BarChart(clients: List<ClientBancaire>) {
    if (clients.isEmpty()) {
        Text(text = "Aucune donnée disponible")
        return
    }

    val maxSolde = clients.maxOf { it.solde } * 1.1f // Ajout de 10% pour la marge

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 40.dp, end = 8.dp, top = 8.dp, bottom = 32.dp)
    ) {
        // Axe Y
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(Color.Gray)
                .align(Alignment.CenterStart)
        )

        // Axe X
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray)
                .align(Alignment.BottomCenter)
        )

        // Barres du graphique
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            clients.forEach { client ->
                val barHeight = (client.solde / maxSolde * 100).toFloat()

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .fillMaxHeight(barHeight / 100f)
                            .background(
                                when(client.getCategorieSolde()) {
                                    "insuffisant" -> Color.Red.copy(alpha = 0.7f)
                                    "moyen" -> Color(0xFFFFA000).copy(alpha = 0.7f)
                                    else -> Color.Green.copy(alpha = 0.7f)
                                }
                            )
                    )
                    Text(
                        text = client.nom.split(" ")[0],
                        fontSize = 10.sp,
                        maxLines = 1,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Légende de l'axe Y (valeurs de solde)
        val steps = 5
        for (i in 0..steps) {
            val value = maxSolde.toDouble() * i / steps
            val yPosition = Modifier
                .align(Alignment.CenterStart)
                .offset(y = -(i.toFloat() / steps * 100).dp)

            Box(modifier = yPosition) {
                Text(
                    text = String.format("%.0f €", value),
                    fontSize = 10.sp,
                    modifier = Modifier.offset(x = (-35).dp)
                )
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .height(1.dp)
                        .background(Color.Gray)
                        .offset(x = (-3).dp)
                )
            }
        }
    }
}