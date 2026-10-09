package com.abcode.quran.data

data class JuzDetails(
    val number: Int,
    val name: String,
    val subtitle: String,
    val revelationType: String
)

object JuzDataProvider {
    val juzList = listOf(
        JuzDetails(1, "Alif Lam Meem", "(The Opener's Part)", "Makki/Madani mix"),
        JuzDetails(2, "Sayaqool", "Surah mix", "Makki/Madani mix"),
        JuzDetails(3, "Tilkal Rusul", "Surah mix", "Makki/Madani mix"),
        JuzDetails(4, "Lan Tanaloo", "Surah mix", "Makki/Madani mix"),
        JuzDetails(5, "Wal Muhsanat", "Surah mix", "Madani"),
        JuzDetails(6, "La Yuhibbullah", "Surah mix", "Madani"),
        JuzDetails(7, "Wa Iza Samiu", "Surah mix", "Makki/Madani mix"),
        JuzDetails(8, "Wa Lau Annana", "Surah mix", "Makki"),
        JuzDetails(9, "Qal Al-Mala", "Surah mix", "Makki"),
        JuzDetails(10, "Wa A'lamu", "Surah mix", "Makki/Madani mix"),
        JuzDetails(11, "Ya'tazirun", "Surah mix", "Makki"),
        JuzDetails(12, "Wa Ma Min Daabbatin", "Surah mix", "Makki"),
        JuzDetails(13, "Wa Ma Ubarri'u", "Surah mix", "Makki/Madani mix"),
        JuzDetails(14, "Alif Lam Ra", "Surah mix", "Makki"),
        JuzDetails(15, "Subhanallazi", "Surah mix", "Makki"),
        JuzDetails(16, "Qala Alam", "Surah mix", "Makki"),
        JuzDetails(17, "Iqtaraba Linnasi", "Surah mix", "Makki"),
        JuzDetails(18, "Qad Aflaha", "Surah mix", "Makki/Madani mix"),
        JuzDetails(19, "Wa Qalallazina", "Surah mix", "Makki"),
        JuzDetails(20, "Aman Khalaqa", "Surah mix", "Makki"),
        JuzDetails(21, "Utlu Ma Uhiya", "Surah mix", "Makki/Madani mix"),
        JuzDetails(22, "Wa Man Yaqnut", "Surah mix", "Makki/Madani mix"),
        JuzDetails(23, "Wa Maliya", "Surah mix", "Makki"),
        JuzDetails(24, "Faman Azlamu", "Surah mix", "Makki"),
        JuzDetails(25, "Ilaihi Yuraddu", "Surah mix", "Makki"),
        JuzDetails(26, "Ha Meem", "Surah mix", "Makki/Madani mix"),
        JuzDetails(27, "Qala Fama Khatbukum", "Surah mix", "Makki/Madani mix"),
        JuzDetails(28, "Qad Sami Allah", "Surah mix", "Madani"),
        JuzDetails(29, "Tabarakallazi", "Surah mix", "Makki/Madani mix"),
        JuzDetails(30, "Amma Yatasa'alun", "Surah mix", "Makki")
    )

    fun getJuzDetails(number: Int): JuzDetails? {
        return juzList.find { it.number == number }
    }
}
