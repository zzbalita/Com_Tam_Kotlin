package com.example.com_tam_ph46626.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.com_tam_ph46626.Model.Checkout.CheckoutItem
import com.example.com_tam_ph46626.Model.Checkout.CheckoutRequest
import com.example.com_tam_ph46626.Model.Checkout.CheckoutResponse
import com.example.com_tam_ph46626.Retrofit.RetrofitClient
import com.example.com_tam_ph46626.Service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutViewModel {
    private val apiService: ApiService = RetrofitClient.instance

    // MutableLiveData để chứa phản hồi checkout từ API
    private val _checkoutResponse = MutableLiveData<CheckoutResponse>()
    val checkoutResponse: LiveData<CheckoutResponse> get() = _checkoutResponse

    // MutableLiveData để chứa lỗi nếu có
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    fun checkout(
//        products: List<ProductData>,
//        userId: String,
//        phoneNumber: String,
//        address: String,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit
        items: List<CheckoutItem>,   // Sử dụng danh sách các CheckoutItem thay vì ProductData
        phoneNumber: String,
        address: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
//        val checkoutRequest = CheckoutRequest(
//            products = products,
//            userId = userId,
//            phoneNumber = phoneNumber,
//            address = address,
//        )
// Tạo đối tượng CheckoutRequest với các tham số cần thiết
        val checkoutRequest = CheckoutRequest(
            items = items,
            phoneNumber = phoneNumber,
            address = address
        )

        apiService.checkout(checkoutRequest).enqueue(object : Callback<CheckoutResponse?> {
            override fun onResponse(
                call: Call<CheckoutResponse?>,
                response: Response<CheckoutResponse?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    // Cập nhật LiveData khi thành công
                    _checkoutResponse.value = response.body()
                    onSuccess()
                } else {
                    // Nếu có lỗi từ phía server
                    _error.value = "Lỗi từ server: ${response.message()}"
                    onError("Lỗi từ server: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CheckoutResponse?>, t: Throwable) {
                // Nếu có lỗi về kết nối hoặc lỗi khác
                _error.value = "Lỗi kết nối: ${t.message}"
                onError("Lỗi kết nối: ${t.message}")
            }
        })
    }
}