package com.sample.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sample.photogallery.api.FlickrFetchr

class PhotoGalleryViewModel : ViewModel() {
    val galleryItemLiveData: LiveData<List<GalleryItem>>
    init {
        galleryItemLiveData =
            FlickrFetchr().searchPhotos("bicycle")
    }
}
