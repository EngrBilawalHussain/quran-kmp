package com.abcode.quran.ui.screen.textwithaudio

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import com.abcode.quran.data.*
import com.abcode.quran.ui.theme.QuranTheme
import com.abcode.quran.ui.theme.backgroundColor
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TextwithAudioScreen(
    surahNumber: Int,
    onBack: () -> Unit,
    viewModel: TextwithAudioViewModel = remember { TextwithAudioViewModel() }
) {
    LaunchedEffect(surahNumber) {
        viewModel.fetchSurahContent(surahNumber)
    }

    val listState = rememberLazyListState()

    // Auto-scroll to current ayah
    LaunchedEffect(viewModel.currentAyahIndex) {
        if (viewModel.currentAyahIndex != -1) {
            listState.animateScrollToItem(viewModel.currentAyahIndex + 1) // +1 for header
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "Recitation: ${viewModel.surahData?.surah?.name_english ?: "Loading..."}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (viewModel.isLoading && viewModel.surahData == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF5ABF90)
                )
            } else if (viewModel.surahData != null) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Card
                    item {
                        SurahHeaderCard(viewModel.surahData!!.surah)
                    }

                    // Verses
                    itemsIndexed(viewModel.surahData!!.verses) { index, verse ->
                        AyahListItem(
                            verse = verse,
                            isPlaying = viewModel.currentAyahIndex == index,
                            onPlayClick = { viewModel.playAyah(index) }
                        )
                    }
                    
                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }

                // Floating Playback Overlay
                PlaybackOverlay(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                    currentAyah = viewModel.surahData?.verses?.getOrNull(viewModel.currentAyahIndex),
                    isPlaying = viewModel.isPlaying,
                    currentPosition = viewModel.currentPosition,
                    totalDuration = viewModel.totalDuration,
                    onTogglePlay = { viewModel.togglePlayPause() },
                    onNext = { viewModel.playNextAyah() },
                    onPrev = { viewModel.playPreviousAyah() },
                    onSeek = { viewModel.seekTo(it) }
                )
            }
        }
    }
}

@Composable
fun SurahHeaderCard(surah: UmmahSurahInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A3D37), Color(0xFF0F2626))
                )
            )
            .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SurahNumberIcon(number = surah.number, size = 48)
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = surah.name_english,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = surah.name_translation,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Headset, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Listen", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Rounded.MenuBook, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Read", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                SurahBadge(text = surah.revelation_place.replaceFirstChar { it.uppercase() }, icon = Icons.Rounded.Mosque)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Makki | Verses: ${surah.verses_count} | Juzz 1 |",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "Revealed after: Al-Muddaththir",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.MenuBook,
                    contentDescription = null,
                    tint = Color(0xFFC5A059),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tafsir", color = Color(0xFFC5A059), style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun AyahListItem(
    verse: UmmahVerse,
    isPlaying: Boolean,
    onPlayClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (isPlaying) Modifier
                    .background(Color(0xFF0F2626))
                    .border(1.dp, Color(0xFFC5A059), RoundedCornerShape(16.dp))
                else Modifier
            )
            .clickable { onPlayClick() }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ayah ${verse.ayah}",
                    color = if (isPlaying) Color(0xFFC5A059) else Color.Gray,
                    style = MaterialTheme.typography.labelSmall
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Headset, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Recite Ayah", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Rounded.MenuBook, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Show Tafsir", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Top) {
                SurahNumberIcon(number = verse.ayah, size = 28)
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = verse.arabic,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = if (isPlaying) Color(0xFFC5A059) else Color(0xFF5ABF90),
                            fontWeight = FontWeight.Bold,
                            lineHeight = 40.sp
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = verse.translations.sahih_international,
                        color = if (isPlaying) Color.White else Color.Gray,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PlaybackOverlay(
    modifier: Modifier = Modifier,
    currentAyah: UmmahVerse?,
    isPlaying: Boolean,
    currentPosition: Long,
    totalDuration: Long,
    onTogglePlay: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onSeek: (Long) -> Unit
) {
    if (currentAyah == null) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xE61A3D37), Color(0xE6081C1C))
                )
            )
            .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Now Playing: Ayah ${currentAyah.ayah}",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            var sliderValue by remember(currentPosition) { mutableFloatStateOf(currentPosition.toFloat()) }
            var isDragging by remember { mutableStateOf(false) }

            Slider(
                value = if (isDragging) sliderValue else currentPosition.toFloat(),
                onValueChange = {
                    isDragging = true
                    sliderValue = it
                },
                onValueChangeFinished = {
                    isDragging = false
                    onSeek(sliderValue.toLong())
                },
                valueRange = 0f..totalDuration.coerceAtLeast(1L).toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFC5A059),
                    activeTrackColor = Color(0xFFC5A059),
                    inactiveTrackColor = Color(0x33C5A059)
                ),
                modifier = Modifier.height(24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatTime(currentPosition), color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                Text(formatTime(totalDuration), color = Color.Gray, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Repeat */ }) {
                    Icon(Icons.Rounded.Repeat, contentDescription = null, tint = Color.Gray)
                }
                IconButton(onClick = onPrev) {
                    Icon(Icons.Rounded.SkipPrevious, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
                IconButton(
                    onClick = onTogglePlay,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFC5A059))
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(onClick = onNext) {
                    Icon(Icons.Rounded.SkipNext, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
                IconButton(onClick = { /* Volume/Settings */ }) {
                    Icon(Icons.AutoMirrored.Rounded.VolumeUp, contentDescription = null, tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun SurahBadge(text: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x11FFFFFF))
            .border(0.5.dp, Color(0x22C5A059), RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = Color(0xFF5ABF90)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFFC5A059),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
fun SurahNumberIcon(number: Int, size: Int = 48) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .drawBehind {
                val cx = this.size.width / 2
                val cy = this.size.height / 2
                val outerPath = createStarPath(cx, cy, this.size.width / 2, (this.size.width / 2) * 0.85f)
                drawPath(outerPath, Color(0xFFC5A059), style = Stroke(width = 2.dp.toPx()))
                val innerPath = createStarPath(cx, cy, (this.size.width / 2) * 0.75f, (this.size.width / 2) * 0.65f)
                drawPath(innerPath, Color(0x335ABF90), style = Fill)
                drawPath(innerPath, Color(0xFF5ABF90), style = Stroke(width = 1.dp.toPx()))
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = (size * 0.35f).sp
            )
        )
    }
}

private fun createStarPath(centerX: Float, centerY: Float, outerRadius: Float, innerRadius: Float): Path {
    val path = Path()
    val totalPoints = 16
    for (i in 0 until totalPoints) {
        val isOuter = i % 2 == 0
        val r = if (isOuter) outerRadius else innerRadius
        val angle = (i * (360.0 / totalPoints) - 90.0) * (kotlin.math.PI / 180.0)
        val x = centerX + r * cos(angle).toFloat()
        val y = centerY + r * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    return path
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return minutes.toString().padStart(2, '0') + ":" + seconds.toString().padStart(2, '0')
}

@Preview(showBackground = true)
@Composable
fun TextwithAudioScreenPreview() {
    QuranTheme {
        Box(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                SurahHeaderCard(
                    surah = UmmahSurahInfo(
                        number = 1,
                        name_arabic = "الفاتحة",
                        name_english = "Al-Fatihah",
                        name_translation = "The Opener",
                        revelation_place = "makkah",
                        revelation_order = 5,
                        verses_count = 7
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                AyahListItem(
                    verse = UmmahVerse(
                        verse_key = "1:3",
                        ayah = 3,
                        arabic = "ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
                        translations = UmmahTranslations("The Entirely Merciful, the Especially Merciful.")
                    ),
                    isPlaying = true,
                    onPlayClick = {}
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                PlaybackOverlay(
                    currentAyah = UmmahVerse(
                        verse_key = "1:3",
                        ayah = 3,
                        arabic = "ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
                        translations = UmmahTranslations("The Entirely Merciful, the Especially Merciful.")
                    ),
                    isPlaying = true,
                    currentPosition = 12000L,
                    totalDuration = 45000L,
                    onTogglePlay = {},
                    onNext = {},
                    onPrev = {},
                    onSeek = {}
                )
            }
        }
    }
}
