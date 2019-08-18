package com.ab.demo.ui.player

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.ab.demo.R
import com.ab.demo.base.BaseFragment
import com.ab.demo.databinding.FragmentPlayerBinding
import com.ab.demo.factory.ViewModelFactory
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import kotlinx.android.synthetic.main.fragment_player.*
import javax.inject.Inject


class PlayerFragment : BaseFragment<FragmentPlayerBinding>(), Player.EventListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var appContext: Context
    lateinit var binding: FragmentPlayerBinding
    lateinit var player: SimpleExoPlayer
    private lateinit var viewModel: PlayerViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_player
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding = getDataBinding()
        viewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(PlayerViewModel::class.java)
    }


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(appContext)
        player.playWhenReady = viewModel.playWhenReady
        player_view.player = player
        val mediaSource = buildMediaSource(
            Uri.parse(LINK_VIDEO_STREAM)
        )
        player.prepare(mediaSource, true, false)
        player.seekTo(viewModel.currentWindow, viewModel.playbackPosition)
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory = DefaultHttpDataSourceFactory(
            Util.getUserAgent(
                context,
                getString(R.string.app_name)
            )
        )
        return HlsMediaSource
            .Factory(dataSourceFactory)
            .setAllowChunklessPreparation(true)
            .createMediaSource(uri)
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        player_view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun releasePlayer() {
        viewModel.playbackPosition = player.currentPosition
        viewModel.currentWindow = player.currentWindowIndex
        viewModel.playWhenReady = player.playWhenReady
        player.release()
    }

    private fun showSnackBar(msg: Int) {
        make(binding.root, getString(msg), Snackbar.LENGTH_SHORT).show()
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        showSnackBar(R.string.error_something_went_wrong)
    }

    companion object {
        private const val LINK_VIDEO_STREAM = "https://bitmovin-a.akamaihd.net/content/playhouse-vr/m3u8s/105560.m3u8"
    }
}
