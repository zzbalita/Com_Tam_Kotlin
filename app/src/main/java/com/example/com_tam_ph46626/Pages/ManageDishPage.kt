package com.example.com_tam_ph46626.Pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.com_tam_ph46626.toColor


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun fake() {
    ManageDishPage(rememberNavController())
}

@Composable
fun ManageDishPage(navController: NavController,modifier: Modifier = Modifier) {
    Column (
        modifier = modifier.fillMaxSize()
            .background("#F6F6F6".toColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardItem("Thêm món ăn") {
            try {
                Log.d("ManageDishPage", "Navigating to add_dishhh")
                navController.navigate("add_dish")
            } catch (e: Exception) {
                Log.e("ManageDishPage", "Navigation error: ${e.message}")
            }
        }
        CardItem("Sửa món ăn")


    }

}