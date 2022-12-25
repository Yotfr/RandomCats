package com.yotfr.randomcats.presentation.screens.randomcats

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.yotfr.randomcats.R
import com.yotfr.randomcats.base.isPermanentlyDenied
import com.yotfr.randomcats.base.sdk29AndUp
import com.yotfr.randomcats.presentation.screens.randomcats.event.RandomCatEvent
import com.yotfr.randomcats.presentation.spacing
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RandomCatScreen(
    viewModel: RandomCatViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val permissionState = rememberPermissionState(
        permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val snackbarHostState = remember { SnackbarHostState() }

    val state by viewModel.state.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

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

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = MaterialTheme.spacing.default,
                        start = MaterialTheme.spacing.large,
                        end = MaterialTheme.spacing.large,
                        bottom = MaterialTheme.spacing.large
                    )
            ) {
                val (peekingCatTopRow, catCard, peekingCatBottomRow, buttonRow) = createRefs()

                CatPeekTopRow(
                    modifier = Modifier
                        .height(50.dp)
                        .constrainAs(peekingCatTopRow) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    peekingCatPainter = painterResource(id = R.drawable.peek_cat),
                    onPeekingCatClicked = { viewModel.onEvent(RandomCatEvent.PeekingCatClicked) },
                    peekingCatsLocation = state.peekingCatsLocation
                )
                CatCard(
                    modifier = Modifier
                        .constrainAs(catCard) {
                            top.linkTo(peekingCatTopRow.bottom)
                            bottom.linkTo(peekingCatBottomRow.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.fillToConstraints
                            width = Dimension.fillToConstraints
                        },
                    request = ImageRequest.Builder(context.applicationContext)
                        .data(state.cat?.url ?: "")
                        .crossfade(true)
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .build(),
                    catContentDescription = stringResource(id = R.string.random_cat_image),
                    isLoading = state.isCatLoading,
                    updateBitmap = {
                        bitmap = it
                    },
                    context = context
                )
                CatPeekBottomRow(
                    modifier = Modifier
                        .height(50.dp)
                        .constrainAs(peekingCatBottomRow) {
                            bottom.linkTo(buttonRow.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    peekingCatPainter = painterResource(id = R.drawable.peek_cat),
                    onPeekingCatClicked = { viewModel.onEvent(RandomCatEvent.PeekingCatClicked) },
                    peekingCatsLocation = state.peekingCatsLocation
                )
                FlipButtonRow(
                    modifier = Modifier.constrainAs(buttonRow) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    onFavouriteClicked = {
                        viewModel.onEvent(RandomCatEvent.FavoriteIconClicked)
                    },
                    onRefreshClicked = {
                        viewModel.onEvent(RandomCatEvent.LoadNewButtonClicked)
                    },
                    onSaveGalleryClicked = {
                        if (hasWriteStoragePermission()) {
                            coroutineScope.launch {
                                bitmap?.let {
                                    val isSuccess =
                                        saveMediaToStorage(
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
                    refreshIcon = Icons.Filled.Close,
                    favouriteIcon = Icons.Filled.Favorite,
                    saveGalleryIcon = Icons.Filled.Download,
                    shareIcon = Icons.Filled.Share,
                    flipButtonsIcon = Icons.Filled.Cached,
                    refreshIconDescription = stringResource(id = R.string.load_new_cat),
                    favouriteIconDescription = stringResource(id = R.string.like),
                    saveGalleryIconDescription = stringResource(id = R.string.save_to_gallery),
                    shareIconDescription = stringResource(id = R.string.share),
                    flipButtonsIconDescription = stringResource(id = R.string.more_actions),
                    isButtonsEnabled = state.isButtonsEnabled
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
