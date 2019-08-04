package com.billygdev.billyrecipeappandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_recipe_details.*

class RecipeDetailsActivity : AppCompatActivity() {
    private val localDB = LocalDB(this)
    var recipeID: Int = 0
    var recipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        title = intent.getStringExtra("selectedRecipeName")
        recipeID = intent.getIntExtra("selectedRecipeID", 0)

        loadRecipe()
    }

    fun loadRecipe() {
        recipe = localDB.getRecipeDetails(recipeID)

        val iv = findViewById<ImageView>(R.id.recipeDetailsIV)
        val imageURL = "${this.filesDir}/${recipe?.imageURL}"
        Glide.with(this).load(imageURL).into(iv)
        recipeDetailsPrepTimeTV.text = "Prep Time: ${recipe?.prepTime}"
        recipeDetailsIngredientTV.text = recipe?.ingredients
        recipeDetailsStepsTV.text = recipe?.steps
    }
}
