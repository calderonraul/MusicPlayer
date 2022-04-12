package com.example.musicplayer

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar

class MainActivity : AppCompatActivity() {
    var mMediaPlayer: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()

    lateinit var playBtn: ImageButton
    lateinit var pauseBtn: ImageButton
    lateinit var stopBtn: ImageButton
    lateinit var infoBtn: Button
    lateinit var seek_bar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playBtn = findViewById(R.id.playButton)
        pauseBtn = findViewById(R.id.pauseButton)
        stopBtn = findViewById(R.id.stopButton)
        infoBtn = findViewById(R.id.moreInfoButton)
        seek_bar = findViewById(R.id.seekBar)

        playBtn.setOnClickListener {
            playSound()

        }

        pauseBtn.setOnClickListener {
            pauseSound()
        }

        stopBtn.setOnClickListener {
            stopSound()
        }

        infoBtn.setOnClickListener {
            val intent = Intent(this, SongDetail::class.java)
            startActivity(intent)
        }

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

    private fun playSound() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.fade)
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
            seek_bar.progress = 0
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null

            handler.removeCallbacks(runnable)

        }

    }

    private fun initializeSeekBar() {
        seek_bar.progress = 0
        seek_bar.max = mMediaPlayer!!.duration

        runnable = Runnable {
            seek_bar.progress = mMediaPlayer!!.currentPosition
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
        mMediaPlayer!!.setOnCompletionListener {
            pauseSound()
        }

    }

}