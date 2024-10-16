package com.example.com_tam_ph46626

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.auth0.android.jwt.JWT
import com.example.com_tam_ph46626.Model.Login.LoginRequest
import com.example.com_tam_ph46626.Model.Login.LoginResponse
import com.example.com_tam_ph46626.Retrofit.RetrofitClient
import com.example.com_tam_ph46626.ui.theme.Com_Tam_Ph46626Theme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Com_Tam_Ph46626Theme  {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Login(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TextField2(
    title: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null
) {

    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            modifier = Modifier
                .width(400.dp),
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            trailingIcon = {
                trailingIcon?.invoke()
            },
        )
    }
}


val String.toColor: Color
    get() = Color(android.graphics.Color.parseColor(this))

fun loginUser(email: String, password: String, context: Context) {
    // Kiểm tra dữ liệu đầu vào
    if (email.isEmpty() || password.isEmpty()) {
        Toast.makeText(context, "Các trường không được bỏ trống", Toast.LENGTH_SHORT).show()
        return
    }

    // Tạo Retrofit instance và gọi API
    val apiService = RetrofitClient.instance

    val loginRequest = LoginRequest(
        email = email,
        password = password
    )



    apiService.loginUser(loginRequest).enqueue(object : Callback<LoginResponse?> {
        override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>) {
            if (response.isSuccessful) {
                val token = response.body()?.token
//                val userId = response.body()?.userId // Lấy userId từ API trả về

                val jwt = JWT(token!!)
                val userId = jwt.getClaim("userId").asString()

                Log.d("Login", "Đăng nhập thành công, token: $token")
                Log.d("Login", "Đăng nhập thành công, userId: $userId")
                Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()


                //  lưu token vào SharedPreferences để sử dụng cho các lần tiếp theo
                val sharedPref = context.getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("auth_token", token)
                editor.putString("user_id", userId) // Lưu userId
                RetrofitClient.token = token
                editor.apply()

                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            } else {
                Log.e("Login", "Đăng nhập thất bại: ${response.code()}")
                Toast.makeText(context, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()

            }
        }

        override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
            Log.e("Login", "Lỗi khi đăng nhập: ${t.message}")
            Toast.makeText(context, "Lỗi khi đăng nhập", Toast.LENGTH_SHORT).show()

        }
    })
}


@Composable
fun Login(modifier: Modifier = Modifier) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val saveLogin = remember { mutableStateOf(false) } // State để lưu trạng thái Switch

    val currentContext = LocalContext.current;

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = "#F6F6F6".toColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "My Image",
            modifier = Modifier
                .size(190.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Fit
        )





        Text(
            text = "Login",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 20.dp, bottom = 8.dp),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
        )

        TextField2(
            title = "E-mail",
            placeholder = "Enter your email",
            trailingIcon = {
                IconButton(onClick = { /* Xử lý khi nhấn */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.email_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            value = email.value,
            onValueChange = {email.value = it}
        )
        TextField2(
            title = "Password",
            placeholder = "Enter your password",
            trailingIcon = {
                IconButton(onClick = { /* Xử lý khi nhấn */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.eye_24),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            value = password.value,
            onValueChange = {password.value = it}
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp).fillMaxWidth()
        ) {

            Switch(
                checked = saveLogin.value,
                onCheckedChange = { saveLogin.value = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = "#FE724C".toColor,
                    uncheckedThumbColor = Color.Gray
                )
            )
            Text(
                text = " Save login info",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        Text(
            text = "Forgot password?",
            color = "#FE724C".toColor, // Màu cam
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        val context = LocalContext.current

        Button(
            onClick = {
                loginUser(email.value, password.value, currentContext)
            },
            modifier = Modifier
                .padding(16.dp)
                .height(45.dp)
                .width(140.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = "#FE724C".toColor,
                contentColor = "#FFFFFF".toColor
            )
        ) {
            Text(text = "LOGIN")
        }




        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            TextButton(
                onClick = {
                    val intent = Intent(context, SignUpScreen::class.java)
                    context.startActivity(intent)
                }
            ) {
                Text(
                    text = "Sign Up",
                    color = "#FE724C".toColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview3() {
    Com_Tam_Ph46626Theme  {
        Login()
    }
}
