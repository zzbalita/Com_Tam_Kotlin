package com.example.com_tam_ph46626

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.com_tam_ph46626.Retrofit.RetrofitClient
import com.example.com_tam_ph46626.ui.theme.Com_Tam_Ph46626Theme
import kotlinx.coroutines.delay


class SplashScreen : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Com_Tam_Ph46626Theme  {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WelcomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}





@Composable
fun WelcomeScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current



    LaunchedEffect(Unit) {
        // Kiểm tra token trong SharedPreferences
        val sharedPref = context.getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        val token = sharedPref.getString("auth_token", null)
        Log.e("tokennn", token.toString())
        RetrofitClient.token = token
        delay(2000)
        context.startActivity(Intent(context, LoginScreen::class.java))
        (context as? Activity)?.finish()

        if (token != null) {
            // Nếu token tồn tại, chuyển đến MainActivity
            context.startActivity(Intent(context, MainActivity::class.java))
        } else {
            // Nếu không có token, chuyển đến LoginScreen
            context.startActivity(Intent(context, LoginScreen::class.java))
        }

        (context as? Activity)?.finish()
    }

    Column (
        modifier =modifier
            .fillMaxSize()
            .background(color = "#C7C2C1".toColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview2() {
    Com_Tam_Ph46626Theme  {
        WelcomeScreen()
    }
}