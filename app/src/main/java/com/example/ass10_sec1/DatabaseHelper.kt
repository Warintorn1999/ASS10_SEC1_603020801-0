package com.example.ass10_sec1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context: Context): SQLiteOpenHelper(context,DB_NAME,null,DB_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME" +
                "($COLUME_ID TEXT PRIMARY KEY, $COLUME_NAME TEXT," +
                "$COLUME_AGE integer)"
        db?.execSQL(CREATE_TABLE)
        val sqlInsert = "INSERT INTO $TABLE_NAME VALUES('1','Alice',20)"
        db?.execSQL(sqlInsert);
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXITS $TABLE_NAME")
        onCreate(db)
    }

    companion object{
        private val DB_NAME = "StudentDB"
        private val DB_VERSION = 1
        private val TABLE_NAME = "student"
        private val COLUME_ID = "id"
        private val COLUME_NAME = "name"
        private val COLUME_AGE = "age"

    }
    fun getAllStudents():ArrayList<Student>{
        val std = ArrayList<Student>()
        val db=writableDatabase
        var cursor:Cursor? = null
        try {
            cursor = db.rawQuery("Select * from $TABLE_NAME",null)
        }catch (e: SQLException){
            onCreate(db)
            return ArrayList()
        }
        var id : String
        var name : String
        var age : Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getString(cursor.getColumnIndex(COLUME_ID))
                name = cursor.getString(cursor.getColumnIndex(COLUME_NAME))
                age = cursor.getInt(cursor.getColumnIndex(COLUME_AGE))

                std.add(Student(id, name, age))
                cursor.moveToNext()
            }
        }
        db.close()
        return std
    }
    fun insertStudent(std: Student):Long{
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUME_ID,std.id)
        values.put(COLUME_NAME, std.name)
        values.put(COLUME_AGE, std.age)
        val success = db.insert(TABLE_NAME,null,values)
        db.close()
        return success
    }
    fun updateStudent(std: Student):Int{
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUME_NAME, std.name)
        values.put(COLUME_AGE, std.age)
        val success = db.update(TABLE_NAME,values,"$COLUME_ID = ?", arrayOf(std.id))
        db.close()
        return success
    }
    fun deleteStudent(std_id:String):Int{
        val db = writableDatabase
        val success = db.delete(TABLE_NAME,"$COLUME_ID = ?", arrayOf(std_id))
        db.close()
        return success
    }
}