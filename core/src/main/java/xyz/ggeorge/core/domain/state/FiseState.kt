package xyz.ggeorge.core.domain.state

enum class FiseState {
    TO_SEND,
    PREVIOUSLY_PROCESSED,
    PROCESSED,
    WRONG,
    SYNTAX_ERROR,
    CHECK_BALANCE
}