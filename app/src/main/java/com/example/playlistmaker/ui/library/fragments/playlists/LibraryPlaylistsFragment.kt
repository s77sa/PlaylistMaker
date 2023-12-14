package com.example.playlistmaker.ui.library.fragments.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.accessibility.AccessibilityViewCommand.MoveWindowArguments
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryPlaylistsBinding
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.library.fragments.favorites.LibraryFavoritesFragment
import com.example.playlistmaker.ui.playlist.CreatePlaylistFragment
import com.example.playlistmaker.ui.root.RootActivity
import org.koin.androidx.scope.fragmentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryPlaylistsFragment : Fragment() {

    private var _binding: FragmentLibraryPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FragmentPlaylistsViewModel by viewModel<FragmentPlaylistsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initObservers() {
        viewModel.isEditing.observe(viewLifecycleOwner) {
            //Empty
        }
    }

    private fun initListeners(){
        binding.btnNewPlaylist.setOnClickListener{
            callAddNewPlaylist()
        }
    }

    private fun callAddNewPlaylist(){
        findNavController().navigate(R.id.createPlaylistFragment)
    }

    companion object {
        fun newInstance() = LibraryPlaylistsFragment().apply {
            arguments = Bundle()
        }
    }
}