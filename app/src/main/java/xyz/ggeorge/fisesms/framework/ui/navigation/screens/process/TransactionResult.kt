package xyz.ggeorge.fisesms.framework.ui.navigation.screens.process

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.ggeorge.core.domain.Fise
import xyz.ggeorge.core.domain.exceptions.FiseError
import xyz.ggeorge.core.domain.state.FiseState
import xyz.ggeorge.core.domain.state.ProcessState

data class TransactionResultColors(
    val successColor: Color,
    val warningColor: Color,
    val errorColor: Color
)

@Composable
fun TransactionResult(
    processState: ProcessState,
    fise: Fise,
    lastFiseSent: Fise.ToSend?,
    fiseError: FiseError,
    accentColors: TransactionResultColors,
    modifier: Modifier = Modifier
) {
    when (processState) {
        ProcessState.INITIAL -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sin transacciones",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.alpha(0.35f),
                    textAlign = TextAlign.Center
                )
            }
        }

        ProcessState.PROCESSING_COUPON -> {
            ProcessingIndicator(modifier = modifier)
        }

        ProcessState.COUPON_RECEIVED -> {
            var visible by remember(processState, fise) { mutableStateOf(false) }
            LaunchedEffect(processState, fise) {
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(400)) + slideInVertically(tween(300))
            ) {
                when (fise.state) {
                    FiseState.PROCESSED -> {
                        val processed = fise as Fise.Processed
                        ResultRow(
                            icon = Icons.Rounded.CheckCircle,
                            accentColor = accentColors.successColor,
                            title = "Vale Procesado",
                            detail = "Vale: ${processed.code}\nDNI: ${processed.dni} · S/${processed.amount}",
                            modifier = modifier
                        )
                    }

                    FiseState.PREVIOUSLY_PROCESSED -> {
                        val prev = fise as Fise.PreviouslyProcessed
                        ResultRow(
                            icon = Icons.Rounded.Warning,
                            accentColor = accentColors.warningColor,
                            title = "Procesado anteriormente",
                            detail = "Vale: ${lastFiseSent?.code ?: "N/A"}\nAgente: ${prev.agentDNI}",
                            modifier = modifier
                        )
                    }

                    FiseState.WRONG -> {
                        ResultRow(
                            icon = Icons.Rounded.Cancel,
                            accentColor = accentColors.errorColor,
                            title = "Datos incorrectos",
                            detail = "Verifique que los datos estén correctos",
                            modifier = modifier
                        )
                    }

                    FiseState.SYNTAX_ERROR -> {
                        ResultRow(
                            icon = Icons.Rounded.Cancel,
                            accentColor = accentColors.errorColor,
                            title = "Error de sintaxis",
                            detail = "Verifique el DNI y/o código de vale",
                            modifier = modifier
                        )
                    }

                    else -> {}
                }
            }
        }

        ProcessState.ERROR_PROCESSING_COUPON -> {
            var visible by remember(processState, fiseError) { mutableStateOf(false) }
            LaunchedEffect(processState, fiseError) {
                visible = true
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(400)) + slideInVertically(tween(300))
            ) {
                ResultRow(
                    icon = Icons.Rounded.Cancel,
                    accentColor = accentColors.errorColor,
                    title = "Error al procesar",
                    detail = fiseError.message,
                    modifier = modifier
                )
            }
        }

        else -> {}
    }
}

@Composable
private fun ProcessingIndicator(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "processing")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                strokeWidth = 3.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Procesando vale",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.graphicsLayer(alpha = pulse)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Esperando respuesta del servicio...",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.alpha(0.5f)
                )
            }
        }
    }
}

@Composable
private fun ResultRow(
    icon: ImageVector,
    accentColor: Color,
    title: String,
    detail: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = accentColor.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.alpha(0.6f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}
