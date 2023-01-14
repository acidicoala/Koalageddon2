package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.ui.composition.LocalStrings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun DevelopmentPlaceholder() {
    val strings = LocalStrings.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🚧", fontSize = 128.sp)

        Text(strings.inDevelopment, fontSize = 24.sp)
    }
}