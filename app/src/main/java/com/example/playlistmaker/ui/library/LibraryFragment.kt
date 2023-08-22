package com.example.playlistmaker.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment : Fragment() {

    private lateinit var binding: FragmentLibraryBinding
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.libraryViewPager.adapter =
            LibraryViewPagerAdapter(childFragmentManager, lifecycle)

        initTabMediator()
    }

    private fun initTabMediator() {
        tabLayoutMediator =
            TabLayoutMediator(binding.libraryLayout, binding.libraryViewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.library_tabLayout_favoritesTracks)
                    1 -> tab.text = getString(R.string.library_tabLayout_playLists)
                }
            }
        tabLayoutMediator.attach()
    }
}