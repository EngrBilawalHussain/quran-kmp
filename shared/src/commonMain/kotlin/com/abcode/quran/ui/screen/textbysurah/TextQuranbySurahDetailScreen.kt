package com.abcode.quran.ui.screen.textbysurah

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Headset
import androidx.compose.material.icons.rounded.Mosque
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import com.abcode.quran.data.*
import com.abcode.quran.ui.theme.QuranTheme
import com.abcode.quran.ui.theme.backgroundColor

@Composable
fun TextQuranbySurahDetailScreen(
    surahNumber: Int,
    onBack: () -> Unit,
    viewModel: TextQuranbySurahDetailViewModel = remember { TextQuranbySurahDetailViewModel() }
) {
    LaunchedEffect(surahNumber) {
        viewModel.fetchSurahContent(surahNumber)
    }

    TextQuranbySurahDetailContent(
        surahData = viewModel.surahData,
        isLoading = viewModel.isLoading,
        onBack = onBack
    )
}

@Composable
fun TextQuranbySurahDetailContent(
    surahData: UmmahSurahData?,
    isLoading: Boolean,
    onBack: () -> Unit
) {
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
                    text = surahData?.surah?.let { "${it.name_english} (${it.name_translation})" } ?: "Loading...",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF5ABF90)
                )
            } else if (surahData != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Card
                    item {
                        SurahHeaderCard(surahData.surah)
                    }

                    // Verses
                    items(surahData.verses) { verse ->
                        AyahItem(verse)
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = Color(0x11FFFFFF)
                        )
                    }
                }
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
                // Using SurahNumberIcon from TextQuranbySurah.kt
                SurahNumberIcon(number = surah.number)
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
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { }) {
                        Icon(Icons.Rounded.Headset, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Listen", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { }) {
                        Icon(Icons.AutoMirrored.Rounded.MenuBook, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Read", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                val place = surah.revelation_place.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                SurahBadge(text = place, icon = Icons.Rounded.Mosque)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Makki | Verses: ${surah.verses_count} | Juzz 1 |", // Juzz 1 as placeholder
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "Revealed after: Al-Muddaththir", // Placeholder
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
fun AyahItem(verse: UmmahVerse) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ayah ${verse.ayah}",
                color = Color.Gray,
                style = MaterialTheme.typography.labelSmall
            )
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { }) {
                    Icon(Icons.Rounded.Headset, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Recite Ayah", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { }) {
                    Icon(Icons.AutoMirrored.Rounded.MenuBook, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Show Tafsir", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.Top) {
            // Using SurahNumberIcon from TextQuranbySurah.kt
            SurahNumberIcon(number = verse.ayah)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = verse.arabic,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color(0xFF5ABF90),
                        fontWeight = FontWeight.Bold,
                        lineHeight = 40.sp
                    )
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = verse.translations.sahih_international,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextQuranbySurahDetailScreenPreview() {
    QuranTheme {
        TextQuranbySurahDetailContent(
            surahData = UmmahSurahData(
                surah = UmmahSurahInfo(
                    number = 1,
                    name_arabic = "الفاتحة",
                    name_english = "Al-Fatihah",
                    name_translation = "The Opener",
                    revelation_place = "makkah",
                    revelation_order = 5,
                    verses_count = 7
                ),
                verses = listOf(
                    UmmahVerse(
                        verse_key = "1:1",
                        ayah = 1,
                        arabic = "بِسْمِ ٱللَّهِ ٱلرَّحْمَـٰنِ ٱلرَّحِيمِ",
                        translations = UmmahTranslations("In the name of Allah, the Entirely Merciful, the Especially Merciful.")
                    )
                )
            ),
            isLoading = false,
            onBack = {}
        )
    }
}
