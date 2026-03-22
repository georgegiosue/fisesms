package xyz.ggeorge.fisesms.framework.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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

private data class TabItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Any
)

private val tabs = listOf(
    TabItem("Procesar", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline, Process),
    TabItem("Historial", Icons.Filled.Receipt, Icons.Outlined.ReceiptLong, Transactions),
    TabItem("Ajustes", Icons.Filled.Settings, Icons.Outlined.Settings, Settings)
)

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

@Composable
fun ActivityContent(
    viewModel: FiseViewModel,
    settingsViewModel: SettingsViewModel,
    chartsViewModel: ChartsViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                tabs.forEach { tab ->
                    val selected = currentDestination?.hasRoute(tab.route::class) == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.label
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                fontSize = 10.sp,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        },
    ) { innerPadding ->

        NavHost(
            modifier = Modifier.padding(innerPadding),
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
