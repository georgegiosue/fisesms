package xyz.ggeorge.fisesms.framework.ui.navigation.screens.transactions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.TextStyle
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

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "Transacciones",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 16.dp)
        )


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
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "No hay vales FISE procesados.",
            style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            Icons.Outlined.ConfirmationNumber,
            contentDescription = "No hay vales procesados."
        )
    }
}

@Composable
fun TransactionsList(coupons: List<FiseEntity>) {
    LazyColumn(
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(coupons) { fise ->
            FiseCard(fise = fise.toDomain())
        }
    }
}
