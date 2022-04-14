package com.example.musicplayer

import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.databinding.ActivitySongDetailBinding

class SongDetail : AppCompatActivity() {
    private lateinit var binding: ActivitySongDetailBinding
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var metaRetriever: MediaMetadataRetriever

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySongDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences= getSharedPreferences("MySP", MODE_PRIVATE)

        setAlbumCover()
        binding.albumNameTV.text=sharedPreferences.getString("albumName","Unknown")
        binding.artistNameTV.text=sharedPreferences.getString("artistName","Unknown")
        binding.yearTV.text=sharedPreferences.getString("year","0000")
        binding.songNameTV.text=sharedPreferences.getString("songName","Unknown")

    }


    private fun setAlbumCover(){
        val filepath= Uri.parse(sharedPreferences.getString("filePath",""))
        metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(this, filepath)
        val data: ByteArray? = metaRetriever.embeddedPicture
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data!!.size)
        binding.imageView.setImageBitmap(bitmap)
    }
}