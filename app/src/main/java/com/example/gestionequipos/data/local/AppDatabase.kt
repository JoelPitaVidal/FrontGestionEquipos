package com.example.gestionequipos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gestionequipos.data.local.dao.EquipoDao
import com.example.gestionequipos.data.local.Entity.EquipoEntity
import com.example.gestionequipos.data.local.Entity.EstadoEntity
@Database(entities = [EquipoEntity::class, EstadoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun equipoDao(): EquipoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gestion_equipos_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}