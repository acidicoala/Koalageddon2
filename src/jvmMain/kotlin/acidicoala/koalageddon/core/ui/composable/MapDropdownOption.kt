package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.model.ILangString
import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <K : Comparable<K>, V : ILangString> MapDropdownOption(
    label: String,
    keyLabel: String,
    map: Map<K, V>,
    defaultEntry: Pair<K, V>,
    validValues: Array<V>,
    keyboardType: KeyboardType,
    keyMapper: (String) -> K?,
    onMapChange: (Map<K, V>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val arrowDegrees by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    val keyValuePairs = remember {
        map.entries
            .map { it.toPair() }
            .toMutableStateList()
    }

    fun onChange(transform: SnapshotStateList<Pair<K, V>>.() -> Unit) {
        keyValuePairs.apply(transform)
        onMapChange(keyValuePairs.toMap())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onBackground.copy(alpha = .5f),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(DefaultContentPadding)
        ) {
            Text(text = label)

            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                modifier = Modifier.rotate(arrowDegrees)
            )
        }

        AnimatedContent(targetState = expanded) { targetExpanded ->
            if (targetExpanded) {
                Column {
                    keyValuePairs.forEachIndexed { index, (key, value) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            IconButton(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                onClick = {
                                    onChange {
                                        removeAt(index)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )
                            }

                            OutlinedTextField(
                                value = "$key",
                                singleLine = true,
                                label = { Text(text = keyLabel) },
                                modifier = Modifier.widthIn(min = 64.dp).weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                                onValueChange = {
                                    val newKey = keyMapper(it) ?: return@OutlinedTextField
                                    onChange {
                                        set(index, newKey to value)
                                    }
                                }
                            )

                            DropdownButton(
                                selected = value,
                                items = validValues,
                                modifier = Modifier.padding(horizontal = DefaultContentPadding),
                                onSelect = {
                                    onChange {
                                        set(index, key to it)
                                    }
                                }
                            )
                        }
                    }

                    Row {
                        IconButton(
                            modifier = Modifier.padding(8.dp),
                            onClick = {
                                onChange {
                                    if (!contains(defaultEntry)) {
                                        add(defaultEntry)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}