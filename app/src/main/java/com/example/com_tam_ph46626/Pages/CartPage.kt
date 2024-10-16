package com.example.com_tam_ph46626.Pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.com_tam_ph46626.Model.Cart.CartData
import com.example.com_tam_ph46626.R
import com.example.com_tam_ph46626.ViewModel.CartViewModel
import com.example.com_tam_ph46626.ViewModel.CheckoutViewModel
import com.example.com_tam_ph46626.toColor
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CartPage(navController: NavController, modifier: Modifier = Modifier) {

    val viewModelCart: CartViewModel = remember { CartViewModel() }


    LaunchedEffect(Unit) {
        viewModelCart.fetchCart()
    }

    val context = LocalContext.current

    // Lấy dữ liệu từ LiveData
    val carts by viewModelCart.carts.observeAsState(initial = emptyList<CartData>())

    Log.d("zz", carts.toString())

    val checkouViewModel: CheckoutViewModel = remember { CheckoutViewModel() }

    val gson = Gson() // Khởi tạo Gson
    val cartJson = gson.toJson(carts) // Chuyển đổi danh sách sản phẩm thành JSON

    val cartList: List<CartData> = gson.fromJson(cartJson, Array<CartData>::class.java).toList()

    val total = cartList.sumOf { it.quantity * it.product.price }
    // Tính tổng tiền
    Log.d("dd", cartJson.toString())


    Column(
        modifier = modifier
            .fillMaxSize()
            .background("#F6F6F6".toColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (carts!!.isEmpty()) {
            Text(
                text = "Giỏ hàng trống",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f) // Chiếm không gian còn lại
                .fillMaxSize()
        ) {
            items(carts ?: emptyList()) { cart ->
                ProductItem_2(cart, context, navController, viewModelCart)
            }
        }

        // Card hiển thị tổng tiền
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Tổng tiền:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "${total}K", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
        // Nút thanh toán
        PaymentButton(navController, cartJson, checkouViewModel)


    }

}


@Composable
fun ProductItem_2(cart: CartData, context: Context, navController: NavController, viewModelCart: CartViewModel) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = "#DFDDDD".toColor
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {


            DisplayImageFromUri(cart.product.image_url)
            Column(
                modifier = Modifier
                    .padding(top = 8.dp, start = 10.dp)
                    .weight(1f)
            ) {
                Text(text = "${cart.product.name}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "${cart.product.price}K", fontSize = 16.sp, color = "#FE724C".toColor)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                horizontalArrangement = Arrangement.spacedBy(8.dp), // Tạo khoảng cách giữa các nút
                modifier = Modifier
//                    .background(Color.LightGray, RoundedCornerShape(10.dp))
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {
                // Nút giảm số lượng
                Icon(
                    painter = painterResource(R.drawable.ic_minus),
                    contentDescription = "Decrease quantity",
                    modifier = Modifier
                        .background(
                            color = "#FE724C".toColor,
                            RoundedCornerShape(50.dp)
                        ) // Background bo tròn
                        .clickable {
                            // Xử lý khi nhấn nút giảm số lượng
                            if (cart.quantity > 1) {
                                viewModelCart.updateCartQuantity(cart.product._id, cart.quantity - 1)
                            }
                        }
                )
                // Hiển thị số lượng sản phẩm
                Text(
                    text = "${cart.quantity}", // Số lượng sản phẩm
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(8.dp)
                )
                // Nút tăng số lượng
                Icon(
                    painter = painterResource(R.drawable.ic_plus),
                    contentDescription = "Increase quantity",
                    modifier = Modifier
                        .background(
                            color = "#FE724C".toColor,
                            RoundedCornerShape(50.dp)
                        ) // Background bo tròn
                        .clickable {
                            viewModelCart.updateCartQuantity(cart.product._id, cart.quantity + 1)
                        }
                )
            }

        }
    }
}


@Composable
fun PaymentButton(
    navController: NavController,
    cartJson: String,
    checkoutViewModel: CheckoutViewModel
) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
            .background(color = "#FE724C".toColor, RoundedCornerShape(8.dp))
            .clickable {


                val encodedCartJson = URLEncoder.encode(cartJson, StandardCharsets.UTF_8.toString())
                Log.d("EndCode", encodedCartJson)

                navController.navigate("checkout/$encodedCartJson")

            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Thanh Toán",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp) // Thêm padding cho text
        )
    }
}

