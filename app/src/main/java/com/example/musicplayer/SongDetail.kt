package com.example.musicplayer

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.databinding.ActivitySongDetailBinding

class SongDetail : AppCompatActivity() {
    private lateinit var binding: ActivitySongDetailBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySongDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences= getSharedPreferences("MySP", MODE_PRIVATE)


        binding.albumNameTV.text=sharedPreferences.getString("albumName","Unknown")
        binding.artistNameTV.text=sharedPreferences.getString("artistName","Unknown")
        binding.yearTV.text=sharedPreferences.getString("year","0000")
        binding.songNameTV.text=sharedPreferences.getString("songName","Unknown")

    }
}