package com.example.mybank.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mybank.ui.screens.main.MenuItem
import com.example.mybank.ui.theme.BankColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun SidebarMenuItems(
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