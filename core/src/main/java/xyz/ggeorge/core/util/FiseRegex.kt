package xyz.ggeorge.core.util

sealed class FiseRegex {
    infix fun String.match(target: String): MatchResult? {
        return this.toRegex().find(target)
    }

    object PROCESSED : FiseRegex() {
        const val DNI: String = """DNI: (\d+)"""
        const val VALE: String = """Cupon: (\d+)"""
        const val AMOUNT: String = """Importe: S/. (\d+\.\d+)"""
    }

    object PREVIOUSLY_PROCESSED : FiseRegex() {
        const val DATE: String = """\d{4}/\d{2}/\d{2} \d{2}:\d{2}:\d{2}"""
        const val AGENT_DNI: String = """\d{14}"""
    }

    object WRONG : FiseRegex() {
        const val MSG = """.*"""
    }

    object SYNTAX_ERROR : FiseRegex() {
        const val MSG = """.*"""
    }
}