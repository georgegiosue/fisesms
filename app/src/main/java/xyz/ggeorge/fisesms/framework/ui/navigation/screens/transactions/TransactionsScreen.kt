package xyz.ggeorge.fisesms.framework.ui.navigation.screens.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
fun TransactionsScreen(vm: FiseViewModel) {

    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Historial",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.37.sp
        )

        if (state.coupons.isNotEmpty()) {
            Text(
                text = "${state.coupons.size} vale${if (state.coupons.size != 1) "s" else ""} procesado${if (state.coupons.size != 1) "s" else ""}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .alpha(0.45f)
                    .padding(top = 4.dp)
            )
        }

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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Outlined.ReceiptLong,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .alpha(0.2f)
            )
            Text(
                text = "Sin vales procesados",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.alpha(0.35f)
            )
            Text(
                text = "Los vales FISE que proceses\naparecen aqui.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(0.25f)
            )
        }
    }
}

@Composable
fun TransactionsList(coupons: List<FiseEntity>) {
    LazyColumn(
        state = rememberLazyListState(),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ) {
                Column {
                    coupons.forEachIndexed { index, fise ->
                        FiseCard(fise = fise.toDomain())
                        if (index < coupons.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 70.dp, end = 16.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
