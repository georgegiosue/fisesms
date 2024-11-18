package xyz.ggeorge.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun AnimatedFadeInText(
    text: String,
    fontSize: TextUnit,
    maxRepeatCount: Int,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        repeat(maxRepeatCount) {
            isVisible = true
            delay(text.length * 310L)
            isVisible = false
            delay(text.length * 280L)
        }
    }

    Row(modifier = modifier) {
        text.forEachIndexed { index, char ->
            val alpha by animateFloatAsState(
                targetValue = if (isVisible) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = index * 100,
                    easing = FastOutSlowInEasing
                ),
                label = "alpha-$index"
            )
            
            Text(
                text = char.toString(),
                style = TextStyle(fontSize = fontSize, lineHeight = 24.sp),
                fontSize = fontSize,
                modifier = Modifier.graphicsLayer(alpha = alpha)
            )
        }
    }
}

