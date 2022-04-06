package dev.nh7.itube.utils;

enum class Setting(val displayName: String, val default: String) {

    PLAY_SONG_AFTER_DOWNLOAD("Play song after download", "true"),
    DOWNLOAD_BUFFER_SIZE("Download buffer size (KB)", "1000");

}