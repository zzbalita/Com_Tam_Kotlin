package com.example.com_tam_ph46626.Pages

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.com_tam_ph46626.Model.TypeProduct.TypeProductData
import com.example.com_tam_ph46626.R
import com.example.com_tam_ph46626.ViewModel.AddDishViewModel
import com.example.com_tam_ph46626.ViewModel.TypeProductViewModel
import com.example.com_tam_ph46626.toColor
import java.io.ByteArrayOutputStream


@Composable
fun TextField3(
    title: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {

    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 8.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            modifier = Modifier
                .width(400.dp)
                .height(56.dp),
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            trailingIcon = {
                trailingIcon?.invoke()
            },
        )
    }
}


fun encodeImageToBase64(context: Context, imageUri: Uri): String? {
    try {
        val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Sử dụng ImageDecoder cho các phiên bản mới hơn
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            // Sử dụng MediaStore cho các phiên bản cũ hơn
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        // Chuyển Bitmap thành mảng byte
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()

        // Mã hóa mảng byte thành chuỗi Base64 sử dụng java.util.Base64
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}

@Composable
fun ImagePicker(onImageSelected: (String) -> Unit) {
    var imageUri by remember { mutableStateOf<String?>(null) }

    // Trình chọn ảnh
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            onImageSelected(it.toString())
            imageUri = it.toString() // Lưu URI của ảnh đã chọn
        }
    }

    Box(
        modifier = Modifier
            .size(140.dp) // Kích thước của ô vuông
            .background(color = Color.Gray) // Màu nền của ô
            .clickable { launcher.launch("image/*") }, // Mở trình chọn ảnh khi nhấn vào ô
        contentAlignment = Alignment.Center // Căn giữa nội dung bên trong ô
    ) {
        // Hiển thị ảnh nếu đã chọn
        if (imageUri != null) {
            // Sử dụng Coil để tải ảnh từ URI
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop// Chiếm toàn bộ ô
            )
        } else {
            // Hiển thị dấu cộng nếu chưa chọn ảnh
            Icon(
                painter = painterResource(id = R.drawable.add_24),
                contentDescription = "Add Image",
                modifier = Modifier.size(40.dp) // Kích thước của dấu cộng
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    selectedCategory: String,
    onCategorySelected: (String, String) -> Unit,
    categories: List<TypeProductData> // Danh sách các loại món
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        // TextField cho dropdown
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = { },
            readOnly = true,
            label = { Text("Loại Món") },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_drop_down_),
                    contentDescription = null
                )
            },

            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)
        )

        // Dropdown Menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },

        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.type_name) },
                    onClick = {
                        onCategorySelected(category._id, category.type_name)
                        expanded = false
                    },

                )
            }
        }
    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddDishPage(modifier: Modifier = Modifier) {

    val gia = remember { mutableStateOf(0.0) }
    val tenMon = remember { mutableStateOf("") }
    val moTa = remember { mutableStateOf("") }
    val imageUri = remember { mutableStateOf("") }


    val context = LocalContext.current


    val loaiMon = remember { mutableStateOf("") }
    val loaiMonId = remember { mutableStateOf("") } // Thêm biến lưu ID loại món

    val viewModelType: TypeProductViewModel =
        remember { TypeProductViewModel() } // Khởi tạo ViewModel

    // Lấy dữ liệu loại món
    viewModelType.fetchTypeProducts()
    val categories by viewModelType.categories.observeAsState(initial = emptyList())

    Log.d("hhhh", categories.toString())


    val viewModel: AddDishViewModel = AddDishViewModel()



    Column(
        modifier = modifier
            .fillMaxSize()
            .background("#F6F6F6".toColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier
                .padding(top = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            ImagePicker { uri ->
                imageUri.value = uri // Lưu URI hình ảnh

            }

            CategoryDropdown(
                selectedCategory = loaiMon.value,
                onCategorySelected = { id, selected ->
                    loaiMon.value = selected // Cập nhật tên loại món
                    loaiMonId.value = id     // Cập nhật ID loại món
                },
                categories = categories
            )
            TextField3(
                title = "Giá",
                placeholder = "Nhập giá bán",
                value = gia.value.toString(),
                onValueChange = { newValue ->
                    gia.value = newValue.toDoubleOrNull() ?: 0.0 // Gán 0 nếu không chuyển đổi được
                }
            )
            TextField3(
                title = "Tên Món",
                placeholder = "Nhập tên món",
                value = tenMon.value,
                onValueChange = { tenMon.value = it }

            )
            TextField3(
                title = "Mô Tả",
                placeholder = "Nhập mô tả",
                value = moTa.value,
                onValueChange = { moTa.value = it }

            )
            // Nút để gửi thông tin món ăn
            Button(
                onClick = {
                    try {

                        // Gọi hàm addDish khi nhấn nút
                        viewModel.addDish(
                            loaiMon = TypeProductData(
                                _id = loaiMonId.value,
                                type_name = loaiMon.value
                            ),
                            gia = gia.value,
                            tenMon = tenMon.value,
                            imageUri = imageUri.value,
                            moTa = moTa.value,
                            onSuccess = {
                                Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT)
                                    .show()

                                // Đặt lại các giá trị của input về trống
                                loaiMon.value = ""
                                gia.value = 0.0
                                tenMon.value = ""
                                moTa.value = ""
                                imageUri.value = ""
                            },
                            onError = { errorMessage ->
                                // Log lỗi khi có lỗi từ hàm onError
                                Log.e("AddDishError", "Lỗi khi thêm món ăn: $errorMessage")
                                Toast.makeText(
                                    context,
                                    "Thêm thất bại: $errorMessage",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    } catch (e: Exception) {
                        // Log ngoại lệ nếu có
                        Log.e("AddDishException", "Exception xảy ra khi thêm món: ${e.message}", e)
                        Toast.makeText(context, "Thêm thất bại: ${e.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                modifier = Modifier
                    .padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = "#FE724C".toColor,
                    contentColor = "#FFFFFF".toColor
                )
            ) {
                Text(text = "Thêm món")
            }
        }

    }

}