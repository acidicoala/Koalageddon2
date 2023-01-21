package acidicoala.koalageddon.home.ui

import acidicoala.koalageddon.core.event.CoreEvent
import acidicoala.koalageddon.core.ui.composable.DevelopmentPlaceholder
import acidicoala.koalageddon.core.ui.theme.DefaultIconSize
import acidicoala.koalageddon.core.ui.theme.DefaultMaxWidth
import acidicoala.koalageddon.home.model.HomeTab
import acidicoala.koalageddon.settings.ui.SettingsScreen
import acidicoala.koalageddon.start.ui.StartScreen
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

sealed interface VerticalTabElement {
    class Tab(val tab: HomeTab) : VerticalTabElement
    object Spacer : VerticalTabElement
}

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

    var selectedTab: HomeTab by remember { mutableStateOf(HomeTab.Start) }

    val homeTabs = remember {
        HomeTab.values().filter { it.store?.installed ?: true }.sortedBy(HomeTab::priority).map(VerticalTabElement::Tab)
            .toMutableList<VerticalTabElement>().apply {
                add(2, VerticalTabElement.Spacer)
            }
    }

    Scaffold(
        content = { paddingValues ->
            Row(Modifier.padding(paddingValues)) {
                Column(Modifier.width(256.dp)) {
                    homeTabs.forEachIndexed { index, element ->
                        val modifier = when (element) {
                            is VerticalTabElement.Spacer -> Modifier.weight(1f)
                            is VerticalTabElement.Tab -> Modifier
                        }
                        Box(modifier) {
                            when (element) {
                                is VerticalTabElement.Tab -> {
                                    val tab = element.tab

                                    LeadingIconTab(selected = tab == selectedTab,
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
                                                text = tab.label(), color = MaterialTheme.colors.onSurface
                                            )
                                        })
                                }
                                else -> Unit
                            }

                            if (index < homeTabs.size - 1) {
                                Divider(Modifier.align(Alignment.BottomCenter))
                            }
                        }
                    }
                }

                Divider(Modifier.fillMaxHeight().width(1.dp))

                Box(Modifier.fillMaxSize()) {
                    Crossfade(selectedTab) { tab ->
                        when (tab) {
                            HomeTab.Start -> StartScreen()
                            HomeTab.Steam -> SteamScreen()
                            HomeTab.Settings -> SettingsScreen()
                            HomeTab.Epic -> DevelopmentPlaceholder()
                            HomeTab.Ubisoft -> DevelopmentPlaceholder()
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