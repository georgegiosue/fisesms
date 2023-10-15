package xyz.ggeorge.core.state

enum class FiseState {
    TO_SEND,
    PREVIOUSLY_PROCESSED,
    PROCESSED,
    WRONG,
    SYNTAX_ERROR,
}