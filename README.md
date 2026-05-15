# Kavya-Kanaja 📖

**Kavya-Kanaja** is a modern Android application dedicated to the beauty of Kannada literature. Built with Jetpack Compose and Material 3, it offers a seamless experience for reading, listening to, and exploring the works of legendary poets.

## ✨ Features

- **🌅 Poem of the Day**: Discover a new masterpiece every time you open the app.
- **📜 Extensive Poem Library**: Browse through a curated collection of poems with bilingual support (Kannada & English).
- **🎙️ Audio & TTS Support**: Listen to poems via pre-recorded audio or high-quality Text-to-Speech (TTS).
- **🎭 Kavi Mandapa**: Explore detailed profiles of "The guiding stars of Kannada literature," including their life history and notable achievements.
- **💖 Favorites**: Save your favorite poems to your personal collection for quick access.
- **🔍 Smart Search**: Quickly find poems by title in either Kannada or English.
- **🌓 Dark Mode Support**: A beautiful, eye-friendly interface that adapts to your system theme.
- **🌍 Bilingual Interface**: Toggle seamlessly between Kannada and English.
- **📲 Easy Sharing**: Share your favorite verses with friends and family via social media.

## 🛠️ Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern declarative UI toolkit.
- **Design**: [Material 3](https://m3.material.io/) - Google's latest design system.
- **Navigation**: [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) - For smooth screen transitions.
- **Data**: [GSON](https://github.com/google/gson) - For parsing poet and poem data.
- **Media**: `MediaPlayer` & `TextToSpeech` - For an immersive auditory experience.
- **Language**: [Kotlin](https://kotlinlang.org/) - 100% written in Kotlin.

## 🚀 Getting Started

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Niranjanmotagi/Sahitya-Setu-android.git
   ```
2. **Open in Android Studio**:
   - Open Android Studio (Ladybug or newer recommended).
   - Select **File > Open** and navigate to the cloned folder.
3. **Build and Run**:
   - Sync the project with Gradle files.
   - Click the **Run** button to launch the app on your emulator or physical device.

## 📂 Project Structure

```text
app/
├── src/main/
│   ├── java/com/example/app/
│   │   ├── data/           # Repository logic for data loading
│   │   │   └── PoemRepository.kt
│   │   ├── model/          # Data classes (Poem, Poet, DailyVerse)
│   │   │   ├── Poem.kt
│   │   │   ├── Poet.kt
│   │   │   └── DailyVerse.kt
│   │   ├── ui/theme/       # Compose UI Screens and Theme
│   │   │   ├── PoemScreen.kt   # Main UI implementation
│   │   │   ├── Theme.kt
│   │   │   ├── Color.kt
│   │   │   └── Type.kt
│   │   └── MainActivity.kt # Entry point of the application
│   ├── assets/             # JSON data files for poems and poets
│   │   ├── poems.json
│   │   ├── poets.json
│   │   └── special_poems.json
│   └── res/raw/            # Audio files (.mp3) for poem recitations
web/                        # Project Landing Page (Hosted on Firebase)
│   ├── images/             # Visual assets for the website
│   └── index.html          # Professional landing page source
```

## 🤝 Contributing

Contributions are welcome! If you have suggestions for new poems, poets, or features, feel free to open an issue or submit a pull request.

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
*Created with ❤️ for Kannada Literature.*
