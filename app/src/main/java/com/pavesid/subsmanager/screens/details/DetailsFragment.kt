package com.pavesid.subsmanager.screens.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.pavesid.subsmanager.databinding.FragmentDetailsBinding
import com.pavesid.subsmanager.models.Subscription
import com.pavesid.subsmanager.utils.clearHours
import com.pavesid.subsmanager.utils.setInitialDateTime
import com.pavesid.subsmanager.viewmodels.SubscriptionsViewModel
import java.util.Calendar

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() = _binding!!

    private val viewModel: SubscriptionsViewModel by activityViewModels()

    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(layoutInflater)

        val subscription = viewModel.getSubscription(position)

        initView(subscription)
        return binding.root
    }

    private fun initView(subscription: Subscription) {
        val barData = BarData(
            BarDataSet(
                listOf(
                    BarEntry(
                        0f,
                        Calendar.getInstance().clearHours().timeInMillis.toFloat() / 1_000_000_000
                    )
                ), ""
            )
        )
        barData.setDrawValues(false)

        binding.detailsChart.apply {
            description.isEnabled = false
            setPinchZoom(false)
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)
            data = barData
            axisLeft.setDrawGridLines(false)
            animateX(1500)
            legend.isEnabled = false

            xAxis.isEnabled = false
            axisRight.isEnabled = false

            axisLeft.apply {
                axisMaximum = subscription.ended.toFloat() / 1_000_000_000
                axisMinimum = subscription.started.toFloat() / 1_000_000_000
                labelCount = 2
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return ""
                    }
                }
            }
        }

        binding.detailsPrice.text = "${subscription.price}$"
        binding.detailsTitle.text = subscription.title
        binding.detailsCardView.setBackgroundResource(subscription.color)
        binding.detailsEnd.setInitialDateTime(requireContext(), subscription.ended.toLong())
        binding.detailsClose.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.detailsDelete.setOnClickListener {
            viewModel.deleteSubscription(subscription)
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                }
            }

        private const val POSITION = "position"
    }
}