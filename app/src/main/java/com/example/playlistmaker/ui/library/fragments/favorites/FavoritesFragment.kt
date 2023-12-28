package com.example.playlistmaker.ui.library.fragments.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.databinding.FragmentLibraryFavoritesBinding
import com.example.playlistmaker.domain.library.TrackStorage
import com.example.playlistmaker.ui.search.recyclerview.TrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesFragmentViewModel by viewModel<FavoritesFragmentViewModel>()
    private var _binding: FragmentLibraryFavoritesBinding? = null
    private val binding get() = _binding!!
    private var trackList: MutableList<Track> = mutableListOf()
    private var rvTrackListAdapter: TrackListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        initObservers()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavoriteTracks()
    }

    private fun initListeners() {
        onClickRVItem()
    }

    private fun onClickRVItem() {
        rvTrackListAdapter?.setOnClickListener(object : TrackListAdapter.OnClickListener {
            override fun onClick(position: Int, track: Track) {
                callPlayerFragment(track)
            }
        })
    }

    private fun callPlayerFragment(track: Track){
        (requireActivity() as TrackStorage).setTrack(track)
        findNavController().navigate(R.id.playerFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initAdapters() {
        rvTrackListAdapter = TrackListAdapter(trackList, viewLifecycleOwner.lifecycleScope)
        binding.rvFavorites.adapter = rvTrackListAdapter
    }

    private fun addFavoritesListToRecycle(list: List<Track>) {
        if (list != trackList) {
            trackList.clear()
            trackList.addAll(list)
            val itemCount = binding.rvFavorites.adapter?.itemCount
            if (itemCount != null) {
                binding.rvFavorites.adapter?.notifyItemRangeRemoved(0, itemCount)
            }
            binding.rvFavorites.adapter?.notifyItemRangeChanged(0, trackList.size)
        }
    }

    private fun initObservers() {
        viewModel.trackList.observe(viewLifecycleOwner) {
            addFavoritesListToRecycle(it)
            showInvisibleLayout()
        }
    }

    private fun showInvisibleLayout() {
        if (trackList.size > 0) {
            binding.rvFavorites.visibility = View.VISIBLE
            binding.layoutIsEmpty.visibility = View.GONE
        } else {
            binding.rvFavorites.visibility = View.GONE
            binding.layoutIsEmpty.visibility = View.VISIBLE
        }
    }

    companion object {
        private val TAG = FavoritesFragment::class.simpleName!!
        fun newInstance() = FavoritesFragment().apply {
            arguments = Bundle()
        }
    }
}