package com.example.com_tam_ph46626

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.com_tam_ph46626.Model.Register.RegisterRequest
import com.example.com_tam_ph46626.Model.Register.RegisterResponse
import com.example.com_tam_ph46626.Retrofit.RetrofitClient
import com.example.com_tam_ph46626.ui.theme.Com_Tam_Ph46626Theme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Com_Tam_Ph46626Theme  {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SignUp(

                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

fun registerUser(fullName: String, username: String, email: String, password: String, context: Context) {
    // Kiểm tra dữ liệu nhập vào
    if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
        Toast.makeText(context, "Các trường không được bỏ trống", Toast.LENGTH_SHORT).show()
        return
    }

    // Tạo Retrofit instance và gọi API
    val apiService = RetrofitClient.instance

    val registerRequest = RegisterRequest(
        name = fullName,
        username = username,
        email = email,
        password = password
    )

    apiService.registerUser(registerRequest).enqueue(object : Callback<RegisterResponse?> {
        override fun onResponse(
            call: Call<RegisterResponse?>,
            response: Response<RegisterResponse?>
        ) {
            if (response.isSuccessful) {
                Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Đăng ký thất bại: ${response.code()}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {
            Log.e("Register", "Lỗi khi đăng ký: ${t.message}")
            Toast.makeText(context, "Lỗi khi đăng ký: ${t.message}", Toast.LENGTH_SHORT).show()

        }
    })
}


@Composable
fun SignUp(modifier: Modifier = Modifier) {

    val fullName = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

   Column (
       modifier = modifier
           .fillMaxSize()
           .background(color = "#F6F6F6".toColor),
       verticalArrangement = Arrangement.Center,
       horizontalAlignment = Alignment.CenterHorizontally
   ) {
       Text(
           text = "Sign Up",
           modifier = Modifier
               .align(Alignment.Start)
               .padding(start = 20.dp, bottom = 8.dp),
           fontSize = 26.sp,
           fontWeight = FontWeight.Bold,
       )

       TextField2("User name",
           "Enter your user name",
           value = username.value,
           onValueChange = {username.value = it})

       TextField2("Full name",
           "Enter your full name",
           value = fullName.value,
           onValueChange = {fullName.value = it})

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

       val currentContext = LocalContext.current;

       Button(
           onClick = {
               Log.i("MyButton", "Button clicked!")
               registerUser(fullName.value, username.value, email.value, password.value, currentContext)
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
           Text(text = "SIGN UP")
       }


       val context = LocalContext.current

       Row(
           modifier = Modifier.padding(top = 16.dp),
           verticalAlignment = Alignment.CenterVertically
       ) {
           Text(
               text = "Already have an account?",
               color = Color.Black,
               fontSize = 16.sp,
               fontWeight = FontWeight.Normal
           )
           TextButton(
               onClick = {
                   val intent = Intent(context, LoginScreen::class.java)
                   context.startActivity(intent)
               }
           ) {
               Text(
                   text = "Login",
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
fun GreetingPreview4() {
    Com_Tam_Ph46626Theme  {
        SignUp()
    }
}