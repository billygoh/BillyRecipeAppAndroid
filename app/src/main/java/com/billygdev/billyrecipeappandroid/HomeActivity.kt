package com.billygdev.billyrecipeappandroid

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner

class HomeActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val localDB = LocalDB(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val sharedPref = this.getSharedPreferences("RECIPE_PREF", Context.MODE_PRIVATE)
        val selectedRecipeTypeID = sharedPref.getInt("selectedRecipeTypeID", 0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)

        val item: MenuItem = menu!!.findItem(R.id.filter_action)
        val spinner: Spinner = item.actionView as Spinner

        spinner.onItemSelectedListener = this
        val arrayAdapter = SpinnerCustomAdapter(this, localDB.showRecipeTypes(), true)
        spinner.adapter = arrayAdapter

        return true
    }

    override fun onItemSelected(av: AdapterView<*>?, view: View?, index: Int, id: Long) {
        println(":::$id")
    }

    override fun onNothingSelected(av: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
