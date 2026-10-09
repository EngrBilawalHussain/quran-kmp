package com.abcode.quran.ui.screen.textbyjuzz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Headset
import androidx.compose.material.icons.rounded.Mosque
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.abcode.quran.data.JuzDetails
import com.abcode.quran.data.UmmahJuzData
import com.abcode.quran.data.UmmahTranslations
import com.abcode.quran.data.UmmahVerse
import com.abcode.quran.ui.theme.QuranTheme
import com.abcode.quran.ui.theme.backgroundColor
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TextQuranbyJuzzScreen(
    onBack: () -> Unit,
    viewModel: JuzViewModel = remember { JuzViewModel() }
) {
    if (viewModel.selectedJuzDetails == null) {
        JuzSelectionContent(
            juzList = viewModel.juzList,
            onJuzClick = { viewModel.selectJuz(it) },
            onBack = onBack
        )
    } else {
        JuzDetailContent(
            juzDetails = viewModel.selectedJuzDetails!!,
            juzData = viewModel.juzContent,
            isLoading = viewModel.isLoading,
            onBack = { viewModel.clearSelection() }
        )
    }
}

@Composable
fun JuzSelectionContent(
    juzList: List<JuzDetails>,
    onJuzClick: (JuzDetails) -> Unit,
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
                    text = "SELECT Juzz",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(juzList) { juz ->
                JuzGridItem(juz = juz, onClick = { onJuzClick(juz) })
            }
        }
    }
}

@Composable
fun JuzGridItem(juz: JuzDetails, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D3D3D), Color(0xFF081C1C))
                )
            )
            .border(0.5.dp, Color(0x22FFFFFF), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                JuzNumberIcon(number = juz.number, size = 36)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Juzz ${juz.number}",
                        color = Color(0xFFC5A059),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = juz.name,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            }
            
            Text(
                text = juz.subtitle,
                color = Color.Gray,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SurahBadge(text = juz.revelationType.split("/").first(), icon = Icons.Rounded.Mosque)
                SurahBadge(text = "Surah mix", icon = Icons.AutoMirrored.Rounded.MenuBook)
            }
        }
    }
}

@Composable
fun JuzDetailContent(
    juzDetails: JuzDetails,
    juzData: UmmahJuzData?,
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
                    text = "JUZZ ${juzDetails.number}: ${juzDetails.name.uppercase()}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
            }
        },
        bottomBar = {
            JuzBottomBar()
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF5ABF90)
                )
            } else if (juzData != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Card
                    item {
                        JuzHeaderCard(juzDetails)
                    }

                    // Verses
                    items(juzData.verses) { verse ->
                        VerseItem(verse)
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
fun JuzHeaderCard(juz: JuzDetails) {
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            JuzNumberIcon(number = juz.number, size = 48)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${juz.name} ${juz.subtitle}",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SurahBadge(text = "Makki", icon = Icons.Rounded.Mosque)
                    SurahBadge(text = juz.revelationType, icon = Icons.AutoMirrored.Rounded.MenuBook)
                }
            }
        }
    }
}

@Composable
fun VerseItem(verse: UmmahVerse) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Arabic Text
        Text(
            text = verse.arabic,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF5ABF90),
                fontWeight = FontWeight.Bold,
                lineHeight = 48.sp
            )
        )
        
        Text(
            text = "Surah ${verse.surah_name}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            color = Color.Gray,
            style = MaterialTheme.typography.labelSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Translation
        Row {
            Text(
                text = "[${verse.ayah}]",
                color = Color(0xFFC5A059),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Saheeh International:",
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall
                )
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

@Composable
fun JuzBottomBar() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF081C1C),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { }
            ) {
                Icon(Icons.Rounded.Headset, contentDescription = null, tint = Color(0xFFC5A059))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Listen", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1A3D37))
                    .clickable { }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Translation & Tafsir", color = Color.White, style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Rounded.Translate, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
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
fun JuzNumberIcon(number: Int, size: Int = 48) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .drawBehind {
                val cx = this.size.width / 2
                val cy = this.size.height / 2
                
                // Outer gold star
                val outerPath = createStarPath(cx, cy, this.size.width / 2, (this.size.width / 2) * 0.85f)
                drawPath(outerPath, Color(0xFFC5A059), style = Stroke(width = 2.dp.toPx()))
                
                // Inner teal fill star
                val innerPath = createStarPath(cx, cy, (this.size.width / 2) * 0.75f, (this.size.width / 2) * 0.65f)
                drawPath(innerPath, Color(0x335ABF90), style = Fill)
                drawPath(innerPath, Color(0xFF5ABF90), style = Stroke(width = 1.dp.toPx()))
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.titleSmall.copy(
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

@Preview(showBackground = true)
@Composable
fun TextQuranbyJuzzScreenPreview() {
    QuranTheme {
        JuzSelectionContent(
            juzList = listOf(
                JuzDetails(1, "Alif Lam Meem", "(The Opener's Part)", "Makki/Madani mix"),
                JuzDetails(2, "Sayaqool", "Surah mix", "Makki/Madani mix")
            ),
            onJuzClick = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun JuzDetailContentPreview() {
    QuranTheme {
        JuzDetailContent(
            juzDetails = JuzDetails(1, "Alif Lam Meem", "(The Opener's Part)", "Makki/Madani mix"),
            juzData = UmmahJuzData(
                juz_number = 1,
                verses = listOf(
                    UmmahVerse(
                        verse_key = "1:1",
                        surah_name = "Al-Fatihah",
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
