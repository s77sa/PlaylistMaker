package com.example.playlistmaker.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.library.TrackStorage
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.search.recyclerview.TrackListAdapter
import com.example.playlistmaker.ui.utils.Helpers
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        private val TAG = SearchFragment::class.simpleName!!
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchFragmentViewModel>()

    private var searchTrackList: MutableList<Track> = mutableListOf()
    private var historyTrackList: MutableList<Track> = mutableListOf()
    private var rvTrackListAdapter: TrackListAdapter? = null
    private var rvHistoryAdapter: TrackListAdapter? = null
    private var searchText = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTextWatcher()
        initAdapters()
        initOnClickListeners()
        initObservers()
        viewModel.checkState()
    }

    private fun initAdapters() {
        rvTrackListAdapter = TrackListAdapter(searchTrackList, viewLifecycleOwner.lifecycleScope)
        rvHistoryAdapter = TrackListAdapter(historyTrackList, viewLifecycleOwner.lifecycleScope)
        binding.rvSearch.adapter = rvTrackListAdapter
        binding.rvHistory.adapter = rvHistoryAdapter
    }

    private fun initObservers() {

        viewModel.searchText.observe(viewLifecycleOwner) {
            searchText = it
            Log.d(TAG, "observe searchText = $it")
        }
        viewModel.searchTrackList.observe(viewLifecycleOwner) {
            addSearchResultToRecycle(it)
            Log.d(TAG, "observe searchTrackList = ${it.size}")
        }
        viewModel.historyTrackList.observe(viewLifecycleOwner) {
            addHistoryResultToRecycle(it)
            Log.d(TAG, "observe historyTrackList = ${it.size}")
        }
        viewModel.searchActivityState.observe(viewLifecycleOwner) {
            Log.d(TAG, "observe ActivityState = $it")
            showInvisibleLayout(it)
        }
    }

    private fun clearHistory() {
        Log.d(TAG, "clearHistory")
        if (historyTrackList.size > 0) historyTrackList.clear()
        showInvisibleLayout(ActivityState.HIDE_ALL)
        viewModel.clearHistory()
        Log.println(Log.INFO, TAG, "clearHistory")
    }

    private fun onFocusListenerSearchInit() {
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            Log.println(Log.INFO, TAG, "onFocusListenerInit")
            if (hasFocus) {
                viewModel.checkState()
            }
        }
    }

    private fun searchRefresh() {
        Log.d(TAG, "searchRefresh")
        viewModel.searchRequest()
    }

    private fun addSearchResultToRecycle(list: List<Track>) {
        Log.d(TAG, "addSearchResultToRecycle: ${list.size}")
        val itemCount = binding.rvSearch.adapter?.itemCount
        searchTrackList.clear()
        searchTrackList.addAll(list)
        if (itemCount != null) {
            binding.rvSearch.adapter?.notifyItemRangeRemoved(0, itemCount)
            binding.rvSearch.adapter?.notifyItemRangeInserted(0, list.size)
        }
    }

    private fun addHistoryResultToRecycle(list: List<Track>) {
        for (item in list) {
            Log.d(TAG, "add To History Adapter: ${item.trackId}")
        }
        historyTrackList.clear()
        historyTrackList.addAll(list)
        binding.rvHistory.adapter?.notifyItemRangeChanged(0, list.size)
    }

    private fun clearSearchResult() {
        clearResultFromSearchRecycle()
        viewModel.checkState()
    }

    private fun clearResultFromSearchRecycle() {
        val itemCount = binding.rvSearch.adapter?.itemCount
        searchTrackList.clear()
        if (itemCount != null) {
            binding.rvSearch.adapter?.notifyItemRangeRemoved(0, itemCount)
        }
    }

    private fun initTextWatcher() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.ivSearchClear.visibility = clearButtonVisibility(s)
                val text = binding.etSearch.text.toString()
                Log.d(TAG, "onTextChanged=$text")
                viewModel.setSearchText(text)
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, "afterTextChanged")
            }
        }
        binding.etSearch.addTextChangedListener(simpleTextWatcher)
    }

    private fun clearInputText() {
        Log.println(Log.INFO, TAG, "clearInputText")
        binding.etSearch.setText("")
        viewModel.checkState()
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun onClickRecyclerViewHistoryItem() {
        rvHistoryAdapter?.setOnClickListener(object : TrackListAdapter.OnClickListener {
            override fun onClick(position: Int, track: Track) {
                callPlayerFragment(track)
            }
        })
    }

    private fun onClickRecyclerViewSearchItem() {
        rvTrackListAdapter?.setOnClickListener(object : TrackListAdapter.OnClickListener {
            override fun onClick(position: Int, track: Track) {
                Toast.makeText(
                    requireContext(),
                    "Add to history: ${track.trackName}",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.addTrackToHistory(track)
                callPlayerFragment(track)
            }
        })
    }

    private fun callPlayerFragment(track: Track) {
        (requireActivity() as TrackStorage).setTrack(track)
        findNavController().navigate(R.id.playerFragment)
    }

    private fun clearButtonListener() {
        Helpers.hideKeyboard(requireActivity())
        clearInputText()
        clearSearchResult()
    }

    private fun initOnClickListeners() {
        binding.ivSearchClear.setOnClickListener { clearButtonListener() }
        binding.etSearch.setOnClickListener { }
        binding.btnSearchRefresh.setOnClickListener { searchRefresh() }
        binding.btnClearHistory.setOnClickListener { clearHistory() }
        onClickRecyclerViewSearchItem()
        onClickRecyclerViewHistoryItem()
        onFocusListenerSearchInit()
    }

    private fun showInvisibleLayout(state: ActivityState = ActivityState.HIDE_ALL) {
        Log.d(TAG, "ActivityState = $state")
        binding.rvSearch.visibility = View.GONE
        binding.layoutNoInternet.visibility = View.GONE
        binding.layoutIsEmpty.visibility = View.GONE
        binding.layoutHistory.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        when (state) {
            ActivityState.SEARCH_RESULT -> binding.rvSearch.visibility = View.VISIBLE
            ActivityState.NOT_FOUND -> binding.layoutIsEmpty.visibility = View.VISIBLE
            ActivityState.NO_INTERNET -> binding.layoutNoInternet.visibility = View.VISIBLE
            ActivityState.HISTORY_RESULT -> binding.layoutHistory.visibility = View.VISIBLE
            ActivityState.PROGRESS_BAR -> binding.progressBar.visibility = View.VISIBLE
            else -> {}
        }
    }
}