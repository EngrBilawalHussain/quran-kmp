package com.abcode.quran.ui.screen.audioscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Mosque
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.runtime.remember
import com.abcode.quran.data.ArchiveFile
import com.abcode.quran.data.Qari
import com.abcode.quran.data.SurahDataProvider
import com.abcode.quran.ui.theme.QuranTheme
import com.abcode.quran.ui.theme.backgroundColor
import com.abcode.quran.util.shareText
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AudioQuranScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: AudioQuranViewModel = remember { AudioQuranViewModel() }
) {
    AudioQuranContent(
        surahs = viewModel.surahs,
        isLoading = viewModel.isLoading,
        isPlaying = viewModel.isPlaying,
        isBuffering = viewModel.isBuffering,
        currentPlayingSurah = viewModel.currentPlayingSurah,
        currentPosition = viewModel.currentPosition,
        totalDuration = viewModel.totalDuration,
        playbackSpeed = viewModel.playbackSpeed,
        repeatMode = viewModel.repeatMode,
        bookmarkedSurahs = viewModel.bookmarkedSurahs,
        selectedQari = viewModel.selectedQari,
        availableQaris = viewModel.filteredQaris,
        qariSearchQuery = viewModel.qariSearchQuery,
        isSegmentRepeatEnabled = viewModel.isSegmentRepeatEnabled,
        segmentStart = viewModel.segmentStart,
        segmentEnd = viewModel.segmentEnd,
        segmentRepeatCount = viewModel.segmentRepeatCount,
        onSurahClick = { surah -> viewModel.playSurah(surah) },
        onSeek = { position -> viewModel.seekTo(position) },
        onPlayNext = { viewModel.playNext() },
        onPlayPrevious = { viewModel.playPrevious() },
        onToggleRepeat = { viewModel.toggleRepeatMode() },
        onSetSpeed = { speed -> viewModel.changePlaybackSpeed(speed) },
        onToggleBookmark = { surah -> viewModel.toggleBookmark(surah) },
        onQariSelected = { qari -> viewModel.changeQari(qari) },
        onQariSearchQueryChange = { query -> viewModel.qariSearchQuery = query },
        onToggleSegmentRepeat = { viewModel.toggleSegmentRepeat(it) },
        onSetSegmentRange = { start, end ->
            viewModel.segmentStart = start
            viewModel.segmentEnd = end
        },
        onSetSegmentRepeatCount = { viewModel.segmentRepeatCount = it },
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun AudioQuranContent(
    surahs: List<ArchiveFile>,
    isLoading: Boolean,
    isPlaying: Boolean,
    isBuffering: Boolean,
    currentPlayingSurah: ArchiveFile?,
    currentPosition: Long,
    totalDuration: Long,
    playbackSpeed: Float,
    repeatMode: CustomRepeatMode,
    bookmarkedSurahs: Set<String>,
    selectedQari: Qari,
    availableQaris: List<Qari>,
    qariSearchQuery: String,
    isSegmentRepeatEnabled: Boolean,
    segmentStart: Long,
    segmentEnd: Long,
    segmentRepeatCount: Int,
    onSurahClick: (ArchiveFile) -> Unit,
    onSeek: (Long) -> Unit,
    onPlayNext: () -> Unit,
    onPlayPrevious: () -> Unit,
    onToggleRepeat: () -> Unit,
    onSetSpeed: (Float) -> Unit,
    onToggleBookmark: (ArchiveFile) -> Unit,
    onQariSelected: (Qari) -> Unit,
    onQariSearchQueryChange: (String) -> Unit,
    onToggleSegmentRepeat: (Boolean) -> Unit,
    onSetSegmentRange: (Long, Long) -> Unit,
    onSetSegmentRepeatCount: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showQariSelector by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = backgroundColor,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clickable { showQariSelector = true }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = selectedQari.name,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    QariDropdownIcon()
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFF5ABF90))
            } else if (surahs.isEmpty()) {
                Text(text = "No surahs found", color = Color.Gray)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(surahs) { surah ->
                        val isCurrent = currentPlayingSurah == surah
                        SurahListItem(
                            surah = surah,
                            isCurrent = isCurrent,
                            isPlaying = isPlaying && isCurrent,
                            isBuffering = isBuffering && isCurrent,
                            currentPosition = if (isCurrent) currentPosition else 0L,
                            totalDuration = if (isCurrent) totalDuration else 0L,
                            playbackSpeed = playbackSpeed,
                            repeatMode = repeatMode,
                            isBookmarked = bookmarkedSurahs.contains(surah.name),
                            isSegmentRepeatEnabled = isSegmentRepeatEnabled,
                            segmentStart = segmentStart,
                            segmentEnd = segmentEnd,
                            segmentRepeatCount = segmentRepeatCount,
                            onSeek = onSeek,
                            onPlayNext = onPlayNext,
                            onPlayPrevious = onPlayPrevious,
                            onToggleRepeat = onToggleRepeat,
                            onSetSpeed = onSetSpeed,
                            onToggleBookmark = { onToggleBookmark(surah) },
                            onToggleSegmentRepeat = onToggleSegmentRepeat,
                            onSetSegmentRange = onSetSegmentRange,
                            onSetSegmentRepeatCount = onSetSegmentRepeatCount,
                            onClick = { onSurahClick(surah) }
                        )
                    }
                }
            }
        }

        if (showQariSelector) {
            QariSelectionDialog(
                selectedQari = selectedQari,
                availableQaris = availableQaris,
                searchQuery = qariSearchQuery,
                onQueryChange = onQariSearchQueryChange,
                onDismiss = { showQariSelector = false },
                onQariSelected = {
                    onQariSelected(it)
                    showQariSelector = false
                }
            )
        }
    }
}

@Composable
fun QariDropdownIcon() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF0F2626))
            .border(1.dp, Color(0x335ABF90), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowDropDown,
            contentDescription = "Select Qari",
            tint = Color(0xFF5ABF90),
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun QariSelectionDialog(
    selectedQari: Qari,
    availableQaris: List<Qari>,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onQariSelected: (Qari) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header with search
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onQueryChange,
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Search Qari...", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = Color.Gray) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onQueryChange("") }) {
                                    Icon(Icons.Rounded.Close, contentDescription = "Clear", tint = Color.Gray)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5ABF90),
                            unfocusedBorderColor = Color(0x33FFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color(0xFF5ABF90)
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Qari List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    items(availableQaris) { qari ->
                        val isSelected = qari == selectedQari
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0x225ABF90) else Color(0x11FFFFFF))
                                .clickable { onQariSelected(qari) }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) Color(0xFF5ABF90) else Color(0x22FFFFFF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = qari.name.first().toString(),
                                    color = if (isSelected) Color.White else Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = qari.name,
                                color = if (isSelected) Color(0xFF5ABF90) else Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SurahListItem(
    surah: ArchiveFile,
    isCurrent: Boolean,
    isPlaying: Boolean,
    isBuffering: Boolean,
    currentPosition: Long,
    totalDuration: Long,
    playbackSpeed: Float,
    repeatMode: CustomRepeatMode,
    isBookmarked: Boolean,
    isSegmentRepeatEnabled: Boolean,
    segmentStart: Long,
    segmentEnd: Long,
    segmentRepeatCount: Int,
    onSeek: (Long) -> Unit,
    onPlayNext: () -> Unit,
    onPlayPrevious: () -> Unit,
    onToggleRepeat: () -> Unit,
    onSetSpeed: (Float) -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleSegmentRepeat: (Boolean) -> Unit,
    onSetSegmentRange: (Long, Long) -> Unit,
    onSetSegmentRepeatCount: (Int) -> Unit,
    onClick: () -> Unit
) {
    val surahNumber = surah.name.substringBefore(".").filter { it.isDigit() }.toIntOrNull() ?: 0
    val surahDetails = SurahDataProvider.getSurahDetails(surahNumber)

    var sliderValue by remember { mutableFloatStateOf(currentPosition.toFloat()) }
    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(currentPosition) {
        if (!isDragging) {
            sliderValue = currentPosition.toFloat()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = if (isCurrent) {
                        listOf(Color(0xFF0D3D3D), Color(0xFF081C1C))
                    } else {
                        listOf(Color(0xFF0F2626), Color(0xFF0A1A1A))
                    }
                )
            )
            .border(1.dp, if (isCurrent) Color(0x445ABF90) else Color(0x11FFFFFF), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SurahNumberIcon(number = surahNumber)

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = surahDetails?.name ?: (surah.title ?: surah.name),
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${surahDetails?.englishName ?: ""})",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SurahBadge(
                            text = surahDetails?.revelationType ?: "Unknown",
                            icon = Icons.Rounded.Mosque
                        )
                        Box(modifier = Modifier.size(3.dp).background(Color(0xFF5ABF90), CircleShape))
                        SurahBadge(
                            text = "${surahDetails?.verseCount ?: 0}",
                            icon = Icons.AutoMirrored.Rounded.MenuBook
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "❖", color = Color(0xFFC5A059), fontSize = 10.sp)
                        Text(
                            text = surahDetails?.arabicName ?: "",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color(0xFF5ABF90),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Text(text = "❖", color = Color(0xFFC5A059), fontSize = 10.sp)
                    }
                    
                    if (!isCurrent) {
                        Spacer(modifier = Modifier.height(4.dp))
                        IconButton(
                            onClick = onClick,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0x11FFFFFF))
                                .border(1.5.dp, Color(0xFF5ABF90), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFFC5A059)
                            )
                        }
                    }
                }
            }

            if (isCurrent) {
                Spacer(modifier = Modifier.height(20.dp))
                
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatTime(sliderValue.toLong()),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF5ABF90),
                            fontSize = 11.sp
                        )
                        Text(
                            text = formatTime(totalDuration),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                    
                    Slider(
                        value = sliderValue,
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
                            thumbColor = Color(0xFF5ABF90),
                            activeTrackColor = Color(0xFF5ABF90),
                            inactiveTrackColor = Color(0x335ABF90)
                        ),
                        modifier = Modifier.height(24.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Segment Repeat Section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x0AFFFFFF))
                            .border(0.5.dp, Color(0x1A5ABF90), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Repeat Segment",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = Color.Gray
                                    )
                                }
                                Switch(
                                    checked = isSegmentRepeatEnabled,
                                    onCheckedChange = onToggleSegmentRepeat,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color(0xFF5ABF90),
                                        uncheckedThumbColor = Color.Gray,
                                        uncheckedTrackColor = Color(0x33FFFFFF)
                                    ),
                                    modifier = Modifier.scale(0.7f)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            val safeTotalDuration = totalDuration.coerceAtLeast(1L).toFloat()
                            val safeSegmentStart = segmentStart.toFloat().coerceIn(0f, safeTotalDuration)
                            val safeSegmentEnd = segmentEnd.toFloat().coerceIn(safeSegmentStart, safeTotalDuration)

                            RangeSlider(
                                value = safeSegmentStart..safeSegmentEnd,
                                onValueChange = { range ->
                                    onSetSegmentRange(range.start.toLong(), range.endInclusive.toLong())
                                },
                                valueRange = 0f..safeTotalDuration,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFFC5A059),
                                    activeTrackColor = Color(0xFFC5A059),
                                    inactiveTrackColor = Color(0x22C5A059)
                                ),
                                modifier = Modifier.height(24.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = formatTime(segmentStart),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFC5A059)
                                )
                                Text(
                                    text = formatTime(segmentEnd),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFC5A059)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Repeating ${formatTime(segmentStart)} - ${formatTime(segmentEnd)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                                
                                var showRepeatCountMenu by remember { mutableStateOf(false) }
                                Box {
                                    Text(
                                        text = if (segmentRepeatCount == 0) "Repeat Infinite ⌵" else "Repeat $segmentRepeatCount times ⌵",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF5ABF90),
                                        modifier = Modifier.clickable { showRepeatCountMenu = true }
                                    )
                                    DropdownMenu(
                                        expanded = showRepeatCountMenu,
                                        onDismissRequest = { showRepeatCountMenu = false },
                                        modifier = Modifier.background(Color(0xFF0F2626))
                                    ) {
                                        listOf(0, 1, 2, 3, 5, 10).forEach { count ->
                                            DropdownMenuItem(
                                                text = { 
                                                    Text(
                                                        text = if (count == 0) "Infinite" else "$count times",
                                                        color = Color.White
                                                    ) 
                                                },
                                                onClick = {
                                                    onSetSegmentRepeatCount(count)
                                                    showRepeatCountMenu = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Speed
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x11FFFFFF))
                            .border(0.5.dp, Color(0x22FFFFFF), RoundedCornerShape(12.dp))
                            .clickable {
                                val newSpeed = if (playbackSpeed >= 2.0f) 0.5f else playbackSpeed + 0.25f
                                onSetSpeed(newSpeed)
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${playbackSpeed}x",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.LightGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Repeat
                    IconButton(onClick = onToggleRepeat, modifier = Modifier.size(32.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Repeat,
                                contentDescription = "Repeat",
                                modifier = Modifier.size(22.dp),
                                tint = if (repeatMode != CustomRepeatMode.OFF) Color(0xFF5ABF90) else Color(0x66FFFFFF)
                            )
                            if (repeatMode == CustomRepeatMode.ONE || repeatMode == CustomRepeatMode.TWO || repeatMode == CustomRepeatMode.THREE) {
                                Text(
                                    text = when (repeatMode) {
                                        CustomRepeatMode.ONE -> "1"
                                        CustomRepeatMode.TWO -> "2"
                                        CustomRepeatMode.THREE -> "3"
                                        else -> ""
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF5ABF90),
                                        fontWeight = FontWeight.Black,
                                        fontSize = 8.sp
                                    ),
                                    modifier = Modifier
                                        .padding(top = 1.dp)
                                        .padding(horizontal = 1.dp)
                                )
                            }
                        }
                    }

                    // Previous
                    IconButton(onClick = onPlayPrevious, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.SkipPrevious,
                            contentDescription = "Previous",
                            modifier = Modifier.size(24.dp),
                            tint = Color(0x88FFFFFF)
                        )
                    }

                    // Main Play/Pause (Glowing)
                    IconButton(
                        onClick = onClick,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0D3D3D))
                            .border(1.5.dp, Color(0xFF5ABF90), CircleShape)
                    ) {
                        if (isBuffering) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                strokeWidth = 2.5.dp,
                                color = Color(0xFF5ABF90)
                            )
                        } else {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                modifier = Modifier.size(32.dp),
                                tint = Color(0xFF5ABF90)
                            )
                        }
                    }

                    // Next
                    IconButton(onClick = onPlayNext, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.SkipNext,
                            contentDescription = "Next",
                            modifier = Modifier.size(24.dp),
                            tint = Color(0x88FFFFFF)
                        )
                    }

                    // Bookmark
                    IconButton(onClick = onToggleBookmark, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                            contentDescription = "Bookmark",
                            modifier = Modifier.size(20.dp),
                            tint = if (isBookmarked) Color(0xFF5ABF90) else Color(0x66FFFFFF)
                        )
                    }

                    // More
                    IconButton(
                        onClick = { 
                            shareText("Listening to Surah ${surahDetails?.name ?: surah.name} by Mahmoud Khalil Al-Husary")
                        }, 
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MoreHoriz,
                            contentDescription = "More",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0x66FFFFFF)
                        )
                    }
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

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return minutes.toString().padStart(2, '0') + ":" + seconds.toString().padStart(2, '0')
}

@Composable
fun SurahNumberIcon(number: Int) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .drawBehind {
                val centerX = size.width / 2
                val centerY = size.height / 2
                
                // Outer gold star
                val outerPath = createStarPath(centerX, centerY, size.width / 2, (size.width / 2) * 0.85f)
                drawPath(outerPath, Color(0xFFC5A059), style = Stroke(width = 2.dp.toPx()))
                
                // Inner teal fill star
                val innerPath = createStarPath(centerX, centerY, (size.width / 2) * 0.75f, (size.width / 2) * 0.65f)
                drawPath(innerPath, Color(0x335ABF90), style = Fill)
                drawPath(innerPath, Color(0xFF5ABF90), style = Stroke(width = 1.dp.toPx()))
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            fontSize = 16.sp
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

@Preview(showBackground = true)
@Composable
fun AudioQuranScreenPreview() {
    val sampleSurahs = listOf(
        ArchiveFile(name = "001.mp3", title = "Preview 1", length = "01:00"),
        ArchiveFile(name = "002.mp3", title = "Preview 2", length = "2:30:00"),
        ArchiveFile(name = "114.mp3", title = "Preview 114", length = "00:30")
    )
    QuranTheme {
        AudioQuranContent(
            surahs = sampleSurahs,
            isLoading = false,
            isPlaying = true,
            isBuffering = false,
            currentPlayingSurah = sampleSurahs[0],
            currentPosition = 30000L,
            totalDuration = 60000L,
            playbackSpeed = 1.0f,
            repeatMode = CustomRepeatMode.OFF,
            bookmarkedSurahs = emptySet(),
            selectedQari = SurahDataProvider.availableQaris.first(),
            availableQaris = SurahDataProvider.availableQaris,
            qariSearchQuery = "",
            isSegmentRepeatEnabled = false,
            segmentStart = 15000L,
            segmentEnd = 45000L,
            segmentRepeatCount = 3,
            onSurahClick = {},
            onSeek = {},
            onPlayNext = {},
            onPlayPrevious = {},
            onToggleRepeat = {},
            onSetSpeed = {},
            onToggleBookmark = {},
            onQariSelected = {},
            onQariSearchQueryChange = {},
            onToggleSegmentRepeat = {},
            onSetSegmentRange = { _, _ -> },
            onSetSegmentRepeatCount = {},
            onBack = {}
        )
    }
}
