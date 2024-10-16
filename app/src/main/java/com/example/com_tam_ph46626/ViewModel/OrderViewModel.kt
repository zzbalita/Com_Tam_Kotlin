package com.example.com_tam_ph46626.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.com_tam_ph46626.Model.Order.Order
import com.example.com_tam_ph46626.Retrofit.RetrofitClient
import com.example.com_tam_ph46626.Service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel : ViewModel() {
    private val apiService: ApiService = RetrofitClient.instance

    private val _orders = MutableLiveData<List<Order>?>()
    val orders: MutableLiveData<List<Order>?> get() = _orders

    fun fetchOrder() {
        apiService.getOrders().enqueue(object : Callback<List<Order>?> {
            override fun onResponse(call: Call<List<Order>?>, response: Response<List<Order>?>) {
                if (response.isSuccessful) {
                    response.body()?.let { orderList ->
                        _orders.value = orderList
                    } ?: run {
                        _orders.value = emptyList()
                    }
                }
            }

            override fun onFailure(call: Call<List<Order>?>, t: Throwable) {
                // Có thể cập nhật giá trị orders thành null hoặc log lỗi
                _orders.value = emptyList() // Hoặc _orders.value = null
                // Ghi log lỗi nếu cần
                Log.e("OrderViewModel", "Error fetching orders", t)
            }
        })
    }
}