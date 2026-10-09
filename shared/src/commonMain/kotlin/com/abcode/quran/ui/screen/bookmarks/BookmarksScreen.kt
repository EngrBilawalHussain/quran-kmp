package com.abcode.quran.ui.screen.bookmarks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import quran.shared.generated.resources.Res
import quran.shared.generated.resources.ic_background
import com.abcode.quran.ui.components.PremiumHeader
import com.abcode.quran.ui.components.SurahNumberIcon
import com.abcode.quran.ui.theme.QuranTheme

@Composable
fun BookmarksScreen(
    onBack: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.ic_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                PremiumHeader(
                    title = "Bookmarks",
                    subtitle = "Your saved verses for easy access",
                    onBack = onBack
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                BookmarkTabs()
                Spacer(modifier = Modifier.height(16.dp))
                BookmarkList()
            }
        }
    }
}

@Composable
fun BookmarkTabs() {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        BookmarkTab("All Bookmarks", Icons.Rounded.Bookmark, isSelected = true)
        BookmarkTab("Surahs", Icons.Rounded.MenuBook)
        BookmarkTab("Juz", Icons.Rounded.LibraryBooks)
    }
}

@Composable
fun BookmarkTab(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean = false) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFFC5A059).copy(alpha = 0.2f) else Color(0x11FFFFFF))
            .border(1.dp, if (isSelected) Color(0xFFC5A059) else Color(0x11FFFFFF), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = if (isSelected) Color(0xFFC5A059) else Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text, color = if (isSelected) Color.White else Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BookmarkList() {
    val items = listOf(
        BookmarkItem(1, "Al-Fatihah", "الفاتحة", "Juz 1 • Verse 1", "Saved on 12 Mar 2025"),
        BookmarkItem(12, "Yusuf", "يوسف", "Juz 12 • Verse 4", "Saved on 10 Mar 2025"),
        BookmarkItem(18, "Al-Kahf", "الكهف", "Juz 15 • Verse 9", "Saved on 8 Mar 2025")
    )
    
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(items) { item ->
            BookmarkCard(item)
        }
    }
}

data class BookmarkItem(val number: Int, val english: String, val arabic: String, val location: String, val date: String)

@Composable
fun BookmarkCard(item: BookmarkItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF0F2626))
            .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SurahNumberIcon(number = item.number, size = 36)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.english, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(item.arabic, color = Color(0xFFC5A059), fontSize = 14.sp)
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Rounded.PlayCircle, contentDescription = null, tint = Color(0xFFC5A059), modifier = Modifier.size(28.dp))
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Rounded.Delete, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.LibraryBooks, contentDescription = null, tint = Color(0xFF5ABF90), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(item.location, color = Color.Gray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.CalendarToday, contentDescription = null, tint = Color(0xFF5ABF90).copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(item.date, color = Color.Gray.copy(alpha = 0.6f), fontSize = 10.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookmarksScreenPreview() {
    QuranTheme {
        BookmarksScreen()
    }
}
