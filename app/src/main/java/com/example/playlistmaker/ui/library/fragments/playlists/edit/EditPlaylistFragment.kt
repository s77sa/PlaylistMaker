package com.example.playlistmaker.ui.library.fragments.playlists.edit

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.library.PlaylistStorage
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.library.fragments.playlists.create.CreatePlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatePlaylistFragment() {

    override val viewModel by viewModel<EditPlaylistFragmentViewModel>()
    private var playlist: Playlist? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = (requireActivity() as PlaylistStorage).getPlaylist()
        redefineMessage()
        loadCurrentPlaylist()
    }

    private fun redefineMessage() {
        binding.tvNewPlaylist.text = getString(R.string.tv_edit_playlist)
        binding.btnCreate.text = getString(R.string.btn_save)
        super.alertDialog.setTitle(R.string.message_header_edit_playlist)
    }

    private fun loadCurrentPlaylist() {
        playlist?.let {
            viewModel.setPlaylistId(it.id)
        }
        playlist?.imagePath?.let {
            viewModel.setImageUri(it)
        }
        playlist?.name.let {
            if (it != null) {
                super.viewModel.setPlayListName(it)
                (binding.etName as TextView).text = it
            }
        }
        playlist?.description?.let {
            super.viewModel.setPlaylistDescription(it)
            (binding.etDesc as TextView).text = it
        }
    }

    override fun callMessageSavePlaylist() {
        val message: String =
            getString(R.string.message_edit_playlist).replace(
                MESSAGE_REPLACE_PATTERN,
                binding.etName.text.toString(),
                false
            )
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }
}