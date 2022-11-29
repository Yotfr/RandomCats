package com.yotfr.randomcats.presentation.screens.cats_list_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.yotfr.randomcats.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HorizontalPagerScreen(
    viewModel:CatListViewModel = hiltViewModel(),
    onBackPressed:() -> Unit
) {

    val state = viewModel.state.value.groupedCats.values.flatten()
    val pagerState = rememberPagerState()

    var date by remember {
        mutableStateOf("")
    }

    var time by remember {
        mutableStateOf("")
    }

    var barsVisibility by remember {
        mutableStateOf(true)
    }
    
    Scaffold (
        topBar = {
            TopBar (
                isVisible = barsVisibility,
                date = date,
                time = time,
                onBackPressed = { onBackPressed() }
            )
        },
        content = {
            HorizontalPager(
                count = state.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        barsVisibility = !barsVisibility
                    }
            ) { page ->

                Log.d("TEST","page $page")

                date = state[page].createdDateString.substringBeforeLast(" ")
                time = state[page].createdDateString.substringAfterLast(" ")

                Box(modifier = Modifier.fillMaxSize()) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(state[page].url)
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(id = R.string.random_cat_image),
                        contentScale = ContentScale.FillWidth,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    ) {
                        val painterState = painter.state
                        if (painterState is AsyncImagePainter.State.Loading ||
                            painterState is AsyncImagePainter.State.Error
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.wrapContentSize(),
                            )
                        } else {
                            SubcomposeAsyncImageContent()
                        }
                    }
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    isVisible:Boolean,
    date:String,
    time:String,
    onBackPressed: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it }
    ) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                IconButton(onClick = {
                    onBackPressed()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
            title = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = date)
                    Text(text = time)
                }
            }
        )
    }
}