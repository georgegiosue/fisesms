package xyz.ggeorge.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

// TODO: Will be remove
@Composable
fun OnReload(value: String) {
    Text(
        text = value,
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth(),
        textAlign = TextAlign.End
    )
}