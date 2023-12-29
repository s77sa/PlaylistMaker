package com.example.playlistmaker.ui.player

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.library.TrackStorage
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.library.recyclerview.PlaylistListAdapter
import com.example.playlistmaker.ui.utils.Helpers
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var track: Track
    private var trackIsFavorites: Boolean = false
    private val viewModel by viewModel<PlayerFragmentViewModel> { parametersOf(track) }

    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private var playlistList: MutableList<Playlist> = mutableListOf()

    private var playlistListAdapter: PlaylistListAdapter? = null
    private lateinit var rvPlaylist: RecyclerView

    private var playlistName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        track = (requireActivity() as TrackStorage).getTrack()!!
        initAdapters()
        initObserver()
        clickListenersInit()
        initBottomSheet()
        viewModel.saveValues()
        viewModel.preparePlayer()
        viewModel.checkFavoriteTrackJob()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun initAdapters() {
        rvPlaylist = requireView().findViewById(R.id.rv_bottom_playlist)
        playlistListAdapter = PlaylistListAdapter(
            playlistList,
            viewLifecycleOwner.lifecycleScope,
            R.layout.bottom_sheet_list_playlist_item
        )
        rvPlaylist.adapter = playlistListAdapter
    }

    private fun initOnClickBottomSheetPlaylist() {
        playlistListAdapter!!.setOnClickListener(object : PlaylistListAdapter.OnClickListener {
            override fun onClick(position: Int, playlist: Playlist) {
                playlistName = playlist.name
                callAddTrack(playlist)
            }
        })
    }

    private fun callAddTrack(playlist: Playlist) {
        viewModel.addTrackToPlaylist(track, playlist)
    }

    private fun showMessage(messageStatus: MessageStatus) {
        when (messageStatus) {
            MessageStatus.TRACK_ADDED -> {
                Toast.makeText(
                    requireContext(), getString(R.string.message_add_to_playlist).replace(
                        REPLACE_MESSAGE_PATTERN,
                        playlistName
                    ), Toast.LENGTH_SHORT
                ).show()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            MessageStatus.TRACK_EXIST -> {
                Toast.makeText(
                    requireContext(), getString(R.string.message_track_exist_playlist).replace(
                        REPLACE_MESSAGE_PATTERN,
                        playlistName
                    ), Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }
    }

    private fun initBottomSheet() {
        bottomSheetContainer = requireView().findViewById<LinearLayout>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        viewModel.getPlaylists()
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

    private fun initObserver() {
        viewModel.getLoadingValues().observe(viewLifecycleOwner) { track ->
            Log.d(TAG, "init - Player Observer - ${track.trackName}")
            writeTrackDataToView()
        }
        viewModel.getPlayingPositionLiveData().observe(viewLifecycleOwner) { value ->
            binding.tvTrackTimeCurrent.text = value
        }
        viewModel.getIsPlayingLiveData().observe(viewLifecycleOwner) {
            setButtonPlayState(it)
        }
        viewModel.getIsFavorites().observe(viewLifecycleOwner) {
            trackIsFavorites = it
            changeIconFavorites()
            Log.d(TAG, "Observe Favorites: $it")
        }
        viewModel.getPlaylistList().observe(viewLifecycleOwner) {
            Log.d("Playlist observe: ", it.size.toString())
            addPlaylistToAdapter(it)
        }
        viewModel.addedTrackInPlaylist().observe(viewLifecycleOwner) {
            showMessage(it)
        }
    }

    private fun addPlaylistToAdapter(list: List<Playlist>) {
        Log.d("Adapter add: ", list.size.toString())
        if (list != playlistList) {
            playlistList.clear()
            playlistList.addAll(list)
            val itemCount = rvPlaylist.adapter?.itemCount
            if (itemCount != null) {
                rvPlaylist.adapter?.notifyItemRangeRemoved(0, itemCount)
            }
            rvPlaylist.adapter?.notifyItemRangeChanged(0, playlistList.size)
        }
    }

    private fun setButtonPlayState(status: Boolean) {
        if (status) {
            binding.ivPlay.setImageResource(R.drawable.ic_button_pause)
        } else {
            binding.ivPlay.setImageResource(R.drawable.ic_button_play)
        }
    }

    private fun clickListenersInit() {
        binding.ivPlayerBack.setOnClickListener { finishActivity() }
        binding.ivPlay.setOnClickListener { startPlaying() }
        binding.ivFavoriteBorder.setOnClickListener { onClickFavorites() }
        binding.ivPlaylist.setOnClickListener { showBottomSheet() }
        binding.overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        requireView().findViewById<Button>(R.id.btn_new_playlist_bottom)
            .setOnClickListener {
                findNavController().navigate(R.id.createPlaylistFragment)
            }

        initOnClickBottomSheetPlaylist()
    }


    private fun showBottomSheet() {
        bottomSheetContainer.visibility = View.VISIBLE
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    private fun onClickFavorites() {
        viewModel.changeTrackStatus()
    }

    private fun changeIconFavorites() {
        if (trackIsFavorites) {
            binding.ivFavoriteBorder.setImageResource(R.drawable.ic_button_favorite_red)
        } else {
            binding.ivFavoriteBorder.setImageResource(R.drawable.ic_button_favorite_border)
        }
    }

    private fun startPlaying() {
        viewModel.playbackControl()
    }

    private fun finishActivity() {
        findNavController().popBackStack()
    }

    private fun writeTrackDataToView() {
        binding.tvTrackName.text = viewModel.getLoadingValues().value?.trackName
        binding.tvArtistName.text = viewModel.getLoadingValues().value?.artistName
        binding.tvTrackTimeValue.text =
            viewModel.getLoadingValues().value?.trackTimeMillis?.let { Helpers.millisToString(it) }
        writeCollectionName(viewModel.getLoadingValues().value?.collectionName)
        binding.tvReleaseDateValue.text =
            getReleaseDateValue(viewModel.getLoadingValues().value?.releaseDate)
        binding.tvPrimaryGenreValue.text = viewModel.getLoadingValues().value?.primaryGenreName
        binding.tvCountryValue.text = viewModel.getLoadingValues().value?.country
        viewModel.getLoadingValues().value?.artworkUrl100?.let { writeArtWorkToImageView(it) }
    }

    private fun writeCollectionName(value: String?) {
        if (!value.isNullOrEmpty()) {
            binding.tvCollectionNameValue.text = value
            binding.tvCollectionNameValue.visibility = View.VISIBLE
            binding.tvCollectionNameHeader.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getReleaseDateValue(value: String?): String? {
        val inputDate = value?.let { SimpleDateFormat("yyyy-M-d").parse(it) }
        return inputDate?.let { SimpleDateFormat("yyyy").format(it) }
    }

    private fun writeArtWorkToImageView(link: String) {
        val newLink = link.replaceAfterLast('/', REPLACE_LINK_PATTERN)
        Helpers.glideBind(newLink, binding.ivArtWorkBig)
    }

    companion object {
        private const val REPLACE_LINK_PATTERN: String = "512x512bb.jpg"
        private val TAG = PlayerFragment::class.simpleName
        private const val REPLACE_MESSAGE_PATTERN = "[playlistName]"
    }

}