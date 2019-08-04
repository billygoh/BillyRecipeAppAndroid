package com.billygdev.billyrecipeappandroid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.xmlpull.v1.XmlPullParser

class MainActivity : AppCompatActivity() {

    val localDB = LocalDB(this, null, null, 1, null)

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
        }
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
}
