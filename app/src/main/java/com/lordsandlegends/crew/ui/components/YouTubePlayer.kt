package com.lordsandlegends.crew.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView



// AndroidView was used to allow youtube videos to be displayed documentation will be added later on
//
@Composable
fun YouTubePlayer(
    videoId: String, // The 11-character ID from a YouTube URL (e.g., dQw4w9WgXcQ)
    modifier: Modifier = Modifier
) {
    // AndroidView is the bridge between Compose and traditional Android Views
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            // We clip the view to give it rounded corners matching the app's style so the video is more round
            .clip(RoundedCornerShape(12.dp)),
        
        factory = { context ->
            // This 'factory' block runs once when the component is first created
            YouTubePlayerView(context).apply {

                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        // We use loadVideo for better compatibility.
                        // This prepares the video and shows the thumbnail without 

                        youTubePlayer.cueVideo(videoId, 0f)
                    }
                })
            }
        }
    )
}
