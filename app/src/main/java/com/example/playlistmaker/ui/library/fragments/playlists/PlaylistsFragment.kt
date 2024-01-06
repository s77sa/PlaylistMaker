package com.example.playlistmaker.ui.library.fragments.playlists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryPlaylistsBinding
import com.example.playlistmaker.domain.library.PlaylistStorage
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.library.recyclerview.PlaylistListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private var _binding: FragmentLibraryPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaylistsFragmentViewModel by viewModel<PlaylistsFragmentViewModel>()

    private var playlistList: MutableList<Playlist> = mutableListOf()
    private var playlistListAdapter: PlaylistListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initAdapters()
        viewModel.getPlaylists()
        checkVisibility()
        initClickListeners()
    }

    private fun initObservers() {
        viewModel.playlistList.observe(viewLifecycleOwner) {
            addPlaylistToAdapter(it)
            checkVisibility()
        }
    }

    private fun initClickListeners() {
        binding.btnNewPlaylist.setOnClickListener {
            callAddNewPlaylist()
        }
        onClickPlaylistItem()
    }

    private fun onClickPlaylistItem() {
        playlistListAdapter?.setOnClickListener(object : PlaylistListAdapter.OnClickListener {
            override fun onClick(position: Int, playlist: Playlist) {
                Log.d(TAG, "Click to: ${playlist.name}")
                callInfoPlaylistFragment(playlist)
            }
        })
    }

    private fun callInfoPlaylistFragment(playlist: Playlist) {
        (requireActivity() as PlaylistStorage).setPlaylist(playlist)
        findNavController().navigate(R.id.infoPlaylistFragment)
    }

    private fun callAddNewPlaylist() {
        findNavController().navigate(R.id.createPlaylistFragment)
    }

    private fun initAdapters() {
        playlistListAdapter = PlaylistListAdapter(
            playlistList,
            viewLifecycleOwner.lifecycleScope,
            R.layout.playlist_item
        )
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

    private fun checkVisibility() {
        if (playlistList.size > 0) {
            binding.rvPlaylist.visibility = View.VISIBLE
            binding.favoritesIsEmpty.visibility = View.GONE
        } else {
            binding.rvPlaylist.visibility = View.GONE
            binding.favoritesIsEmpty.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment().apply {
            arguments = Bundle()
        }

        private const val RECYCLERVIEW_COLUMN = 2

        private val TAG = PlaylistsFragment::class.java.simpleName

    }
}