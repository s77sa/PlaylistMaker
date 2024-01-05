package com.example.playlistmaker.ui.library.fragments.playlists.edit

import android.app.ActionBar.LayoutParams
import android.content.res.Resources
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistEditBinding
import com.example.playlistmaker.domain.library.PlaylistStorage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel


class EditPlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<EditPlaylistFragmentViewModel>()

    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    //private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

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
        initClickListeners()
        initObservers()
        initBottomSheet()
    }

    private fun initObservers(){
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
    }
    
    private fun setImageToImageView(uri: Uri?){
        if(uri != null){
            binding.ivPlaylistArt.setImageURI(uri)
            binding.ivPlaylistArt.layoutParams.width = LayoutParams.MATCH_PARENT
            binding.ivPlaylistArt.layoutParams.height = LayoutParams.MATCH_PARENT
        }

    }

    private fun setValueName(value: String){
        binding.tvPlaylistName.text = value
    }

    private fun setValueDescription(value: String?){
        if(!value.isNullOrEmpty()){
            binding.tvPlaylistDesc.text = value
            binding.tvPlaylistDesc.visibility = View.VISIBLE
        }
        else
        {
            binding.tvPlaylistDesc.visibility = View.GONE
        }
    }

    private fun setValueTime(value: String){
        Log.d(TAG, "setValueTime: $value")
        binding.tvPlaylistDuration.text = value
    }

    private fun setValueCount(value: String){
        Log.d(TAG, "setValueCount: $value")
        binding.tvPlaylistCount.text = value
    }

    private fun initClickListeners() {
        binding.playlistEditToolbar.setNavigationOnClickListener {
            finishFragment()
        }
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
//        bottomSheetBehavior.state = BottomSheetBehavior
        //bottomSheetBehavior.peekHeight = getHeight()

    }

    private fun getHeight(): Int {

        val dm = Resources.getSystem().displayMetrics
        Log.d(TAG, "heightPixels: ${dm.heightPixels} widthPixels: ${dm.widthPixels}")
        val rect = dm.run { Rect(0, 0, heightPixels, widthPixels) }
        Log.d(TAG, "rect heightPixels: ${rect.width()}")
//        return rect.height()
//        val display = Resources.getSystem().displayMetrics.densityDpi
//
        val location = IntArray(2)
        binding.tvPlaylistName.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]

        Log.d(TAG, "$x $y")
//        val value = binding.plEditGuideline.measuredWidth
////        val value = (location[1].toFloat() * 0.75f).toInt()
////        val value = display
////
//        Log.d(TAG, value.toString())

        return y
    }

    companion object {
        private val TAG = EditPlaylistFragment::class.java.simpleName
    }

}


