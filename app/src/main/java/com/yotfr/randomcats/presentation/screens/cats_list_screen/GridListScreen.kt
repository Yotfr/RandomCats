package com.yotfr.randomcats.presentation.screens.cats_list_screen

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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.yotfr.randomcats.R
import com.yotfr.randomcats.base.compose_ext.header
import com.yotfr.randomcats.domain.model.Cat
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


@Composable
fun GridListScreen(
    viewModel: CatListViewModel = hiltViewModel(),
    navController: NavController
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

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        state.groupedCats.forEach { entry ->

            header {
                Text(text = entry.key)
            }

            items(entry.value) { cat ->
                GridCell(
                    cat = cat,
                    onDownloadClicked = {
                        if (hasWriteStoragePermission) {
                            coroutineScope.launch {
                                val bitMap = getBitmapFromUrl(
                                    url = it.url,
                                    context = context
                                )
                                saveMediaToStorage(
                                    bitmap = bitMap,
                                    fileId = it.id,
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
                                url = it.url,
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
                    onDeleteFromFavClicked = {

                    },
                    onGridClicked = {
                        navController.navigate("pager")
                    }
                )
            }
        }
    }
}


@Composable
fun GridCell(
    cat: Cat,
    onDownloadClicked: (cat: Cat) -> Unit,
    onShareClicked: (cat: Cat) -> Unit,
    onDeleteFromFavClicked: (cat: Cat) -> Unit,
    onGridClicked: () -> Unit
) {
    Card {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cat.url)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(id = R.string.random_cat_image),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable {
                    onGridClicked()
                }
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
        ActionButtons(
            modifier = Modifier.fillMaxWidth(),
            onDownloadClicked = {
                onDownloadClicked(cat)
            },
            onShareClicked = {
                onShareClicked(cat)
            },
            onDeleteFromFavClicked = {
                onDeleteFromFavClicked(cat)
            }
        )
    }
}

@Composable
fun ActionButtons(
    onDownloadClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onDeleteFromFavClicked: () -> Unit,
    modifier: Modifier
) {

    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = { onShareClicked() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = stringResource(id = R.string.share)
            )
        }
        IconButton(onClick = { onDownloadClicked() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_download),
                contentDescription = stringResource(id = R.string.save_to_gallery)
            )
        }
        Box {
            IconButton(onClick = { dropDownMenuExpanded = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vertical),
                    contentDescription = stringResource(id = R.string.more_options)
                )
            }
            DropdownMenu(
                expanded = dropDownMenuExpanded,
                onDismissRequest = { dropDownMenuExpanded = false },
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = R.string.delete_from_favorite))
                    },
                    onClick = {
                        onDeleteFromFavClicked()
                        dropDownMenuExpanded = false
                    }
                )
            }
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

    } catch (e: Exception) {
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