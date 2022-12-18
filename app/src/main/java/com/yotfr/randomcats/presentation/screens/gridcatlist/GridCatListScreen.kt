package com.yotfr.randomcats.presentation.screens.gridcatlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.gridcatlist.event.GridCatListEvent
import com.yotfr.randomcats.presentation.screens.gridcatlist.event.GridCatListScreenEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridListScreen(
    viewModel: GridCatListViewModel = hiltViewModel(),
    navigateToImageDetails: (selectedIndex: Int) -> Unit,
    navigateToAuth: () -> Unit,
    selectedIndex: Int
) {
    val state by viewModel.state.collectAsState()
    val uiEvent = viewModel.uiEvent
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    // collecting one time uiEvents
    LaunchedEffect(key1 = true) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEvent.collectLatest { uiEvent ->
                when (uiEvent) {
                    is GridCatListScreenEvent.NavigateToPagerScreen -> {
                        navigateToImageDetails(
                            uiEvent.selectedIndex
                        )
                    }
                    GridCatListScreenEvent.NavigateToAuth -> {
                        navigateToAuth()
                    }
                }
            }
        }
    }

    // scroll to item when navigate back from pager screen
    LaunchedEffect(key1 = selectedIndex) {
        coroutineScope.launch {
            lazyStaggeredGridState.scrollToItem(
                selectedIndex
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalStaggeredGrid(
            modifier = Modifier.fillMaxSize(),
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
            state = lazyStaggeredGridState
        ) {
            itemsIndexed(state.cats) { index, cat ->
                GridCell(
                    request = ImageRequest.Builder(context.applicationContext)
                        .data(cat.url)
                        .crossfade(true)
                        .diskCacheKey(cat.url)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    onGridClicked = {
                        viewModel.onEvent(
                            GridCatListEvent.GridCatListItemClicked(
                                selectedIndex = index
                            )
                        )
                    },
                    catContentDescription = stringResource(id = R.string.your_favorite_cat)
                )
            }
        }
        if (state.isLoading) {
            ProgressIndicator(
                modifier = Modifier.align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )
        }
    }
}
