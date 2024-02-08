package com.lifecontroller.coockibook

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Post::class],
    version = 1
)
abstract class DB: RoomDatabase()  {
    abstract val dao: PostDao

    companion object{
        const val DATABASE_NAME = "cocki.db"

        @Volatile
        private var INSTANCE: DB? = null
        fun getInstance(context: android.content.Context): DB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DB::class.java,
                        DATABASE_NAME
                    )
                        .createFromAsset("coocki.db")
                        .allowMainThreadQueries()
                        .build()
                }
                return instance
            }
        }
    }
}

@Entity
data class Post(
    @PrimaryKey val id: Long? = null,
    val title: String,
    val description: String
)

@Dao
interface PostDao {
    @Query("SELECT * FROM post")
    fun getPosts(): List<Post>
    @Query("SELECT * FROM post WHERE id = :id")
    fun getPostById(id: Long): Post?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: Post)
    @Delete
    fun deletePost(post: Post)
}