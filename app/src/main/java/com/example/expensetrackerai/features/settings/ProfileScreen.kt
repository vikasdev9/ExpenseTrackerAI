package com.example.expensetrackerai.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetrackerai.core.ui.FintechCard

@Composable
fun ProfileScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.settingsState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ProfileHeader()
        }

        item {
            Text("Preferences", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        item {
            SettingsItem(
                icon = Icons.Default.CurrencyExchange,
                title = "Default Currency",
                subtitle = state.selectedCurrency,
                onClick = { /* Navigate to currency picker */ }
            )
        }

        item {
            SettingsToggleItem(
                icon = Icons.Default.DarkMode,
                title = "Dark Mode",
                isSelected = state.isDarkMode,
                onToggle = { viewModel.onThemeChange(it) }
            )
        }

        item {
            SettingsToggleItem(
                icon = Icons.Default.Fingerprint,
                title = "Biometric Lock",
                isSelected = state.isBiometricEnabled,
                onToggle = { /* Handle toggle */ }
            )
        }

        item {
            Text("Data \u0026 Security", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        item {
            SettingsItem(
                icon = Icons.Default.Backup,
                title = "Cloud Backup",
                subtitle = "Last sync: 2 hours ago",
                onClick = { /* Handle backup */ }
            )
        }

        item {
            SettingsItem(
                icon = Icons.Default.DeleteForever,
                title = "Delete All Data",
                subtitle = "This action cannot be undone",
                contentColor = MaterialTheme.colorScheme.error,
                onClick = { /* Show confirmation dialog */ }
            )
        }
    }
}

@Composable
fun ProfileHeader() {
    FintechCard {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(32.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Vikas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Premium Member", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    FintechCard(onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(icon, contentDescription = null, tint = contentColor)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, color = contentColor)
                if (subtitle != null) {
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    isSelected: Boolean,
    onToggle: (Boolean) -> Unit
) {
    FintechCard {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Switch(checked = isSelected, onCheckedChange = onToggle)
        }
    }
}
