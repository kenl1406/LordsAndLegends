package com.lordsandlegends.crew.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lordsandlegends.crew.ui.theme.LLColors
import com.lordsandlegends.crew.ui.theme.LLType

//here we define what the youtube video is by title we can add there thumbnails and a descrition this is the
//videos view model
data class VideoSheetState(
    val cat: String,      // Category (e.g., "Cocktail")
    val title: String,    // Video Title
    val desc: String,     // Short description
    val youtubeId: String, // The YouTube ID used by the player
)

data class VideoCardData(
    val key: String,
    val cat: String,
    val title: String,
    val desc: String,
    val duration: String,
    val gradient: List<Color>,
    val youtubeId: String,
)

@Composable
fun VideoGroup(
    eyebrow: String,
    headline: String,
    items: List<VideoCardData>,
    onPlay: (VideoSheetState) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Column {
                Eyebrow(eyebrow)
                Text(headline, style = LLType.Title, color = LLColors.Ink)
            }
            Text(
                "See all",
                style = LLType.BodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = LLColors.CopperDeep,
            )
        }
        Spacer(Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 4.dp),
        ) {
            items(items.size) { i ->
                VideoCard(items[i], onPlay)
            }
        }
    }
}

@Composable
private fun VideoCard(card: VideoCardData, onPlay: (VideoSheetState) -> Unit) {
    Column(
        modifier = Modifier
            .width(248.dp)
            .background(LLColors.Surface, RoundedCornerShape(16.dp))
            .border(1.dp, LLColors.Line, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { onPlay(VideoSheetState(card.cat, card.title, card.desc, card.youtubeId)) },
    ) {
        // Thumbnail from YouTube
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            contentAlignment = Alignment.Center,
        ) {
            // This loads the real thumbnail from YouTube's servers
            AsyncImage(
                model = "https://img.youtube.com/vi/${card.youtubeId}/hqdefault.jpg",
                contentDescription = card.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Crops the image to fill the 130dp height perfectly
            )

            // Duration pill (keep this so users know how long the video is)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
                    .background(Color(0xB30F1620), RoundedCornerShape(99.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            ) {
                Text(
                    card.duration,
                    color = Color.White,
                    style = LLType.Eyebrow.copy(letterSpacing = 0.3.sp, fontSize = 11.sp),
                )
            }
        }
        Column(modifier = Modifier.padding(14.dp)) {
            Eyebrow(card.cat)
            Spacer(Modifier.height(4.dp))
            Text(card.title, style = LLType.Title.copy(fontSize = 17.sp), color = LLColors.Ink)
            Spacer(Modifier.height(4.dp))
            Text(card.desc, style = LLType.BodySmall, color = LLColors.Muted)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoSheet(state: VideoSheetState, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = LLColors.Parchment2,
        dragHandle = null,
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp).padding(bottom = 30.dp)) {



            Spacer(Modifier.height(12.dp))

            YouTubePlayer(
                videoId = state.youtubeId,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f) // Keeps the video in standard widescreen ratio so it works on the phone beter
            )

            Spacer(Modifier.height(14.dp))
            Eyebrow(state.cat)
            Spacer(Modifier.height(4.dp))
            Text(state.title, style = LLType.Headline, color = LLColors.Ink)
            Spacer(Modifier.height(6.dp))
            Text(state.desc, style = LLType.BodySmall, color = LLColors.Muted)
        }
    }
}
