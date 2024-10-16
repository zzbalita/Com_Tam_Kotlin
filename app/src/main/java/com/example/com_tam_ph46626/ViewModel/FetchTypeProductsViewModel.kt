package com.example.com_tam_ph46626.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.com_tam_ph46626.Model.TypeProduct.TypeProductData
import com.example.com_tam_ph46626.Retrofit.RetrofitClient
import com.example.com_tam_ph46626.Service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TypeProductViewModel : ViewModel() {
    private val _categories = MutableLiveData<List<TypeProductData>>()
    val categories: LiveData<List<TypeProductData>> get() = _categories

    private val apiService: ApiService = RetrofitClient.instance


    fun fetchTypeProducts() {
        apiService.getTypeProducts().enqueue(object : Callback<List<TypeProductData>> {
            override fun onResponse(call: Call<List<TypeProductData>>, response: Response<List<TypeProductData>>) {
                if (response.isSuccessful) {
                    // Cập nhật danh sách loại món
                    _categories.value = response.body() ?: emptyList()
                } else {
                    // Xử lý lỗi nếu cần
                    _categories.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<TypeProductData>>, t: Throwable) {
                // Xử lý lỗi khi gọi API thất bại
                _categories.value = emptyList()
            }
        })
    }
}
