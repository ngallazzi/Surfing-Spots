package com.ngallazzi.surfingspots.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.core.view.isVisible

class LayoutUtils {
    companion object{
        fun crossFade(viewToShow: View, viewToHide: View, duration: Int = 500) {
            if (!viewToShow.isVisible) {
                viewToHide.visibility = View.VISIBLE
                viewToShow.fadeIn(duration)
                // Animate the loading viewToShow to 0% opacity. After the animation ends,
                // set its visibility to GONE as an optimization step (it won't
                // participate in layout passes, etc.)
                viewToHide.animate()
                    .alpha(0f)
                    .setDuration(duration.toLong())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            viewToHide.visibility = View.GONE
                        }
                    })
            }
        }
    }
}