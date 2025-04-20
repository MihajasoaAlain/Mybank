
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybank.ui.theme.BankColors

@Composable
fun ClientListScreen(repository: ClientRepository, onEditClient: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Liste des Clients",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Liste des clients
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // En-tête du tableau
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BankColors.primaryColor.copy(alpha = 0.1f))
                    .padding(8.dp)
            ) {
                Text(
                    text = "Numéro",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Nom",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1.5f)
                )
                Text(
                    text = "Solde",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Catégorie",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(80.dp))
            }

            // Corps du tableau
            repository.clients.forEachIndexed { index, client ->
                ClientRow(
                    client = client,
                    onEdit = { onEditClient(index) },
                    onDelete = { repository.supprimerClient(index) }
                )
            }
        }

        // Statistiques en bas du tableau
        StatistiquesBas(repository)
    }
}

@Composable
fun ClientRow(
    client: ClientBancaire,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val backgroundColor = when(client.getCategorieSolde()) {
        "insuffisant" -> Color.Red.copy(alpha = 0.1f)
        "moyen" -> Color.Yellow.copy(alpha = 0.1f)
        else -> Color.Green.copy(alpha = 0.1f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = client.numCompte,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = client.nom,
            modifier = Modifier.weight(1.5f)
        )
        Text(
            text = String.format("%.2f €", client.solde),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = client.getCategorieSolde(),
            modifier = Modifier.weight(1f)
        )
        Row(modifier = Modifier.width(80.dp)) {
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Modifier",
                    tint = BankColors.primaryColor
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun StatistiquesBas(repository: ClientRepository) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(16.dp)
    ) {
        Text(
            text = "Statistiques",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatItem("Solde Total", repository.getSoldeTotal())
            StatItem("Solde Minimal", repository.getSoldeMinimal())
            StatItem("Solde Maximal", repository.getSoldeMaximal())
        }
    }
}

@Composable
fun StatItem(label: String, value: Double) {
    Column {
        Text(text = label, fontWeight = FontWeight.Medium)
        Text(
            text = String.format("%.2f €", value),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}