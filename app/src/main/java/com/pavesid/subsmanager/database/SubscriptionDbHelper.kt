package com.pavesid.subsmanager.database

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.annotation.WorkerThread
import com.pavesid.subsmanager.models.Subscription

class SubscriptionDbHelper(context: Context) : SQLiteOpenHelper(context,
    DB_NAME, null,
    VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(CREATE_TABLE_SCRIPT)
        } catch (e: SQLException) {
            Log.e("Error", e.message ?: "")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            db?.execSQL(DROP_TABLE_SCRIPT)
            db?.execSQL(CREATE_TABLE_SCRIPT)
        } catch (e: SQLException) {
            Log.e("Error", e.message ?: "")
        }
    }

    @WorkerThread
    fun getListOfSubscription(): List<Subscription> {
        val listOfSubscription = mutableListOf<Subscription>()
        readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(FIELD_ID))
                    val title = cursor.getString(cursor.getColumnIndex(FIELD_TITLE))
                    val start = cursor.getString(cursor.getColumnIndex(FIELD_START_SUBSCRIPTION))
                    val end = cursor.getString(cursor.getColumnIndex(FIELD_END_SUBSCRIPTION))
                    val price = cursor.getString(cursor.getColumnIndex(FIELD_PRICE))
                    val color = cursor.getInt(cursor.getColumnIndex(FIELD_COLOR))
                    val image = cursor.getInt(cursor.getColumnIndex(FIELD_IMAGE))
                    listOfSubscription.add( Subscription(title, start, end, price, color, image, 0, 0f, id) )
                } while (cursor.moveToNext())
            }
        }
        return listOfSubscription
    }

    @WorkerThread
    fun addSubscription(subscription: Subscription) {
        val contentValues = ContentValues()
        contentValues.put(FIELD_TITLE, subscription.title)
        contentValues.put(FIELD_START_SUBSCRIPTION, subscription.started)
        contentValues.put(FIELD_END_SUBSCRIPTION, subscription.ended)
        contentValues.put(FIELD_PRICE, subscription.price)
        contentValues.put(FIELD_COLOR, subscription.color)
        contentValues.put(FIELD_IMAGE, subscription.image)

        try {
            writableDatabase.insert(TABLE_NAME, null, contentValues)
        } catch (e: SQLiteException) {
            Log.d("Exception", "insert", e)
        }
    }

    @WorkerThread
    fun updateSubscription(subscription: Subscription) {
        val contentValues = ContentValues()
        contentValues.put(FIELD_TITLE, subscription.title)
        contentValues.put(FIELD_START_SUBSCRIPTION, subscription.started)
        contentValues.put(FIELD_END_SUBSCRIPTION, subscription.ended)
        contentValues.put(FIELD_PRICE, subscription.price)
        contentValues.put(FIELD_COLOR, subscription.color)
        contentValues.put(FIELD_IMAGE, subscription.image)

        try {
            writableDatabase.update(TABLE_NAME, contentValues, "$FIELD_ID = ?", arrayOf(subscription.id.toString()))
        } catch (e: SQLiteException) {
            Log.d("Exception", "update", e)
        }
        writableDatabase.close()
    }

    @WorkerThread
    fun removeSubscription(subscription: Subscription) {
        try {
            writableDatabase.delete(TABLE_NAME, "$FIELD_ID = ?", arrayOf(subscription.id.toString()))
        } catch (e: SQLiteException) {
            Log.d("Exception", "delete", e)
        }
    }

    companion object {
        private const val DB_NAME = "subscriptionsDatabase"
        const val TABLE_NAME = "subscriptions"

        private const val FIELD_TITLE = "title"
        private const val FIELD_START_SUBSCRIPTION = "started"
        private const val FIELD_END_SUBSCRIPTION = "ended"
        private const val FIELD_PRICE = "price"
        private const val FIELD_COLOR = "color"
        private const val FIELD_IMAGE = "image"
        private const val FIELD_ID = "_id"

        private const val VERSION = 1

        private const val CREATE_TABLE_SCRIPT =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($FIELD_ID INTEGER PRIMARY KEY AUTOINCREMENT, $FIELD_TITLE TEXT, $FIELD_START_SUBSCRIPTION TEXT, $FIELD_END_SUBSCRIPTION TEXT, $FIELD_PRICE TEXT, $FIELD_COLOR INTEGER, $FIELD_IMAGE INTEGER)"

        private const val DROP_TABLE_SCRIPT = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}