package acidicoala.koalageddon.home.ui

import acidicoala.koalageddon.core.event.CoreEvent
import acidicoala.koalageddon.core.ui.composition.LocalSettings
import acidicoala.koalageddon.core.ui.theme.DefaultIconSize
import acidicoala.koalageddon.core.ui.theme.DefaultMaxWidth
import acidicoala.koalageddon.core.model.Settings
import acidicoala.koalageddon.settings.domain.use_case.SaveSettings
import acidicoala.koalageddon.settings.ui.SettingsScreen
import acidicoala.koalageddon.steam.ui.SteamScreen
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun HomeScreen() {
    // Handle core events
    // Handle them in a screen model
    val snackbarState = remember { SnackbarHostState() }
    val coreEventFlow: MutableSharedFlow<CoreEvent> by localDI().instance()
    val appScope: CoroutineScope by localDI().instance()

    LaunchedEffect(appScope) {
        appScope.launch {
            coreEventFlow.collect { event ->
                when (event) {
                    is CoreEvent.ShowSnackbar -> snackbarState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel,
                        duration = event.duration
                    )
                }
            }
        }
    }

    val settings = LocalSettings.current
    var selectedTab: Settings.HomeTab by remember { mutableStateOf(settings.lastHomeTab) }
    val saveSettings: SaveSettings by localDI().instance()
    LaunchedEffect(selectedTab) {
        saveSettings { copy(lastHomeTab = selectedTab) }
    }

    Scaffold(
        content = { paddingValues ->
            Row(Modifier.padding(paddingValues)) {
                Column(Modifier.width(256.dp)) {
                    Settings.HomeTab.values().forEachIndexed { index, tab ->
                        Box {
                            LeadingIconTab(
                                selected = tab == selectedTab,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { selectedTab = tab },
                                icon = {
                                    Image(
                                        painter = tab.painter(),
                                        contentDescription = tab.label(),
                                        modifier = Modifier.size(DefaultIconSize),
                                    )
                                },
                                text = {
                                    Text(
                                        text = tab.label(),
                                        color = MaterialTheme.colors.onSurface
                                    )
                                }
                            )

                            if (index == 0) {
                                Divider(Modifier.align(Alignment.BottomCenter))
                            }

                            if (index != 0) {
                                Divider(Modifier.align(Alignment.TopCenter))
                            }
                        }

                        if (index == 0) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }

                Divider(Modifier.fillMaxHeight().width(1.dp))

                Box(Modifier.fillMaxSize()) {
                    Crossfade(selectedTab) { tab ->
                        when (tab) {
                            Settings.HomeTab.Steam -> SteamScreen()
                            Settings.HomeTab.Settings -> SettingsScreen()
                        }
                    }
                }
            }
        },
        snackbarHost = {
            Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxWidth()) {
                SnackbarHost(snackbarState, modifier = Modifier.widthIn(max = DefaultMaxWidth))
            }
        },
    )
}