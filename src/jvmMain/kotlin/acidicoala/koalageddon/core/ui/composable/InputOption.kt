package acidicoala.koalageddon.core.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputOption(
    label: String,
    value: String = "",
    placeholder: String,
    onClick: (String) -> Unit,
    enabled: Boolean = true,
    buttonLabel: String = "Save"
) {
    ControlOption(label) {
        var text by remember { mutableStateOf(value) }

        val color = MaterialTheme.colors.onSurface

        BasicTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .drawBehind {
                            drawLine(
                                color = color,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                        .padding(horizontal = 8.dp, vertical = 8.dp), // inner padding
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.LightGray,
                        )
                    }
                    innerTextField(
                    )
                }
            }
        )
        DefaultSpacer()
        Button(onClick = {
                         onClick(text)
        }, enabled = enabled) {
            Text(text = buttonLabel)
        }

    }
}