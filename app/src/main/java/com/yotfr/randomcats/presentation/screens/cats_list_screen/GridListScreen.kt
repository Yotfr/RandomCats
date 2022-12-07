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
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
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
import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.presentation.screens.cats_list_screen.event.CatListEvent
import com.yotfr.randomcats.presentation.screens.cats_list_screen.model.CatListModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridListScreen(
    viewModel: CatListViewModel = hiltViewModel(),
    navController: NavController,
    currentIndex: String?
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

    val lazyGridState = rememberLazyGridState()


    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
    ) {
        itemsIndexed(state.groupedCats) { index,cat ->
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
                    viewModel.onEvent(
                        CatListEvent.DeleteCatFromFavorite(
                            cat = it
                        )
                    )
                },
                onGridClicked = {
                    viewModel.onEvent(
                        CatListEvent.GridListItemClicked(
                            selectedIndex = index
                        )
                    )
                },
                catContentDescription = stringResource(id = R.string.your_favorite_cat),
                loadingPlaceholderPainter = painterResource(id = R.drawable.card_cat_placeholder),
                downloadContentDescription = stringResource(id = R.string.save_to_gallery),
                shareContentDescription = stringResource(id = R.string.share),
                deleteFromFavoriteContentDescription = stringResource(id = R.string.delete_from_favorite),
                expandActionsContentDescription = stringResource(id = R.string.more_actions)
            )
        }
    }
}


@Composable
fun GridCell(
    cat: CatListModel,
    onDownloadClicked: (cat: CatListModel) -> Unit,
    onShareClicked: (cat: CatListModel) -> Unit,
    onDeleteFromFavClicked: (cat: CatListModel) -> Unit,
    onGridClicked: () -> Unit,
    catContentDescription: String,
    loadingPlaceholderPainter: Painter,
    downloadContentDescription: String,
    shareContentDescription: String,
    deleteFromFavoriteContentDescription: String,
    expandActionsContentDescription: String
) {

    var isExpanded by remember {
        mutableStateOf(false)
    }

    ElevatedCard {
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
        ActionButtons(
            onDownloadClicked = { onDownloadClicked(cat) },
            onShareClicked = { onShareClicked(cat) },
            onDeleteFromFavClicked = { onDeleteFromFavClicked(cat) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            downloadContentDescription = downloadContentDescription,
            shareContentDescription = shareContentDescription,
            deleteFromFavoriteContentDescription = deleteFromFavoriteContentDescription,
            expandActionsContentDescription = expandActionsContentDescription,
            isExpanded = isExpanded,
            onExpandClicked = {
                isExpanded = !isExpanded
            }
        )
    }
}

@Composable
fun ActionButtons(
    modifier: Modifier,
    onDownloadClicked: () -> Unit,
    downloadContentDescription: String,
    onShareClicked: () -> Unit,
    shareContentDescription: String,
    onDeleteFromFavClicked: () -> Unit,
    deleteFromFavoriteContentDescription: String,
    expandActionsContentDescription: String,
    isExpanded: Boolean,
    onExpandClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .animateContentSize()
    ) {
        if (isExpanded) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onDownloadClicked()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Download,
                        contentDescription = downloadContentDescription
                    )
                }
                IconButton(
                    onClick = {
                        onShareClicked()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = shareContentDescription
                    )
                }
                IconButton(
                    onClick = {
                        onDeleteFromFavClicked()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = deleteFromFavoriteContentDescription
                    )
                }
            }
        }
        IconButton(
            onClick = {
                onExpandClicked()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Filled.ExpandLess
                else Icons.Filled.ExpandMore,
                contentDescription = expandActionsContentDescription
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