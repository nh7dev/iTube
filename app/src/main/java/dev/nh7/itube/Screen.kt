package dev.nh7.itube;

enum class Screen(val displayName: String, val icon: Int, val showInNavigationBar: Boolean) {

    SEARCH("Search", R.drawable.icon_search, true),
    DOWNLOADS("Downloads", R.drawable.icon_downloads, true),
    SETTINGS("Settings", R.drawable.icon_settings, false);

}