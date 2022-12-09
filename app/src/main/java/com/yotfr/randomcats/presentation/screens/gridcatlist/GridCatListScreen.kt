package com.yotfr.randomcats.presentation.screens.gridcatlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.gridcatlist.event.GridCatListEvent
import com.yotfr.randomcats.presentation.screens.gridcatlist.event.GridCatListScreenEvent
import com.yotfr.randomcats.presentation.screens.gridcatlist.model.GridCatListModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridListScreen(
    viewModel: GridCatListViewModel = hiltViewModel(),
    navigateToImageDetails: (selectedIndex: Int) -> Unit,
    selectedIndex: Int
) {
    val state by viewModel.state.collectAsState()
    val event = viewModel.event

    val coroutineScope = rememberCoroutineScope()

    val lazyStaggeredGridState = rememberLazyStaggeredGridState()

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(key1 = true) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            event.collectLatest { uiEvent ->
                when (uiEvent) {
                    is GridCatListScreenEvent.NavigateToPagerScreenCat -> {
                        navigateToImageDetails(
                            uiEvent.selectedIndex
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = selectedIndex) {
        coroutineScope.launch {
            lazyStaggeredGridState.scrollToItem(
                selectedIndex
            )
        }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
        state = lazyStaggeredGridState
    ) {
        itemsIndexed(state.cats) { index, cat ->
            GridCell(
                cat = cat,
                onGridClicked = {
                    viewModel.onEvent(
                        GridCatListEvent.GridCatListItemClicked(
                            selectedIndex = index
                        )
                    )
                },
                catContentDescription = stringResource(id = R.string.your_favorite_cat),
                loadingPlaceholderPainter = painterResource(id = R.drawable.card_cat_placeholder)
            )
        }
    }
}

@Composable
fun GridCell(
    cat: GridCatListModel,
    onGridClicked: () -> Unit,
    catContentDescription: String,
    loadingPlaceholderPainter: Painter
) {
    SubcomposeAsyncImage(
        modifier = Modifier.fillMaxWidth(),
        model = ImageRequest.Builder(LocalContext.current)
            .data(cat.url)
            .crossfade(true)
            .build(),
        contentDescription = catContentDescription,
        contentScale = ContentScale.FillWidth,
        alignment = Alignment.Center
    ) {
        val painterState = painter.state
        if (painterState is AsyncImagePainter.State.Loading ||
            painterState is AsyncImagePainter.State.Error
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(64.dp)
                    .alpha(0.5f),
                painter = loadingPlaceholderPainter,
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.Center,
                colorFilter = ColorFilter.tint(
                    color = Color.Gray
                )
            )
        } else {
            SubcomposeAsyncImageContent(
                modifier = Modifier.clickable {
                    onGridClicked()
                }
            )
        }
    }
}
