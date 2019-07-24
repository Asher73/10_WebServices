package com.example.webservices

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class adminBD(context: Context) : SQLiteOpenHelper(context, DATABASE, null, 1) {
    companion object {
        val DATABASE = "Escuela"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "Create Table alumno(" +
                    "nocontrol text primary key, " +
                    "nombre text, " +
                    "carrera text, " +
                    "telefono text)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun Ejecuta(sentencia: String): Int {
        try {
            val db = this.writableDatabase
            db.execSQL(sentencia)
            db.close()
            return 1
        } catch (ex: Exception) {
            return 0
        }
    }

    fun Consulta(select: String): Cursor? {
        try {
            val db = this.readableDatabase
            return db.rawQuery(select, null)
        } catch (ex: Exception) {
            return null
        }
    }
}
