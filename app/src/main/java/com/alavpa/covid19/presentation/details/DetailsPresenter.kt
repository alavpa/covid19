package com.alavpa.covid19.presentation.details

import androidx.lifecycle.MutableLiveData
import com.alavpa.covid19.domain.interactors.GetConfirmed
import com.alavpa.covid19.domain.interactors.GetDeaths
import com.alavpa.covid19.domain.interactors.GetRecovered
import com.alavpa.covid19.domain.model.Case
import com.alavpa.covid19.domain.model.Country
import com.alavpa.covid19.presentation.base.Presenter
import com.github.mikephil.charting.data.Entry
import io.reactivex.Single
import io.reactivex.functions.Function3
import java.text.DateFormat
import java.util.*

class DetailsPresenter(
    private val getConfirmed: GetConfirmed,
    private val getRecovered: GetRecovered,
    private val getDeaths: GetDeaths
) : Presenter() {

    val renderLiveData = MutableLiveData<ViewModel>()
    private val viewModel: ViewModel
        get() = renderLiveData.value ?: ViewModel()

    fun start(country: Country) {
        render(
            viewModel.copy(
                title = country.country,
                slug = country.slug
            )
        )
    }

    fun load() {
        getConfirmed.slug = viewModel.slug
        getRecovered.slug = viewModel.slug
        getDeaths.slug = viewModel.slug

        Single.zip(
            getConfirmed.build(),
            getRecovered.build(),
            getDeaths.build(),
            Function3<List<Case>, List<Case>, List<Case>, ViewModel> { confirmed, recovered, deaths ->
                val date = viewModel.date ?: confirmed.last().date
                viewModel.copy(
                    dateText = date?.let {
                        DateFormat.getDateInstance(DateFormat.MEDIUM).format(it)
                    } ?: "",
                    date = date,
                    confirmed = confirmed,
                    currentConfirmed = confirmed.find { it.date.equalDays(date) }?.cases ?: 0,
                    recovered = recovered,
                    currentRecovered = recovered.find { it.date.equalDays(date) }?.cases ?: 0,
                    deaths = deaths,
                    currentDeaths = deaths.find { it.date.equalDays(date) }?.cases ?: 0,
                    showDatePicker = false,
                    minDate = confirmed.first().date,
                    maxDate = confirmed.last().date
                )
            }
        ).exec({
            it.printStackTrace()
        }, {
            render(it)
        })
    }

    private fun render(viewModel: ViewModel) {
        renderLiveData.value = viewModel
    }

    fun onClickDateIcon() {
        render(viewModel.copy(showDatePicker = true))
    }

    fun selectDay(calendar: Calendar) {
        val date = calendar.time
        render(
            viewModel.copy(
                dateText = date?.let {
                    DateFormat.getDateInstance(DateFormat.MEDIUM).format(it)
                } ?: "",
                date = date,
                currentConfirmed = viewModel.confirmed.find { it.date.equalDays(date) }?.cases ?: 0,
                currentRecovered = viewModel.recovered.find { it.date.equalDays(date) }?.cases ?: 0,
                currentDeaths = viewModel.deaths.find { it.date.equalDays(date) }?.cases ?: 0,
                showDatePicker = false
            )
        )
    }

    fun onCancelDatePicker() {
        render(viewModel.copy(showDatePicker = false))
    }

    data class ViewModel(
        val title: String = "",
        val slug: String = "",
        val dateText: String = "",
        val date: Date? = null,
        val confirmed: List<Case> = listOf(),
        val currentConfirmed: Int = 0,
        val recovered: List<Case> = listOf(),
        val currentRecovered: Int = 0,
        val deaths: List<Case> = listOf(),
        val currentDeaths: Int = 0,
        val showDatePicker: Boolean = false,
        val minDate: Date? = null,
        val maxDate: Date? = null
    )

    private fun Date?.equalDays(date: Date?): Boolean {
        return this?.let { thisDate ->
            date?.let { otherDate ->
                val thisCalendar = Calendar.getInstance().apply { time = thisDate }
                val otherCalendar = Calendar.getInstance().apply { time = otherDate }
                thisCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR) &&
                        thisCalendar.get(Calendar.MONTH) == otherCalendar.get(Calendar.MONTH) &&
                        thisCalendar.get(Calendar.DAY_OF_MONTH) == otherCalendar.get(Calendar.DAY_OF_MONTH)
            } ?: false
        } ?: false
    }

    fun onSelectEntry(entry: Entry) {
        val date = viewModel.confirmed[entry.x.toInt()].date ?: viewModel.confirmed.last().date
        selectDay(Calendar.getInstance().apply { time = date })
    }
}