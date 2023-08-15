package com.example.playlistmaker.ui.library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.library.fragments.LibraryFavoritesFragment
import com.example.playlistmaker.ui.library.fragments.LibraryPlaylistsFragment

class LibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return LIBRARY_VIEW_PAGER_ITEM_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LibraryFavoritesFragment.newInstance()
            1 -> LibraryPlaylistsFragment.newInstance()
            else -> LibraryPlaylistsFragment.newInstance()
        }
    }

    companion object {
        private const val LIBRARY_VIEW_PAGER_ITEM_COUNT = 2
    }
}