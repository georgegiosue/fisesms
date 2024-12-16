package xyz.ggeorge.fisesms.framework.ui.navigation.screens.process

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.ggeorge.components.AlertDialog
import xyz.ggeorge.components.AnimatedFadeInText
import xyz.ggeorge.components.CardIndicator
import xyz.ggeorge.components.FormFiseComponent
import xyz.ggeorge.components.camera.RealtimeVision
import xyz.ggeorge.core.domain.Fise
import xyz.ggeorge.core.domain.events.AppEvent
import xyz.ggeorge.core.domain.state.BalanceState
import xyz.ggeorge.core.domain.state.FiseState
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

    val fiseSuccessfullyColor = if (isDark) FiseSuccessfullColorDark else FiseSuccessfullColorLight
    val fiseWrongColor = if (isDark) FiseWrongColorDark else FiseWrongColorLight
    val fiseWasProcessedColor =
        if (isDark) FiseWasProcessedColorDark else FiseWasProcessedColorLight

    val state = vm.processState.collectAsState()
    val fise = vm.fise.collectAsState()
    val lastFiseSent = vm.lastFiseSent.collectAsState()
    val balance = vm.balance.collectAsState()
    val balanceState = vm.balanceState.collectAsState()
    val fiseError = vm.fiseError.collectAsState()
    val aiCouponValue = vm.aiCouponValue.collectAsState()
    val aiDniValue = vm.aiDniValue.collectAsState()
    val onAIResponse = vm.onAIResult.collectAsState()

    val realtimeVisionFeatureEnabled =
        settingsViewModel.realtimeVisionFeatureEnabled.collectAsState(initial = false)

    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scroll)
    ) {
        Text(
            text = "Procesar",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.padding(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {

            CardIndicator(
                title = "Saldo contable",
                icon = {
                    Icon(Icons.Rounded.Refresh, contentDescription = "forward")
                },
                data = balance.value,
                onSuspense = balanceState.value == BalanceState.CHECKING_BALANCE,
                fallback = {
                    AnimatedFadeInText(
                        text = "Consultando...",
                        fontSize = 16.sp,
                        maxRepeatCount = 99,
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 4.dp)
                            .alpha(0.8f)
                    )
                },
                onClick = {
                    coroutine.launch {

                        vm.onEvent(AppEvent.CHECK_BALANCE, ctx)
                    }
                })
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        AnimatedVisibility(realtimeVisionFeatureEnabled.value) {
            RealtimeVision { imagePath: String? ->
                coroutine.launch {
                    vm.setAIImagePath(imagePath)
                    vm.onEvent(AppEvent.AI_PROCESS, ctx)
                }

            }
        }

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

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            text = "Ultima Transacción",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        when (state.value) {
            ProcessState.INITIAL -> {
                Text(
                    modifier = Modifier.align(CenterHorizontally),
                    text = "Aun no tiene transacciones"
                )
            }

            ProcessState.PROCESSING_COUPON -> {

                AnimatedFadeInText(
                    text = "Procesando...",
                    fontSize = 20.sp,
                    maxRepeatCount = 99,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .alpha(0.8f)
                )
            }

            ProcessState.COUPON_RECEIVED -> {

                when (fise.value.state) {
                    FiseState.PROCESSED -> {

                        val fiseProcessed = fise.value as Fise.Processed

                        AlertDialog(
                            title = "¡Vale Procesado Exitosamente!",
                            timer = "Se proceso el vale: ${fiseProcessed.code}",
                            color = fiseSuccessfullyColor
                        )
                    }

                    FiseState.PREVIOUSLY_PROCESSED -> {

                        val fiseWasProcessed = fise.value as Fise.PreviouslyProcessed

                        AlertDialog(
                            title = "¡Vale Procesado anteriormente!",
                            timer = "Vale: ${lastFiseSent.value!!.code} \n Procesado por: ${fiseWasProcessed.agentDNI}",
                            color = fiseWasProcessedColor
                        )
                    }

                    FiseState.WRONG -> {
                        AlertDialog(
                            title = "¡DOC BENEFICIARIO O VALE ERRADO!",
                            timer = "Verifique que los datos esten correctos",
                            color = fiseWrongColor
                        )
                    }

                    FiseState.SYNTAX_ERROR -> {
                        AlertDialog(
                            title = "¡Error de Sintaxis!",
                            timer = "Verifique que el DNI y/o el codigo de VALE esten correctos",
                            color = fiseWrongColor
                        )
                    }

                    else -> {}
                }
            }

            ProcessState.ERROR_PROCESSING_COUPON -> {

                AlertDialog(
                    title = "¡ERROR al procesar el VALE FISE!",
                    timer = fiseError.value.message,
                    color = fiseWrongColor
                )
            }

            ProcessState.ERROR_AI_PROCESS -> {

                AlertDialog(
                    title = "¡ERROR al procesar la imagen con IA!",
                    timer = fiseError.value.message,
                    color = fiseWrongColor
                )
            }
        }
    }
}
