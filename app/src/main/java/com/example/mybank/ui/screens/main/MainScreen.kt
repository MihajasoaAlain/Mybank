package com.example.mybank.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybank.ui.theme.BankColors
import com.example.mybank.ui.theme.MyBankTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // États
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedMenuItem by remember { mutableStateOf(0) }

    // Données
    val menuItems = getSidebarMenuItems()
    val contentScreens = getContentScreens()

    // Structure principale avec DrawerLayout
    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SidebarContent(
                selectedMenuItem = selectedMenuItem,
                onMenuItemSelected = {
                    selectedMenuItem = it
                    // Fermer le drawer après sélection sur petit écran
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainTopBar(
    title: String,
    onMenuClick: () -> Unit
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BankColors.primaryColor,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SidebarContent(
    selectedMenuItem: Int,
    onMenuItemSelected: (Int) -> Unit
) {
    val menuItems = getSidebarMenuItems()

    DismissibleDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.surface) {
        Spacer(modifier = Modifier.height(16.dp))

        // En-tête de la sidebar
        SidebarHeader()

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Éléments du menu
        SidebarMenuItems(
            menuItems = menuItems,
            selectedMenuItem = selectedMenuItem,
            onMenuItemSelected = onMenuItemSelected
        )

        Spacer(modifier = Modifier.weight(1f))

        // Pied de page avec bouton de déconnexion
        SidebarFooter()

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SidebarHeader() {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SidebarMenuItems(
    menuItems: List<MenuItem>,
    selectedMenuItem: Int,
    onMenuItemSelected: (Int) -> Unit
) {
    menuItems.forEachIndexed { index, item ->
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = if (selectedMenuItem == index) BankColors.accentColor else MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            label = {
                Text(
                    text = item.title,
                    fontWeight = if (selectedMenuItem == index) FontWeight.Bold else FontWeight.Normal
                )
            },
            selected = selectedMenuItem == index,
            onClick = { onMenuItemSelected(index) },
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = BankColors.accentColor.copy(alpha = 0.1f),
                unselectedContainerColor = MaterialTheme.colorScheme.surface,
                selectedTextColor = BankColors.accentColor,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SidebarFooter() {
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Déconnexion",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        label = { Text("Déconnexion") },
        selected = false,
        onClick = { /* Action de déconnexion */ },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = MaterialTheme.colorScheme.surface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyBankTheme {
        MainScreen()
    }
}