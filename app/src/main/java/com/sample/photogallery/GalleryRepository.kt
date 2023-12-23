package com.sample.photogallery

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.sample.photogallery.database.GalleryDatabase
import org.json.JSONObject.NULL
import java.util.UUID
import java.util.concurrent.Executors

private const val DATABASE_NAME = "gallery"
class GalleryRepository private constructor(context: Context) {
    private val database: GalleryDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            GalleryDatabase::class.java,
            DATABASE_NAME
        )
        .build()
    private val executor = Executors.newSingleThreadExecutor()
    fun getPhotos(): LiveData<List<Item>> = database.galleryDao().getAllPhotos()
    private fun GalleryItem.toItem(): Item {
        return Item(title, id, url)
    }


    fun addPhoto(photo: GalleryItem) {
        val item = photo.toItem()
        executor.execute {
            if (database.galleryDao().getOnePhoto(item.url)!=item){
                database.galleryDao().addPhoto(item)
            }
        }
    }

    fun deleteAllPhotos() {
        executor.execute {
            database.galleryDao().deleteAllPhotos()
        }
    }

    companion object {
        private var INSTANCE: GalleryRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = GalleryRepository(context)
            }
        }
        fun get(): GalleryRepository {
            return INSTANCE ?:
            throw
            IllegalStateException("GalleryRepository must be initialized")
        }
    }

}