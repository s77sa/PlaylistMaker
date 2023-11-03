package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListenerThemeSwitch()
        initListeners()
        viewModel.getThemeFromSharedPrefs()
        initObservers()
    }

    private fun initObservers() {
        viewModel.darkTheme.observe(requireActivity()) {
            themeSwitchCheck(it)
        }
    }

    private fun initListeners() {
        binding.tvSupport.setOnClickListener {
            externalNavigatorEmail()
        }
        binding.tvShare.setOnClickListener {
            externalNavigatorSend()
        }
        binding.tvTerms.setOnClickListener {
            externalNavigatorOpenLink()
        }
    }

    private fun themeSwitchCheck(isDarkTheme: Boolean) {
        binding.switchTheme.isChecked = isDarkTheme
    }

    private fun initListenerThemeSwitch() {
        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.saveAndApplyTheme(checked)
        }
    }

    private fun showToastErrorMessage() {
        Toast.makeText(requireContext(), R.string.external_navigator_error, Toast.LENGTH_SHORT)
            .show()
    }

    private fun externalNavigatorEmail() {
        val result: String? = viewModel.callEmailIntent(
            getString(R.string.email_dest),
            getString(R.string.email_subject),
            getString(R.string.email_text)
        )
        if (!result.isNullOrEmpty()) {
            showToastErrorMessage()
        }
    }

    private fun externalNavigatorSend() {
        val result: String? = viewModel.callSendIntent(getString(R.string.link_practicum_ad))
        if (!result.isNullOrEmpty()) {
            showToastErrorMessage()
        }
    }

    private fun externalNavigatorOpenLink() {
        val result: String? = viewModel.callOpenLinkIntent(getString(R.string.link_terms))
        if (!result.isNullOrEmpty()) {
            showToastErrorMessage()
        }
    }
}