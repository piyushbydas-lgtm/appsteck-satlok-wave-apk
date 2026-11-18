package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.data.repository.SatlokRepository
import com.example.ui.SatlokMainApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SatlokRepository.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            SatlokMainApp()
        }
    }
}
