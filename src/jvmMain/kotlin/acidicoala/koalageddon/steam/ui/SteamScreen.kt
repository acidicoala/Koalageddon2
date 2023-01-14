package acidicoala.koalageddon.steam.ui

import acidicoala.koalageddon.core.ui.composable.PlatformScreen
import androidx.compose.runtime.Composable

@Composable
fun SteamScreen() {
    PlatformScreen(
        storeTab = { SteamStoreScreen() },
        gameTab = { SteamGameScreen() }
    )
}