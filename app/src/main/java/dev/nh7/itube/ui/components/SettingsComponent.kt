package dev.nh7.itube.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.nh7.itube.utils.Setting


@Composable
fun SettingsComponent(
    settings: Map<String, String>,
    onSettingChange: (String, String) -> Unit
) {

    fun getSettingValue(setting: Setting): String {
        return settings[setting.name] ?: setting.default
    }

    Column(modifier = Modifier.padding(16.dp)) {

        val setting1 = Setting.PLAY_SONG_AFTER_DOWNLOAD
        val settingValue1 = getSettingValue(setting1)
        SwitchSetting(
            name = setting1.displayName,
            value = settingValue1.toBoolean(),
            onValueChange = { newValue ->
                onSettingChange(setting1.name, newValue.toString())
            }
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        val setting2 = Setting.DOWNLOAD_BUFFER_SIZE
        val settingValue2 = getSettingValue(setting2)
        InputSetting(
            name = setting2.displayName,
            value = settingValue2,
            onValueChange = { newValue ->
                onSettingChange(setting2.name, newValue)
            }
        )
    }
}

@Composable
private fun SwitchSetting(name: String, value: Boolean, onValueChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = name, maxLines = 1, modifier = Modifier.weight(1f))
        val enabled = remember { mutableStateOf(value) }
        Button(onClick = {
            enabled.value = !enabled.value
            onValueChange(enabled.value)
        }) {
            Text(text = if (enabled.value) "On" else "Off")
        }
        /*Switch(
            checked = value,
            onCheckedChange = onValueChange
        )*/
    }
}

@Composable
private fun InputSetting(name: String, value: String, onValueChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = name, maxLines = 1, modifier = Modifier.weight(1f))
        OutlinedTextField(
            value = value,
            onValueChange = { value -> if (value.length <= 5) onValueChange(value) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(100.dp) //todo improve
        )
    }
}