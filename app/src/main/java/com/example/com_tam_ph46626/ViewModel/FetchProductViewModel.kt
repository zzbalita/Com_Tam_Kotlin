package com.example.com_tam_ph46626.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.com_tam_ph46626.Model.Product.ProductData
import com.example.com_tam_ph46626.Retrofit.RetrofitClient
import com.example.com_tam_ph46626.Service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel : ViewModel() {
    private val apiService: ApiService = RetrofitClient.instance


    private val _products = MutableLiveData<List<ProductData>>()
    val products: LiveData<List<ProductData>> get() = _products

    // LiveData chứa chi tiết sản phẩm theo id
    private val _productDetail = MutableLiveData<ProductData?>()
    val productDetail: LiveData<ProductData?> get() = _productDetail

    // Hàm để fetch danh sách sản phẩm
    fun fetchProduct() {
        apiService.getProducts().enqueue(object : Callback<List<ProductData>?> {
            override fun onResponse(
                call: Call<List<ProductData>?>,
                response: Response<List<ProductData>?>
            ) {
                if (response.isSuccessful) {
                    _products.value = response.body() ?: emptyList()
                    Log.d("ProductViewModel", "Products fetched: ${_products.value}")

                } else {
                    Log.e("ProductViewModel", "Error response: ${response.errorBody()?.string()}")
                    _products.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<ProductData>?>, t: Throwable) {
                _products.value = emptyList()
                Log.e("ProductViewModel", "API call failed: ${t.message}")

            }
        })
    }

    fun fetchProductById(productId: String) {
        apiService.getProductById(productId).enqueue(object : Callback<ProductData?> {
            override fun onResponse(call: Call<ProductData?>, response: Response<ProductData?>) {
                if (response.isSuccessful) {
                    _productDetail.value = response.body()
                    Log.d("ProductViewModel", "Product fetched: ${_productDetail.value}")
                } else {
                    Log.e("ProductViewModel", "Error response: ${response.errorBody()?.string()}")
                    _productDetail.value = null
                }
            }

            override fun onFailure(call: Call<ProductData?>, t: Throwable) {
                _productDetail.value = null
                Log.e("ProductViewModel", "API call failed: ${t.message}")
            }
        })
    }


    // search
    fun searchProduct(query: String) {
        apiService.searchProducts(query).enqueue(object : Callback<List<ProductData>?> {
            override fun onResponse(
                call: Call<List<ProductData>?>,
                response: Response<List<ProductData>?>
            ) {
                Log.d("ProductViewModel", "Search query: $query")

                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("ProductViewModel", "Search result: $result")  // Thêm log để kiểm tra kết quả tìm kiếm
                    _products.postValue(result ?: emptyList())

                } else {
                    Log.e("ProductViewModel", "Search error: ${response.errorBody()?.string()}")
                    _products.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<List<ProductData>?>, t: Throwable) {
                Log.e("ProductViewModel", "Search failed: ${t.message}")
                _products.postValue(emptyList())

            }
        })
    }



}