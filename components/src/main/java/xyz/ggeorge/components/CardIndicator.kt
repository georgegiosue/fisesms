package xyz.ggeorge.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardIndicator(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    data: String,
    onSuspense: Boolean = false,
    fallback: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp)),
        border = BorderStroke(color = Color.Transparent, width = 0.dp)
    ) {
        Column(modifier = Modifier.padding(4.dp)) {

            if (data.isNotEmpty() || onSuspense) {
                if (onSuspense) {
                    fallback()
                } else {
                    Text(
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                        text = data,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        softWrap = true
                    )
                }

            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = subtitle,
                        fontWeight = FontWeight.ExtraLight,
                        fontSize = 12.sp
                    )
                }

                IconButton(
                    modifier = Modifier.clip(CircleShape), onClick = onClick

                ) {
                    icon()
                }
            }
        }
    }
}