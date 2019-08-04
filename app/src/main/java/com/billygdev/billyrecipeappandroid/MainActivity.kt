package com.billygdev.billyrecipeappandroid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_main.*
import org.xmlpull.v1.XmlPullParser
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.Handler
import android.widget.Toast
import kotlin.concurrent.schedule
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val localDB = LocalDB(this)
    var currentSelectedRecipeTypeID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(checkRecipeTypeSelected()) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        } else {
            var recipeTypeArr: Array<String> = emptyArray()

            val xmlParser: XmlPullParser = this.resources.getXml(R.xml.recipetypes)
            while (xmlParser.eventType != XmlPullParser.END_DOCUMENT) {
                if(xmlParser.name == "type") {
                    val name = xmlParser.getAttributeValue(null,"name")
                    if(name != null) {
                        recipeTypeArr += name
                    }
                }
                xmlParser.next()
            }

            localDB.populateRecipeTypes(recipeTypeArr)

            recipeTypeSpinner.onItemSelectedListener = this
            val arrayAdapter = SpinnerCustomAdapter(this, localDB.showRecipeTypes(), false)
            recipeTypeSpinner.adapter = arrayAdapter
        }

        beginBtn.setOnClickListener(View.OnClickListener {
            val sharedPref = this.getSharedPreferences("RECIPE_PREF", Context.MODE_PRIVATE)
            loadingIndicator.visibility = View.VISIBLE
            beginBtn.isEnabled = false

            Handler().postDelayed({
                for (i in 1..5) {
                    preAddData(i)
                }

                if(currentSelectedRecipeTypeID == 0) {
                    currentSelectedRecipeTypeID = localDB.showRecipeTypes()[0].id
                }

                sharedPref.edit().putInt("selectedRecipeTypeID", currentSelectedRecipeTypeID).commit()

                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }, 500)
        })
    }

    private fun checkRecipeTypeSelected(): Boolean {
        val recipeTypeArr = localDB.showRecipeTypes()
        val sharedPref = this.getSharedPreferences("RECIPE_PREF", Context.MODE_PRIVATE)

        if(recipeTypeArr.isNotEmpty()) {
            val selectedRecipeTypeID = sharedPref.getInt("selectedRecipeTypeID", 0)
            if(selectedRecipeTypeID != 0) {
                return true
            }
        }

        return false
    }

    private fun preAddData(recipeTypeID: Int) {
        var recipeArr: Array<Recipe> = emptyArray()
        val mockText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam eu lectus eget purus placerat dignissim non et neque. Ut euismod libero eu ante egestas ultricies. Ut nec dictum metus. Sed lacus nibh, gravida sed blandit sed, aliquet nec sapien. Fusce vulputate nulla nec interdum imperdiet. Duis fringilla quis diam in tempus. Sed pretium luctus mauris nec molestie. Praesent tristique lobortis nisi convallis cursus. Sed non aliquam neque. Suspendisse ut libero facilisis turpis consectetur semper in ac augue. Quisque tincidunt eros tellus, non mollis risus luctus at. Nunc eu posuere augue, eu faucibus nibh. Duis eleifend placerat velit sit amet fermentum. Quisque eu libero at est sodales pellentesque. Nulla pharetra euismod leo, eu blandit dolor pharetra nec. "
        val mockPrepTimes = "30 Minutes"

        when(recipeTypeID) {
            1 -> {
                addImageToDocumentPath(R.drawable.burrito, "burrito.jpg")
                addImageToDocumentPath(R.drawable.quesaddila, "quesaddila.jpg")
                addImageToDocumentPath(R.drawable.taco, "taco.jpg")
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Burrito", "burrito.jpg", mockText, mockText, mockPrepTimes)
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Quesaddila", "quesaddila.jpg", mockText, mockText, mockPrepTimes)
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Taco", "taco.jpg", mockText, mockText, mockPrepTimes)
            }
            2 -> {
                addImageToDocumentPath(R.drawable.aglioolio, "aglioolio.jpg")
                addImageToDocumentPath(R.drawable.pizza, "pizza.jpg")
                addImageToDocumentPath(R.drawable.carbonara, "carbonara.jpg")
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Aglio Olio", "aglioolio.jpg", mockText, mockText, mockPrepTimes)
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Pizza", "pizza.jpg", mockText, mockText, mockPrepTimes)
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Carbonara", "carbonara.jpg", mockText, mockText, mockPrepTimes)
            }
            3 -> {
                addImageToDocumentPath(R.drawable.sushi, "sushi.jpg")
                addImageToDocumentPath(R.drawable.soba, "soba.jpg")
                addImageToDocumentPath(R.drawable.udon, "udon.jpg")
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Sushi", "sushi.jpg", mockText, mockText, mockPrepTimes)
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Soba", "soba.jpg", mockText, mockText, mockPrepTimes)
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Udon", "udon.jpg", mockText, mockText, mockPrepTimes)
            }
            4 -> {
                addImageToDocumentPath(R.drawable.kimchi, "kimchi.jpg")
                addImageToDocumentPath(R.drawable.friedchicken, "friedchicken.jpg")
                addImageToDocumentPath(R.drawable.bibimbap, "bibimbap.jpg")
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Kimchi", "kimchi.jpg", mockText, mockText, mockPrepTimes)
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Korean Fried Chicken", "friedchicken.jpg", mockText, mockText, mockPrepTimes)
                recipeArr += Recipe(0, currentSelectedRecipeTypeID, "Bibimbap", "bibimbap.jpg", mockText, mockText, mockPrepTimes)
            }
        }

        localDB.addRecipe(recipeArr, recipeTypeID)
    }

    private fun addImageToDocumentPath(imageDrawable: Int, imageFileName: String) {
        val bm = BitmapFactory.decodeResource(this.resources, imageDrawable)
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

    override fun onItemSelected(av: AdapterView<*>?, view: View?, index: Int, id: Long) {
        currentSelectedRecipeTypeID = id.toInt()
    }

    override fun onNothingSelected(av: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
