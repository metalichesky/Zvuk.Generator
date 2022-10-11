package com.metalichesky.zvuk.generator.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.metalichesky.zvuk.generator.R
import com.metalichesky.zvuk.generator.model.PlaybackState

class PlaybackButton : FrameLayout {
    companion object {
        private const val ANIMATION_DURATION = 150L
    }

    var currentPlaybackState: PlaybackState = PlaybackState.PLAY
    var view: View? = null
    var stateListener: StateListener? = null

    var currentStateBuffer: ImageView? = null
    var previousStateBuffer: ImageView? = null


    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_playback_button, this, false)
        this.addView(view)
        currentStateBuffer = view?.findViewById(R.id.ivCurrentState)
        previousStateBuffer = view?.findViewById(R.id.ivPreviousState)
        setState(PlaybackState.PLAY)
    }

    private fun getDrawableResId(state: PlaybackState): Int = when (state) {
        PlaybackState.PLAY -> R.drawable.ic_play
        PlaybackState.PAUSE -> R.drawable.ic_pause
        PlaybackState.STOP -> R.drawable.ic_stop
    }

    fun setState(newState: PlaybackState) {
        val oldState = currentPlaybackState
        currentPlaybackState = newState
        stateListener?.onChanged(oldState, newState)

        val newStateDrawable = ContextCompat.getDrawable(context, getDrawableResId(newState))
        val oldStateDrawable = ContextCompat.getDrawable(context, getDrawableResId(oldState))

        currentStateBuffer?.setImageDrawable(newStateDrawable)
        previousStateBuffer?.setImageDrawable(oldStateDrawable)

        val fadeInAnimator =
            ObjectAnimator.ofFloat(currentStateBuffer, View.ALPHA, 0.1f, 0.2f, 1f).apply {
                duration = ANIMATION_DURATION
            }
        val fadeOutAnimator =
            ObjectAnimator.ofFloat(previousStateBuffer, View.ALPHA, 0.9f, 0.8f, 0f).apply {
                duration = ANIMATION_DURATION
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator) {}
                    override fun onAnimationEnd(p0: Animator) {}
                    override fun onAnimationCancel(p0: Animator) {}
                    override fun onAnimationStart(p0: Animator) {}
                })
            }
        fadeInAnimator.start()
        fadeOutAnimator.start()

        swapBuffers()
    }

    private fun swapBuffers() {
        val temp = currentStateBuffer
        currentStateBuffer = previousStateBuffer
        previousStateBuffer = temp
    }

    interface StateListener {
        fun onChanged(previousState: PlaybackState, newState: PlaybackState)
    }
}
