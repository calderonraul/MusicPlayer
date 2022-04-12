package com.example.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    var mMediaPlayer: MediaPlayer? = null
    lateinit var playBtn: ImageButton
    lateinit var pauseBtn: ImageButton
    lateinit var stopBtn:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playBtn=findViewById(R.id.playButton)
        pauseBtn=findViewById(R.id.pauseButton)
        stopBtn=findViewById(R.id.stopButton)


        playBtn.setOnClickListener {
            playSound()
        }

        pauseBtn.setOnClickListener{
            pauseSound()
        }

        stopBtn.setOnClickListener {
            stopSound()
        }


    }



    private fun playSound(){
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.fade)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    // 2. Pause playback
    private fun pauseSound() {
        if (mMediaPlayer?.isPlaying == true) mMediaPlayer?.pause()
    }

    // 3. Stops playback
    private fun stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }
}