package com.example.playlistmaker.ui.library.fragments.playlists.info

import android.app.ActionBar.LayoutParams
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.example.playlistmaker.domain.library.PlaylistStorage
import com.example.playlistmaker.domain.library.TrackStorage
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.search.recyclerview.TrackListAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class InfoPlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<InfoPlaylistFragmentViewModel>()

    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var bottomSheetContainerExtra: LinearLayout
    private lateinit var bottomSheetBehaviorExtra: BottomSheetBehavior<LinearLayout>

    private var tracksList: MutableList<Track> = mutableListOf()
    private var tracksAdapter: TrackListAdapter? = null
    private lateinit var rvTracks: RecyclerView

    private lateinit var deletePlaylistDialog: MaterialAlertDialogBuilder
    private lateinit var deleteTrackDialog: MaterialAlertDialogBuilder
    private var alertDialogTrackItem: Track? = null
    private var playlist: Playlist? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlist = (requireActivity() as PlaylistStorage).getPlaylist()
        getPlaylist()
        initObservers()
        initBottomSheet()
        initBottomSheetExtra()
        initAdapters()
        initDeleteTrackDialog()
        initDeletePlaylistDialog()
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

    private fun callEditPlaylist() {
        playlist?.let { (requireActivity() as PlaylistStorage).setPlaylist(it) }
        findNavController().navigate(R.id.editPlaylistFragment)
    }

    private fun callDeletePlaylist() {
        val message: String? = playlist?.name?.let {
            getString(R.string.message_delete_playlist).toString().replace(
                MESSAGE_REPLACE_PATTERN,
                it
            )
        }
        deletePlaylistDialog.setMessage(message)
        deletePlaylistDialog.show()
    }

    private fun callSharePlaylist() {
        viewModel.callSharePlaylist()
    }

    private fun callMorePlaylist() {
        bottomSheetBehaviorExtra.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    private fun initAdapters() {
        rvTracks = requireView().findViewById(R.id.rv_bottom_tracks)
        tracksAdapter = TrackListAdapter(
            tracksList,
            viewLifecycleOwner.lifecycleScope
        )
        rvTracks.adapter = tracksAdapter
    }

    private fun addTracksToAdapter(list: List<Track>) {
        if (list != tracksList) {
            val itemCount = rvTracks.adapter?.itemCount
            tracksList.clear()
            tracksList.addAll(list)
            if (itemCount != null) {
                rvTracks.adapter?.notifyItemRangeRemoved(0, itemCount)
                rvTracks.adapter?.notifyItemRangeChanged(0, tracksList.size)
            }
        }
    }

    private fun onClickTrackItem() {
        tracksAdapter?.setOnClickListener(object : TrackListAdapter.OnClickListener {
            override fun onClick(position: Int, track: Track) {
                Log.d(TAG, "Click on: ${track.trackName}")
                callPlayerFragment(track)
            }
        })

        tracksAdapter?.onLongClickListener(object : TrackListAdapter.OnLongClickListener {
            override fun onLongClick(position: Int, track: Track): Boolean {
                Log.d(TAG, "Long Click on: ${track.trackName}")
                alertDialogTrackItem = track
                deleteTrackDialog.show()
                callOverlay(true)
                return true
            }
        })
    }

    private fun initDeleteTrackDialog() {
        deleteTrackDialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogPlayListEditTheme)
                .setMessage(R.string.message_delete_track)
                .setNegativeButton(R.string.message_dialog_negative) { _, _ ->

                }
                .setPositiveButton(R.string.message_dialog_positive) { _, _ ->
                    alertDialogTrackItem?.let {
                        viewModel.callDeleteTrack(it)
                        alertDialogTrackItem = null
                    }
                }
    }

    private fun initDeletePlaylistDialog() {
        deletePlaylistDialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogPlayListEditTheme)
                .setNegativeButton(R.string.message_dialog_negative) { _, _ ->

                }
                .setPositiveButton(R.string.message_dialog_positive) { _, _ ->
                    viewModel.initDeletePlaylist()
                    findNavController().popBackStack()
                }
    }

    private fun callOverlay(enableOverlay: Boolean) {
        if (enableOverlay) {
            binding.overlay.visibility = View.VISIBLE
        } else {
            binding.overlay.visibility = View.GONE
        }
    }

    private fun callPlayerFragment(track: Track) {
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
        binding.tvPlaylistDuration.text = value
    }

    private fun setValueCount(value: String) {
        binding.tvPlaylistCount.text = value
    }

    private fun initClickListeners() {
        binding.playlistEditToolbar.setNavigationOnClickListener {
            finishFragment()
        }
        onClickTrackItem()
        deleteTrackDialog.setOnDismissListener {
            callOverlay(false)
        }
        binding.ivPlaylistShare.setOnClickListener {
            callSharePlaylist()
        }
        binding.ivPlaylistMore.setOnClickListener {
            callMorePlaylist()
        }
        binding.overlay.setOnClickListener {
            bottomSheetBehaviorExtra.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.root.findViewById<TextView>(R.id.tv_extra_share).setOnClickListener {
            callSharePlaylist()
        }
        binding.root.findViewById<TextView>(R.id.tv_extra_edit).setOnClickListener {
            callEditPlaylist()
        }
        binding.root.findViewById<TextView>(R.id.tv_extra_delete).setOnClickListener {
            callDeletePlaylist()
        }
    }

    private fun finishFragment() {
        findNavController().popBackStack()
    }

    private fun getPlaylist() {
        val playlist = (requireActivity() as PlaylistStorage).getPlaylist()
        if (playlist != null) {
            viewModel.setPlaylist(playlist)
            Log.d(TAG, "getPlaylist: ${playlist.name}")
        }
    }

    private fun initBottomSheet() {
        bottomSheetContainer = requireView().findViewById<LinearLayout>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
    }

    private fun initBottomSheetExtra() {
        bottomSheetContainerExtra =
            requireView().findViewById<LinearLayout>(R.id.bottom_sheet_extra)
        bottomSheetBehaviorExtra = BottomSheetBehavior.from(bottomSheetContainerExtra)
        bottomSheetBehaviorExtra.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehaviorExtra.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        binding.overlay.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    companion object {
        private val TAG = InfoPlaylistFragment::class.java.simpleName
        private const val MESSAGE_REPLACE_PATTERN = "[playlistName]"
    }

}




