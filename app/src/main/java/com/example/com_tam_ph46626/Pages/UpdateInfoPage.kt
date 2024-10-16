package com.example.com_tam_ph46626.Pages


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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.com_tam_ph46626.TextField2
import com.example.com_tam_ph46626.toColor

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UpdateInfoPage() {
    // Trạng thái lưu trữ giá trị nhập vào
    val phoneNumber = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Ô nhập tên
        TextField2(
            title = "Tên",
            placeholder = "Nhập tên",
            value = name.value,
            onValueChange = { name.value = it }
        )

        // Ô nhập email
        TextField2(
            title = "Email",
            placeholder = "Nhập email",
            value = email.value,
            onValueChange = { email.value = it }
        )

        // Ô nhập số điện thoại
        TextField2(
            title = "Số điện thoại",
            placeholder = "Nhập số điện thoại",
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it }
        )

        // Ô nhập đường
        TextField2(
            title = "Địa chỉ",
            placeholder = "Nhập địa chỉ của bạn",
            value = address.value,
            onValueChange = { address.value = it }
        )



        // Nút cập nhật thông tin
        Button(
            onClick = {

            },
            modifier = Modifier.padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = "#FE724C".toColor,
                contentColor = "#FFFFFF".toColor
            )
        ) {
            Text(text = "Cập nhật thông tin")
        }
    }
}
