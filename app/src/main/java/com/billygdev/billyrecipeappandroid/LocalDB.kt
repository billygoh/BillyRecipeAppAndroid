package com.billygdev.billyrecipeappandroid

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocalDB(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int,
    errorHandler: DatabaseErrorHandler?
) : SQLiteOpenHelper(context, name, factory, version, errorHandler) {

    override fun onCreate(db: SQLiteDatabase?) {
        println("DB CREATEEEEEE")
        val CREATE_RECIPE_TYPE_TABLE = ("CREATE TABLE IF NOT EXISTS " +
                TABLE_RECIPE_TYPES + "("
                + COLUMN_RECIPE_TYPE_ID + " INTEGER PRIMARY KEY," +
                COLUMN_RECIPE_TYPE_NAME
                + " TEXT" + ")")

        val CREATE_RECIPE_TABLE = ("CREATE TABLE IF NOT EXISTS " +
                TABLE_RECIPE + "("
                + COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY," +
                COLUMN_RECIPE_TYPE_ID_FK
                + " INTEGER," + COLUMN_RECIPE_NAME
                + " TEXT," + COLUMN_RECIPE_IMAGEURL
                + " TEXT," + COLUMN_RECIPE_INGREDIENTS
                + " TEXT," + COLUMN_RECIPE_STEPS
                + " TEXT," + COLUMN_RECIPE_PREPTIME
                + " TEXT" + ")")

        println("table:::"+CREATE_RECIPE_TABLE)

        db.run {
            this?.execSQL(CREATE_RECIPE_TYPE_TABLE)
            this?.execSQL(CREATE_RECIPE_TABLE)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db.run {
            this?.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_TYPES)
            this?.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE)
            onCreate(this)
        }
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "recipe.db"
        val TABLE_RECIPE_TYPES = "recipe_types"
        val COLUMN_RECIPE_TYPE_ID = "id"
        val COLUMN_RECIPE_TYPE_NAME = "productname"

        val TABLE_RECIPE = "reecipe"
        val COLUMN_RECIPE_ID = "id"
        val COLUMN_RECIPE_TYPE_ID_FK = "recipeTypeId"
        val COLUMN_RECIPE_NAME = "name"
        val COLUMN_RECIPE_IMAGEURL = "imageURL"
        val COLUMN_RECIPE_INGREDIENTS = "ingredients"
        val COLUMN_RECIPE_STEPS = "steps"
        val COLUMN_RECIPE_PREPTIME = "prepTime"
    }

    fun populateRecipeTypes(recipeTypeNameArr: Array<String>) {
        val db = this.writableDatabase

        for(recipeType in recipeTypeNameArr) {
            val values = ContentValues()
            values.put(COLUMN_RECIPE_TYPE_NAME, recipeType)

            db.insert(TABLE_RECIPE_TYPES, null, values)
        }

//        db.close()
    }

    fun showRecipeTypes(): Array<RecipeType> {

        val query =
            "SELECT * FROM $TABLE_RECIPE_TYPES"

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        var recipeTypeArr: Array<RecipeType> = emptyArray()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                var recipeType: RecipeType
                val id = Integer.parseInt(cursor.getString(0))
                val name = cursor.getString(1)

                recipeType = RecipeType(id, name)
                recipeTypeArr += recipeType

                cursor.moveToNext()
            }
        }

//        db.close()
        return recipeTypeArr
    }
}