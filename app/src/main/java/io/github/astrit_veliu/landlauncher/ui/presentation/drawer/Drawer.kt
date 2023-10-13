package io.github.astrit_veliu.landlauncher.ui.presentation.drawer

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import io.github.astrit_veliu.landlauncher.R
import io.github.astrit_veliu.landlauncher.common.utils.handlePackage
import io.github.astrit_veliu.landlauncher.common.utils.openPackage
import io.github.astrit_veliu.landlauncher.common.utils.openPlayStore
import io.github.astrit_veliu.landlauncher.ui.LauncherAppState
import io.github.astrit_veliu.landlauncher.ui.composables.SnappingLazyRow
import io.github.astrit_veliu.landlauncher.ui.theme.LauncherTypography
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Drawer(appState: LauncherAppState) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val state = rememberLazyListState()
    val focusedIndex = remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
            .navigationBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 15.dp)
        ) {
            appState.homeApps?.let { apps ->
                var selected by remember {
                    mutableIntStateOf(0)
                }
                val listState = rememberLazyListState()
                val scope = rememberCoroutineScope()

                BackHandler {
                    scope.launch {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        listState.animateScrollToItem(0)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.weight(1f)) {
                        Text(
                            modifier = Modifier.padding(start = 12.dp),
                            text = "Home",
                            style = LauncherTypography.titleMedium
                        )
                        Text(
                            modifier = Modifier.padding(start = 18.dp),
                            text = "All Apps",
                            style = LauncherTypography.bodyLarge
                        )
                        Text(
                            modifier = Modifier.padding(start = 18.dp),
                            text = "Favorites",
                            style = LauncherTypography.bodyLarge
                        )
                    }

                    Row {
                        Image(
                            modifier = Modifier
                                .padding(start = 18.dp)
                                .size(24.dp)
                                .clickable {
                                    context.handlePackage(Intent.CATEGORY_APP_BROWSER)
                                },
                            colorFilter = ColorFilter.tint(
                                MaterialTheme.colorScheme.primary,
                                BlendMode.SrcIn
                            ),
                            painter = painterResource(id = R.drawable.ic_browser_glass),
                            contentDescription = null
                        )

                        Image(
                            modifier = Modifier
                                .padding(start = 25.dp)
                                .size(24.dp)
                                .clickable {
                                    openPlayStore(context)
                                },
                            colorFilter = ColorFilter.tint(
                                MaterialTheme.colorScheme.primary,
                                BlendMode.SrcIn
                            ),
                            painter = painterResource(id = R.drawable.ic_store_glass),
                            contentDescription = null
                        )

                        Image(
                            modifier = Modifier
                                .padding(start = 25.dp, end = 12.dp)
                                .size(24.dp)
                                .clickable {
                                    context.startActivity(Intent(Settings.ACTION_SETTINGS))
                                },
                            colorFilter = ColorFilter.tint(
                                MaterialTheme.colorScheme.primary,
                                BlendMode.SrcIn
                            ),
                            painter = painterResource(id = R.drawable.ic_settings_glass),
                            contentDescription = null
                        )
                    }
                }

                Box(modifier = Modifier.weight(1f)) {

                    Column(modifier = Modifier.padding(start = 280.dp, top = 25.dp)) {
                        apps.getOrNull(selected)?.let {
                            Text(
                                text = it.appName,
                                style = LauncherTypography.titleLarge
                            )
                            Text(
                                text = it.category?.value ?: "",
                                style = LauncherTypography.bodyLarge
                            )
                            Button(modifier = Modifier.padding(top = 1.dp), onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                context.openPackage(it.packageName)
                            }) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 2.dp),
                                    text = "Open",
                                    style = LauncherTypography.bodyLarge
                                )
                            }
                        }
                    }

                    SnappingLazyRow(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp),
                        scaleCalculator = { offset, halfRowWidth ->
                            (1f - minOf(
                                1f,
                                kotlin.math.abs(offset).toFloat() / halfRowWidth
                            ) * 0.5f)
                        },
                        key = { index, item ->
                            item.appName
                        },
                        items = apps,
                        itemWidth = 200.dp,
                        onSelect = {
                            selected = it
                            scope.launch {
                                listState.animateScrollToItem(it)
                            }
                        },
                        listState = listState,
                        verticalAlignment = Alignment.Bottom
                    ) { index, item, scale ->
                        val heightInDp = animateDpAsState(
                            targetValue = if (scale >= 0.96f) 320.dp else 200.dp,
                            animationSpec = spring(DampingRatioLowBouncy),
                            label = ""
                        )

                        val interactionSource = remember { MutableInteractionSource() }

                        Box(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .width(250.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = {
                                        selected = index
                                        scope.launch {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            listState.animateScrollToItem(index)
                                        }
                                    }
                                )
                                .scale(scale.coerceAtLeast(0.94f))
                                .background(
                                    getGradientBackground(logo = item.icon),
                                    RoundedCornerShape(26.dp)
                                )
                                .clip(RoundedCornerShape(26.dp))
                                .height(heightInDp.value),
                            contentAlignment = Alignment.Center
                        ) {

                            item.banner?.let {
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    model = it,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            } ?: AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = item.icon,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp, top = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(
                            MaterialTheme.colorScheme.primary,
                            BlendMode.SrcIn
                        ),
                        painter = painterResource(id = R.drawable.ic_dpad),
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        text = "Navigate",
                        style = LauncherTypography.bodyLarge
                    )

                    Image(
                        modifier = Modifier
                            .padding(start = 18.dp)
                            .size(24.dp),
                        colorFilter = ColorFilter.tint(
                            MaterialTheme.colorScheme.primary,
                            BlendMode.SrcIn
                        ),
                        painter = painterResource(id = R.drawable.ic_a),
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        text = "Confirm",
                        style = LauncherTypography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun getGradientBackground(logo: Drawable): Brush {
    val logoBitmap = logo.toBitmap()
    val palette = generatePaletteFromBitmap(logoBitmap)
    val vibrantColor = palette?.vibrantSwatch?.rgb ?: Black.toArgb()
    val mutedColor = palette?.mutedSwatch?.rgb ?: Transparent.toArgb()
    return Brush.linearGradient(
        colors = listOf(Color(vibrantColor), Color(mutedColor)),
        start = Offset(0f, 0f),
        end = Offset(1f, 1f)
    )
}

@Composable
fun generatePaletteFromBitmap(bitmap: Bitmap): Palette? {
    return remember(bitmap) {
        val p: Palette = Palette.Builder(bitmap).generate()
        p
    }
}


