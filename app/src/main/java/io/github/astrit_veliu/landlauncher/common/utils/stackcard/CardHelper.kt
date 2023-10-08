package io.github.astrit_veliu.landlauncher.common.utils.stackcard

import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.SmoothScroller.ScrollVectorProvider
import java.security.InvalidParameterException
import kotlin.math.ceil
import kotlin.math.floor

class CardHelper : LinearSnapHelper() {
    private var recyclerView: RecyclerView? = null

    @Throws(IllegalStateException::class) override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
        if (recyclerView != null && recyclerView.layoutManager !is CardLayoutManager) {
            throw InvalidParameterException("LayoutManager must be instance of CardSliderLayoutManager")
        }
        this.recyclerView = recyclerView
    }

    override fun findTargetSnapPosition(layoutManager: LayoutManager, velocityX: Int, velocityY: Int): Int {
        val lm: CardLayoutManager = layoutManager as CardLayoutManager
        val itemCount: Int = lm.itemCount
        if (itemCount == 0) {
            return RecyclerView.NO_POSITION
        }
        val vectorProvider = layoutManager as ScrollVectorProvider
        val vectorForEnd = vectorProvider.computeScrollVectorForPosition(itemCount - 1) ?: return RecyclerView.NO_POSITION
        val distance = calculateScrollDistance(velocityX, velocityY)[0]
        var deltaJump: Int
        deltaJump = if (distance > 0) {
            floor(distance / lm.cardWidth.toDouble()).toInt()
        } else {
            ceil(distance / lm.cardWidth.toDouble()).toInt()
        }
        val deltaSign = Integer.signum(deltaJump)
        deltaJump = deltaSign * Math.min(3, Math.abs(deltaJump))
        if (vectorForEnd.x < 0) {
            deltaJump = -deltaJump
        }
        if (deltaJump == 0) {
            return RecyclerView.NO_POSITION
        }
        val currentPosition: Int = lm.activeCardPosition
        if (currentPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION
        }
        var targetPos = currentPosition + deltaJump
        if (targetPos < 0 || targetPos >= itemCount) {
            targetPos = RecyclerView.NO_POSITION
        }
        return targetPos
    }

    override fun findSnapView(layoutManager: LayoutManager): View? {
        return (layoutManager as CardLayoutManager).topView
    }

    override fun calculateDistanceToFinalSnap(
        layoutManager: LayoutManager,
        targetView: View
    ): IntArray? {
        val lm: CardLayoutManager = layoutManager as CardLayoutManager
        val viewLeft: Int = lm.getDecoratedLeft(targetView)
        val activeCardLeft: Int = lm.activeCardLeft
        val activeCardCenter: Int = lm.activeCardLeft + lm.cardWidth / 2
        val activeCardRight: Int = lm.activeCardLeft + lm.cardWidth
        val out = intArrayOf(0, 0)
        if (viewLeft < activeCardCenter) {
            val targetPos: Int = lm.getPosition(targetView)
            val activeCardPos: Int = lm.activeCardPosition
            if (targetPos != activeCardPos) {
                out[0] = -(activeCardPos - targetPos) * lm.cardWidth
            } else {
                out[0] = viewLeft - activeCardLeft
            }
        } else {
            out[0] = viewLeft - activeCardRight + 1
        }
        if (out[0] != 0) {
            recyclerView?.smoothScrollBy(out[0], 0, AccelerateInterpolator())
        }
        return intArrayOf(0, 0)
    }

    override fun createSnapScroller(layoutManager: LayoutManager): LinearSmoothScroller? {
        return recyclerView?.let { (layoutManager as CardLayoutManager).getSmoothScroller(it) }
    }
}
