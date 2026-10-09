package com.abcode.quran.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import quran.shared.generated.resources.Res
import quran.shared.generated.resources.ic_background
import com.abcode.quran.ui.theme.QuranTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MainScreen(
    onAudioQuranClick: () -> Unit = {},
    onJuzzClick: () -> Unit = {},
    onSurahClick: () -> Unit = {},
    onTextWithAudioClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = remember { MainViewModel() }
) {
    MainScreenContent(
        prayerTimes = viewModel.prayerTimes,
        isLoading = viewModel.isLoading,
        onAudioQuranClick = onAudioQuranClick,
        onJuzzClick = onJuzzClick,
        onSurahClick = onSurahClick,
        onTextWithAudioClick = onTextWithAudioClick,
        modifier = modifier
    )
}

@Composable
fun MainScreenContent(
    prayerTimes: List<PrayerTime>,
    isLoading: Boolean,
    onAudioQuranClick: () -> Unit,
    onJuzzClick: () -> Unit,
    onSurahClick: () -> Unit,
    onTextWithAudioClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.ic_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                MainTopBar()
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Text(
                        text = "Assalamu'alaikum",
                        color = Color(0xFFC5A059),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                
                // Prayer Times Card
                item {
                    PrayerTimesCard(
                        prayerTimes = prayerTimes,
                        isLoading = isLoading
                    )
                }

                // Feature Grid
                item {
                    FeatureGrid(
                        onAudioQuranClick = onAudioQuranClick,
                        onJuzzClick = onJuzzClick,
                        onSurahClick = onSurahClick,
                        onTextWithAudioClick = onTextWithAudioClick
                    )
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun MainTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.Menu,
            contentDescription = "Menu",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = "Search",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun PrayerTimesCard(
    prayerTimes: List<PrayerTime>,
    isLoading: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D3D3D), Color(0xFF081C1C))
                )
            )
            .border(0.5.dp, Color(0x335ABF90), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        if (isLoading && prayerTimes.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().height(250.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFC5A059))
            }
        } else {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x225ABF90))
                            .border(0.5.dp, Color(0x445ABF90), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Mosque, contentDescription = null, tint = Color(0xFFC5A059), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Prayer Times (Salah)",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = Color(0xFFC5A059), modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Lahore, Pakistan",
                                color = Color.Gray,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color(0x11FFFFFF))
                Spacer(modifier = Modifier.height(12.dp))
                
                prayerTimes.forEachIndexed { index, prayer ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = prayer.name,
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = prayer.time,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                imageVector = prayer.icon,
                                contentDescription = null,
                                tint = Color(0xFFC5A059),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    if (index < prayerTimes.size - 1) {
                        HorizontalDivider(color = Color(0x0AFFFFFF), thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureGrid(
    onAudioQuranClick: () -> Unit,
    onJuzzClick: () -> Unit,
    onSurahClick: () -> Unit,
    onTextWithAudioClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            FeatureItem(
                title = "TEXT QURAN\nBY JUZ",
                subTitle = "Read Quran by 30 Juz",
                icon = Icons.Rounded.MenuBook,
                modifier = Modifier.weight(1f),
                color = Color(0xFF043434),
                onClick = onJuzzClick
            )
            FeatureItem(
                title = "TEXT QURAN\nBY SURAH",
                subTitle = "Browse all 114 Surahs",
                icon = Icons.Rounded.AutoStories,
                modifier = Modifier.weight(1f),
                color = Color(0xFF0D3D3D),
                isHighlighted = true,
                onClick = onSurahClick
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            FeatureItem(
                title = "TEXT QURAN\nWITH AUDIO",
                subTitle = "Verse by Verse\nRecitation",
                icon = Icons.Rounded.LibraryMusic,
                modifier = Modifier.weight(1f),
                color = Color(0xFF0A2A2A),
                onClick = onTextWithAudioClick
            )
            FeatureItem(
                title = "AUDIO QURAN\nBY SURAH",
                subTitle = "Listen to Surah\nRecitations",
                icon = Icons.Rounded.Headset,
                modifier = Modifier.weight(1f),
                color = Color(0xFF1E8461),
                onClick = onAudioQuranClick
            )
        }
    }
}

@Composable
fun FeatureItem(
    title: String,
    subTitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    color: Color,
    isHighlighted: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .aspectRatio(0.95f)
            .clip(RoundedCornerShape(24.dp))
            .background(color)
            .drawBehind {
                // Decorative Arch
                val path = Path().apply {
                    val w = size.width
                    val h = size.height
                    moveTo(w * 0.1f, h)
                    lineTo(w * 0.1f, h * 0.4f)
                    quadraticTo(w * 0.1f, h * 0.1f, w * 0.5f, h * 0.1f)
                    quadraticTo(w * 0.9f, h * 0.1f, w * 0.9f, h * 0.4f)
                    lineTo(w * 0.9f, h)
                }
                drawPath(path, Color.White.copy(alpha = 0.05f), style = Stroke(width = 2.dp.toPx()))
            }
            .then(
                if (isHighlighted) Modifier.border(2.dp, Color(0xFFC5A059), RoundedCornerShape(24.dp))
                else Modifier
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isHighlighted) Color(0xFFC5A059) else Color.White,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                color = if (isHighlighted) Color(0xFFC5A059) else Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subTitle,
                color = Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                fontSize = 10.sp
            )
        }
        
        // Bottom right arrow
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(20.dp)
                .clip(CircleShape)
                .border(0.5.dp, Color.White.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
fun MainBottomNavigation(
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF081C1C),
        tonalElevation = 8.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier.navigationBarsPadding().height(80.dp)
        ) {
            val items = listOf(
                Triple("home", "HOME", Icons.Rounded.Home),
                Triple("explore", "EXPLORE", Icons.Rounded.Search),
                Triple("bookmarks", "BOOKMARKS", Icons.Rounded.BookmarkBorder),
                Triple("reciters", "RECITERS", Icons.Rounded.InterpreterMode),
                Triple("settings", "SETTINGS", Icons.Rounded.Settings)
            )
            
            items.forEach { item ->
                val tab = item.first
                val label = item.second
                val icon = item.third
                val isSelected = currentTab == tab
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    icon = { Icon(icon, contentDescription = label) },
                    label = { 
                        Text(
                            text = label,
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        ) 
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFC5A059),
                        selectedTextColor = Color(0xFFC5A059),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color(0xFFC5A059).copy(alpha = 0.1f)
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val samplePrayers = listOf(
        PrayerTime("Fajr (فجر)", "5:15 AM", Icons.Rounded.WbTwilight),
        PrayerTime("Sunrise (شروق)", "6:30 AM", Icons.Rounded.WbSunny),
        PrayerTime("Dhuhr (ظهر)", "12:10 PM", Icons.Rounded.WbSunny),
        PrayerTime("Asr (عصر)", "3:35 PM", Icons.Rounded.WbCloudy),
        PrayerTime("Maghrib (مغرب)", "5:50 PM", Icons.Rounded.WbTwilight),
        PrayerTime("Isha (عشاء)", "7:05 PM", Icons.Rounded.NightsStay)
    )
    QuranTheme {
        MainScreenContent(
            prayerTimes = samplePrayers,
            isLoading = false,
            onAudioQuranClick = {},
            onJuzzClick = {},
            onSurahClick = {},
            onTextWithAudioClick = {}
        )
    }
}
