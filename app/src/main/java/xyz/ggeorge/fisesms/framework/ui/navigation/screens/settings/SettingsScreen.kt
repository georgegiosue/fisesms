package xyz.ggeorge.fisesms.framework.ui.navigation.screens.settings

import android.Manifest
import android.content.pm.PackageManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AirlineStops
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import xyz.ggeorge.fisesms.framework.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, viewModel: SettingsViewModel) {

    val context = LocalContext.current
    val scroll = rememberScrollState()
    val realtimeVisionFeatureEnabled by viewModel.realtimeVisionFeatureEnabled.collectAsState(
        initial = false
    )
    var checkedAuthFeature by remember { mutableStateOf(false) }
    val alias by viewModel.alias.collectAsState(initial = "ah01")
    var openAliasEditor by remember { mutableStateOf(false) }
    val serviceNumber by viewModel.serviceNumber.collectAsState(initial = "55555")
    var openServiceNumberEditor by remember { mutableStateOf(false) }
    val simSubscriptionId by viewModel.simSubscriptionId.collectAsState(initial = -1)
    var openSimSelector by remember { mutableStateOf(false) }

    val activeSimList: List<SubscriptionInfo> = remember {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            val subscriptionManager = SubscriptionManager.from(context)
            subscriptionManager.activeSubscriptionInfoList ?: emptyList()
        } else {
            emptyList()
        }
    }

    val selectedSimLabel = remember(simSubscriptionId, activeSimList) {
        if (simSubscriptionId == -1) {
            "Predeterminada"
        } else {
            activeSimList.find { it.subscriptionId == simSubscriptionId }
                ?.let { "SIM ${it.simSlotIndex + 1} — ${it.carrierName}" }
                ?: "Predeterminada"
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(scroll)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ajustes",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.37.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sección: General
        Text(
            text = "GENERAL",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.8.sp,
            modifier = Modifier
                .alpha(0.45f)
                .padding(bottom = 10.dp)
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ) {
            Column {
                // Alias
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openAliasEditor = !openAliasEditor }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AirlineStops,
                            contentDescription = null,
                            modifier = Modifier.alpha(0.6f)
                        )
                        Text(text = "Alias", fontSize = 17.sp)
                    }
                    Text(
                        text = alias,
                        fontSize = 17.sp,
                        modifier = Modifier.alpha(0.5f)
                    )
                }

                if (openAliasEditor) {
                    var aliasBuffer by remember { mutableStateOf(alias) }

                    AlertDialog(
                        onDismissRequest = { openAliasEditor = false },
                        title = { Text("Alias para procesar los vales") },
                        text = {
                            OutlinedTextField(
                                value = aliasBuffer,
                                onValueChange = { aliasBuffer = it },
                                label = { Text("Alias") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.setAlias(aliasBuffer)
                                openAliasEditor = false
                            }) {
                                Text("Guardar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { openAliasEditor = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }

                // Numero de servicio
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openServiceNumberEditor = !openServiceNumberEditor }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Tag,
                            contentDescription = null,
                            modifier = Modifier.alpha(0.6f)
                        )
                        Text(text = "Numero de servicio", fontSize = 17.sp)
                    }
                    Text(
                        text = serviceNumber,
                        fontSize = 17.sp,
                        modifier = Modifier.alpha(0.5f)
                    )
                }

                if (openServiceNumberEditor) {
                    var serviceNumberBuffer by remember { mutableStateOf(serviceNumber) }

                    AlertDialog(
                        onDismissRequest = { openServiceNumberEditor = false },
                        title = { Text("Numero de servicio del FISE") },
                        text = {
                            OutlinedTextField(
                                value = serviceNumberBuffer,
                                onValueChange = { serviceNumberBuffer = it },
                                label = { Text("Numero de servicio") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.setServiceNumber(serviceNumberBuffer)
                                openServiceNumberEditor = false
                            }) {
                                Text("Guardar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { openServiceNumberEditor = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }

                // Tarjeta SIM
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openSimSelector = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SimCard,
                            contentDescription = null,
                            modifier = Modifier.alpha(0.6f)
                        )
                        Text(text = "Tarjeta SIM", fontSize = 17.sp)
                    }
                    Text(
                        text = selectedSimLabel,
                        fontSize = 17.sp,
                        modifier = Modifier.alpha(0.5f)
                    )
                }

                if (openSimSelector) {
                    AlertDialog(
                        onDismissRequest = { openSimSelector = false },
                        title = { Text("Seleccionar tarjeta SIM") },
                        text = {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.setSimSubscriptionId(-1)
                                            openSimSelector = false
                                        }
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    RadioButton(
                                        selected = simSubscriptionId == -1,
                                        onClick = {
                                            viewModel.setSimSubscriptionId(-1)
                                            openSimSelector = false
                                        }
                                    )
                                    Text("Predeterminada del sistema", fontSize = 16.sp)
                                }
                                activeSimList.forEach { simInfo ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.setSimSubscriptionId(simInfo.subscriptionId)
                                                openSimSelector = false
                                            }
                                            .padding(vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        RadioButton(
                                            selected = simSubscriptionId == simInfo.subscriptionId,
                                            onClick = {
                                                viewModel.setSimSubscriptionId(simInfo.subscriptionId)
                                                openSimSelector = false
                                            }
                                        )
                                        Text(
                                            "SIM ${simInfo.simSlotIndex + 1} — ${simInfo.carrierName}",
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }
                        },
                        confirmButton = {},
                        dismissButton = {
                            TextButton(onClick = { openSimSelector = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Sección: Funciones
        Text(
            text = "FUNCIONES",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.8.sp,
            modifier = Modifier
                .alpha(0.45f)
                .padding(bottom = 10.dp)
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = null,
                            modifier = Modifier.alpha(0.6f)
                        )
                        Text(text = "Vision en tiempo real", fontSize = 17.sp)
                    }
                    Switch(
                        checked = realtimeVisionFeatureEnabled,
                        onCheckedChange = { viewModel.toggleRealtimeVisionFeature(it) },
                        thumbContent = if (realtimeVisionFeatureEnabled) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        } else null
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Fingerprint,
                            contentDescription = null,
                            modifier = Modifier.alpha(0.6f)
                        )
                        Text(text = "Solicitar huella al iniciar", fontSize = 17.sp)
                    }
                    Switch(
                        enabled = false,
                        checked = checkedAuthFeature,
                        onCheckedChange = { checkedAuthFeature = it },
                        thumbContent = if (checkedAuthFeature) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize),
                                )
                            }
                        } else null
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
