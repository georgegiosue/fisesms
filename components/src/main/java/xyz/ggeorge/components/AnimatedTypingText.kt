package xyz.ggeorge.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.delay

@Composable
fun AnimatedTypingText(text: String, fontSize: TextUnit, maxRepeatCount: Int, modifier: Modifier = Modifier) {
    var visibleText by remember { mutableStateOf("") }
    var visibleTextLength by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        repeat(maxRepeatCount) {
            repeat(text.length + 1) { index ->
                delay(50)
                visibleText = text.substring(0, index)
                visibleTextLength = index
            }
            delay(500)
        }
    }

    Text(
        text = visibleText,
        fontSize = fontSize,
        modifier = modifier,
    )
}
