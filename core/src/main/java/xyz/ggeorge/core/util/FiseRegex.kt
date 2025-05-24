package xyz.ggeorge.core.util

/**
 * Templates
 *
 * Date: 22/05/2025
 *
 * 1. Vale Procesado correctamente
 * El cupon se proceso correctamente.
 * DNI: 27152814
 * CUPON: 0505255171462
 * IMPORTE: S/. 20
 *
 * 2. Vale Procesado anteriormente
 * VALE PROCESADO: 2024/10/25 22:45:04 000010271692612
 *
 * 3. Vale No Procesado
 * DOC.BENEF. O VALE ERRADO
 *
 * 4. Consultar de saldo
 * El saldo de su cuenta ah01 es:
 * Saldo disponible: S/. 200.51
 * Saldo contable: S/. 200.51
 */


sealed class FiseRegex {
    abstract val pattern: Regex

    companion object {
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

