package ru.modulkassa.payment.library.ui

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.State

/**
 * Разделитель позиции для recyclerView
 * Не показывает разделитель для самомого последнего элемента
 */
class RecyclerViewMaterialDivider(context: Context?, orientation: Int) : DividerItemDecoration(
    context, orientation
) {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == parent.adapter?.itemCount?.minus(1)) {
            outRect.setEmpty()
        } else {
            super.getItemOffsets(outRect, view, parent, state)
        }
    }
}