# Kavya-Kanaja 📖

**Kavya-Kanaja** is a modern Android application dedicated to revitalizing and preserving the beauty of Kannada literature for the digital age.

## 🎯 Problem Statement
The younger generation often finds it difficult to connect with traditional Kannada poetry due to complex language barriers and a lack of engaging digital platforms. This gap leads to a decline in cultural and literary interest. **Kavya-Kanaja** solves this by providing an interactive, bilingual, and multimedia-rich platform that makes classical and modern Kannada poetry accessible, understandable, and enjoyable for everyone.

## 🌐 Live Demo & Landing Page
Visit our professional landing page to learn more and download the app:
👉 **[Live Landing Page](https://kavya-kanaja-6c987.web.app)** (Hosted on Firebase)

## ✨ Features
- **🌅 Poem of the Day**: Discover a new masterpiece every time you open the app to stay inspired.
- **📜 Extensive Library**: A curated collection of poems with seamless bilingual support (Kannada & English).
- **🎙️ Audio & TTS**: Listen to professional recitations or use high-quality Text-to-Speech (TTS).
- **🎭 Kavi Mandapa**: In-depth profiles of the "Guiding Stars" of Kannada literature, including Jnanpith winners.
- **💖 Favorites & Search**: Save your favorite verses and find poems quickly with smart search.
- **🌓 Adaptive UI**: Fully supports Dark Mode and Material 3 dynamic color themes.

## 🛠️ Tech Stack
- **Language**: 100% [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Declarative UI)
- **Design System**: [Material 3](https://m3.material.io/)
- **Architecture**: MVVM-lite (Clean separation of Data, Model, and UI)
- **Data Handling**: [GSON](https://github.com/google/gson) for local JSON parsing
- **Hosting**: [Firebase Hosting](https://firebase.google.com/) for the web landing page

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug (2024.2.1) or newer.
- Android SDK 34 (Android 14).
- Java 17.

### Installation & Setup
1. **Clone the repository**:
   ```bash
   git clone https://github.com/Niranjanmotagi/Sahitya-Setu-android.git
   ```
2. **Open the Project**:
   - Launch Android Studio and select **Open**.
   - Navigate to the `Sahitya-Setu-android` folder.
3. **Sync Gradle**:
   - Wait for the "Gradle Sync" to finish successfully.

### Build and Run
- **To run on a device/emulator**:
  Click the **Run** button (green play icon) in Android Studio.
- **To build the APK via command line**:
  ```bash
  ./gradlew :app:assembleDebug
  ```
  The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`.

## 📂 Project Structure
```text
app/
├── src/main/
│   ├── java/com/example/app/
│   │   ├── data/           # Data Repository (JSON Loading)
│   │   ├── model/          # Data Models (Poem, Poet)
│   │   ├── ui/theme/       # Compose Screens, Components, and Theme
│   │   └── MainActivity.kt # Main App Entry Point
│   ├── assets/             # Local Data (poems.json, poets.json)
│   └── res/raw/            # Multimedia (Audio .mp3 files)
web/                        # Landing Page Source (Firebase)
```

## 📈 Future Improvements
- **Cloud Integration**: Move JSON and Audio files to Firebase Storage for dynamic updates.
- **User Contributions**: Allow users to submit their own poems for review.
- **Quiz Mode**: Add interactive quizzes about poets and literature to engage students.
- **Social Integration**: Direct sharing of poem snippets as beautiful images on Instagram/WhatsApp.

## 📜 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
*Created with ❤️ by Niranjan M to bridge the gap between tradition and technology.*
