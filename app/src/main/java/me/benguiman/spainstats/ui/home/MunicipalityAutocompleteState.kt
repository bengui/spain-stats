package me.benguiman.spainstats.ui.home

import java.text.Normalizer

class MunicipalityAutocompleteState(private val municipalityList: List<MunicipalityUiState>) {
    fun filterMunicipalityList(selectedOptionText: String) : List<MunicipalityUiState> {
        if (selectedOptionText.isEmpty()) {
            return emptyList()
        }

        val resultList = mutableSetOf<MunicipalityUiState>()
        resultList.addAll(
            municipalityList.asSequence().filter {
                it.name
                    .removeAccents()
                    .startsWith(
                        selectedOptionText.removeAccents(), ignoreCase = true
                    )
            }.take(4)
        )
        resultList.addAll(
            municipalityList
                .asSequence()
                .filter {
                    it.name
                        .removeAccents()
                        .contains(
                            selectedOptionText.removeAccents(), ignoreCase = true
                        )
                }.take(6 - resultList.size)
        )
        return resultList.toList()
    }

    private val REGEX_ACCENTS = "\\p{InCombiningDiacriticalMarks}+".toRegex()

    private fun CharSequence.removeAccents(): String {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        return REGEX_ACCENTS.replace(temp, "")
    }
}
