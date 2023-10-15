package xyz.ggeorge.fisesms.framework.ui.activities

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.ggeorge.components.Page
import xyz.ggeorge.components.TabBar
import xyz.ggeorge.fisesms.framework.ui.lib.PERMISSIONS
import xyz.ggeorge.fisesms.framework.ui.lib.requiredPermissions
import xyz.ggeorge.fisesms.framework.ui.navigation.pages.ProcessPage
import xyz.ggeorge.fisesms.framework.ui.navigation.pages.RequiredPermissionsPage
import xyz.ggeorge.fisesms.framework.ui.viewmodels.FiseViewModel
import xyz.ggeorge.fisesms.interactors.implementation.SmsReceiver
import xyz.ggeorge.theme.FisesmsTheme

class MainActivity : ComponentActivity() {

    private val smsReceiver: SmsReceiver = SmsReceiver()
    private val intentFilter = IntentFilter(SmsReceiver.INTENT_FILTER)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requiredPermissions(listOf(PERMISSIONS.SEND_SMS, PERMISSIONS.RECEIVE_SMS)) { isGranted ->
            if (isGranted) {

                setContent {
                    val vm: FiseViewModel = viewModel()

                    FisesmsTheme {
                        ActivityContent(vm)
                    }

                    registerReceiver(smsReceiver, intentFilter)
                }


            } else {

                setContent {
                    FisesmsTheme {
                        RequiredPermissionsPage()
                    }
                }
            }
        }
    }

    @Composable
    private fun ActivityContent(vm: FiseViewModel) {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                TabBar(
                    header = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Fise",
                                style = TextStyle(
                                    fontSize = 39.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "sms")
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Outlined.ClearAll,
                                contentDescription = "Settings"
                            )
                        }
                    },
                    pages = listOf(
                        Page("Procesar") { ProcessPage(vm = vm) },
                        Page("Transacciones") {/*TODO*/ },
                        Page("Beneficiarios") {/*TODO*/ })
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
    }
}