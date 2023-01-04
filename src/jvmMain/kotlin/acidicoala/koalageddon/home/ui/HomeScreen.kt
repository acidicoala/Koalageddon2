package acidicoala.koalageddon.home.ui

import acidicoala.koalageddon.core.event.CoreEvent
import acidicoala.koalageddon.feature.settings.ui.SettingsScreen
import acidicoala.koalageddon.feature.steam.ui.SteamScreen
import acidicoala.koalageddon.home.model.HomeTab
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var selectedTab: HomeTab by remember { mutableStateOf(HomeTab.Settings) }

    val tabs = HomeTab.values()

    // Handle core events
    val scope = rememberCoroutineScope()
    val snackbarState = remember { SnackbarHostState() }
    val coreEventFlow: MutableSharedFlow<CoreEvent> by localDI().instance()
    scope.launch {
        coreEventFlow.collect { event ->
            when (event) {
                is CoreEvent.ShowSnackbar -> snackbarState.showSnackbar(event.snackbarVisuals)
            }
        }
    }

    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = tabs.indexOf(selectedTab)
            ) {
                tabs.forEach { tab ->
                    LeadingIconTab(
                        selected = tab == selectedTab,
                        onClick = { selectedTab = tab },
                        icon = tab.icon,
                        text = {
                            Text(
                                text = tab.label(),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
            }
        },
        content = { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                when (selectedTab) {
                    HomeTab.Steam -> SteamScreen()
                    HomeTab.Settings -> SettingsScreen()
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarState)
        },
    )
}