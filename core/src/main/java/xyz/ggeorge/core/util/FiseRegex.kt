package xyz.ggeorge.core.util

/**
 * Sealed representation de los distintos patrones regex usados
 * para extraer campos de los SMS FISE.
 */
sealed class FiseRegex {
    abstract val pattern: Regex

    companion object {
        // Precompilamos todos los patrones en un objeto singleton
        object PROCESSED : FiseRegex() {
            override val pattern = Regex(
                "DNI: (\\d+).*?CUPON: (\\d+).*?IMPORTE: S\\/\\.\\s*(\\d+(?:\\.\\d+)?)",
                setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
            )
        }

        object PREVIOUSLY_PROCESSED : FiseRegex() {
            override val pattern = Regex(
                "VALE PROCESADO: (\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2})\\s*(\\d{6,})",
                RegexOption.DOT_MATCHES_ALL
            )
        }

        object WRONG : FiseRegex() {
            override val pattern = Regex("DOC\\.BENEF\\. O VALE ERRADO", RegexOption.IGNORE_CASE)
        }

        object SYNTAX_ERROR : FiseRegex() {
            override val pattern = Regex(".*", RegexOption.DOT_MATCHES_ALL)
        }
    }
}