package com.billygdev.billyrecipeappandroid

import android.content.Context
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val localDB = LocalDB(this)
    var recipeArr = ArrayList<Recipe>()
    var ignoreSpinnerFirstCall = true
    var adapter: RecipeRVCustomAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val sharedPref = this.getSharedPreferences("RECIPE_PREF", Context.MODE_PRIVATE)
        val selectedRecipeTypeID = sharedPref.getInt("selectedRecipeTypeID", 0)

        val gridLayoutManager = GridLayoutManager(this, 2)
        recipeRV.layoutManager = gridLayoutManager

        recipeArr = localDB.getRecipeList(selectedRecipeTypeID)
        adapter = RecipeRVCustomAdapter(this, recipeArr)
        recipeRV.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val sharedPref = this.getSharedPreferences("RECIPE_PREF", Context.MODE_PRIVATE)
        val selectedRecipeTypeID = sharedPref.getInt("selectedRecipeTypeID", 0)

        menuInflater.inflate(R.menu.menu_home, menu)

        val item: MenuItem = menu!!.findItem(R.id.filter_action)
        val spinner: Spinner = item.actionView as Spinner

        spinner.onItemSelectedListener = this
        val arrayAdapter = SpinnerCustomAdapter(this, localDB.showRecipeTypes(), true)
        spinner.adapter = arrayAdapter
        spinner.setSelection(selectedRecipeTypeID-1)

        return true
    }

    override fun onItemSelected(av: AdapterView<*>?, view: View?, index: Int, id: Long) {
        if(!ignoreSpinnerFirstCall) {
            recipeArr.clear()
            recipeArr.addAll(localDB.getRecipeList(id.toInt()))
            adapter?.notifyDataSetChanged()

            val sharedPref = this.getSharedPreferences("RECIPE_PREF", Context.MODE_PRIVATE)
            sharedPref.edit().putInt("selectedRecipeTypeID", id.toInt()).commit()
        }
        ignoreSpinnerFirstCall = false
    }

    override fun onNothingSelected(av: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
