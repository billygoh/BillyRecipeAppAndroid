package com.billygdev.billyrecipeappandroid

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    }

    override fun onResume() {
        super.onResume()
        loadRecipe()
    }

    fun loadRecipe() {
        recipe = localDB.getRecipeDetails(recipeID)
        title = recipe?.name
        val iv = findViewById<ImageView>(R.id.recipeDetailsIV)
        val imageURL = "${this.filesDir}/${recipe?.imageURL}"
        Glide.with(this).load(imageURL).into(iv)
        recipeDetailsPrepTimeTV.text = "Prep Time: ${recipe?.prepTime}"
        recipeDetailsIngredientTV.text = recipe?.ingredients
        recipeDetailsStepsTV.text = recipe?.steps
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_recipe_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.edit_action) {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Select your action")
                .setCancelable(true)
                .setPositiveButton("Edit", DialogInterface.OnClickListener {
                        dialog, id -> toAddUpdateRecipePage()
                })
                .setNegativeButton("Remove", DialogInterface.OnClickListener {
                        dialog, id -> removeRecipe()
                })

            val alert = dialogBuilder.create()
            alert.show()
        }
        return true
    }

    fun toAddUpdateRecipePage() {
        val intent = Intent(this, AddUpdateRecipeActivity::class.java)
        intent.putExtra("isEditingRecipe", true)
        intent.putExtra("recipeID", recipe?.id)
        intent.putExtra("recipeName", recipe?.name)
        intent.putExtra("recipeImageURL", recipe?.imageURL)
        intent.putExtra("recipePrepTime", recipe?.prepTime)
        intent.putExtra("recipeIngredients", recipe?.ingredients)
        intent.putExtra("recipeSteps", recipe?.steps)
        startActivity(intent)
    }

    fun removeRecipe() {
        var success = localDB.removeRecipe(recipeID)
        if(success) {
            Toast.makeText(this, "Successfully removed recipe!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Problem removing recipe", Toast.LENGTH_SHORT).show()
        }
    }
}
