package com.billygdev.billyrecipeappandroid

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_update_recipe.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddUpdateRecipeActivity : AppCompatActivity() {

    private val localDB = LocalDB(this)
    var isEditingRecipe = false
    var imageToUploadURI: Uri? = null
    var recipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_recipe)

        isEditingRecipe = intent.getBooleanExtra("isEditingRecipe", false)

        if(!isEditingRecipe) {
            title = "Add Recipe"
            addUpdateRecipeBtn.text = "Add Recipe"
        } else {
            title = "Edit Recipe"
            addUpdateRecipeBtn.text = "Update Recipe"
        }

        recipeAddImageIV.setOnClickListener(View.OnClickListener {
            performImagePicker()
        })

        addUpdateRecipeBtn.setOnClickListener(View.OnClickListener {
            saveDetails()
        })

    }

    fun performImagePicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else{
                //permission already granted
                galleryPicker();
            }
        } else {
            galleryPicker()
        }
    }

    fun galleryPicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == PERMISSION_CODE) {
            if(grantResults.isNotEmpty() && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                galleryPicker()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == GALLERY_CODE) {
            recipeAddImageIV.setImageURI(data?.data)
            imageToUploadURI = data?.data
        }
    }

    fun saveDetails() {
        val sharedPref = this.getSharedPreferences("RECIPE_PREF", Context.MODE_PRIVATE)
        val selectedRecipeTypeID = sharedPref.getInt("selectedRecipeTypeID", 0)
        var imageFileName = ""
        var success = false
        var successMsg = ""
        if(imageToUploadURI != null) {
            imageFileName = "${SimpleDateFormat("yyyyMMddHHmmss").format(Date())}.jpg"
            println("imagee:::$imageFileName")
            val bm: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageToUploadURI)
            val file = File(this.filesDir, imageFileName)

            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(file)

                bm.compress(Bitmap.CompressFormat.JPEG, 70, fos)

                fos.close()
            } catch (e: IOException) {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }
                }
            }
        }

        if(!isEditingRecipe) {
            var recipeArr: Array<Recipe> = emptyArray()
            recipeArr += Recipe(0, selectedRecipeTypeID, recipeNameET.text.toString(), imageFileName, recipeIngredientET.text.toString(), recipeStepsET.text.toString(), recipePrepTimeET.text.toString())
            success = localDB.addRecipe(recipeArr, selectedRecipeTypeID)
            successMsg = "Successfully added recipe!"
        } else {
            if(recipe != null) {
                if (imageFileName == "") {
                    imageFileName = recipe!!.imageURL
                }
                success = localDB.editRecipe(Recipe(0, selectedRecipeTypeID, recipeNameET.text.toString(), imageFileName, recipeIngredientET.text.toString(), recipeStepsET.text.toString(), recipePrepTimeET.text.toString()))
                successMsg = "Successfully updated recipe!"
            }
        }

        if(success) {
            Toast.makeText(this, successMsg, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "There are problems occurred", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private val GALLERY_CODE = 1
        private val PERMISSION_CODE = 2
    }
}
