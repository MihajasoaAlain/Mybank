package com.example.mybank.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // État pour suivre l'élément de menu sélectionné
    var selectedMenuItem by remember { mutableStateOf(0) }

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SidebarContent(
                selectedMenuItem = selectedMenuItem,
                onMenuItemSelected = { selectedMenuItem = it }
            )
        }
    ) {
        MainContent(
            drawerState = drawerState,
            selectedMenuItem = selectedMenuItem
        )
    }
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

        Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
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
                imageVector = Icons.Default.Logout,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    drawerState: DrawerState,
    selectedMenuItem: Int
) {
    val menuItems = getSidebarMenuItems()
    val contentScreens = getContentScreens()

    Scaffold(
        topBar = {
            MainTopAppBar(
                title = menuItems[selectedMenuItem].title,
                drawerState = drawerState
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BankColors.backgroundColor)
        ) {
            contentScreens[selectedMenuItem]()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(title: String, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    if (drawerState.isClosed) {
                        drawerState.open()
                    } else {
                        drawerState.close()
                    }
                }
            }) {
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

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyBankTheme {
        MainScreen()
    }
}