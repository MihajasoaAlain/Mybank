package com.example.mybank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.mybank.ui.navigation.BankAppNavHost
import com.example.mybank.ui.theme.MyBankTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyBankTheme {
                MyBankApp()
            }
        }
    }
}

@Composable
fun MyBankApp() {
    val navController = rememberNavController()
    BankAppNavHost(navController = navController)
}