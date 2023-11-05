package com.example.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.playlistmaker.data.search.models.Track
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.ui.Helpers
import com.example.playlistmaker.ui.search.recyclerview.SearchAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding? = null
    private val viewModel by viewModel<SearchFragmentViewModel>()

    private var searchTrackList: MutableList<Track> = mutableListOf()
    private var historyTrackList: MutableList<Track> = mutableListOf()
    private val rvSearchAdapter: SearchAdapter by lazy { SearchAdapter(searchTrackList) }
    private val rvHistoryAdapter: SearchAdapter by lazy { SearchAdapter(historyTrackList) }
    private var searchText = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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
        binding?.rvSearch?.adapter = rvSearchAdapter
        binding?.rvHistory?.adapter = rvHistoryAdapter
    }

    private fun initObservers() {

        viewModel.searchText.observe(viewLifecycleOwner) {
            searchText = it
            Log.d("my_tag", "observe searchText = $it")
        }
        viewModel.searchTrackList.observe(viewLifecycleOwner) {
            addSearchResultToRecycle(it)
            Log.d("my_tag", "observe searchTrackList = ${it.size}")
        }
        viewModel.historyTrackList.observe(viewLifecycleOwner) {
            addHistoryResultToRecycle(it)
            Log.d("my_tag", "observe historyTrackList = ${it.size}")
        }
        viewModel.searchActivityState.observe(viewLifecycleOwner) {
            showInvisibleLayout(it)
            Log.d("my_tag", "observe ActivityState = $it")
        }
    }

    private fun clearHistory() {
        if (historyTrackList.size > 0) historyTrackList.clear()
        showInvisibleLayout(ActivityState.HIDE_ALL)
        viewModel.clearHistory()
        Log.println(Log.INFO, "my_tag", "clearHistory")
    }

    private fun onFocusListenerSearchInit() {
        binding?.etSearch?.setOnFocusChangeListener { _, hasFocus ->
            Log.println(Log.INFO, "my_tag", "onFocusListenerInit")
            if (hasFocus) {
                viewModel.checkState()
            }
        }
    }

    private fun searchRefresh() {
        Log.d("my_tag", "searchRefresh")
        viewModel.searchRequest()
    }

    private fun addSearchResultToRecycle(list: List<Track>) {
        searchTrackList.clear()
        searchTrackList.addAll(list)
        binding?.rvSearch?.adapter?.notifyItemRangeInserted(searchTrackList.size, list.size)
    }

    private fun addHistoryResultToRecycle(list: List<Track>) {
        for (item in list) {
            Log.d("my_tag", "add To History Adapter: ${item.trackId}")
        }
        historyTrackList.clear()
        historyTrackList.addAll(list)
        binding?.rvHistory?.adapter?.notifyItemRangeChanged(searchTrackList.size, list.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearSearchResult() {
        Log.println(Log.INFO, "my_tag", "clearRecycle")
        searchTrackList.clear()
        binding?.rvHistory?.adapter?.notifyDataSetChanged()
        viewModel.clearSearchTrackList()
        viewModel.checkState()
    }

    private fun initTextWatcher() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.ivSearchClear?.visibility = clearButtonVisibility(s)
                val text = binding?.etSearch?.text.toString()
                if (text.isNotEmpty()) {
                    Log.d("my_tag", "onTextChanged=$text")
                    viewModel.setSearchText(text)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("my_tag", "afterTextChanged")
            }
        }
        binding?.etSearch?.addTextChangedListener(simpleTextWatcher)
    }

    private fun clearInputText() {
        Log.println(Log.INFO, "my_tag", "clearInputText")
        binding?.etSearch?.setText("")
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
        rvHistoryAdapter.setOnClickListener(object : SearchAdapter.OnClickListener {
            override fun onClick(position: Int, track: Track) {
                viewModel.callPlayerActivity(track)
            }
        })
    }

    private fun onClickRecyclerViewSearchItem() {
        rvSearchAdapter.setOnClickListener(object : SearchAdapter.OnClickListener {
            override fun onClick(position: Int, track: Track) {
                Toast.makeText(
                    requireContext(),
                    "Add to history: ${track.trackName}",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.addTrackToHistory(track)
                viewModel.callPlayerActivity(track)
            }
        })
    }

    private fun clearButtonListener() {
        Helpers.hideKeyboard(requireActivity())
        clearInputText()
        clearSearchResult()
    }

    private fun initOnClickListeners() {
        binding?.ivSearchClear?.setOnClickListener { clearButtonListener() }
        binding?.etSearch?.setOnClickListener { }
        binding?.btnSearchRefresh?.setOnClickListener { searchRefresh() }
        binding?.btnClearHistory?.setOnClickListener { clearHistory() }
        onClickRecyclerViewSearchItem()
        onClickRecyclerViewHistoryItem()
        onFocusListenerSearchInit()
    }

    private fun showInvisibleLayout(state: ActivityState = ActivityState.HIDE_ALL) {
        Log.d("my_tag", "ActivityState = $state")
        binding?.rvSearch?.visibility = View.GONE
        binding?.layoutNoInternet?.visibility = View.GONE
        binding?.layoutIsEmpty?.visibility = View.GONE
        binding?.layoutHistory?.visibility = View.GONE
        binding?.progressBar?.visibility = View.GONE
        when (state) {
            ActivityState.SEARCH_RESULT -> binding?.rvSearch?.visibility = View.VISIBLE
            ActivityState.NOT_FOUND -> binding?.layoutIsEmpty?.visibility = View.VISIBLE
            ActivityState.NO_INTERNET -> binding?.layoutNoInternet?.visibility = View.VISIBLE
            ActivityState.HISTORY_RESULT -> binding?.layoutHistory?.visibility = View.VISIBLE
            ActivityState.PROGRESS_BAR -> binding?.progressBar?.visibility = View.VISIBLE
            else -> {}
        }
    }

}