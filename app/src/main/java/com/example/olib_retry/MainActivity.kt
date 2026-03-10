package com.example.olib_retry


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.olib_retry.ui.OLibApp
import com.example.olib_retry.ui.theme.OLibWorksTheme
import com.example.olib_retry.viewmodel.BooksViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OLibWorksTheme {
                val viewModel: BooksViewModel = viewModel()
                OLibApp(viewModel = viewModel)
            }
        }
    }
}