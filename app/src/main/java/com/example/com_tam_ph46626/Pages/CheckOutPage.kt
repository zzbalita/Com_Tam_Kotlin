package com.example.com_tam_ph46626.Pages

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.auth0.android.jwt.JWT
import com.example.com_tam_ph46626.Model.Cart.CartData
import com.example.com_tam_ph46626.Model.Checkout.CheckoutItem
import com.example.com_tam_ph46626.ViewModel.CheckoutViewModel
import com.example.com_tam_ph46626.toColor
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun CheckOut(carts: List<CartData>, navController: NavController, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) } // State để điều khiển modal
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val cartJson = navController.currentBackStackEntry?.arguments?.getString("cartJson")?.let {
        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
    }

    Log.d("555", cartJson.toString())

    val context = LocalContext.current
    val checkouViewModel: CheckoutViewModel = remember { CheckoutViewModel() }

    val sharedPref = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
    val token = sharedPref.getString("auth_token", null)

    var userId by remember { mutableStateOf<String?>(null) } // Khai báo userId tại đây

    if (token != null) {
        val jwt = JWT(token)
        userId = jwt.getClaim("userId").asString()

        // Kiểm tra nếu userId không null thì xử lý tiếp
        if (userId != null) {
            Log.d("Checkout", "User ID: $userId")
            // Bạn có thể sử dụng userId cho các yêu cầu API hoặc hiển thị thông tin người dùng
        } else {
            Log.e("Checkout", "Không tìm thấy userId trong token")
        }
    } else {
        Log.e("Checkout", "Token không tồn tại trong SharedPreferences")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background("#F6F6F6".toColor),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        Card(
            modifier = Modifier
                .padding(16.dp)
                .background("#F6F6F6".toColor)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors("#DCDCDC".toColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Thông tin thanh toán",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)

                )

                Spacer(modifier = Modifier.height(8.dp)) // Khoảng cách giữa các input
                // Input Address
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Địa chỉ") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Input Phone Number
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Số điện thoại") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Button thanh toán
        Button(
            onClick = { showDialog = true }, // Mở dialog khi bấm nút
            modifier = Modifier
                .width(180.dp)
                .padding(vertical = 16.dp), // Khoảng cách giữa button và các phần khác
            colors = ButtonDefaults.buttonColors("#FE724C".toColor)
        ) {
            Text("Thanh toán")
        }

        // Dialog hiển thị khi người dùng bấm nút
        if (showDialog) {

            AlertDialog(
                onDismissRequest = { showDialog = false }, // Đóng dialog khi nhấn ra ngoài
                title = { Text("Xác nhận thanh toán") },
                text = { Text("Bạn có muốn thanh toán không?", fontSize = 16.sp) },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center // Căn giữa các nút
                    ) {
                        Button(
                            onClick = {
                                if (userId != null) {
                                    Payment(
                                        carts,
                                        address,
                                        phoneNumber,
                                        userId!!,
                                        context,
                                        checkouViewModel
                                    )
                                    address = ""
                                    phoneNumber = ""
                                } else {
                                    Log.e("Checkout", "User ID không tồn tại, không thể thanh toán")
                                }

                                showDialog = false

                            },
                            modifier = Modifier
                                .width(120.dp)
                                .padding(horizontal = 8.dp)
                        ) {
                            Text("Đồng ý")
                        }

                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier
                                .width(120.dp)
                                .padding(horizontal = 8.dp)
                        ) {
                            Text("Hủy")
                        }
                    }
                },
            )
        }


    }


}

fun Payment(
    carts: List<CartData>,
    address: String,
    phoneNumber: String,
    userId: String,
    context: Context,
    checkoutViewModel: CheckoutViewModel
) {
    // Chuyển danh sách CartData thành danh sách CheckoutItem
    val items = carts.map { cartData ->
        CheckoutItem(
            productId = cartData.product._id,  // Giả sử mỗi sản phẩm có thuộc tính productId
            quantity = cartData.quantity,           // Giả sử mỗi sản phẩm có thuộc tính số lượng (quantity)
        )
    }
    checkoutViewModel.checkout(
        items = items,
        address = address,
        phoneNumber = phoneNumber,
        onSuccess = {
            Log.d("Checkout", "Thanh toán thành công")
            Toast.makeText(context, "Thanh toán thành công", Toast.LENGTH_SHORT)
                .show() // Hiển thị Toast

        },
        onError = { errorMessage ->
            Log.e("Checkout", "Lỗi: $errorMessage")
            Toast.makeText(context, "Lỗi: $errorMessage", Toast.LENGTH_SHORT)
                .show() // Hiển thị Toast lỗi
        }

    )
}



