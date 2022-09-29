/*
 * Copyright (c) 2020. Está classe está sendo consedida para uso pessoal
 */
package com.BiblioLivro.criarlivros.storage

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import java.util.ArrayList

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE_CATALOGO = "CREATE TABLE catalogo (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT, autor TEXT," +
                "ano INTEGER);"
        db.execSQL(CREATE_TABLE_CATALOGO)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS catalogo")
    }

    fun insert(cv: ContentValues?): Long {
        val db = this.writableDatabase
        return db.insert(DATABASE_NAME, null, cv)
    }

    fun update(id: Int, updateValue: ContentValues?) {
        val db = this.writableDatabase
        db.update("catalogo", updateValue, "id LIKE ?", arrayOf("%$id%"))
    }

    fun delete(id: Int) {
        val db = this.writableDatabase
        db.delete(DATABASE_NAME, "id LIKE ?", arrayOf("%$id%"))
    }

    fun searchByTitle(titulo: String): List<ContentValues> {
        val sql = "SELECT * FROM catalogo WHERE titulo LIKE ?"
        val where = arrayOf("%$titulo%")
        return search(sql, where)
    }

    fun searchByAuthor(autor: String): List<ContentValues> {
        val sql = "SELECT * FROM catalogo WHERE autor LIKE ?"
        val where = arrayOf("%$autor%")
        return search(sql, where)
    }

    fun searchByYear(ano: Int): List<ContentValues> {
        val sql = "SELECT * FROM catalogo WHERE ano=?"
        val where = arrayOf(ano.toString())
        return search(sql, where)
    }

    fun tableIsExist(): Boolean {
        val sql = "SELECT * FROM catalogo"
        val verifyTable = search(sql, null)
        return verifyTable.isNotEmpty()
    }

    fun searchAll(): List<ContentValues> {
        val sql = "SELECT * FROM catalogo ORDER BY id"
        return search(sql, null)
    }

    private fun search(sql: String, where: Array<String>?): List<ContentValues> {
        val lista: MutableList<ContentValues> = ArrayList()
        val db = this.readableDatabase
        val c = db.rawQuery(sql, where)
        if (c.moveToFirst()) {
            do {
                val cv = ContentValues()
                cv.put("id", c.getInt(c.getColumnIndexOrThrow("id")))
                cv.put("titulo", c.getString(c.getColumnIndexOrThrow("titulo")))
                cv.put("autor", c.getString(c.getColumnIndexOrThrow("autor")))
                cv.put("ano", c.getInt(c.getColumnIndexOrThrow("ano")))
                lista.add(cv)
            } while (c.moveToNext())
        }
        c.close()
        return lista
    }

    companion object {
        //ATRIBUTOS
        private const val DATABASE_NAME = "catalogo"
        private const val DATABASE_VERSION = 3
    }
}