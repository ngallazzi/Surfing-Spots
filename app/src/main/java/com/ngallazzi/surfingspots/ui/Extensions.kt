package com.ngallazzi.surfingspots.ui

import android.view.View

fun View.fadeIn(duration: Int = 500) {
    this.apply {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        alpha = 0f
        visibility = View.VISIBLE

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        animate()
            .alpha(1f)
            .setDuration(duration.toLong())
            .setListener(null)
    }
}