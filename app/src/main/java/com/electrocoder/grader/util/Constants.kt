package com.electrocoder.grader.util

object Constants {

    const val IME_PREDMETA_TAG = "ime_predmeta"

    const val PODSJETNIK_TESTA_TEXT = "test_podsjetnik"
    const val PODSJETNIK_TESTA_CHANNEL_ID = "PODSJETNIK_TESTOVA_CHANNEL_ID"

    const val PRVI_PODSJETNIK_TESTA_ID = 10
    const val DRUGI_PODSJETNIK_TESTA_ID = 20

    enum class CONTEXT_MENU_OPTIONS(opcija: Int) {
        CONTEXT_MENU_DELETE_PREDMET(1),
        CONTEXT_MENU_DELETE_OCJENE_PREDMETA(2)
    }

}