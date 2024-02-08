package com.lifecontroller.coockibook

import android.app.Application
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): DB {
        val db = Room.databaseBuilder(
            app,
            DB::class.java,
            DB.DATABASE_NAME
        )
            .createFromAsset("coocki.db")
            .allowMainThreadQueries()
            .build()

        Log.println(Log.ERROR,"'","db - ${db.query("SELECT * FROM post", args = null).count}")

        return db
    }
}