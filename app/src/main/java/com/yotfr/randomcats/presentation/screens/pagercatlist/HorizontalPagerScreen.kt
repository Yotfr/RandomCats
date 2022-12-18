package com.yotfr.randomcats.presentation.screens.pagercatlist

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
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
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.yotfr.randomcats.R
import com.yotfr.randomcats.base.isPermanentlyDenied
import com.yotfr.randomcats.base.sdk29AndUp
import com.yotfr.randomcats.presentation.screens.pagercatlist.event.PagerCatListEvent
import com.yotfr.randomcats.presentation.screens.pagercatlist.event.PagerCatListScreenEvent
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalPagerApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun HorizontalPagerScreen(
    viewModel: PagerCatListViewModel = hiltViewModel(),
    onBackPressed: (selectedIndex: Int) -> Unit,
    selectedIndex: Int
) {
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()

    fun hasWriteStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            true
        } else when {
            permissionState.status.isGranted -> {
                true
            }
            permissionState.status.shouldShowRationale -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.resources.getString(R.string.permission_request_rationale)
                    )
                }
                false
            }
            permissionState.isPermanentlyDenied() -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.resources.getString(R.string.permission_permanently_denied)
                    )
                }
                false
            }
            else -> false
        }
    }

    val state by viewModel.state.collectAsState()

    val event = viewModel.event

    val pagerState = rememberPagerState(
        initialPage = selectedIndex
    )

    var curPage by remember {
        mutableStateOf(selectedIndex)
    }

    var date by remember {
        mutableStateOf("")
    }

    var time by remember {
        mutableStateOf("")
    }

    var barsVisibility by remember {
        mutableStateOf(false)
    }

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                        bitmap?.let {
                            val uri = getImageToShare(
                                bitmap = it,
                                context = context
                            )
                            shareImage(
                                uri = uri,
                                context = context
                            )
                        }
                    }
                },
                onDownloadClicked = {
                    if (hasWriteStoragePermission()) {
                        coroutineScope.launch {
                            bitmap?.let {
                                val isSuccess = saveMediaToStorage(
                                    bitmap = it,
                                    context = context
                                )
                                if (isSuccess) {
                                    snackbarHostState.showSnackbar(
                                        message = context.resources.getString(R.string.saved_to_gallery)
                                    )
                                }
                            }
                        }
                    } else {
                        permissionState.launchPermissionRequest()
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
                    loadingPlaceholderPainter = painterResource(id = R.drawable.card_cat_placeholder),
                    updateBitmap = {
                        bitmap = it
                    }
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
    request: ImageRequest,
    updateBitmap: (bitmap: Bitmap) -> Unit
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
        SideEffect {
            if (painterState is AsyncImagePainter.State.Success) {
                val bitmap = (painterState.result.drawable as BitmapDrawable).bitmap
                updateBitmap(bitmap)
            }
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
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = {
                        Log.d("TEST", "clicked")
                        onDownloadClicked()
                    }
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

private fun saveMediaToStorage(bitmap: Bitmap, context: Context): Boolean {
    Log.d("TEST", "bm -> $bitmap")

    val imageCollection = sdk29AndUp {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.WIDTH, bitmap.width)
        put(MediaStore.Images.Media.HEIGHT, bitmap.height)
    }

    return try {
        context.contentResolver.insert(imageCollection, contentValues)?.also { uri ->
            context.contentResolver.openOutputStream(uri).use { outputStream ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                    throw IOException("Couldn't save bitmap")
                }
            }
        } ?: throw IOException("Couldn't create MediaStore entry")
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}

private fun shareImage(uri: Uri, context: Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_STREAM, uri)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        type = "image/*"
    }
    context.startActivity(Intent.createChooser(intent, null))
}

private fun getImageToShare(bitmap: Bitmap, context: Context): Uri {
    val imageFolder = File(context.cacheDir, "images")
    var uri: Uri? = null
    try {
        imageFolder.mkdirs()
        val file = File(imageFolder, "shared_image.jpg")
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)

        fileOutputStream.flush()
        fileOutputStream.close()

        uri = FileProvider.getUriForFile(context, "com.yotfr.randomcats", file)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return uri ?: throw IOException("Not found uri")
}
