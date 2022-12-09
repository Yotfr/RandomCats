package com.yotfr.randomcats.presentation.screens.randomcats

import android.Manifest
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
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.randomcats.event.RandomCatEvent
import com.yotfr.randomcats.presentation.screens.randomcats.model.PeekingCatsLocations
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@Composable
fun RandomCatScreen(
    viewModel: RandomCatViewModel = hiltViewModel()
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

    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 0.dp,
                start = 32.dp,
                end = 32.dp,
                bottom = 32.dp
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
            peekingCatPainter = painterResource(id = R.drawable.card_cat_peeking),
            onPeekingCatClicked = { viewModel.onEvent(RandomCatEvent.ChangePeekingCatLocation) },
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
            catUrl = state.cat?.url ?: "",
            catContentDescription = stringResource(id = R.string.random_cat_image),
            isLoading = state.isCatLoading,
            loadingPlaceholderPainter = painterResource(id = R.drawable.card_cat_placeholder)
        )
        CatPeekBottomRow(
            modifier = Modifier
                .height(50.dp)
                .constrainAs(peekingCatBottomRow) {
                    bottom.linkTo(buttonRow.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            peekingCatPainter = painterResource(id = R.drawable.card_cat_peeking),
            onPeekingCatClicked = { viewModel.onEvent(RandomCatEvent.ChangePeekingCatLocation) },
            peekingCatsLocation = state.peekingCatsLocation
        )
        FlipButtonRow(
            modifier = Modifier.constrainAs(buttonRow) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onFavouriteClicked = {
                viewModel.onEvent(RandomCatEvent.FavCat)
            },
            onRefreshClicked = {
                viewModel.onEvent(RandomCatEvent.GetNewCat)
            },
            onSaveGalleryClicked = {
                if (hasWriteStoragePermission) {
                    coroutineScope.launch {
                        val bitMap = getBitmapFromUrl(
                            url = state.cat?.url ?: throw IllegalArgumentException(
                                "Trying to convert null value"
                            ),
                            context = context
                        )
                        saveMediaToStorage(
                            bitmap = bitMap,
                            fileId = state.cat!!.id,
                            context = context
                        )
                    }
                } else {
                    permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            },
            onShareClicked = {
                coroutineScope.launch {
                    val bitMap = getBitmapFromUrl(
                        url = state.cat?.url ?: throw IllegalArgumentException(
                            "Trying to convert null value"
                        ),
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
            refreshIcon = Icons.Filled.Close,
            favouriteIcon = Icons.Filled.Favorite,
            saveGalleryIcon = Icons.Filled.Download,
            shareIcon = Icons.Filled.Share,
            flipButtonsIcon = Icons.Filled.Cached,
            refreshIconDescription = stringResource(id = R.string.load_new_cat),
            favouriteIconDescription = stringResource(id = R.string.like),
            saveGalleryIconDescription = stringResource(id = R.string.save_to_gallery),
            shareIconDescription = stringResource(id = R.string.share),
            flipButtonsIconDescription = stringResource(id = R.string.more_actions)
        )
    }
}

@Composable
fun CatCard(
    modifier: Modifier,
    catUrl: String,
    catContentDescription: String,
    isLoading: Boolean,
    loadingPlaceholderPainter: Painter
) {
    ElevatedCard(
        modifier = modifier
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(catUrl)
                .crossfade(true)
                .build(),
            contentDescription = catContentDescription,
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.Center
        ) {
            val painterState = painter.state
            if (isLoading || painterState is AsyncImagePainter.State.Loading ||
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
}

@Composable
fun CatPeekTopRow(
    modifier: Modifier,
    peekingCatPainter: Painter,
    onPeekingCatClicked: () -> Unit,
    peekingCatsLocation: PeekingCatsLocations
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = peekingCatsLocation == PeekingCatsLocations.TOP_LEFT,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                Image(
                    modifier = Modifier
                        .clickable {
                            onPeekingCatClicked()
                        }
                        .height(50.dp),
                    painter = peekingCatPainter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = peekingCatsLocation == PeekingCatsLocations.TOP_CENTER,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                Image(
                    modifier = Modifier
                        .clickable {
                            onPeekingCatClicked()
                        }
                        .height(50.dp),
                    painter = peekingCatPainter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = peekingCatsLocation == PeekingCatsLocations.TOP_RIGHT,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                Image(
                    modifier = Modifier
                        .clickable {
                            onPeekingCatClicked()
                        }
                        .height(50.dp),
                    painter = peekingCatPainter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun CatPeekBottomRow(
    modifier: Modifier,
    peekingCatPainter: Painter,
    onPeekingCatClicked: () -> Unit,
    peekingCatsLocation: PeekingCatsLocations
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = peekingCatsLocation == PeekingCatsLocations.BOTTOM_LEFT,
                enter = slideInVertically { -it } + fadeIn(),
                exit = shrinkVertically { -it } + fadeOut()
            ) {
                Image(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = 180f
                        }
                        .clickable {
                            onPeekingCatClicked()
                        }
                        .height(50.dp),
                    painter = peekingCatPainter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = peekingCatsLocation == PeekingCatsLocations.BOTTOM_CENTER,
                enter = slideInVertically { -it } + fadeIn(),
                exit = shrinkVertically { -it } + fadeOut()
            ) {
                Image(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = 180f
                        }
                        .clickable {
                            onPeekingCatClicked()
                        }
                        .height(50.dp),
                    painter = peekingCatPainter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = peekingCatsLocation == PeekingCatsLocations.BOTTOM_RIGHT,
                enter = slideInVertically { -it } + fadeIn(),
                exit = shrinkVertically { -it } + fadeOut()
            ) {
                Image(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = 180f
                        }
                        .clickable {
                            onPeekingCatClicked()
                        }
                        .height(50.dp),
                    painter = peekingCatPainter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun FlipButtonRow(
    onFavouriteClicked: () -> Unit,
    onRefreshClicked: () -> Unit,
    onSaveGalleryClicked: () -> Unit,
    onShareClicked: () -> Unit,
    refreshIcon: ImageVector,
    favouriteIcon: ImageVector,
    saveGalleryIcon: ImageVector,
    shareIcon: ImageVector,
    flipButtonsIcon: ImageVector,
    refreshIconDescription: String,
    favouriteIconDescription: String,
    saveGalleryIconDescription: String,
    shareIconDescription: String,
    flipButtonsIconDescription: String,
    modifier: Modifier
) {
    var flipState by remember {
        mutableStateOf(ButtonFace.Front)
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FlipButton(
            buttonFace = flipState,
            onClick = { buttonFace ->
                when (buttonFace) {
                    ButtonFace.Front -> {
                        onRefreshClicked()
                    }
                    ButtonFace.Back -> {
                        onSaveGalleryClicked()
                    }
                }
            },
            backIcon = saveGalleryIcon,
            backIconDescription = saveGalleryIconDescription,
            frontIcon = refreshIcon,
            frontIconDescription = refreshIconDescription
        )
        IconButton(
            onClick = { flipState = flipState.next }
        ) {
            Icon(
                flipButtonsIcon,
                contentDescription = flipButtonsIconDescription
            )
        }
        FlipButton(
            buttonFace = flipState,
            onClick = { buttonFace ->
                when (buttonFace) {
                    ButtonFace.Front -> {
                        onFavouriteClicked()
                    }
                    ButtonFace.Back -> {
                        onShareClicked()
                    }
                }
            },
            backIcon = shareIcon,
            backIconDescription = shareIconDescription,
            frontIcon = favouriteIcon,
            frontIconDescription = favouriteIconDescription
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
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES
                )
            }
            val imageUri: Uri? =
                contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
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

enum class ButtonFace(val angle: Float) {
    Front(0f) {
        override val next: ButtonFace
            get() = Back
    },
    Back(180f) {
        override val next: ButtonFace
            get() = Front
    };

    abstract val next: ButtonFace
}

enum class RotationAxis {
    AxisX,
    AxisY
}

@Composable
fun FlipButton(
    buttonFace: ButtonFace,
    onClick: (buttonFace: ButtonFace) -> Unit,
    modifier: Modifier = Modifier,
    axis: RotationAxis = RotationAxis.AxisY,
    backIcon: ImageVector,
    frontIcon: ImageVector,
    backIconDescription: String,
    frontIconDescription: String
) {
    val rotation = animateFloatAsState(
        targetValue = buttonFace.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    )
    FilledIconButton(
        onClick = { onClick(buttonFace) },
        modifier = modifier
            .height(64.dp)
            .width(64.dp)
            .graphicsLayer {
                if (axis == RotationAxis.AxisX) {
                    rotationX = rotation.value
                } else {
                    rotationY = rotation.value
                }
                cameraDistance = 12f * density
            }
    ) {
        if (rotation.value <= 90) {
            Icon(
                imageVector = frontIcon,
                contentDescription = frontIconDescription
            )
        } else {
            Icon(
                imageVector = backIcon,
                contentDescription = backIconDescription,
                modifier = Modifier
                    .graphicsLayer {
                        if (axis == RotationAxis.AxisX) {
                            rotationX = 180f
                        } else {
                            rotationY = 180f
                        }
                    }
            )
        }
    }
}
