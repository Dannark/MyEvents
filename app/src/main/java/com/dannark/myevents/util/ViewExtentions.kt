package com.dannark.myevents.util

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dannark.myevents.R
import com.dannark.myevents.ScrollChildSwipeRefreshLayout

fun Fragment.setupRefreshLayout(
        refreshLayout: ScrollChildSwipeRefreshLayout,
        scrollUpChild: View? = null
) {
    refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireActivity(), R.color.design_default_color_primary),
            ContextCompat.getColor(requireActivity(), R.color.design_default_color_secondary),
            ContextCompat.getColor(requireActivity(), R.color.design_default_color_primary_dark)
    )
    // Set the scrolling view in the custom SwipeRefreshLayout.
    scrollUpChild?.let {
        refreshLayout.scrollUpChild = it
    }
}
