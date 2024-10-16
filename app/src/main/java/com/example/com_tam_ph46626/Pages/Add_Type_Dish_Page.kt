package com.example.com_tam_ph46626.Pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.com_tam_ph46626.ViewModel.AddTypeDishViewModel
import com.example.com_tam_ph46626.toColor

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddTypeDish(modifier: Modifier = Modifier) {

    val loaiMon = remember { mutableStateOf("") }
    val viewModeType: AddTypeDishViewModel = AddTypeDishViewModel()

    val context = LocalContext.current

    Column (
        modifier = modifier.fillMaxSize()
            .background("#F6F6F6".toColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField3(
            title = "Loại Món",
            placeholder = "Nhập loại món",
            value = loaiMon.value,
            onValueChange = {loaiMon.value = it}

        )

        Button(onClick = {
            viewModeType.addTypeDish(
                type_name = loaiMon.value,
                onSuccess = {
                    Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()

                    // Đặt lại các giá trị của input về trống
                    loaiMon.value = ""
                },
                onError = { errorMessage ->  // Nhận thông tin lỗi từ onError
                    Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show()

                    // Log ra thông tin chi tiết về lỗi
                    Log.e("AddTypeDishError", "Thêm thất bại: $errorMessage")
                }
            )
        },
            modifier = Modifier
                .padding(top = 50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = "#FE724C".toColor,
                contentColor = "#FFFFFF".toColor
            )
        ) {
            Text(text = "Thêm loại món")
        }
    }

}