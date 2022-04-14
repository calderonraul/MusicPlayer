package com.example.musicplayer


import android.app.Activity
import android.app.Notification
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    var mMediaPlayer: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private lateinit var binding: ActivityMainBinding
    lateinit var metaRetriever: MediaMetadataRetriever
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("MySP", Activity.MODE_PRIVATE)

        val mediaSession = MediaSession(this, "mainAct")
        var flag = 0
        val mediaStyle = Notification.MediaStyle().setMediaSession(mediaSession.sessionToken)

        var currentSong = 0
        val songs = listOf(R.raw.fade, R.raw.spectre, R.raw.calling)

        /* val notification = Notification.Builder(this)
             .setStyle(mediaStyle)
             .setSmallIcon(R.drawable.ic_launcher_foreground)
             .build()
 */
        binding.btnPlay.setOnClickListener {
            setCurrentSong(songs[currentSong])
            if (flag == 0) {
                binding.btnPlay.setImageResource(R.drawable.ic_pause)
                playSound(songs[currentSong])
                flag = 1
            } else if (flag == 1) {
                binding.btnPlay.setImageResource(R.drawable.ic_play)
                pauseSound()
                flag = 0
            }
        }

        binding.btnStop.setOnClickListener {
            stopSound()
        }

        binding.btnInfo.setOnClickListener {

            getMEta(songs[currentSong])
            val intent = Intent(this, SongDetail::class.java)
            startActivity(intent)
        }

        binding.btnPre.setOnClickListener {
            stopSound()
            currentSong--
            if (currentSong in 0..songs.size) playSound(songs[currentSong]) else {
                currentSong = 0
                playSound(songs[currentSong])

            }

        }

        binding.btnNext.setOnClickListener {
            stopSound()
            setCurrentSong(songs[currentSong])
            currentSong++
            if (currentSong in 0..songs.size) playSound(songs[currentSong]) else {
                currentSong = 0

                playSound(songs[currentSong])
            }


        }


        binding.seekProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

    private fun playSound(song: Int) {
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
            binding.seekProgress.progress = 0
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null

            handler.removeCallbacks(runnable)

        }

    }

    private fun getMEta(song: Int) {
        val resourceURI: Uri = Uri.parse("android.resource://" + this.packageName + "/" + song)
        val filepath=resourceURI.toString()
        metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(this, resourceURI)
        val songName =
            metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()

        val artistName =
            metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).toString()

        val albumName =
            metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM).toString()
        val year =
            metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR).toString()

        //Toast.makeText(this, albumName, Toast.LENGTH_LONG).show()
        var editor = sharedPreferences.edit()
        editor.putString("artistName", artistName)
        editor.putString("albumName", albumName)
        editor.putString("year", year)
        editor.putString("songName", songName)
        editor.putString("filePath",filepath)
        editor.apply()

    }


    private fun setCurrentSong(song:Int){
        val resourceURI: Uri = Uri.parse("android.resource://" + this.packageName + "/" + song)
        metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(this, resourceURI)
        val songName =
            metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()

        val artistName =
            metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).toString()
        val data: ByteArray? = metaRetriever.embeddedPicture
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data!!.size)
        binding.imgMusic.setImageBitmap(bitmap)
        binding.txtSongName.text=songName
        binding.txtSingerName.text=artistName

    }

    private fun initializeSeekBar() {
        binding.seekProgress.progress = 0
        binding.seekProgress.max = mMediaPlayer!!.duration

        runnable = Runnable {
            binding.seekProgress.progress = mMediaPlayer!!.currentPosition
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
        mMediaPlayer!!.setOnCompletionListener {
            pauseSound()
        }

    }

}