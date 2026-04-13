package com.yey.macflai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.yey.macflai.ui.navigation.MacflaiNavGraph
import com.yey.macflai.ui.theme.MACFLAITheme
import com.yey.macflai.viewmodel.SinclairViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: SinclairViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MACFLAITheme {
                MacflaiNavGraph(viewModel = viewModel)
            }
        }
    }
}