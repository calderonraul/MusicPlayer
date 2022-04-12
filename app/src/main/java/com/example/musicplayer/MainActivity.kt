package com.example.musicplayer

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var mMediaPlayer: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var currentSong=0
        val songs= listOf(R.raw.fade,R.raw.spectre,R.raw.calling)

        binding.playButton.setOnClickListener {
            playSound(songs[currentSong])
        }

        binding.pauseButton.setOnClickListener {
            pauseSound()
        }

        binding.stopButton.setOnClickListener {
            stopSound()
        }

        binding.moreInfoButton.setOnClickListener {
            val intent = Intent(this, SongDetail::class.java)
            startActivity(intent)
        }

        binding.prevButton.setOnClickListener {
            stopSound()
            currentSong--
            if(currentSong in 0..2)playSound(songs[currentSong])else{
                currentSong=0
                playSound(songs[currentSong])
            }

        }

        binding.nextButton.setOnClickListener {
            stopSound()
            currentSong++
            if(currentSong in 0..2)playSound(songs[currentSong])else{
                currentSong=0
                playSound(songs[currentSong])
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {
                    mMediaPlayer!!.seekTo(p1)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaPlayer?.release()
    }

    private fun playSound( song : Int) {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, song)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()

        initializeSeekBar()
    }

    // 2. Pause playback
    private fun pauseSound() {
        if (mMediaPlayer?.isPlaying == true) mMediaPlayer?.pause()
    }

    // 3. Stops playback
    private fun stopSound() {
        if (mMediaPlayer != null) {
            binding.seekBar.progress = 0
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null

            handler.removeCallbacks(runnable)

        }

    }

    private fun initializeSeekBar() {
        binding.seekBar.progress = 0
        binding.seekBar.max = mMediaPlayer!!.duration

        runnable = Runnable {
            binding.seekBar.progress = mMediaPlayer!!.currentPosition
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
        mMediaPlayer!!.setOnCompletionListener {
            pauseSound()
        }

    }

}