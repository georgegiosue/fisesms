package xyz.ggeorge.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabBar(
    header: @Composable () -> Unit,
    pages: List<Page>
) {
    val pagerState = rememberPagerState()
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        Indicator(tabPositions, pagerState)
    }

    val coroutineScope = rememberCoroutineScope()
    val horizontalPadding= 32.dp

    Column(Modifier.fillMaxSize()) {
        Row(Modifier
            .fillMaxWidth()
            .padding(
                horizontal = horizontalPadding,
                vertical = horizontalPadding.div(4)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            header()
        }
        ScrollableTabRow(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            indicator = indicator,
            edgePadding = horizontalPadding,
        ) {
            pages.map(Page::title).forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier.zIndex(6f),
                    text = { Text(text = title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)

                        }
                    },
                )
            }
        }

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            count = pages.size,
            state = pagerState,
        ) { page ->
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding)
                    .padding(top = 16.dp)
            ) {
                pages[page].content()
            }
        }
    }


}


data class Page(val title: String, val content: @Composable () -> Unit)