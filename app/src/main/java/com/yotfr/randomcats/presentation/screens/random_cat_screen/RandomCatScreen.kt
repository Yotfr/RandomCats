package com.yotfr.randomcats.presentation.screens.random_cat_screen

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
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.yotfr.randomcats.presentation.screens.random_cat_screen.event.RandomCatEvent
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@Composable
fun PickerScreen(
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
    val state = viewModel.state.value

    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 32.dp, end = 32.dp, bottom = 84.dp)
    ) {

        val (image, buttons) = createRefs()

        Card(
            modifier = Modifier
                .constrainAs(image) {
                    bottom.linkTo(buttons.top)
                    start.linkTo(parent.start, 0.dp)
                    end.linkTo(parent.end, 0.dp)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(state.cat?.url)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.random_cat_image),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.Center
            ) {
                val painterState = painter.state
                if (state.isLoading || painterState is AsyncImagePainter.State.Loading ||
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
        ButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(buttons) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(16.dp),
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
                            fileId = state.cat.id,
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
            }
        )
    }
}

@Composable
fun ButtonRow(
    onFavouriteClicked: () -> Unit,
    onRefreshClicked: () -> Unit,
    onSaveGalleryClicked: () -> Unit,
    onShareClicked:() -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FilledIconButton(
            onClick = {
                onRefreshClicked()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_refresh),
                contentDescription = stringResource(id = R.string.load_new_cat)
            )
        }
        FilledIconButton(
            onClick = {
                onShareClicked()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = stringResource(id = R.string.share)
            )
        }
        FilledIconButton(
            onClick = {
                onSaveGalleryClicked()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_download),
                contentDescription = stringResource(id = R.string.save_to_gallery)
            )
        }
        FilledIconButton(
            onClick = {
                onFavouriteClicked()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_favourite_filled),
                contentDescription = stringResource(id = R.string.like)
            )
        }
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
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,it)
    }
}

private fun shareImage(uri: Uri, context: Context){

    val intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_STREAM,uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent.type = "image/*"
    context.startActivity(Intent.createChooser(intent,"Share image"))

}

private fun getImageToShare(bitmap: Bitmap, context: Context):Uri{
    val folder = File(context.cacheDir,"images")
    var uri:Uri? = null
    try {
        folder.mkdirs()
        val file = File(folder,"shared_image.jpg")
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, fileOutputStream)

        fileOutputStream.flush()
        fileOutputStream.close()

        uri = FileProvider.getUriForFile(context,"com.yotfr.randomcats",file)

    }catch (e: java.lang.Exception){
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


