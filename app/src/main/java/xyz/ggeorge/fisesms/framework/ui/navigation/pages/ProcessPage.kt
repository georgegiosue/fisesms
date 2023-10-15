package xyz.ggeorge.fisesms.framework.ui.navigation.pages

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xyz.ggeorge.components.AlertDialog
import xyz.ggeorge.components.CardIndicator
import xyz.ggeorge.components.FormFiseComponent
import xyz.ggeorge.components.Header
import xyz.ggeorge.components.OnReload
import xyz.ggeorge.core.domain.Fise
import xyz.ggeorge.core.domain.SMS
import xyz.ggeorge.core.state.FiseState
import xyz.ggeorge.fisesms.framework.ui.lib.toast
import xyz.ggeorge.fisesms.framework.ui.viewmodels.FiseViewModel
import xyz.ggeorge.theme.FiseSuccessfullColorDark
import xyz.ggeorge.theme.FiseWrongColorLight
import xyz.ggeorge.theme.FiseSuccessfullColorLight
import xyz.ggeorge.theme.FiseWasProcessedColorDark
import xyz.ggeorge.theme.FiseWasProcessedColorLight
import xyz.ggeorge.theme.FiseWrongColorDark


@Composable
fun ProcessPage(vm: FiseViewModel, isDark: Boolean = isSystemInDarkTheme()) {

    val coroutine = rememberCoroutineScope()

    val ctx = LocalContext.current

    val error = remember {
        mutableStateOf(false)
    }

    val errorMsg = remember {
        mutableStateOf("")
    }

    val fiseSuccessfullColor = if(isDark) FiseSuccessfullColorDark else FiseSuccessfullColorLight
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
                data = vm.getBalance(),
                onClick = {
                    coroutine.launch {
                        try {
                            vm.smsManager.send(
                                SMS(
                                    Fise.SERVICE_PHONE_NUMBER, Fise.BALANCE
                                ), ctx
                            )

                            ctx.toast("Mensaje Enviado")

                        } catch (e: Exception) {
                            error.value = true
                            errorMsg.value = e.message!!
                        }

                    }
                })
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        FormFiseComponent(dniValue = vm.fise.value.dni, onDniChange = {
            vm.fise.value.dni = it
            vm.statusChange()
        }, valeValue = vm.fise.value.vale, onValeChange = {
            vm.fise.value.vale = it
            vm.statusChange()
        }, coroutine
        ) { coroutineScope: CoroutineScope ->

            coroutineScope.launch {
                try {

                    vm.smsManager.send(
                        SMS(
                            Fise.SERVICE_PHONE_NUMBER, vm.fise.value.payload()
                        ), ctx
                    )
                    vm.setOnProcessing(false)

                    ctx.toast("Mensaje Enviado")
                } catch (err: Exception) {
                    error.value = true
                    errorMsg.value = err.message!!
                }
            }
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            text = "Ultima Transacción",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))


        if (vm.onProcessed.value) {
            val lastSendFise: Fise? = vm.getLastSendFise()
            when (lastSendFise?.state) {
                FiseState.PROCESSED -> {
                    AlertDialog(
                        title = "¡Vale Procesado Exitosamente!",
                        timer = "Se proceso el vale: ${vm.fise.value.vale}",
                        color = fiseSuccessfullColor
                    )
                }

                FiseState.PREVIOUSLY_PROCESSED -> {
                    AlertDialog(
                        title = "¡Vale Procesado anteriormente!",
                        timer = "Vale: ${vm.fise.value.vale} \n Procesado por: ${(vm.currentFise.value as Fise.PreviouslyProcessed).agentDNI}",
                        color = fiseWasProcessedColor
                    )
                }

                FiseState.WRONG -> {
                    AlertDialog(
                        title = "¡Error de Sintaxis o VALE ERRADO!",
                        timer = "Verifique que los datos esten correctos",
                        color = fiseWrongColor
                    )
                }

                else -> Text(
                    modifier = Modifier.align(CenterHorizontally),
                    text = "Aun no tiene transacciones"
                )
            }
        }

        if (error.value) {
            ctx.toast(
                errorMsg.value,
            )
            error.value = false
        }

        OnReload("${vm.count.value}")
    }
}
