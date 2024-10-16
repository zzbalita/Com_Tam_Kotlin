package com.example.com_tam_ph46626.ViewModel

import androidx.lifecycle.ViewModel
import com.example.com_tam_ph46626.Model.Cart.CartRequest
import com.example.com_tam_ph46626.Model.Cart.CartResponse
import com.example.com_tam_ph46626.Retrofit.RetrofitClient
import com.example.com_tam_ph46626.Service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCartViewModel : ViewModel() {
    private val apiService: ApiService = RetrofitClient.instance

    fun addCart(
        productId: String,
        quatity: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val cartRequest = CartRequest(
            productId = productId,
            quantity = quatity
        )

        apiService.addToCart(cartRequest).enqueue(object : Callback<CartResponse?> {
            override fun onResponse(call: Call<CartResponse?>, response: Response<CartResponse?>) {
                if (response.isSuccessful) {
                    // Thành công
                    onSuccess()
                } else {
                    // Xử lý lỗi từ server
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CartResponse?>, t: Throwable) {
                onError("Failure: ${t.message}")

            }
        })
    }
}