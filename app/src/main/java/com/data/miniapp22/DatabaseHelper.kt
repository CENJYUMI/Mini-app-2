package com.data.miniapp22

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        const val DATABASE_NAME = "inventory.db"
        const val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE item (
            item_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            item_name TEXT,
            item_desc TEXT,
            item_quantity INT
            )
        """.trimIndent()
        )
        db.execSQL("""
            CREATE TABLE user (
            user_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            user_username TEXT,
            user_password TEXT,
            user_confirmPw TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS item")
        db.execSQL("DROP TABLE IF EXISTS user")
    }

    //CREATE
    fun insertItems(item: Item) {
        val db = writableDatabase

        val sql = "INSERT INTO item (item_name, item_desc, item_quantity) VALUES (?,?,?)"
        val args = arrayOf(item.name, item.description, item.quantity)
        db.execSQL(sql, args)
    }

    fun insertUsers (user: User){
        val db2 = writableDatabase

        val sql2 = "INSERT INTO user (user_username, user_password, user_confirmPw) VALUES (?,?,?)"
        val args2 = arrayOf(user.username, user.password, user.confirmPw)
        db2.execSQL(sql2, args2)
    }

    fun isUsernameExists(username: String): Boolean {
        val db = readableDatabase
        val selection = "user_username = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query(username, null, selection, selectionArgs, null, null, null)

        val result = cursor.count > 0
        cursor.close()
        return result
    }

    //READ
    fun getAllItems(): MutableList<Item>{
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM item", null)
        val itemList = mutableListOf<Item>()

        while (cursor.moveToNext()) {
            val itemID = cursor.getInt(0)
            val itemName = cursor.getString(1)
            val itemDesc = cursor.getString(2)
            val itemQuantity = cursor.getInt(3)

            val newItem = Item(itemID, itemName, itemDesc, itemQuantity)
            itemList.add(newItem)
        }
        cursor.close()
        return itemList
    }

    fun getAllUsers(): MutableList<User>{
        val db2 = readableDatabase

        val cursor2 = db2.rawQuery("SELECT * FROM user", null)
        val userList = mutableListOf<User>()

        while (cursor2.moveToNext()){
            val userID =cursor2.getInt(0)
            val userUsername = cursor2.getString(1)
            val userPassword = cursor2.getString(2)
            val userConfirmPw = cursor2.getString(3)

            val newUser = User (userID, userUsername, userPassword, userConfirmPw)
            userList.add(newUser)
        }
        cursor2.close()
        return userList
    }

    //UPDATE
    fun updateItemData(item: Item){
        val db = writableDatabase

        val updateQuery = "UPDATE item SET item_name = '${item.name}', item_desc = '${item.description}', item_quantity = '${item.quantity};"
        db.execSQL(updateQuery)
    }

    fun updateUserData(user: User){
        val db2 = writableDatabase

        val updateQuery2 = "UPDATE user SET user_username = '${user.username}', user_password ='${user.password}', user_confirmPw = '${user.confirmPw};"
        db2.execSQL(updateQuery2)
    }

    fun deleteItemData (itemID: Int){
        val db = writableDatabase
        val deleteQuery = "DELETE FROM item WHERE item_ID = $itemID"
        db.execSQL(deleteQuery)
    }

    fun deleteUserData (userID: Int){
        val db2 = writableDatabase
        val deleteQuery2 = "DELETE FROM user WHERE user_ID = $userID"
        db2.execSQL(deleteQuery2)
    }

    //Sign up Activity
    fun isUsernameAvailable(username: String): Boolean {
        val db = readableDatabase
        val selection = "user_username = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query("user", null, selection, selectionArgs, null, null, null)

        val result = cursor.count == 0
        cursor.close()
        return result
    }

    //Login Activity
    fun getUserByUsername(username: String): User? {
        val db = readableDatabase
        val selection = "user_username = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query("user", null, selection, selectionArgs, null, null, null)

        var user: User? = null
        if (cursor != null && cursor.moveToFirst()) {
            val userID = cursor.getInt(0)
            val userUsername = cursor.getString(1)
            val userPassword = cursor.getString(2)
            val userConfirmPw = cursor.getString(3)

        user = User(userID, userUsername, userPassword, userConfirmPw)
        }
        cursor.close()
        return user
    }

    fun checkUserCredentials(username: String, password: String): Boolean {
        val db = readableDatabase
        val args = arrayOf(username, password)
        val cursor = db.rawQuery("SELECT * FROM user WHERE user_username = ? AND user_password = ?", args)
        var found = cursor.count > 0

        cursor.close()
        return found
    }





}

