package com.raya.eightraya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.raya.eightraya.ui.navigation.EightRayaNavHost
import com.raya.eightraya.ui.theme.EightRayaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EightRayaTheme {
                EightRayaNavHost()
            }
        }
    }
}
