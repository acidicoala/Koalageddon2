package acidicoala.koalageddon.start.ui

import acidicoala.koalageddon.BuildConfig
import acidicoala.koalageddon.core.model.LangString
import acidicoala.koalageddon.core.ui.composable.DefaultSpacer
import acidicoala.koalageddon.core.ui.composition.LocalStrings
import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import acidicoala.koalageddon.core.ui.theme.DefaultMaxWidth
import acidicoala.koalageddon.core.use_case.OpenResourceLink
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import java.net.URI

@Composable
fun LinkButton(
    label: String,
    url: String
) {
    val openResourceLink: OpenResourceLink by localDI().instance()

    OutlinedButton(
        modifier = Modifier.padding(vertical = 8.dp),
        onClick = {
            openResourceLink(URI.create(url))
        }
    ) {
        Icon(
            imageVector = Icons.Default.OpenInNew,
            contentDescription = null
        )

        DefaultSpacer()

        Text(label)
    }

}

@Composable
fun StartScreen() {
    val strings = LocalStrings.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = DefaultMaxWidth)
                .padding(DefaultContentPadding),
        ) {
            Text(
                text = LangString("%0" to BuildConfig.APP_VERSION) { strings.startPageWelcome }.text,
                style = MaterialTheme.typography.h5
            )

            Spacer(Modifier.height(64.dp))

            LinkButton(
                label = strings.openLatestReleasePage,
                url = "https://github.com/acidicoala/Koalageddon2/releases/latest",
            )

            LinkButton(
                label = strings.openOfficialForumTopic,
                url = "https://cs.rin.ru/forum/viewtopic.php?f=10&t=112021",
            )
        }
    }
}