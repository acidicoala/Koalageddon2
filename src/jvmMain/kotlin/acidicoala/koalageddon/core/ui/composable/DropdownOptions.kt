package acidicoala.koalageddon.core.ui.composable

import acidicoala.koalageddon.core.model.ILangString
import acidicoala.koalageddon.core.ui.theme.DefaultContentPadding
import androidx.compose.animation.*
import androidx.compose.animation.AnimatedContentScope.SlideDirection
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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

@Composable
fun IntListOption(
    resetKey: Any?,
    label: String,
    itemLabel: String,
    list: List<Int>,
    onListChange: (List<Int>) -> Unit,
    defaultValue: Int = 0
) {
    val listItems = remember(resetKey) { list.toMutableStateList() }

    fun onChange(transform: SnapshotStateList<Int>.() -> Unit) {
        listItems.apply(transform)
        onListChange(listItems.toList())
    }

    ExpandableOption(
        header = { Text(text = label) }
    ) {
        Column {
            listItems.forEachIndexed { index, value ->
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

                    OptionTextField(
                        label = itemLabel,
                        keyboardType = KeyboardType.Number,
                        value = "$value",
                        onValueChange = {
                            val newValue = mapStringToInt(it) ?: return@OptionTextField
                            onChange {
                                set(index, newValue.toInt())
                            }
                        }
                    )

                    Spacer(Modifier.size(DefaultContentPadding))
                }
            }

            NewEntryRow(
                onClick = {
                    onChange {
                        if (!contains(defaultValue)) {
                            add(defaultValue)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun <V : ILangString> IntMapDropdownOption(
    resetKey: Any?,
    label: String,
    keyLabel: String,
    map: Map<String, V>,
    defaultValue: V,
    validValues: Array<V>,
    onMapChange: (Map<String, V>) -> Unit
) {
    MapDropdownOption(
        resetKey = resetKey,
        label = label,
        keyLabel = keyLabel,
        map = map,
        defaultEntry = "0" to defaultValue,
        validValues = validValues,
        keyboardType = KeyboardType.Number,
        keyMapper = ::mapStringToInt,
        onMapChange = onMapChange
    )
}

@Composable
private fun <V : ILangString> MapDropdownOption(
    resetKey: Any?,
    label: String,
    keyLabel: String,
    map: Map<String, V>,
    defaultEntry: Pair<String, V>,
    validValues: Array<V>,
    keyboardType: KeyboardType,
    keyMapper: (String) -> String?,
    onMapChange: (Map<String, V>) -> Unit
) {
    val keyValuePairs = remember(resetKey) {
        map.entries
            .map { it.toPair() }
            .toMutableStateList()
    }

    fun onChange(transform: SnapshotStateList<Pair<String, V>>.() -> Unit) {
        keyValuePairs.apply(transform)
        onMapChange(keyValuePairs.toMap())
    }

    ExpandableOption(
        header = { Text(text = label) }
    ) {
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

                    OptionTextField(
                        label = keyLabel,
                        value = key,
                        keyboardType = keyboardType,
                        onValueChange = {
                            val newKey = keyMapper(it) ?: return@OptionTextField
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

            NewEntryRow(
                onClick = {
                    onChange {
                        if (!contains(defaultEntry)) {
                            add(defaultEntry)
                        }
                    }
                }
            )
        }
    }
}


@Composable
private fun NewEntryRow(onClick: () -> Unit) {
    Row {
        IconButton(
            modifier = Modifier.padding(8.dp),
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ExpandableOption(
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val arrowDegrees by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colors.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onBackground.copy(alpha = .5f),
                shape = MaterialTheme.shapes.medium
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
            Box(Modifier.weight(1f)) {
                header()
            }

            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                modifier = Modifier.rotate(arrowDegrees)
            )
        }

        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                fadeIn() + slideIntoContainer(SlideDirection.Down) with
                        slideOutOfContainer(SlideDirection.Up) + fadeOut()
            }
        ) { targetExpanded ->
            if (targetExpanded) {
                content()
            }
        }
    }
}

@Composable
private fun RowScope.OptionTextField(
    label: String,
    keyboardType: KeyboardType,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        singleLine = true,
        label = { Text(text = label) },
        modifier = Modifier.widthIn(min = 64.dp).weight(1f),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        onValueChange = onValueChange
    )
}

private fun mapStringToInt(string: String) = if (string.isBlank()) "0" else string.toIntOrNull()?.toString()
