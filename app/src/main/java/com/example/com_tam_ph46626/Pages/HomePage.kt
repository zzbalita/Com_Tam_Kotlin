package com.example.com_tam_ph46626.Pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.com_tam_ph46626.Model.Product.ProductData
import com.example.com_tam_ph46626.R
import com.example.com_tam_ph46626.ViewModel.ProductViewModel
import com.example.com_tam_ph46626.toColor


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun fake1() {
    HomePage(rememberNavController())
}


@Composable
fun HomePage(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current


    val viewModelProduct: ProductViewModel = remember { ProductViewModel() }

    val products by viewModelProduct.products.observeAsState(initial = emptyList())

    Log.d("ggg", products.toString())

    // State lưu trữ giá trị tìm kiếm
    var searchProduct by remember { mutableStateOf(TextFieldValue("")) }


    // Khởi tạo launcher
    LaunchedEffect(Unit) {
        viewModelProduct.fetchProduct()
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background("#F6F6F6".toColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = rememberImagePainter(R.drawable.banner),
            contentDescription = "Product Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(350.dp)
                .height(190.dp)
                .clip(RoundedCornerShape(16.dp)) // Bo góc 16.dp cho ảnh

        )

        // Ô tìm kiếm
        OutlinedTextField(
            value = searchProduct,
            onValueChange = { newText ->
                searchProduct = newText
                if (newText.text.isNotEmpty()) {
                    viewModelProduct.searchProduct(newText.text)
                    Log.d("Search", viewModelProduct.searchProduct(newText.text).toString())
                } else {
                    viewModelProduct.fetchProduct()
                }
            },
            label = { Text("Tìm kiếm món ăn") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )


        // LazyColumn để hiển thị danh sách món ăn
        if (products.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(products) { product ->
                    ProductItem(product, context, navController) // Gọi hàm để hiển thị từng sản phẩm
                }
            }
        } else {
            Text(text = "No products available", modifier = Modifier.padding(16.dp))
        }

    }
}



@Composable
fun ProductItem(product: ProductData, context: Context, navController: NavController) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Điều hướng đến màn hình chi tiết và truyền id sản phẩm
                navController.navigate("detail/${product._id}")
            }
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = "#DFDDDD".toColor
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {


            DisplayImageFromUri(product.image_url)
            Column(
                modifier = Modifier.padding(top = 8.dp, start = 10.dp)
            ) {
                Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "${product.price}K", fontSize = 16.sp, color = "#FE724C".toColor)
            }

        }
    }
}





@Composable
fun DisplayImageFromUri(imageUri: String) {
    Image(
        painter = rememberImagePainter(
            data = imageUri,
            builder = {
                error(R.drawable.error_placeholder) // Hiển thị ảnh lỗi nếu không tải được
                listener(
                    onError = { request, throwable ->
                        Log.e(
                            "CoilError",
                            "Error loading image: ${throwable.throwable ?: "Unknown error"}"
                        )
                    },
                    onSuccess = { _, _ ->
                        Log.d("CoilSuccess", "Image loaded successfully")
                    }
                )
            }
        ),
        contentDescription = "Selected Image",
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .size(60.dp), // Chỉnh sửa kích thước theo nhu cầu
        contentScale = ContentScale.Crop
    )
}


