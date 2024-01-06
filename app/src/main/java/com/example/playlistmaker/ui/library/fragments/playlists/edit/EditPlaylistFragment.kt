package com.example.playlistmaker.ui.library.fragments.playlists.edit

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.TextView
import androidx.core.net.toUri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.library.PlaylistStorage
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.library.fragments.playlists.create.CreatePlaylistFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment(): CreatePlaylistFragment() {

    override val viewModel by viewModel<EditPlaylistFragmentViewModel>()
    private var playlist: Playlist? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlist = (requireActivity() as PlaylistStorage).getPlaylist()
        redefineTextView()
    }

    private fun redefineTextView(){
        binding.tvNewPlaylist.text = getString(R.string.tv_edit_playlist)
        binding.btnCreate.text = getString(R.string.btn_save)
        (binding.etName as TextView).text = playlist?.name ?: ""
        (binding.etDesc as TextView).text = playlist?.description ?: ""
        binding.ivMain.setImageURI(playlist?.imagePath?.toUri())
        super.alertDialog.setTitle(R.string.message_header_edit_playlist)
    }
}