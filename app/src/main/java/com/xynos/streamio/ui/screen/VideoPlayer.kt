package com.xynos.streamio.ui.screen

import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@androidx.media3.common.util.UnstableApi
@Composable
fun VideoPlayer(uri: Uri) {
    val context = LocalContext.current

    val player = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(context)
                val source = ProgressiveMediaSource
                    .Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(uri))
                setMediaSource(source)
                prepare()
                playWhenReady = true
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                repeatMode = Player.REPEAT_MODE_ONE
            }
    }

    AndroidView(factory = { ctx ->
        PlayerView(ctx).apply {
            //hideController()
            useController = true
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            this.player = player
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
    })

    DisposableEffect(player) {
        onDispose { player.release() }
    }
}
