package com.example.mybank.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mybank.ui.screens.main.getSidebarMenuItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun SidebarContent(
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

        SidebarFooter()

        Spacer(modifier = Modifier.height(24.dp))
    }
}