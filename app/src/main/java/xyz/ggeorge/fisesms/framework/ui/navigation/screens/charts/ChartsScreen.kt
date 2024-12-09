package xyz.ggeorge.fisesms.framework.ui.navigation.screens.charts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.ggeorge.fisesms.framework.ui.charts.LineChartFullCurved
import xyz.ggeorge.fisesms.framework.ui.viewmodels.ChartsViewModel

@Composable
fun ChartsScreen(modifier: Modifier = Modifier, vm: ChartsViewModel) {

    val scroll = rememberScrollState()

    val fiseData = vm.getFiseData().collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scroll)
    ) {
        Text(
            text = "Graficos",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        val dataPoints = fiseData.value.map { it.amount!!.toFloat() }
        val times = fiseData.value.map { it.processedTimestamp }

        LineChartFullCurved(dataPoints = dataPoints, times = times)

    }
}