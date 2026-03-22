package xyz.ggeorge.fisesms.framework.ui.activities

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
import xyz.ggeorge.fisesms.FiseSMSApp
import xyz.ggeorge.fisesms.framework.ui.lib.RequestPermissions
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.charts.Charts
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.charts.ChartsScreen
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.process.Process
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.process.ProcessScreen
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.settings.Settings
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.settings.SettingsScreen
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.transactions.Transactions
import xyz.ggeorge.fisesms.framework.ui.navigation.screens.transactions.TransactionsScreen
import xyz.ggeorge.fisesms.framework.ui.viewmodels.ChartsViewModel
import xyz.ggeorge.fisesms.framework.ui.viewmodels.FiseViewModel
import xyz.ggeorge.fisesms.framework.ui.viewmodels.SettingsViewModel
import xyz.ggeorge.theme.FisesmsTheme

class MainActivity : ComponentActivity() {

    private val fiseDao by lazy { (application as FiseSMSApp).fiseDao }

    private val viewModel by viewModels<FiseViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FiseViewModel(fiseDao) as T
                }
            }
        }
    )

    private val chartsViewModel by viewModels<ChartsViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ChartsViewModel(fiseDao) as T
                }
            }
        }
    )

    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FisesmsTheme {
                RequestPermissions(onDismissRequest = {
                    finish()
                })

                ActivityContent(viewModel, settingsViewModel, chartsViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityContent(
    viewModel: FiseViewModel,
    settingsViewModel: SettingsViewModel,
    chartsViewModel: ChartsViewModel
) {
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

                        /* IconButton(
                             onClick = {
                                 navController.navigate(Charts)
                             }
                         ) {
                             Icon(
                                 imageVector = Icons.Outlined.PieChartOutline,
                                 contentDescription = "Graficos"
                             )
                         }*/
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
            modifier = Modifier
                .padding(innerPadding),
            navController = navController,
            startDestination = Process
        ) {
            composable<Process> {
                ProcessScreen(vm = viewModel, settingsViewModel = settingsViewModel)
            }
            composable<Transactions> {
                TransactionsScreen(vm = viewModel)
            }
            composable<Charts> {
                ChartsScreen(vm = chartsViewModel)
            }
            composable<Settings> {
                SettingsScreen(viewModel = settingsViewModel)
            }
        }
    }
}
