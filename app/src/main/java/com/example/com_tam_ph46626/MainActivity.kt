package com.example.com_tam_ph46626

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.com_tam_ph46626.BottomNavigation.MainScreen
import com.example.com_tam_ph46626.ui.theme.Com_Tam_Ph46626Theme

class MainActivity : ComponentActivity() {


    companion object {
        private const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Com_Tam_Ph46626Theme  {
                MainScreen()
            }
        }
    }
}


