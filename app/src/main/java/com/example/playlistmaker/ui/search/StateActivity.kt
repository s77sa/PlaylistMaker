package com.example.playlistmaker.ui.search

enum class StateActivity {
    SUCCESS, // Show RV
    ERROR, // Show Layout NoInternet
    NOT_FOUND, // Show Layout Empty
    SHOW_HISTORY, // Show Layout History
    PROGRESS_BAR, // Show progress bar
    HIDE_ALL // Hide all Layout and RV
}