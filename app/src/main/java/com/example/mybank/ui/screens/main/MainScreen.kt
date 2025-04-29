package com.example.mybank.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mybank.ui.components.MainTopBar
import com.example.mybank.ui.components.SidebarContent
import com.example.mybank.ui.theme.BankColors
import com.example.mybank.ui.theme.MyBankTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedMenuItem by remember { mutableStateOf(0) }
    val menuItems = getSidebarMenuItems()
    val contentScreens = getContentScreens()
    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SidebarContent(
                selectedMenuItem = selectedMenuItem,
                onMenuItemSelected = {
                    selectedMenuItem = it
                    scope.launch {
                        drawerState.close()
                    }
                }
            )
        }
    ) {
        // Scaffold pour le contenu principal
        Scaffold(
            topBar = {
                MainTopBar(
                    title = menuItems[selectedMenuItem].title,
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }
                )
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(BankColors.backgroundColor)
                ) {
                    contentScreens[selectedMenuItem]()
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyBankTheme {
        MainScreen()
    }
}