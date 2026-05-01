package com.raya.eightraya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.raya.eightraya.ui.subjects.SubjectsRoute
import com.raya.eightraya.ui.subjects.SubjectsViewModel
import com.raya.eightraya.ui.theme.EightRayaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val subjectsViewModel: SubjectsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EightRayaTheme {
                SubjectsRoute(viewModel = subjectsViewModel)
            }
        }
    }
}
