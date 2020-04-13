package com.alavpa.covid19.presentation.main

import androidx.lifecycle.MutableLiveData
import com.alavpa.covid19.domain.interactors.GetCountries
import com.alavpa.covid19.domain.model.Country
import com.alavpa.covid19.presentation.base.Presenter
import java.util.*

class MainPresenter(private val getCountries: GetCountries) : Presenter() {
    fun loadCountries() {
        getCountries.exec {
            val countries = if (viewModel.query.isEmpty()) it else it.filter { country ->
                country.country.toLowerCase(Locale.ROOT)
                    .contains(viewModel.query.toLowerCase(Locale.ROOT))
            }
            render(viewModel.copy(countries = countries, country = null))
        }
    }

    val renderLiveData = MutableLiveData<ViewModel>()
    private val viewModel: ViewModel
        get() = renderLiveData.value ?: ViewModel()

    private fun render(viewModel: ViewModel) {
        renderLiveData.value = viewModel
    }

    fun submitQuery(query: String?) {
        if (viewModel.query != query) {
            render(viewModel.copy(query = query ?: ""))
            loadCountries()
        }
    }

    fun onQueryChange(newText: String?) {
        if (viewModel.query != newText) {
            render(viewModel.copy(query = newText ?: ""))
            loadCountries()
        }
    }

    fun openCountry(country: Country) {
        render(viewModel.copy(firstRun = false, country = country))
    }

    data class ViewModel(
        val firstRun: Boolean = true,
        val countries: List<Country> = listOf(),
        val query: String = "",
        val country: Country? = null
    )
}