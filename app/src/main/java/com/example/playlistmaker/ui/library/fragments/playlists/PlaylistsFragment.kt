package com.example.playlistmaker.ui.library.fragments.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.databinding.FragmentLibraryPlaylistsBinding
import com.example.playlistmaker.ui.library.fragments.playlists.recyclerview.PlaylistListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentLibraryPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistsFragmentViewModel by viewModel<PlaylistsFragmentViewModel>()

    private var playlistList: MutableList<Playlist> = mutableListOf()
    private var playlistListAdapter: PlaylistListAdapter? = null
    private val recyclerView: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initListeners()
        initAdapters()
        viewModel.getPlaylists()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initObservers() {
        viewModel.playlistList.observe(viewLifecycleOwner) {
            addPlaylistToAdapter(it)
        }
    }

    private fun initListeners() {
        binding.btnNewPlaylist.setOnClickListener {
            callAddNewPlaylist()
        }
    }

    private fun callAddNewPlaylist() {
        findNavController().navigate(R.id.createPlaylistFragment)
    }

    private fun initAdapters() {
        playlistListAdapter = PlaylistListAdapter(playlistList, viewLifecycleOwner.lifecycleScope)
        binding.rvPlaylist.layoutManager = GridLayoutManager(context, RECYCLERVIEW_COLUMN)
        binding.rvPlaylist.adapter = playlistListAdapter
    }

    private fun addPlaylistToAdapter(list: List<Playlist>) {
        if (list != playlistList) {
            playlistList.clear()
            playlistList.addAll(list)
            val itemCount = binding.rvPlaylist.adapter?.itemCount
            if (itemCount != null) {
                binding.rvPlaylist.adapter?.notifyItemRangeRemoved(0, itemCount)
            }
            binding.rvPlaylist.adapter?.notifyItemRangeChanged(0, playlistList.size)
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment().apply {
            arguments = Bundle()
        }

        private const val RECYCLERVIEW_COLUMN = 2

    }
}