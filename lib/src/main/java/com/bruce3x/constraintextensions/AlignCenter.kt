package com.bruce3x.constraintextensions

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout

class AlignCenter @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintHelper(context, attrs, defStyleAttr) {

    private var orientation = 0

    override fun init(attrs: AttributeSet?) {
        super.init(attrs)
        visibility = View.GONE
        mUseViewMeasure = false

        context.obtainStyledAttributes(attrs, R.styleable.AlignCenter).apply {
            orientation = getInt(R.styleable.AlignCenter_align_orientation, 0)
            recycle()
        }
    }

    override fun updatePreLayout(container: ConstraintLayout) {
        val ids = referencedIds
        if (ids.isEmpty()) return

        var parentId = ids.first()
        var index = 1
        while (index < ids.size) {
            val childId = ids[index++]
            applyAlign(container, parentId, childId)
            parentId = childId
        }
    }

    private fun applyAlign(container: ConstraintLayout, parentId: Int, childId: Int) {
        val parent = container.findViewById<View>(parentId)
        val child = container.findViewById<View>(childId)

        val relatedId = if (parent == container) ConstraintLayout.LayoutParams.PARENT_ID else parentId
        val lp = child.layoutParams as ConstraintLayout.LayoutParams

        if (orientation and ORIENTATION_HORIZONTAL != 0) {
            lp.topToTop = relatedId
            lp.bottomToBottom = relatedId
        }
        if (orientation and ORIENTATION_VERTICAL != 0) {
            lp.startToStart = relatedId
            lp.endToEnd = relatedId
        }

        child.layoutParams = lp
    }

    companion object {
        private const val ORIENTATION_HORIZONTAL = 1 shl 0
        private const val ORIENTATION_VERTICAL = 1 shl 1
        private const val ORIENTATION_BOTH = ORIENTATION_HORIZONTAL or ORIENTATION_VERTICAL
    }
}
