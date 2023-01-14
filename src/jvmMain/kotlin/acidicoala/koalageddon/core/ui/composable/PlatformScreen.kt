package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.DefaultIconSize
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Store
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector

private enum class PlatformTab(
    val label: @Composable () -> String,
    val icon: ImageVector
) {
    Store(
        label = { LocalStrings.current.storeMode },
        icon = Icons.Default.Store,
    ),

    Game(
        label = { LocalStrings.current.gameMode },
        icon = Icons.Default.SportsEsports,
    );
}

@Composable
fun PlatformScreen(
    storeTab: @Composable () -> Unit,
    gameTab: @Composable () -> Unit,
) {
    val tabs = remember { PlatformTab.values() }
    var selectedTab: PlatformTab by remember { mutableStateOf(PlatformTab.Store) }

    Scaffold(
        topBar = {
            TabRow(
                selectedTabIndex = tabs.indexOf(selectedTab)
            ) {
                PlatformTab.values().forEach { tab ->
                    LeadingIconTab(selected = tab == selectedTab,
                        onClick = { selectedTab = tab },
                        icon = {
                            Image(
                                imageVector = tab.icon,
                                contentDescription = tab.label(),
                                colorFilter = ColorFilter.tint(LocalContentColor.current),
                                alpha = LocalContentAlpha.current,
                                modifier = Modifier.size(DefaultIconSize)
                            )
                        },
                        text = {
                            Text(
                                text = tab.label(),
                            )
                        }
                    )
                }
            }
        },
        content = { paddingValues ->
            Crossfade(targetState = selectedTab, modifier = Modifier.padding(paddingValues)) { tab ->
                when (tab) {
                    PlatformTab.Store -> storeTab()
                    PlatformTab.Game -> gameTab()
                }
            }
        }
    )
}