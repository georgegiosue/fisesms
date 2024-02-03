package xyz.ggeorge.fisesms.framework.ui.activities

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import xyz.ggeorge.components.Screen
import xyz.ggeorge.components.TabBar
import xyz.ggeorge.fisesms.data.database.AppDatabase
import xyz.ggeorge.fisesms.framework.ui.lib.PERMISSIONS
import xyz.ggeorge.fisesms.framework.ui.lib.requiredPermissions
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.ProcessScreen
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.RequiredPermissionsPage
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.TransactionsScreen
import xyz.ggeorge.fisesms.framework.ui.viewmodels.FiseViewModel
import xyz.ggeorge.fisesms.interactors.implementation.SmsReceiver
import xyz.ggeorge.theme.FisesmsTheme

class MainActivity : ComponentActivity() {

    private val smsReceiver: SmsReceiver = SmsReceiver()
    private val intentFilter = IntentFilter(SmsReceiver.INTENT_FILTER)

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "fise_db"
        ).build()
    }

    private val viewModel by viewModels<FiseViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FiseViewModel(db.fiseDao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requiredPermissions(listOf(PERMISSIONS.SEND_SMS, PERMISSIONS.RECEIVE_SMS)) { isGranted ->
            if (isGranted) {

                setContent {

                    FisesmsTheme {
                        ActivityContent(viewModel)
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
    private fun ActivityContent(viewModel: FiseViewModel) {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Box(
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
                    screens = listOf(
                        Screen("Procesar") { ProcessScreen(vm = viewModel) },
                        Screen("Transacciones") { TransactionsScreen(vm = viewModel) }
                    )
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
    }
}