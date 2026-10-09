package com.abcode.quran.ui.screen.settings

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
import androidx.compose.ui.draw.scale
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
fun SettingsScreen(
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
                    title = "Settings",
                    subtitle = "Customize your Quran experience",
                    onBack = onBack
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Profile Card
                item {
                    ProfileCard()
                }

                // App Settings Group
                item {
                    SettingsGroup("App Settings", listOf(
                        SettingItem("Quran Reading", "Arabic text, translation & transliteration", Icons.Rounded.MenuBook),
                        SettingItem("Audio Settings", "Recitation voice, speed & repeat", Icons.Rounded.VolumeUp),
                        SettingItem("Appearance", "Theme, font size & display", Icons.Rounded.WbSunny),
                        SettingItem("Notifications", "Daily reminders & alerts", Icons.Rounded.Notifications),
                        SettingItem("Language", "Select your preferred language", Icons.Rounded.Language),
                        SettingItem("Download Settings", "Auto-download & storage", Icons.Rounded.Download)
                    ))
                }

                // Support & More Group
                item {
                    SettingsGroup("Support & More", listOf(
                        SettingItem("Share App", "Invite your friends", Icons.Rounded.Share),
                        SettingItem("Rate Us", "Help us improve", Icons.Rounded.Star),
                        SettingItem("Privacy Policy", "Your data is safe with us", Icons.Rounded.Security),
                        SettingItem("About Us", "Learn more about Quran App", Icons.Rounded.Info)
                    ))
                }

                // Dark Mode Toggle
                item {
                    DarkModeToggle()
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun ProfileCard() {
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
            .clickable { }
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0x225ABF90))
                    .border(1.5.dp, Color(0xFFC5A059), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.MenuBook, contentDescription = null, tint = Color(0xFFC5A059), modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Engr Bilawal Hussain", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("bilawalhussain@gmail.com", color = Color.Gray, fontSize = 14.sp)
            }
            Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun SettingsGroup(title: String, items: List<SettingItem>) {
    Column {
        Text(
            text = title,
            color = Color(0xFFC5A059),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF0F2626).copy(alpha = 0.8f))
                .border(0.5.dp, Color(0x22FFFFFF), RoundedCornerShape(20.dp))
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    SettingRow(item)
                    if (index < items.size - 1) {
                        HorizontalDivider(color = Color(0x11FFFFFF), modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

data class SettingItem(val title: String, val subtitle: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun SettingRow(item: SettingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x225ABF90))
                .border(0.5.dp, Color(0x445ABF90), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, contentDescription = null, tint = Color(0xFF5ABF90), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(item.subtitle, color = Color.Gray, fontSize = 11.sp)
        }
        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun DarkModeToggle() {
    var isDark by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF0F2626).copy(alpha = 0.8f))
            .border(0.5.dp, Color(0x22FFFFFF), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x225ABF90)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.NightsStay, contentDescription = null, tint = Color(0xFF5ABF90), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Dark Mode", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text("Switch between light and dark mode", color = Color.Gray, fontSize = 11.sp)
            }
            Switch(
                checked = isDark,
                onCheckedChange = { isDark = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF5ABF90),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color(0x33FFFFFF)
                ),
                modifier = Modifier.scale(0.8f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    QuranTheme {
        SettingsScreen()
    }
}
