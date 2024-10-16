package com.example.com_tam_ph46626.Model.Checkout

//data class CheckoutRequest(
//    val products: List<ProductData>,
//    val userId: String,
//    val address: String,
//    val phoneNumber: String,
//
//)
//
//data class CheckoutResponse(
//    val success: Boolean,             // Trạng thái thành công của yêu cầu đặt hàng
//    val message: String,              // Tin nhắn phản hồi từ server
//    val orderId: String?,             // ID của đơn hàng vừa được tạo
//    val totalAmount: Double?,         // Tổng giá trị đơn hàng
//    val items: List<CheckoutItem>?,   // Danh sách sản phẩm trong đơn hàng
//    val shippingAddress: ShippingAddress?, // Địa chỉ giao hàng
//    val status: String?,              // Trạng thái của đơn hàng (Pending, Shipped, Delivered)
//    val createdAt: String?            // Thời gian tạo đơn hàng
//)
//
//data class CheckoutItem(
//    val productId: String,
//    val quantity: Int
//)
//data class ShippingAddress(
//    val address: String,     // Địa chỉ giao hàng
//    val phoneNumber: String  // Số điện thoại người nhận
//)


// Yêu cầu gửi đơn hàng (CheckoutRequest)
data class CheckoutRequest(
    val items: List<CheckoutItem>,  // Danh sách các sản phẩm và số lượng
    val address: String,            // Địa chỉ giao hàng
    val phoneNumber: String         // Số điện thoại người nhận
)

// Phản hồi khi gửi đơn hàng thành công (CheckoutResponse)
data class CheckoutResponse(
    val success: Boolean,             // Trạng thái thành công của yêu cầu đặt hàng
    val message: String,              // Tin nhắn phản hồi từ server
    val orderId: String?,             // ID của đơn hàng vừa được tạo
    val totalAmount: Double?,         // Tổng giá trị đơn hàng
    val items: List<CheckoutItem>?,   // Danh sách sản phẩm trong đơn hàng
    val shippingAddress: ShippingAddress?, // Địa chỉ giao hàng
    val status: String?,              // Trạng thái của đơn hàng (Pending, Shipped, Delivered)
    val createdAt: String?            // Thời gian tạo đơn hàng
)

// Dữ liệu của mỗi sản phẩm trong giỏ hàng
data class CheckoutItem(
    val productId: String,  // ID sản phẩm
    val quantity: Int       // Số lượng sản phẩm
)

// Thông tin địa chỉ giao hàng
data class ShippingAddress(
    val address: String,     // Địa chỉ giao hàng
    val phoneNumber: String  // Số điện thoại người nhận
)
