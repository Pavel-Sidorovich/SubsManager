package com.pavesid.subsmanager.screens.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import com.pavesid.subsmanager.databinding.FragmentAddBinding
import com.pavesid.subsmanager.models.Subscription
import com.pavesid.subsmanager.objects.DatePicker
import com.pavesid.subsmanager.utils.clearHours
import com.pavesid.subsmanager.viewmodels.SubscriptionsViewModel
import java.util.Calendar

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding: FragmentAddBinding
        get() = _binding!!

    private val startDate by lazy { DatePicker(requireContext(), binding.addStartText) }
    private val endDate by lazy { DatePicker(requireContext(), binding.addEndText) }

    private val viewModel: SubscriptionsViewModel by activityViewModels()

    private var color: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(layoutInflater)

        configureButton()
        configureClickListener()
        configureResultListener()

        return binding.root
    }

    @MainThread
    private fun configureButton() {
        binding.apply.setOnClickListener {
            val title = binding.addTitleEdit.text.toString()
            val price = binding.addPriceEdit.text.toString().replace(',', '.')
            if (checkString(title, price) && checkDate(startDate.time, endDate.time) && checkColor(color)) {
                viewModel.addSubscription(
                    Subscription(
                        title = title,
                        price = price,
                        started = startDate.time.toString(),
                        ended = endDate.time.toString(),
                        color = color
                    )
                )
                activity?.onBackPressed()
            } else {
                Toast.makeText(requireContext(), "Check the entered data again", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.addStartText.setOnClickListener {
            startDate.setDate()
        }

        binding.addEndText.setOnClickListener {
            endDate.setDate()
        }

        binding.addClose.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    @MainThread
    private fun configureClickListener() {
        binding.colorChoice.setOnClickListener {
            val bottomDialogFragment = FragmentColorBottomDialog()
            bottomDialogFragment.show(childFragmentManager, "AvatarBottomDialogFragment")
        }
    }

    @MainThread
    private fun configureResultListener() {
        childFragmentManager.setFragmentResultListener(REQUEST_KEY, viewLifecycleOwner) {
                requestKey, bundle ->
            if (requestKey == REQUEST_KEY) {
                color = bundle.getInt(BUNDLE_KEY)
                binding.addCardView.setBackgroundResource(color)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @MainThread
    private fun checkDate(start: Long, end: Long): Boolean {
        return if (start != 0L && end != 0L) {
            (start < end && Calendar.getInstance().clearHours().timeInMillis < end)
        } else {
            false
        }
    }

    @MainThread
    private fun checkString(title: String, price: String): Boolean {
        if (title == "" || price == "") {
            return false
        }
        return try {
            price.length <= 10 && price.toDouble() >= 0 && title.length <= 20
        } catch (e: NumberFormatException) {
            false
        }
    }

    @MainThread
    private fun checkColor(@ColorRes color: Int): Boolean = color != -1

    companion object {
        const val REQUEST_KEY = "requestKey"
        const val BUNDLE_KEY = "bundleKey"
    }
}