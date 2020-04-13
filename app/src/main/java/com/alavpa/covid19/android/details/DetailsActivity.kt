package com.alavpa.covid19.android.details

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.alavpa.covid19.R
import com.alavpa.covid19.domain.model.Country
import com.alavpa.covid19.presentation.details.DetailsPresenter
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class DetailsActivity : OnChartValueSelectedListener, AppCompatActivity() {

    private val presenter: DetailsPresenter by viewModel()

    companion object {
        private const val EXTRA_COUNTRY = "EXTRA_COUNTRY"
        fun intent(context: Context, country: Country): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_COUNTRY, country)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        chart1?.setOnChartValueSelectedListener(this)
        chart1?.description?.text = "covid19api.com"

        val country = intent.getParcelableExtra<Country>(EXTRA_COUNTRY)
        presenter.renderLiveData.observe(this, Observer(::render))
        presenter.start(country)
    }

    override fun onResume() {
        super.onResume()
        presenter.load()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_date -> {
                presenter.onClickDateIcon()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun render(viewModel: DetailsPresenter.ViewModel?) {
        viewModel?.run {
            supportActionBar?.title = title
            tv_date?.text = dateText
            tv_confirmed_value?.text = currentConfirmed.toString()
            tv_recovered_value?.text = currentRecovered.toString()
            tv_deaths_value?.text = currentDeaths.toString()

            chart1?.clear()
            val setConfirmed = LineDataSet(
                confirmed.mapIndexed { index, case ->
                    Entry(index.toFloat(), case.cases.toFloat())
                },
                getString(R.string.confirmed)
            ).apply {
                color = ContextCompat.getColor(this@DetailsActivity, R.color.confirmed)
                setCircleColor(color)
            }

            val setRecovered = LineDataSet(
                recovered.mapIndexed { index, case ->
                    Entry(index.toFloat(), case.cases.toFloat())
                },
                getString(R.string.recovered)
            ).apply {
                color = ContextCompat.getColor(this@DetailsActivity, R.color.recovered)
                setCircleColor(color)
            }

            val setDeaths = LineDataSet(
                deaths.mapIndexed { index, case ->
                    Entry(index.toFloat(), case.cases.toFloat())
                },
                getString(R.string.deaths)
            ).apply {
                color = ContextCompat.getColor(this@DetailsActivity, R.color.deaths)
                setCircleColor(color)
            }


            val dataSet = listOf(setConfirmed, setRecovered, setDeaths)
            chart1?.data = LineData(dataSet)

            if (showDatePicker) {
                val calendar = Calendar.getInstance().apply { time = date }
                DatePickerDialog(
                    this@DetailsActivity,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val calendarSelected = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }
                        presenter.selectDay(calendarSelected)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).apply {
                    this.datePicker.maxDate = maxDate?.time ?: 0
                    this.datePicker.minDate = minDate?.time ?: 0
                    setCancelable(false)
                    setOnCancelListener {
                        presenter.onCancelDatePicker()
                        it.dismiss()
                    }
                }.show()
            }
        }
    }

    override fun onNothingSelected() {
        // no-op
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        e?.also {
            presenter.onSelectEntry(it)
        }
    }
}