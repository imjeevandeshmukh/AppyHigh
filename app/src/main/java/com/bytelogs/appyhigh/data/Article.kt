package com.bytelogs.appyhigh.data

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Article (

	@SerializedName("author") val author : String,
	@SerializedName("title") val title : String,
	@SerializedName("description") val description : String,
	@SerializedName("url") val url : String,
	@SerializedName("urlToImage") val urlToImage : String,
	@SerializedName("publishedAt") val publishedAt : String,
	@SerializedName("content") val content : String


):Serializable{

	companion object{
		@SuppressWarnings
        @BindingAdapter("android:imageUrl")
        @JvmStatic
        fun setImageUrl(imageView: ImageView, url: String?) {
            Glide.with(imageView.context).load(url).into(imageView)
        }

    }

}