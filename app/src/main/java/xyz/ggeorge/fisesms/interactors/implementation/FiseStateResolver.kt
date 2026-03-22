package xyz.ggeorge.fisesms.interactors.implementation

import timber.log.Timber
import xyz.ggeorge.core.domain.state.FiseState

object FiseStateResolver {

    private val TOKEN_SPLIT_REGEX: Regex = "\\s+".toRegex()
    private val logger = Timber.tag("FiseStateResolver")

    fun determine(body: String?): FiseState {
        val tokens: List<String> = body
            ?.trim()
            ?.split(TOKEN_SPLIT_REGEX)
            .orEmpty()

        tokens.getOrNull(1)
            ?.takeIf { it.equals("saldo", ignoreCase = true) }
            ?.let { return FiseState.CHECK_BALANCE }

        return when (tokens.firstOrNull()?.uppercase()) {
            "EL" -> FiseState.PROCESSED
            "VALE" -> FiseState.PREVIOUSLY_PROCESSED
            "DOC.BENEF." -> FiseState.WRONG
            else -> {
                logger.w("Token inesperado: ${tokens.firstOrNull()}")
                FiseState.SYNTAX_ERROR
            }
        }
    }
}
