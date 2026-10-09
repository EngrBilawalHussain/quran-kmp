package com.abcode.quran.ui.screen.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import org.jetbrains.compose.resources.DrawableResource
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
fun ExploreScreen(
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
                Column {
                    SearchBar()
                    PremiumHeader(
                        title = "EXPLORE",
                        onBack = onBack
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                CategoryScroll()
                Spacer(modifier = Modifier.height(16.dp))
                ExploreGrid()
            }
        }
    }
}

@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Rounded.Search, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Search", color = Color.Gray, fontSize = 16.sp)
    }
}

@Composable
fun CategoryScroll() {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        CategoryChip("ART & ARCHITECTURE", Icons.Rounded.AccountBalance, isSelected = true)
        CategoryChip("SPIRITUAL RECIPES", Icons.Rounded.Restaurant)
    }
}

@Composable
fun CategoryChip(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean = false) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (isSelected) Color(0xFFC5A059).copy(alpha = 0.2f) else Color(0xFF0F2626))
            .border(1.dp, if (isSelected) Color(0xFFC5A059) else Color(0x33FFFFFF), RoundedCornerShape(50))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = if (isSelected) Color(0xFFC5A059) else Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, color = if (isSelected) Color(0xFFC5A059) else Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ExploreGrid() {
    val items = listOf(
        ExploreItem("MOSQUE OF THE WORLD", "A unique historical mosque...", Res.drawable.ic_background),
        ExploreItem("CALLIGRAPHY WORKSHOP", "A master thumbnail of a master calligrapher...", Res.drawable.ic_background),
        ExploreItem("RECIPE: Traditional Halva", "Explore hiss and cush of halva.", Res.drawable.ic_background),
        ExploreItem("ISLAMIC GEOMETRY", "Colored freauiled vector keet and vector pattern.", Res.drawable.ic_background)
    )
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { item ->
            ExploreCard(item)
        }
    }
}

data class ExploreItem(val title: String, val subtitle: String, val imageRes: DrawableResource)

@Composable
fun ExploreCard(item: ExploreItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF0F2626))
            .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(20.dp))
    ) {
        Column {
            Image(
                painter = painterResource(item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(item.title, color = Color(0xFFC5A059), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.subtitle, color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp, lineHeight = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Read Story", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Rounded.BookmarkBorder, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    QuranTheme {
        ExploreScreen()
    }
}
