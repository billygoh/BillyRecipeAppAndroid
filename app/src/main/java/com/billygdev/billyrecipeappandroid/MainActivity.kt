package com.billygdev.billyrecipeappandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.xmlpull.v1.XmlPullParser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val xmlParser: XmlPullParser = this.resources.getXml(R.xml.recipetypes)
        while (xmlParser.eventType != XmlPullParser.END_DOCUMENT) {
            if(xmlParser.name == "type") {
                val name = xmlParser.getAttributeValue(null,"name")
                if(name != null) {
                    println("okok::"+name)
                }
            }
            xmlParser.next()
        }
    }
}
