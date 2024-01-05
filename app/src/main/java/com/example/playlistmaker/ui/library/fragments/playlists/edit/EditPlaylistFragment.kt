package com.example.playlistmaker.ui.library.fragments.playlists.edit

import android.app.ActionBar.LayoutParams
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistEditBinding
import com.example.playlistmaker.domain.library.PlaylistStorage
import com.example.playlistmaker.domain.library.TrackStorage
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.library.fragments.playlists.PlaylistsFragment
import com.example.playlistmaker.ui.library.recyclerview.PlaylistListAdapter
import com.example.playlistmaker.ui.search.recyclerview.TrackListAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditPlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<EditPlaylistFragmentViewModel>()

    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private var tracksList: MutableList<Track> = mutableListOf()
    private var tracksAdapter: TrackListAdapter? = null
    private lateinit var rvTracks: RecyclerView

    //private lateinit var alertDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistEditBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as PlaylistStorage).getPlaylist()
        getPlaylist()
        initObservers()
        initBottomSheet()
        initAdapters()
        initClickListeners()
    }

    private fun initObservers() {
        viewModel.playlistName.observe(viewLifecycleOwner) {
            setValueName(it)
        }
        viewModel.playlistDescription.observe(viewLifecycleOwner) {
            setValueDescription(it)
        }
        viewModel.playlistTotalMinute.observe(viewLifecycleOwner) {
            setValueTime(it)
        }
        viewModel.playlistTrackCount.observe(viewLifecycleOwner) {
            setValueCount(it)
        }
        viewModel.fileUri.observe(viewLifecycleOwner) {
            setImageToImageView(it)
        }
        viewModel.tracksInPlaylist.observe(viewLifecycleOwner) {
            addTracksToAdapter(it)
        }
    }

    private fun initAdapters() {
        rvTracks = requireView().findViewById(R.id.rv_bottom_tracks)
        tracksAdapter = TrackListAdapter(
            tracksList,
            viewLifecycleOwner.lifecycleScope
        )
        rvTracks.adapter = tracksAdapter
    }

    private fun addTracksToAdapter(list: List<Track>){
        if(list != tracksList){
            tracksList.clear()
            tracksList.addAll(list)
            val itemCount = rvTracks.adapter?.itemCount
            if (itemCount != null){
                rvTracks.adapter?.notifyItemRangeRemoved(0, itemCount)
                rvTracks.adapter?.notifyItemRangeChanged(0, tracksList.size)
            }
        }
    }

    private fun onClickTrackItem(){
        tracksAdapter?.setOnClickListener(object : TrackListAdapter.OnClickListener {
            override fun onClick(position: Int, track: Track) {
                Log.d(TAG, "Click on: ${track.trackName}")
                callPlayerFragment(track)
            }
        })

        tracksAdapter?.onLongClickListener(object : TrackListAdapter.OnLongClickListener{
            override fun onLongClick(position: Int, track: Track): Boolean {
                Log.d(TAG, "Long Click on: ${track.trackName}")
                return true
            }

        })
    }

    private fun callPlayerFragment(track: Track){
        (requireActivity() as TrackStorage).setTrack(track)
        findNavController().navigate(R.id.playerFragment)
    }

    private fun setImageToImageView(uri: Uri?) {
        if (uri != null) {
            binding.ivPlaylistArt.setImageURI(uri)
            binding.ivPlaylistArt.layoutParams.width = LayoutParams.MATCH_PARENT
            binding.ivPlaylistArt.layoutParams.height = LayoutParams.MATCH_PARENT
        }

    }

    private fun setValueName(value: String) {
        binding.tvPlaylistName.text = value
    }

    private fun setValueDescription(value: String?) {
        if (!value.isNullOrEmpty()) {
            binding.tvPlaylistDesc.text = value
            binding.tvPlaylistDesc.visibility = View.VISIBLE
        } else {
            binding.tvPlaylistDesc.visibility = View.GONE
        }
    }

    private fun setValueTime(value: String) {
        Log.d(TAG, "setValueTime: $value")
        binding.tvPlaylistDuration.text = value
    }

    private fun setValueCount(value: String) {
        Log.d(TAG, "setValueCount: $value")
        binding.tvPlaylistCount.text = value
    }

    private fun initClickListeners() {
        binding.playlistEditToolbar.setNavigationOnClickListener {
            finishFragment()
        }
        onClickTrackItem()
    }

    private fun finishFragment() {
        findNavController().popBackStack()
    }

    private fun getPlaylist() {
        val playlist = (requireActivity() as PlaylistStorage).getPlaylist()
        if (playlist != null) {
            viewModel.setPlaylist(playlist)
        }
    }

    private fun initBottomSheet() {
        bottomSheetContainer = requireView().findViewById<LinearLayout>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
    }

    companion object {
        private val TAG = EditPlaylistFragment::class.java.simpleName
    }

}


