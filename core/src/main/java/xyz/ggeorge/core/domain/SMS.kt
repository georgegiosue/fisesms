package xyz.ggeorge.core.domain

data class SMS(val phone: String, val body: String) {
    private fun normalize(number: String) = number.removePrefix("+")

    fun passSms(): Boolean {
        val normalizedPhone = normalize(phone)
        return Fise.SERVICE_PHONE_NUMBERS
            .map { normalize(it) }
            .any { it == normalizedPhone }
    }
}