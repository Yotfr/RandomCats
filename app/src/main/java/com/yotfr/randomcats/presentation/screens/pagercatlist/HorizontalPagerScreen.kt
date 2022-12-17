package com.yotfr.randomcats.presentation.screens.pagercatlist

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.pagercatlist.event.PagerCatListEvent
import com.yotfr.randomcats.presentation.screens.pagercatlist.event.PagerCatListScreenEvent
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HorizontalPagerScreen(
    viewModel: PagerCatListViewModel = hiltViewModel(),
    onBackPressed: (selectedIndex: Int) -> Unit,
    selectedIndex: Int
) {
    val context = LocalContext.current
    var hasWriteStoragePermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasWriteStoragePermission = isGranted
    }

    val state by viewModel.state.collectAsState()

    val event = viewModel.event

    val pagerState = rememberPagerState(
        initialPage = selectedIndex
    )

    var curPage by remember {
        mutableStateOf(selectedIndex)
    }

    val coroutineScope = rememberCoroutineScope()

    var date by remember {
        mutableStateOf("")
    }

    var time by remember {
        mutableStateOf("")
    }

    var barsVisibility by remember {
        mutableStateOf(false)
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            event.collect { uiEvent ->
                when (uiEvent) {
                    is PagerCatListScreenEvent.NavigateToGridCatList -> {
                        onBackPressed(uiEvent.selectedIndex)
                    }
                }
            }
        }
    }

    LaunchedEffect(pagerState, state) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (state.cats.isNotEmpty()) {
                date = state.cats[page].createdDateString.substringBeforeLast(" ")
                time = state.cats[page].createdDateString.substringAfterLast(" ")
                curPage = page
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                isVisible = barsVisibility,
                date = date,
                time = time,
                onBackPressed = {
                    viewModel.onEvent(
                        PagerCatListEvent.BackArrowPressed(
                            selectedIndex = curPage
                        )
                    )
                }
            )
        },
        bottomBar = {
            BottomBar(
                isVisible = barsVisibility,
                onShareClicked = {
                    coroutineScope.launch {
                        val bitMap = getBitmapFromUrl(
                            url = state.cats[curPage].url,
                            context = context
                        )
                        val uri = getImageToShare(
                            bitmap = bitMap,
                            context = context
                        )
                        shareImage(
                            uri = uri,
                            context = context
                        )
                    }
                },
                onDownloadClicked = {
                    if (hasWriteStoragePermission) {
                        coroutineScope.launch {
                            val bitMap = getBitmapFromUrl(
                                url = state.cats[curPage].url,
                                context = context
                            )
                            saveMediaToStorage(
                                bitmap = bitMap,
                                fileId = state.cats[curPage].url,
                                context = context
                            )
                        }
                    } else {
                        permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                },
                onDeleteClicked = {
                    viewModel.onEvent(
                        PagerCatListEvent.DeleteCatFromFavorite(
                            cat = state.cats[curPage]
                        )
                    )
                }
            )
        },
        content = {
            HorizontalPager(
                count = state.cats.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        barsVisibility = !barsVisibility
                    }
            ) { page ->
                Page(
                    modifier = Modifier.fillMaxSize(),
                    request = ImageRequest.Builder(context.applicationContext)
                        .data(state.cats[page].url)
                        .crossfade(true)
                        .diskCacheKey(state.cats[page].url)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    catContentDescription = stringResource(id = R.string.random_cat_image),
                    loadingPlaceholderPainter = painterResource(id = R.drawable.card_cat_placeholder)
                )
            }
        }
    )
}

@Composable
fun Page(
    modifier: Modifier,
    catContentDescription: String,
    loadingPlaceholderPainter: Painter,
    request: ImageRequest
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = request,
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
            SubcomposeAsyncImageContent()
        }
    }
}

@Composable
fun BottomBar(
    isVisible: Boolean,
    onShareClicked: () -> Unit,
    onDownloadClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        BottomAppBar(
            modifier = Modifier.fillMaxWidth()
                .height(72.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = { onDownloadClicked() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = stringResource(id = R.string.save_to_gallery)
                    )
                }
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = { onShareClicked() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = stringResource(id = R.string.share)
                    )
                }
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = { onDeleteClicked() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_favorite_minus),
                        contentDescription = stringResource(id = R.string.delete_from_favorite)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    isVisible: Boolean,
    date: String,
    time: String,
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
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                    1.dp
                )
            )
        )
    }
}

private fun saveMediaToStorage(bitmap: Bitmap, fileId: String, context: Context) {
    var fos: OutputStream? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.contentResolver?.also { contentResolver ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileId)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri: Uri? =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { contentResolver.openOutputStream(it) }
        }
    } else {
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, fileId)
        fos = FileOutputStream(image)
    }
    fos?.use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
}

private fun shareImage(uri: Uri, context: Context) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent.type = "image/*"
    context.startActivity(Intent.createChooser(intent, "Share image"))
}

private fun getImageToShare(bitmap: Bitmap, context: Context): Uri {
    val folder = File(context.cacheDir, "images")
    var uri: Uri? = null
    try {
        folder.mkdirs()
        val file = File(folder, "shared_image.jpg")
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)

        fileOutputStream.flush()
        fileOutputStream.close()

        uri = FileProvider.getUriForFile(context, "com.yotfr.randomcats", file)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return uri ?: throw Exception("Not found uri")
}

private suspend fun getBitmapFromUrl(url: String, context: Context): Bitmap {
    val loading = ImageLoader(context = context)
    val request = ImageRequest.Builder(context)
        .data(url)
        .build()
    val result = (loading.execute(request) as SuccessResult).drawable
    return (result as BitmapDrawable).bitmap
}
