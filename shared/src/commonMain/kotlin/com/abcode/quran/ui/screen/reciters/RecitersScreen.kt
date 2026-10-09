package com.abcode.quran.ui.screen.reciters

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.abcode.quran.ui.theme.QuranTheme

@Composable
fun RecitersScreen(
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
                    title = "Reciters",
                    subtitle = "Listen to the most beautiful voices of the Quran",
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
                FeaturedReciterCard()
                Spacer(modifier = Modifier.height(24.dp))
                ReciterFilterRow()
                Spacer(modifier = Modifier.height(16.dp))
                ReciterList()
            }
        }
    }
}

@Composable
fun FeaturedReciterCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A3D37), Color(0xFF0F2626))
                )
            )
            .border(0.5.dp, Color(0x335ABF90), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFFC5A059), CircleShape)
            ) {
                // Placeholder image
                Image(
                    painter = painterResource(Res.drawable.ic_background),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Mishary Rashid Alafasy", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Star, contentDescription = null, tint = Color(0xFFC5A059), modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Popular Reciter", color = Color(0xFFC5A059), fontSize = 12.sp)
                }
                Text("Known for his soothing voice and beautiful recitation.", color = Color.Gray, fontSize = 11.sp, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Headset, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("114 Surahs", color = Color.Gray, fontSize = 10.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Star, contentDescription = null, tint = Color(0xFFC5A059), modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("5.0", color = Color.Gray, fontSize = 10.sp)
                    }
                }
            }
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F2626))
                    .border(1.dp, Color(0xFF5ABF90), CircleShape)
            ) {
                Icon(Icons.Rounded.PlayArrow, contentDescription = null, tint = Color(0xFFC5A059), modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
fun ReciterFilterRow() {
    val filters = listOf("All Reciters", "Popular", "Male", "Female", "Arabic")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(filters) { filter ->
            val isSelected = filter == "All Reciters"
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) Color(0xFF1E8461).copy(alpha = 0.2f) else Color(0x11FFFFFF))
                    .border(1.dp, if (isSelected) Color(0xFF1E8461) else Color(0x22FFFFFF), RoundedCornerShape(50))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(filter, color = if (isSelected) Color(0xFF5ABF90) else Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun ReciterList() {
    val items = listOf(
        ReciterItem("Mishary Rashid Alafasy", "Male", "5.0"),
        ReciterItem("Abdul Rahman Al-Sudais", "Male", "4.9"),
        ReciterItem("Maher Al-Muaiqly", "Male", "4.8"),
        ReciterItem("Saud Al-Shuraim", "Male", "4.8")
    )
    
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(items) { reciter ->
            ReciterCard(reciter)
        }
    }
}

data class ReciterItem(val name: String, val gender: String, val rating: String)

@Composable
fun ReciterCard(item: ReciterItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF0D3D3D).copy(alpha = 0.6f))
            .border(0.5.dp, Color(0x11FFFFFF), RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_background),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Person, contentDescription = null, tint = Color(0xFF5ABF90), modifier = Modifier.size(10.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(item.gender, color = Color.Gray, fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Headset, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("114 Surahs", color = Color.Gray, fontSize = 9.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Star, contentDescription = null, tint = Color(0xFFC5A059), modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(item.rating, color = Color.Gray, fontSize = 9.sp)
                    }
                }
            }
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0x11FFFFFF))
                    .border(0.5.dp, Color(0xFFC5A059), CircleShape)
            ) {
                Icon(Icons.Rounded.PlayArrow, contentDescription = null, tint = Color(0xFFC5A059), modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecitersScreenPreview() {
    QuranTheme {
        RecitersScreen()
    }
}
