package io.github.astrit_veliu.landlauncher.ui.composables

import android.util.Log
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import android.view.KeyEvent.KEYCODE_DPAD_RIGHT
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class, ExperimentalSnapperApi::class)
@Composable
fun <T> SnappingLazyRow(
    items: List<T>,
    itemWidth: Dp,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal =
        if (!reverseLayout)
            Arrangement.Start
        else
            Arrangement.End,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    userScrollEnabled: Boolean = true,
    key: ((index: Int, item: T) -> Any)? = null,
    scaleCalculator: (Int, Float) -> Float = { offset, halfRowWidth ->
        (1f - minOf(1f, abs(offset).toFloat() / halfRowWidth) * 0.5f)
    },
    onSelect: (Int) -> Unit,
    item: @Composable (Int, T, Float) -> Unit
) {
    val layoutInfo = rememberLazyListSnapperLayoutInfo(listState)
    var apparentCurrentItem by remember {
        mutableStateOf(-1)
    }
    var isEffectLaunched by remember { mutableStateOf(false) }

   if(isEffectLaunched) LaunchedEffect(listState.isScrollInProgress, apparentCurrentItem) {
        val d = layoutInfo.currentItem
        d?.let {
            if (apparentCurrentItem > -1) {
                onSelect(apparentCurrentItem)
            }
            isEffectLaunched = false
        }
    }
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.BottomStart
    ) {
        val full = LocalConfiguration.current.screenWidthDp.dp
        val halfRowWidth = constraints.maxWidth / 2f
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            LazyRow(
                userScrollEnabled = userScrollEnabled,
                horizontalArrangement = horizontalArrangement,
                reverseLayout = reverseLayout,
                verticalAlignment = verticalAlignment,
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .onKeyEvent { event ->
                        when (event.key.nativeKeyCode) {
                            KEYCODE_DPAD_LEFT -> { // Left arrow key
                                val index = listState.firstVisibleItemIndex
                                if (index > 0) {
                                    apparentCurrentItem = index - 1
                                    isEffectLaunched = true
                                }
                                true
                            }

                            KEYCODE_DPAD_RIGHT -> { // Right arrow key
                                val index = listState.firstVisibleItemIndex
                                if (index < items.size - 1) {
                                    apparentCurrentItem = index + 1
                                    isEffectLaunched = true
                                }
                                true
                            }
                            else -> false
                        }
                    },
                flingBehavior = rememberSnapperFlingBehavior(listState)
            ) {
                itemsIndexed(
                    items,
                    key = key
                ) { i, item ->
                    val opacity by remember {
                        derivedStateOf {
                            val currentItemInfo = listState
                                .layoutInfo.visibleItemsInfo
                                .firstOrNull { it.index == i }
                                ?: return@derivedStateOf 0f
                            val offset = currentItemInfo.offset
                            scaleCalculator(offset, halfRowWidth)
                        }
                    }
                    LaunchedEffect(key1 = opacity) {
                        if (1f - opacity <= 0.1f) {
                            apparentCurrentItem = i
                            isEffectLaunched = true
                        }
                    }
                    item(i, item, opacity)
                }
                item {
                    Spacer(modifier = Modifier.width((full - itemWidth)))
                }
            }
        }
    }
}