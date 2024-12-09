package xyz.ggeorge.fisesms.framework.ui.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// REF: https://github.dev/ahuamana/CurvedLineChart-Compose
@Composable
fun LineChartFullCurved(
    dataPoints: List<Float>,
    times: List<String>,
    color: Color = Color(0xFF453DE0),
    height: Dp = 200.dp
) {
    // Validar que `dataPoints` y `times` tengan el mismo tamaño
    require(dataPoints.size == times.size) { "dataPoints and times must have the same size" }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val linePath = androidx.compose.ui.graphics.Path()
        val xStep = size.width / (dataPoints.size - 1)
        val yStep = size.height / (dataPoints.maxOrNull() ?: 1f)

        dataPoints.forEachIndexed { index, dataPoint ->
            val xPos = index * xStep
            val yPos = size.height - (dataPoint * yStep)

            if (index == 0) {
                linePath.moveTo(xPos, yPos)
            } else {
                val prevDataPoint = dataPoints[index - 1]
                val prevXPos = (index - 1) * xStep
                val prevYPos = size.height - (prevDataPoint * yStep)

                val controlX1 = prevXPos + (xPos - prevXPos) / 2
                val controlY1 = prevYPos
                val controlX2 = prevXPos + (xPos - prevXPos) / 2
                val controlY2 = yPos

                linePath.cubicTo(controlX1, controlY1, controlX2, controlY2, xPos, yPos)

                drawCircle(
                    color = color,
                    radius = 4.dp.toPx(),
                    center = androidx.compose.ui.geometry.Offset(xPos, yPos)
                )
            }

            // Dibujar el valor sobre cada punto
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "%.2f".format(dataPoint), // Formatear el valor
                    xPos,
                    yPos - 10.dp.toPx(), // Desplazar ligeramente el texto hacia arriba
                    android.graphics.Paint().apply {

                        textSize = 24f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }

        drawPath(
            path = linePath,
            color = color,
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = 4.dp.toPx()
            )
        )

        // Dibujar los tiempos en el eje X
        times.forEachIndexed { index, time ->
            val xPos = index * xStep

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    time,
                    xPos,
                    size.height + 20.dp.toPx(), // Desplazar el texto bajo el gráfico
                    android.graphics.Paint().apply {

                        textSize = 24f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    }
}
