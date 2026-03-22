package xyz.ggeorge.fisesms.framework.ui.navigation.screens.process

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.ggeorge.components.FormFiseComponent
import xyz.ggeorge.components.camera.RealtimeVision
import xyz.ggeorge.core.domain.Fise
import xyz.ggeorge.core.domain.events.AppEvent
import xyz.ggeorge.core.domain.state.BalanceState
import xyz.ggeorge.core.domain.state.ProcessState
import xyz.ggeorge.fisesms.framework.ui.viewmodels.FiseViewModel
import xyz.ggeorge.fisesms.framework.ui.viewmodels.SettingsViewModel
import xyz.ggeorge.theme.FiseSuccessfullColorDark
import xyz.ggeorge.theme.FiseSuccessfullColorLight
import xyz.ggeorge.theme.FiseWasProcessedColorDark
import xyz.ggeorge.theme.FiseWasProcessedColorLight
import xyz.ggeorge.theme.FiseWrongColorDark
import xyz.ggeorge.theme.FiseWrongColorLight

@Composable
fun ProcessScreen(
    vm: FiseViewModel,
    isDark: Boolean = isSystemInDarkTheme(),
    settingsViewModel: SettingsViewModel
) {

    val coroutine = rememberCoroutineScope()

    val ctx = LocalContext.current

    val accentColors = TransactionResultColors(
        successColor = if (isDark) FiseSuccessfullColorDark else FiseSuccessfullColorLight,
        warningColor = if (isDark) FiseWasProcessedColorDark else FiseWasProcessedColorLight,
        errorColor = if (isDark) FiseWrongColorDark else FiseWrongColorLight
    )

    val state = vm.processState.collectAsState()
    val fise = vm.fise.collectAsState()
    val lastFiseSent = vm.lastFiseSent.collectAsState()
    val balance = vm.balance.collectAsState()
    val balanceState = vm.balanceState.collectAsState()
    val fiseError = vm.fiseError.collectAsState()
    val aiCouponValue = vm.aiCouponValue.collectAsState()
    val aiDniValue = vm.aiDniValue.collectAsState()
    val onAIResponse = vm.onAIResult.collectAsState()
    val processingTimeSeconds = vm.processingTimeSeconds.collectAsState()

    val realtimeVisionFeatureEnabled =
        settingsViewModel.realtimeVisionFeatureEnabled.collectAsState(initial = false)

    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(scroll)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // (1) Titulo — iOS Large Title style
        Text(
            text = "Procesar",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.37.sp
        )
        Spacer(modifier = Modifier.height(32.dp))

        // (2) Saldo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SALDO CONTABLE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.alpha(0.45f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                if (balanceState.value == BalanceState.CHECKING_BALANCE) {
                    BalanceLoadingIndicator()
                } else {
                    Text(
                        text = balance.value,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.37.sp
                    )
                }
            }
            IconButton(onClick = {
                coroutine.launch {
                    vm.onEvent(AppEvent.CHECK_BALANCE, ctx)
                }
            }) {
                Icon(
                    Icons.Rounded.Refresh,
                    contentDescription = "Consultar saldo",
                    modifier = Modifier
                        .size(26.dp)
                        .alpha(0.4f)
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        // (3) Camara AI
        AnimatedVisibility(realtimeVisionFeatureEnabled.value) {
            Column {
                RealtimeVision { imagePath: String? ->
                    coroutine.launch {
                        vm.setAIImagePath(imagePath)
                        vm.onEvent(AppEvent.AI_PROCESS, ctx)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (processingTimeSeconds.value > 0.00F) {
            Text(
                text = "Imagen procesada en ${processingTimeSeconds.value}s",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.alpha(0.5f)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (state.value == ProcessState.ERROR_AI_PROCESS) {
            var isVisible by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                delay(5000)
                isVisible = false
            }

            if (isVisible) {
                Text(
                    text = "Error al procesar la imagen, intente de nuevo",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // (4) Formulario
        FormFiseComponent(
            coroutine,
            onAIResponse.value,
            aiCouponValue.value,
            aiDniValue.value,
            { vm.setOnAIResult(false) },
            onSubmit = { coroutineScope: CoroutineScope, fise ->

                coroutineScope.launch {

                    with(vm) {

                        setFise(fise)
                        onEvent(AppEvent.PROCESS_COUPON, ctx)
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(32.dp))

        // (5) Resultado
        Text(
            text = "ULTIMA TRANSACCION",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.8.sp,
            modifier = Modifier.alpha(0.45f)
        )
        Spacer(modifier = Modifier.height(14.dp))

        TransactionResult(
            processState = state.value,
            fise = fise.value,
            lastFiseSent = lastFiseSent.value,
            fiseError = fiseError.value,
            accentColors = accentColors
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun BalanceLoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "balance")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "balancePulse"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(22.dp),
            strokeWidth = 2.5.dp,
            strokeCap = StrokeCap.Round,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Consultando...",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.graphicsLayer(alpha = pulse)
        )
    }
}
