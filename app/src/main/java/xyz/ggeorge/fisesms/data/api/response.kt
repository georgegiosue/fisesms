package xyz.ggeorge.fisesms.data.api

data class ApiResponse(
    val coupon: Coupon,
    val dni: Dni,
    val metadata: Metadata
)

data class Coupon(
    val region: String,
    val code: String
)

data class Dni(
    val region: String,
    val number: String
)

data class Metadata(
    val processing_time: String
)
