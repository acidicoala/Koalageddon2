package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DefaultSpacer() {
    Spacer(Modifier.size(DefaultContentPadding))
}