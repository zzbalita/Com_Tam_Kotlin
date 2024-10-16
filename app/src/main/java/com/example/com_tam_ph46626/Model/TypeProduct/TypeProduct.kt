package com.example.com_tam_ph46626.Model.TypeProduct

data class TypeProductRequest(
    val type_name: String,
)

data class TypeProductResponse(
    val msg: String,
    val type_product: TypeProductData
)

data class TypeProductData(
    val _id: String,
    val type_name: String,
)
