package com.yotfr.randomcats.presentation.screens.pagercatlist

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
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
import com.yotfr.randomcats.util.isPermanentlyDenied
import com.yotfr.randomcats.util.sdk29AndUp
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
    val state by viewModel.state.collectAsState()
    val uiEvent = viewModel.uiEvent
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val coroutineScope = rememberCoroutineScope()
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val pagerState = rememberPagerState(
        initialPage = selectedIndex
    )

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

    // collecting uiEvents
    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEvent.collect { uiEvent ->
                when (uiEvent) {
                    is PagerCatListScreenEvent.NavigateToGridCatList -> {
                        onBackPressed(uiEvent.selectedIndex)
                    }
                    // hardcoded selected index because if this is triggered cats list is empty
                    PagerCatListScreenEvent.NavigateBack -> {
                        onBackPressed(0)
                    }
                }
            }
        }
    }

    // change top app bar date and time on page change
    LaunchedEffect(pagerState.currentPage) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (state.cats.isNotEmpty()) {
                viewModel.onEvent(
                    PagerCatListEvent.PageChanged(
                        pageIndex = page
                    )
                )
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            HorizontalPagerTopBar(
                isVisible = state.isSystemBarsVisible,
                date = state.displayDate,
                time = state.displayTime,
                onBackPressed = {
                    viewModel.onEvent(
                        PagerCatListEvent.BackArrowPressed(
                            selectedIndex = state.currentPage
                        )
                    )
                }
            )
        },
        bottomBar = {
            HorizontalPagerBottomBar(
                isVisible = state.isSystemBarsVisible,
                onShareClicked = {
                    coroutineScope.launch {
                        state.currentlyDisplayedImageBitmap?.let {
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
                            state.currentlyDisplayedImageBitmap?.let {
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
                    if (state.cats.isNotEmpty()) {
                        viewModel.onEvent(
                            PagerCatListEvent.DeleteCatClicked(
                                cat = state.cats[state.currentPage]
                            )
                        )
                    }else {
                        onBackPressed(0)
                    }
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
                        viewModel.onEvent(PagerCatListEvent.OnScreenClicked)
                    }
            ) { page ->
                /**
                 * Launched effect is only triggered once to update top bar display date
                 * time with selected index coming from fridList screen
                 */
                LaunchedEffect(key1 = true) {
                    viewModel.onEvent(
                        PagerCatListEvent.PageChanged(
                            pageIndex = selectedIndex
                        )
                    )
                }
                Page(
                    modifier = Modifier.fillMaxSize(),
                    request = ImageRequest.Builder(context.applicationContext)
                        .data(state.cats[page].url)
                        .crossfade(true)
                        .diskCacheKey(state.cats[page].url)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    catContentDescription = stringResource(id = R.string.random_cat_image),
                    loadingPlaceholderPainter = painterResource(id = R.drawable.cat_profile_image),
                    updateBitmap = { bitmap ->
                        viewModel.onEvent(PagerCatListEvent.ChangeBitmap(bitmap))
                    }
                )
            }
        }
    )
}

private fun saveMediaToStorage(bitmap: Bitmap, context: Context): Boolean {
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
