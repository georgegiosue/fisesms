package xyz.ggeorge.core.domain.state

enum class ProcessState {
    INITIAL,
    PROCESSING_COUPON,
    COUPON_RECEIVED,
    ERROR_PROCESSING_COUPON,
    ERROR_AI_PROCESS,
}