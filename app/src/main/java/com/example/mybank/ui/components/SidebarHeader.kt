package com.example.mybank.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybank.api.RetrofitInstance
import com.example.mybank.ui.theme.BankColors

@Composable
 fun SidebarHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = "Bank Logo",
                tint = BankColors.primaryColor,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                "MyBank",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BankColors.primaryColor
            )
        }
    }
}
 suspend fun testBackendConnection() {
    try {
        val clients = RetrofitInstance.api.getClients()
        println("Connexion réussie, clients récupérés : $clients")
    } catch (e: Exception) {
        println("Échec de la connexion : ${e.message}")
    }
}