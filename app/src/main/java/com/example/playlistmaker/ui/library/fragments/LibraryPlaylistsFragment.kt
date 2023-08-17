package com.example.playlistmaker.ui.library.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryPlaylistsFragment : Fragment() {

    private val viewModel: FragmentPlaylistsViewModel by viewModel<FragmentPlaylistsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_library_playlists, container, false)
    }

    private fun initObservers() {
        viewModel.isEditing.observe(viewLifecycleOwner) {
            //Empty
        }
    }

    companion object {
        fun newInstance() = LibraryPlaylistsFragment().apply {
            arguments = Bundle()
        }
    }
}