package xyz.ggeorge.fisesms.framework.ui.navigation.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import xyz.ggeorge.components.AnimatedTypingText
import xyz.ggeorge.components.CardIndicator
import xyz.ggeorge.components.FormFiseComponent
import xyz.ggeorge.components.Header
import xyz.ggeorge.core.domain.Fise
import xyz.ggeorge.core.domain.state.FiseState
import xyz.ggeorge.core.domain.events.AppEvent
import xyz.ggeorge.core.domain.state.BalanceState
import xyz.ggeorge.core.domain.state.ProcessState
import xyz.ggeorge.fisesms.framework.ui.viewmodels.FiseViewModel
import xyz.ggeorge.theme.FiseSuccessfullColorDark
import xyz.ggeorge.theme.FiseSuccessfullColorLight
import xyz.ggeorge.theme.FiseWasProcessedColorDark
import xyz.ggeorge.theme.FiseWasProcessedColorLight
import xyz.ggeorge.theme.FiseWrongColorDark
import xyz.ggeorge.theme.FiseWrongColorLight


@Composable
fun ProcessScreen(vm: FiseViewModel, isDark: Boolean = isSystemInDarkTheme()) {

    val coroutine = rememberCoroutineScope()

    val ctx = LocalContext.current

    val fiseSuccessfullyColor = if(isDark) FiseSuccessfullColorDark else FiseSuccessfullColorLight
    val fiseWrongColor = if(isDark) FiseWrongColorDark else FiseWrongColorLight
    val fiseWasProcessedColor = if(isDark) FiseWasProcessedColorDark else FiseWasProcessedColorLight

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Header(header = Pair("Procesar", "Vale"))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {

            CardIndicator(
                title = "Saldo",
                subtitle = "Contable",
                icon = {
                    Icon(Icons.Rounded.Refresh, contentDescription = "forward")
                },
                data = vm.balance.value,
                onSuspense = vm.balanceState.value == BalanceState.CHECKING_BALANCE,
                fallback = {
                    AnimatedTypingText(
                        text = "Consultando...",
                        fontSize = 20.sp,
                        maxRepeatCount = 99,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp).alpha(0.8f)
                    )
                },
                onClick = {
                    coroutine.launch {

                        vm.onEvent(AppEvent.CHECK_BALANCE, ctx)
                    }
                })
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        FormFiseComponent(
            coroutine,
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


        when(vm.processState.value) {
            ProcessState.INITIAL -> {
                Text(
                    modifier = Modifier.align(CenterHorizontally),
                    text = "Aun no tiene transacciones"
                )
            }
            ProcessState.PROCESSING_COUPON -> {

                AnimatedTypingText(
                    text = "Procesando...",
                    fontSize = 20.sp,
                    maxRepeatCount = 99,
                    modifier = Modifier
                        .align(CenterHorizontally)
                        .alpha(0.8f)
                    )
            }
            ProcessState.COUPON_RECEIVED -> {

                val fise: Fise = vm.fise.value

                when (fise.state) {
                    FiseState.PROCESSED -> {

                        val fiseProcessed = fise as Fise.Processed

                        AlertDialog(
                            title = "¡Vale Procesado Exitosamente!",
                            timer = "Se proceso el vale: ${fiseProcessed.code}",
                            color = fiseSuccessfullyColor
                        )
                    }

                    FiseState.PREVIOUSLY_PROCESSED -> {

                        val fiseWasProcessed = fise as Fise.PreviouslyProcessed

                        AlertDialog(
                            title = "¡Vale Procesado anteriormente!",
                            timer = "Vale: ${vm.lastFiseSent.value!!.code} \n Procesado por: ${fiseWasProcessed.agentDNI}",
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
                    timer = vm.fiseError.value.message,
                    color = fiseWrongColor
                )
            }
        }
    }
}
