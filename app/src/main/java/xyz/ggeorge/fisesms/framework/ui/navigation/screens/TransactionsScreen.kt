package xyz.ggeorge.fisesms.framework.ui.navigation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import xyz.ggeorge.components.FiseCard
import xyz.ggeorge.components.Header
import xyz.ggeorge.fisesms.framework.ui.viewmodels.FiseViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TransactionsScreen(vm: FiseViewModel, isDark: Boolean = isSystemInDarkTheme()) {

    val coroutine = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val ctx = LocalContext.current

    val state by vm.state.collectAsState()

    val screenHeight = ctx.resources.displayMetrics.heightPixels.dp

    val lazyColumnHeight = screenHeight.div(4)

    Header(header = Pair("Transacciones", "Vales procesados"))

    Spacer(modifier = Modifier.height(16.dp))
    
    LazyColumn(
        modifier = Modifier.height(lazyColumnHeight),
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(state.coupons) { fise ->
            FiseCard(fise = fise.toDomain())
        }
    }
}
