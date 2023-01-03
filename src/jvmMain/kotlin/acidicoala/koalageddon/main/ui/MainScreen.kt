package acidicoala.koalageddon.main.ui

import acidicoala.koalageddon.main.model.MainTab
import acidicoala.koalageddon.feature.settings.ui.SettingsScreen
import acidicoala.koalageddon.feature.steam.ui.SteamScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedTab: MainTab by remember { mutableStateOf(MainTab.Settings) }

    val tabs = remember {
        listOf(
            MainTab.Steam,
            MainTab.Settings,
        )
    }

    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = tabs.indexOf(selectedTab)
            ) {
                tabs.forEach { tab ->
                    LeadingIconTab(
                        selected = tab == selectedTab,
                        text = { Text(text = tab.label(), color = MaterialTheme.colorScheme.onSurface) },
                        icon = tab.icon,
                        onClick = { selectedTab = tab }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            when (selectedTab) {
                MainTab.Steam -> SteamScreen()
                MainTab.Settings -> SettingsScreen()
            }
        }
    }
}