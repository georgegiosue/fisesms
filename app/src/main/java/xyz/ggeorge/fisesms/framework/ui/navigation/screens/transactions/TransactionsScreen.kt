package xyz.ggeorge.fisesms.framework.ui.navigation.screens.transactions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.ggeorge.components.FiseCard
import xyz.ggeorge.fisesms.data.entities.FiseEntity
import xyz.ggeorge.fisesms.framework.ui.viewmodels.FiseViewModel

@Composable
fun TransactionsScreen(vm: FiseViewModel, isDark: Boolean = isSystemInDarkTheme()) {

    val state by vm.state.collectAsState()

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Historial",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.37.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state.coupons.isEmpty()) {
            EmptyTransactionsMessage()
        } else {
            TransactionsList(state.coupons)
        }
    }
}

@Composable
fun EmptyTransactionsMessage() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "No hay vales FISE procesados.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(0.4f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            Icons.Outlined.ConfirmationNumber,
            contentDescription = null,
            modifier = Modifier.alpha(0.4f)
        )
    }
}

@Composable
fun TransactionsList(coupons: List<FiseEntity>) {
    LazyColumn(
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(coupons) { fise ->
            FiseCard(fise = fise.toDomain())
        }
    }
}
