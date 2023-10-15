package xyz.ggeorge.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun Header(header: Pair<String, String>) {
    Text(
        text = header.first,
        fontSize = 31.sp,
        fontWeight = FontWeight.SemiBold
       )
    Text(
        text = header.second,
        fontSize = 16.sp,
        fontWeight = FontWeight.Light
    )
}