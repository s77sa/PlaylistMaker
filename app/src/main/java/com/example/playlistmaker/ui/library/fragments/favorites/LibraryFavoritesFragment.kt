package com.example.playlistmaker.ui.library.fragments.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.data.models.Track
import com.example.playlistmaker.databinding.FragmentLibraryFavoritesBinding
import com.example.playlistmaker.ui.search.recyclerview.TrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class LibraryFavoritesFragment : Fragment() {

    companion object {
        private val TAG = LibraryFavoritesFragment::class.simpleName!!
        fun newInstance() = LibraryFavoritesFragment().apply {
            arguments = Bundle()
        }
    }

    private val viewModel: FragmentFavoritesViewModel by viewModel<FragmentFavoritesViewModel>()
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
                viewModel.callPlayerActivity(track)
            }
        })
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
}