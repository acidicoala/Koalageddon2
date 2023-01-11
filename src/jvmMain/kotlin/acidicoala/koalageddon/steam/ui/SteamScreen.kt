package acidicoala.koalageddon.steam.ui

import acidicoala.koalageddon.core.ui.composable.DefaultSpacer
import acidicoala.koalageddon.core.ui.composable.VerticalScrollContainer
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import acidicoala.koalageddon.core.ui.theme.DefaultMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun SteamScreen() {
    val screenModel: SteamScreenModel by localDI().instance()
    val strings = LocalStrings.current

    // TODO: Fetch releases from https://api.github.com/repos/acidicoala/SmokeAPI/releases

    VerticalScrollContainer(
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = DefaultMaxWidth)
                .padding(DefaultContentPadding),
        ) {
            Button(onClick = screenModel::onReloadConfig) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = strings.reloadConfig)

                DefaultSpacer()

                Text(text = strings.reloadConfig)
            }
        }
    }

}
