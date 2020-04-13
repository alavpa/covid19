package com.alavpa.covid19.android.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alavpa.covid19.R
import com.alavpa.covid19.android.details.DetailsActivity
import com.alavpa.covid19.domain.model.Country
import com.alavpa.covid19.presentation.main.MainPresenter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    companion object {
        private const val KEY_COUNTRY = "KEY_COUNTRY"
    }

    private val presenter: MainPresenter by viewModel()
    private val preferences: SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        rv_countries?.layoutManager = LinearLayoutManager(this)
        rv_countries?.adapter = CountryAdapter(this) {
            presenter.openCountry(it)
        }
        presenter.renderLiveData.observe(this, Observer(::render))
    }

    private fun openCountry(country: Country) {
        val serialized = Gson().toJson(country)
        preferences.edit().putString(KEY_COUNTRY, serialized).apply()
        DetailsActivity.intent(this, country).also { intent ->
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as? SearchView
        searchView?.setIconifiedByDefault(false)
        searchView?.setOnQueryTextListener(this)
        return true
    }

    private fun render(viewModel: MainPresenter.ViewModel?) {
        viewModel?.run {
            val adapter = rv_countries?.adapter as? CountryAdapter
            adapter?.set(countries)
            if (firstRun && preferences.contains(KEY_COUNTRY)) {
                val serialized: String? = preferences.getString(KEY_COUNTRY, null)
                serialized?.also {
                    val country = Gson().fromJson(it, Country::class.java)
                    presenter.openCountry(country)
                }
            }

            if (country != null) {
                openCountry(country)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadCountries()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    class CountryAdapter(
        private val context: Context,
        private val items: MutableList<Country> = mutableListOf(),
        private val onClickItem: (Country) -> Unit
    ) : RecyclerView.Adapter<CountryVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryVH {
            return LayoutInflater.from(context)
                .inflate(R.layout.view_country, parent, false)
                .let { CountryVH(it) }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: CountryVH, position: Int) {
            holder.bind(items[position])
            holder.itemView.setOnClickListener { onClickItem(items[position]) }
        }

        fun set(countries: List<Country>) {
            this.items.clear()
            this.items.addAll(countries)
            notifyDataSetChanged()
        }
    }

    class CountryVH(view: View) : RecyclerView.ViewHolder(view) {
        private val name = itemView.findViewById<TextView>(R.id.name)
        fun bind(country: Country) {
            name.text = country.country
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        presenter.submitQuery(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        presenter.onQueryChange(newText)
        return true
    }
}
