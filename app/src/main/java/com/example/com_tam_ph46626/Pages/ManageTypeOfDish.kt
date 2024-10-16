package com.example.com_tam_ph46626.Pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.com_tam_ph46626.toColor


@Composable
fun ManageTypeOfDishPage(navController: NavController,modifier: Modifier = Modifier) {
    Column (
        modifier = modifier.fillMaxSize()
            .background("#F6F6F6".toColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardItem("Thêm loại món ăn") {
            try {
                Log.d("ManageTypeDishPage", "Navigating to add_type_dish ")
                navController.navigate("add_type_dish")
            } catch (e: Exception) {
                Log.e("ManageTypeDishPage", "Navigation error: ${e.message}")
            }
        }
        CardItem("Sửa loại món ăn")

        // Nút quay lại
        Button(onClick = {
            navController.popBackStack() // Quay lại màn hình trước đó
        }) {
            Text(text = "Quay lại")
        }


    }

}