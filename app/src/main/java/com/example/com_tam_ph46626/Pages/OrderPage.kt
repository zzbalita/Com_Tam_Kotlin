package com.example.com_tam_ph46626.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.com_tam_ph46626.toColor

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrderPage(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    Column (
        modifier = modifier.fillMaxSize()
            .background("#F6F6F6".toColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Order Page",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Green
        )

    }

}