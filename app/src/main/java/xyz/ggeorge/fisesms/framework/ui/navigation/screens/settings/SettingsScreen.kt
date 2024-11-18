package xyz.ggeorge.fisesms.framework.ui.navigation.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirlineStops
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.ggeorge.fisesms.framework.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, viewModel: SettingsViewModel) {

    val scroll = rememberScrollState()
    val realtimeVisionFeatureEnabled by viewModel.realtimeVisionFeatureEnabled.collectAsState(
        initial = false
    )
    var checkedAuthFeature by remember { mutableStateOf(false) }
    val alias by viewModel.alias.collectAsState(initial = "ah01")
    var openAliasEditor by remember { mutableStateOf(false) }
    val serviceNumber by viewModel.serviceNumber.collectAsState(initial = "55555")
    var openServiceNumberEditor by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scroll)
    ) {
        Text(
            text = "Configuración",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.padding(vertical = 16.dp))

        Card() {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(true) {
                            openAliasEditor = !openAliasEditor
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.6f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AirlineStops,
                            contentDescription = "Alias Icono"
                        )
                        Text(text = "Alias", fontSize = 18.sp)
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(0.4f),
                        text = alias,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End
                    )

                    if (openAliasEditor) {

                        var aliasBuffer by remember { mutableStateOf(alias) }

                        AlertDialog(
                            onDismissRequest = { openAliasEditor = false },
                            title = { Text("Alias para procesar los vales") },
                            text = {
                                OutlinedTextField(
                                    value = aliasBuffer,
                                    onValueChange = {
                                        aliasBuffer = it
                                    },
                                    label = { Text("Alias") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        viewModel.setAlias(aliasBuffer)
                                        openAliasEditor = false
                                    }
                                ) {
                                    Text("Guardar")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        openAliasEditor = false
                                    }
                                ) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(true) {
                            openServiceNumberEditor = !openServiceNumberEditor
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.6f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Tag,
                            contentDescription = "Numero de servicio Icono"
                        )
                        Text(text = "Numero de Servicio", fontSize = 18.sp)
                    }

                    Text(
                        modifier = Modifier.fillMaxWidth(0.4f),
                        text = serviceNumber,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.End
                    )

                    if (openServiceNumberEditor) {

                        var serviceNumberBuffer by remember { mutableStateOf(serviceNumber) }

                        AlertDialog(
                            onDismissRequest = { openServiceNumberEditor = false },
                            title = { Text("Numero de servicio del FISE") },
                            text = {
                                OutlinedTextField(
                                    value = serviceNumberBuffer,
                                    onValueChange = {
                                        serviceNumberBuffer = it
                                    },
                                    label = { Text("Numero de servicio") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        viewModel.setServiceNumber(serviceNumberBuffer)
                                        openServiceNumberEditor = false
                                    }
                                ) {
                                    Text("Guardar")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        openServiceNumberEditor = false
                                    }
                                ) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        Card() {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.6f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "Vision en tiempo real Icono"
                        )
                        Text(text = "Vision en tiempo real", fontSize = 18.sp)
                    }
                    Switch(
                        modifier = Modifier.fillMaxWidth(0.4f),
                        checked = realtimeVisionFeatureEnabled,
                        onCheckedChange = {
                            viewModel.toggleRealtimeVisionFeature(it)
                        },
                        thumbContent = if (realtimeVisionFeatureEnabled) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        } else {
                            null
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.6f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Fingerprint,
                            contentDescription = "Solicitar huella al iniciar Icono"
                        )
                        Text(text = "Solicitar huella al iniciar", fontSize = 18.sp)
                    }
                    Switch(
                        modifier = Modifier.fillMaxWidth(0.4f),
                        enabled = false,
                        checked = checkedAuthFeature,
                        onCheckedChange = {
                            checkedAuthFeature = it
                        },
                        thumbContent = if (checkedAuthFeature) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            }
        }
    }
}