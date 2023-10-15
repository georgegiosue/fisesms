package xyz.ggeorge.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    title: String,
    timer: String,
    color: Color?
) {

    val cardColor = color ?: MaterialTheme.colorScheme.surfaceVariant

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
                    containerColor = cardColor,
        ),
        border = BorderStroke(color = Color.Transparent, width = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 4.dp),
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                softWrap = true,
            )

            Text(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 4.dp),
                text = timer,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                softWrap = true,
            )
        }
    }
}