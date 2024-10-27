package xyz.ggeorge.fisesms.framework.ui.navigation.screens.transactions

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.ggeorge.components.FiseCard
import xyz.ggeorge.fisesms.framework.ui.viewmodels.FiseViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TransactionsScreen(vm: FiseViewModel, isDark: Boolean = isSystemInDarkTheme()) {

    val ctx = LocalContext.current

    val state by vm.state.collectAsState()

    val screenHeight = ctx.resources.displayMetrics.heightPixels.dp

    val lazyColumnHeight = screenHeight.div(4)

    Spacer(modifier = Modifier.height(16.dp))

    if (state.coupons.isEmpty()) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "No hay vales FISE procesados.",
                style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(Icons.Outlined.ConfirmationNumber, contentDescription = "No hay vales procesados.")
        }
    } else {

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
}
