package xyz.ggeorge.core.domain.state

enum class BalanceState {
    INITIAL,
    CHECKING_BALANCE,
    BALANCE_CHECKED,
    ERROR_CHECKING_BALANCE
}