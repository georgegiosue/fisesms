package xyz.ggeorge.core.domain

data class SMS(val phone: String, val body: String) {
    fun passSms(): Boolean = this.phone == Fise.SERVICE_PHONE_NUMBER
}