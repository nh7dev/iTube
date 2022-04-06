package dev.nh7.itube

import android.content.pm.PackageManager.GET_PERMISSIONS
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.nh7.itube.ui.components.MainComponent
import dev.nh7.itube.ui.theme.MainTheme
import dev.nh7.itube.utils.LOG

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LOG("onCreate Activity")

        installSplashScreen()

        val permissions =
            packageManager.getPackageInfo(packageName, GET_PERMISSIONS).requestedPermissions
        val missingPermission = mutableStateOf(getFirstMissingPermission(permissions))
        val permissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                missingPermission.value = getFirstMissingPermission(permissions)
            }

        setContent {
            MainTheme {

                MainComponent()

                val permission = missingPermission.value
                if (permission != null) {
                    PermissionsDialog(
                        permission = permission.replace("android.permission.", ""),
                        onClickAllow = {
                            permissionRequest.launch(permission)
                        }
                    )
                }

            }
        }
    }

    // back handler

    private var lastClick = 0L

    override fun onBackPressed() {
        val browser = (application as MainApplication).browser

        if (browser?.canGoBack() == true) {
            browser.goBack()
            return
        }

        val now = System.currentTimeMillis()
        if (now - lastClick < 1000L) {
            finish()
            return
        }

        lastClick = now
        Toast.makeText(applicationContext, "Click again to close app", Toast.LENGTH_SHORT).show()
    }

    // permissions

    private fun getFirstMissingPermission(permissions: Array<String>): String? {
        for (permission in permissions) {
            if (!checkPermission(permission)) {
                return permission
            }
        }
        return null
    }

    private fun checkPermission(permission: String): Boolean {
        val permissionStatus = ContextCompat.checkSelfPermission(this, permission)
        return permissionStatus == PERMISSION_GRANTED
    }

    @Composable
    fun PermissionsDialog(permission: String, onClickAllow: () -> Unit) {
        AlertDialog(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_permissions),
                    contentDescription = "Permissions"
                )
            },
            title = {
                Text(text = "Permission required")
            },
            text = {
                Text(text = permission)
            },
            confirmButton = {
                Button(onClick = onClickAllow) {
                    Text(text = "Allow")
                }
            },
            onDismissRequest = {}
        )
    }

}