package com.example.playlistmaker.ui.library.fragments.playlists.create

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<CreatePlaylistFragmentViewModel>()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private lateinit var alertDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initPicker()
        initObserver()
        initNameTextWatcher()
        initDescriptionTextWatcher()
        initDialog()
        initCallBack()
    }

    private fun initObserver() {
        viewModel.fileUri.observe(viewLifecycleOwner) {
            Log.d(TAG, "initObserver")
            binding.ivMain.setImageURI(it)
        }
    }

    private fun initListeners() {
        binding.ivBack.setOnClickListener {
            callBack()
        }
        binding.ivMain.setOnClickListener {
            callPicker()
        }
        binding.btnCreate.setOnClickListener {
            callSavePlaylist()
        }
    }

    private fun callSavePlaylist() {
        viewModel.savePlaylist()
        callMessageSavePlaylist()
        findNavController().popBackStack()
    }

    private fun callMessageSavePlaylist() {
        val message: String =
            getString(R.string.message_save_playlist).replace(
                MESSAGE_REPLACE_PATTERN,
                binding.etName.text.toString(),
                false
            )
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }

    private fun initPicker() {
        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.saveImageToPrivateStorage(uri)
                } else {
                    Log.d(TAG, "No media selected")
                }
            }
    }

    private fun callPicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun initNameTextWatcher() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = binding.etName.text.toString()
                if (text.isNotEmpty()) {
                    viewModel.setPlayListName(text)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                setEnabledCreateButton()
                setColorNameEditText()
            }
        }
        binding.etName.addTextChangedListener(simpleTextWatcher)
    }

    private fun initDescriptionTextWatcher() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = binding.etDesc.text.toString()
                if (text.isNotEmpty()) {
                    viewModel.setPlaylistDescription(text)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                setColorDescriptionEditText()
            }
        }
        binding.etDesc.addTextChangedListener(simpleTextWatcher)
    }


    private fun setEnabledCreateButton() {
        binding.btnCreate.isEnabled = binding.etName.text.isNotEmpty()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setColorNameEditText() {
        if (binding.etName.text.isNotEmpty()) {
            binding.etName.background = resources.getDrawable(R.drawable.ed_add_playlist_blue)
            binding.tvPlName.visibility = View.VISIBLE
        } else {
            binding.etName.background = resources.getDrawable(R.drawable.ed_add_playlist_grey)
            binding.tvPlName.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setColorDescriptionEditText() {
        if (binding.etDesc.text.isNotEmpty()) {
            binding.etDesc.background = resources.getDrawable(R.drawable.ed_add_playlist_blue)
            binding.tvPlDesc.visibility = View.VISIBLE
        } else {
            binding.etDesc.background = resources.getDrawable(R.drawable.ed_add_playlist_grey)
            binding.tvPlDesc.visibility = View.INVISIBLE
        }
    }

    private fun callBack() {
        if (binding.etName.text.isNotEmpty() || binding.etDesc.text.isNotEmpty()) {
            showMessage()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun initDialog() {
        alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.message_header_create_playlist)
            .setMessage(R.string.message_body_create_playlist)
            .setNeutralButton(R.string.dialog_botton_cancel) { dialog, whitch ->

            }
            .setPositiveButton(R.string.dialog_botton_complete) { dialog, whitch ->
                findNavController().popBackStack()
            }
    }

    private fun initCallBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            callBack()
        }
    }

    private fun showMessage() {
        alertDialog.show()
    }

    companion object {
        private val TAG = CreatePlaylistFragment::class.simpleName
        private const val MESSAGE_REPLACE_PATTERN = "[playlistName]"
    }

}


