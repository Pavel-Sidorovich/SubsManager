package com.pavesid.subsmanager.screens.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pavesid.subsmanager.R
import com.pavesid.subsmanager.models.Color
import com.pavesid.subsmanager.utils.Utils
import kotlinx.android.synthetic.main.layout_color_bottom_sheet.color_recycler_view

class FragmentColorBottomDialog : BottomSheetDialogFragment(), ColorAdapter.ColorListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_color_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        color_recycler_view.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = ColorAdapter(Utils.COLORS, this@FragmentColorBottomDialog)
        }
    }

    override fun colorClicked(color: Color) {
        setFragmentResult(AddFragment.REQUEST_KEY, bundleOf(AddFragment.BUNDLE_KEY to color.color))
    }
}