package com.billygdev.billyrecipeappandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.xmlpull.v1.XmlPullParser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        val localDB = LocalDB(this, null, null, 1, null)
        localDB.populateRecipeTypes(recipeTypeArr)

        println(":::"+localDB.showRecipeTypes().size)
    }
}
