package xyz.ggeorge.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MultipleCardIndicator(
    modifier: Modifier = Modifier
        .padding(4.dp)
        .clip(RoundedCornerShape(8.dp)),
    title: String,
    subtitle: String,
    data: List<Pair<String, Color>>,
    onClick: () -> Unit,
    backgroundColor: Color
) {
    Card(modifier = modifier, backgroundColor = backgroundColor, border = BorderStroke(color = Color.Transparent, width = 0.dp), elevation = 0.dp) {
        Column {

            Row(
                modifier
                    .padding(top = 8.dp).align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically) {
                data.map { value ->
                    Text(text = value.first, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = value.second)
                }
            }

            Row(
                modifier
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column {
                    Text(text = subtitle, fontWeight = FontWeight.ExtraLight, fontSize = 12.sp)
                    Text(text = title, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                }

                IconButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            backgroundColor.copy(
                                red = (backgroundColor.red * 0.95f).coerceIn(0f, 1f),
                                green = (backgroundColor.green * 0.95f).coerceIn(0f, 1f),
                                blue = (backgroundColor.blue * 0.95f).coerceIn(0f, 1f)
                            )
                        ),
                    onClick = onClick) {
                    Icon(Icons.Rounded.ArrowForward, contentDescription = "forward")
                }
            }
        }
    }
}
