package com.pavesid.subsmanager.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavesid.subsmanager.activities.MainActivity
import com.pavesid.subsmanager.databinding.FragmentMainBinding
import com.pavesid.subsmanager.screens.add.AddFragment
import com.pavesid.subsmanager.screens.details.DetailsFragment
import com.pavesid.subsmanager.viewmodels.SubscriptionsViewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    private val subscriptionsAdapter by lazy {
        SubscriptionsAdapter {
            MainActivity.mainActivity.replaceFragment(DetailsFragment.newInstance(it))
        }
    }

    private val viewModel: SubscriptionsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater)

        binding.fab.setOnClickListener {
            MainActivity.mainActivity.replaceFragment(AddFragment())
        }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = subscriptionsAdapter
        }

        initViewModel()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @MainThread
    private fun initViewModel() {
        viewModel.subscriptionsLiveData.observe(viewLifecycleOwner, Observer {
            subscriptionsAdapter.update(it)
        })
    }
}