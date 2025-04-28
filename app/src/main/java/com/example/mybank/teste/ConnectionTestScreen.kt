package com.example.mybank.teste

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybank.api.RetrofitInstance
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionTestScreen() {
    val scope = rememberCoroutineScope()
    var connectionResult by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Test de Connexion") }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            try {
                                val clients = RetrofitInstance.api.getClients()
                                connectionResult = "Connexion réussie ! ${clients.size} clients récupérés."
                            } catch (e: Exception) {
                                connectionResult = "Erreur de connexion : ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                ) {
                    Text("Tester la connexion")
                }

                Spacer(modifier = Modifier.height(24.dp))

                when {
                    isLoading -> {
                        CircularProgressIndicator()
                    }
                    connectionResult != null -> {
                        Text(
                            text = connectionResult ?: "",
                            color = if (connectionResult?.startsWith("Connexion") == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ConnectionTestScreenPreview() {
    ConnectionTestScreen()
}
