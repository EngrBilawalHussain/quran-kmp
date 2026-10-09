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
import androidx.compose.material.icons.rounded.*
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
import com.abcode.quran.data.SurahDetails
import com.abcode.quran.ui.theme.QuranTheme
import com.abcode.quran.ui.theme.backgroundColor
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TextQuranbySurahScreen(
    onBack: () -> Unit,
    onReadClick: (SurahDetails) -> Unit = {},
    viewModel: TextQuranbySurahViewModel = remember { TextQuranbySurahViewModel() }
) {
    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            SurahListTopBar(onBack = onBack)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            SearchBox(
                query = viewModel.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(viewModel.filteredSurahs) { surah ->
                    SurahCard(
                        surah = surah,
                        onReadClick = { onReadClick(surah) },
                        onListenClick = { /* (Optional) future listen logic */ }
                    )
                }
            }
        }
    }
}

@Composable
fun SurahListTopBar(onBack: () -> Unit) {
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
            text = "SURAH LIST",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
        IconButton(
            onClick = { },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(Icons.Rounded.Search, contentDescription = "Search", tint = Color(0xFFC5A059))
        }
    }
}

@Composable
fun SearchBox(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search Surah (e.g., Al-Baqarah)", color = Color.Gray, fontSize = 14.sp) },
        trailingIcon = {
            Icon(Icons.Rounded.FilterList, contentDescription = null, tint = Color.Gray)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF0F2626),
            unfocusedContainerColor = Color(0xFF0F2626),
            focusedBorderColor = Color(0x33FFFFFF),
            unfocusedBorderColor = Color(0x33FFFFFF),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
fun SurahCard(
    surah: SurahDetails,
    onReadClick: () -> Unit,
    onListenClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D3D3D), Color(0xFF081C1C))
                )
            )
            .border(0.5.dp, Color(0x11FFFFFF), RoundedCornerShape(20.dp))
            .clickable { onReadClick() }
            .padding(16.dp)
    ) {
        Column {
            // Top Utilities
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onListenClick() }) {
                    Icon(Icons.Rounded.Headset, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Listen", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onReadClick() }) {
                    Icon(Icons.AutoMirrored.Rounded.MenuBook, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Read", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SurahNumberIcon(number = surah.number)
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = surah.name,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = surah.englishName,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SurahBadge(text = surah.revelationType, icon = Icons.Rounded.Mosque)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "| Ayahs: ${surah.verseCount} | Verses: ${surah.verseCount} | Juzz 1", // Juzz 1 is placeholder
                            color = Color.Gray,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp
                        )
                    }
                }
                Text(
                    text = surah.arabicName,
                    color = Color(0xFFC5A059),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
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
fun SurahNumberIcon(number: Int) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .drawBehind {
                val cx = size.width / 2
                val cy = size.height / 2
                
                // Outer gold star
                val outerPath = createStarPath(cx, cy, size.width / 2, (size.width / 2) * 0.85f)
                drawPath(outerPath, Color(0xFFC5A059), style = Stroke(width = 2.dp.toPx()))
                
                // Inner teal fill star
                val innerPath = createStarPath(cx, cy, (size.width / 2) * 0.75f, (size.width / 2) * 0.65f)
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
fun TextQuranbySurahScreenPreview() {
    QuranTheme {
        TextQuranbySurahScreen(onBack = {})
    }
}
