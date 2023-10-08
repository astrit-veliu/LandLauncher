package io.github.astrit_veliu.landlauncher.common.utils.stackcard

import android.view.View
import androidx.core.view.ViewCompat
import io.github.astrit_veliu.landlauncher.common.utils.stackcard.CardLayoutManager.ViewUpdater


open class CardUpdater : ViewUpdater {
    private var cardWidth = 0
    private var activeCardLeft = 0
    private var activeCardRight = 0
    private var activeCardCenter = 0
    private var cardsGap = 0f
    private var transitionEnd = 0
    private var transitionDistance = 0
    private var transitionRight2Center = 0f
    private var lm: CardLayoutManager? = null
    private var previewView: View? = null
    override fun onLayoutManagerInitialized(lm: CardLayoutManager) {
        this.lm = lm
        cardWidth = lm.cardWidth
        activeCardLeft = lm.activeCardLeft
        activeCardRight = lm.activeCardRight
        activeCardCenter = lm.activeCardCenter
        cardsGap = lm.cardsGap
        transitionEnd = activeCardCenter
        transitionDistance = activeCardRight - transitionEnd
        val centerBorder = (cardWidth - cardWidth * SCALE_CENTER) / 2f
        val rightBorder = (cardWidth - cardWidth * SCALE_RIGHT) / 2f
        val right2centerDistance = activeCardRight + centerBorder - (activeCardRight - rightBorder)
        transitionRight2Center = right2centerDistance - cardsGap
    }

    override fun updateView(view: View, position: Float) {
        val scale: Float
        val alpha: Float
        val z: Float
        var x = 0f
        if (position < 0) {
            val ratio = lm?.getDecoratedLeft(view)?.toFloat() ?: 1f / activeCardLeft.toFloat()
            scale = SCALE_LEFT + SCALE_CENTER_TO_LEFT * ratio
            alpha = 0.1f + ratio
            z = Z_CENTER_1 * ratio
            x = 0f
        } else if (position < 0.5f) {
            scale = SCALE_CENTER
            alpha = 1f
            z = Z_CENTER_1.toFloat()
            x = 0f
        } else if (position < 1f) {
            val viewLeft: Int = lm?.getDecoratedLeft(view) ?: 0
            val ratio = (viewLeft - activeCardCenter).toFloat() / (activeCardRight - activeCardCenter)
            scale = SCALE_CENTER - SCALE_CENTER_TO_RIGHT * ratio
            alpha = 1f
            z = Z_CENTER_2.toFloat()
            x = if (Math.abs(transitionRight2Center) < Math.abs(transitionRight2Center * (viewLeft - transitionEnd) / transitionDistance)) {
                -transitionRight2Center
            } else {
                -transitionRight2Center * (viewLeft - transitionEnd) / transitionDistance
            }
        } else {
            scale = SCALE_RIGHT
            alpha = 1f
            z = Z_RIGHT.toFloat()
            if (previewView != null) {
                previewView?.let {
                    val prevViewScale: Float
                    val prevTransition: Float
                    val prevRight: Int
                    val isFirstRight: Boolean = lm?.getDecoratedRight(it)!! <= activeCardRight
                    if (isFirstRight) {
                        prevViewScale = SCALE_CENTER
                        prevRight = activeCardRight
                        prevTransition = 0f
                    } else {
                        prevViewScale = ViewCompat.getScaleX(it)
                        prevRight = lm?.getDecoratedRight(it)!!
                        prevTransition = ViewCompat.getTranslationX(it)
                    }
                    val prevBorder = (cardWidth - cardWidth * prevViewScale) / 2
                    val currentBorder = (cardWidth - cardWidth * SCALE_RIGHT) / 2
                    val distance: Float = lm?.getDecoratedLeft(view)!! + currentBorder - (prevRight - prevBorder + prevTransition)
                    val transition = distance - cardsGap
                    x = -transition
                }
            } else {
                x = 0f
            }
        }
        ViewCompat.setScaleX(view, scale)
        ViewCompat.setScaleY(view, scale)
        ViewCompat.setZ(view, z)
        ViewCompat.setTranslationX(view, x)
        ViewCompat.setAlpha(view, alpha)
        previewView = view
    }

    protected val layoutManager: CardLayoutManager?
        get() = lm

    companion object {
        const val SCALE_LEFT = 0.65f
        const val SCALE_CENTER = 0.95f
        const val SCALE_RIGHT = 0.8f
        const val SCALE_CENTER_TO_LEFT = SCALE_CENTER - SCALE_LEFT
        const val SCALE_CENTER_TO_RIGHT = SCALE_CENTER - SCALE_RIGHT
        const val Z_CENTER_1 = 12
        const val Z_CENTER_2 = 16
        const val Z_RIGHT = 8
    }
}