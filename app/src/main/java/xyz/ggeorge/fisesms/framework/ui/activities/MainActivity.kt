package xyz.ggeorge.fisesms.framework.ui.activities

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.LineStyle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import xyz.ggeorge.fisesms.data.database.AppDatabase
import xyz.ggeorge.fisesms.framework.ui.lib.RequestCameraAndSMSPermissions
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.NavScreen
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.process.Process
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.process.ProcessScreen
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.settings.Settings
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.settings.SettingsScreen
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.transactions.Transactions
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.transactions.TransactionsScreen
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
        setContent {
            FisesmsTheme {
                RequestCameraAndSMSPermissions(onDismissRequest = {
                    finish()
                })

                ActivityContent(viewModel)

                registerReceiver(smsReceiver, intentFilter)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityContent(viewModel: FiseViewModel) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(

                title = {
                    Text(
                        text = "Fise SMS",
                        style = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate(Process)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ChatBubbleOutline,
                                contentDescription = "Procesar"
                            )
                        }

                        IconButton(
                            onClick = {
                                navController.navigate(Transactions)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LineStyle,
                                contentDescription = "Transacciones"
                            )
                        }
                        IconButton(
                            onClick = {
                                navController.navigate(Settings)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    }
                }
            )
        },
    ) { innerPadding ->

        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Process
        ) {
            composable<Process> {
                NavScreen(title = "Procesar") {
                    ProcessScreen(vm = viewModel)
                }
            }
            composable<Transactions> {
                NavScreen(title = "Transacciones") {
                    TransactionsScreen(vm = viewModel)
                }
            }
            composable<Settings> {
                NavScreen(title = "Configuración") {
                    SettingsScreen()
                }
            }
        }
    }
}