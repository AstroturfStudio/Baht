# 🎲 Baht - Ultimate Randomizer Toolkit

A comprehensive Android app featuring multiple randomizer tools built with Jetpack Compose and Material 3 design.

## 🎯 Features

- **🎯 Lucky Draw** - Random selection from custom lists
- **🪙 Coin Flip** - Classic heads or tails decision maker
- **🎲 Dice Roll** - Roll 1-6 dice with smooth animations
- **🔢 Number Generator** - Generate random numbers in custom ranges
- **⚖️ Weighted Random** - Fair selection based on contribution amounts
- **🎡 Wheel of Fortune** - Spin the wheel to choose from options
- **⚖️ Weighted Wheel** - Probability-based wheel spinner
- **🔐 Password Generator** - Secure random password creation
- **📝 List Splitter** - Divide lists into random groups
- **🔀 List Shuffler** - Randomize list order

## 🎨 Design

- Modern Material 3 design
- Smooth animations and transitions
- Responsive UI for phones and tablets
- Beautiful visual feedback

## 💰 Monetization

- Google AdMob integration
- Banner ads at bottom of screens
- Interstitial ads after actions
- Production/test ad switching

## 🛠 Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Compose
- **Build System**: Gradle with Kotlin DSL
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 14)

## 🚀 Building for Release

### Prerequisites
- Android Studio
- JDK 11+
- Android SDK

### Generate Signing Key
```bash
./generate-keystore.sh
```

### Build Release
```bash
./build-release.sh
```

This will create:
- `app-release.aab` for Play Store submission
- `app-release.apk` for testing

### Manual Build Commands
```bash
# Clean build
./gradlew clean

# Build release AAB (for Play Store)
./gradlew bundleRelease

# Build release APK (for testing)
./gradlew assembleRelease

# Build debug APK
./gradlew assembleDebug
```

## 📱 Play Store Release

See [PLAY_STORE_RELEASE.md](PLAY_STORE_RELEASE.md) for complete release checklist including:
- Required store assets
- App descriptions
- Privacy policy requirements
- Release process steps

## 🔐 Security

- Keystore and signing credentials are excluded from version control
- AdMob IDs automatically switch between test/production
- Local data storage only (no server communication)

## 📄 Privacy

The app:
- Stores all data locally on device
- Does not collect personal information
- Uses AdMob for advertising (see privacy policy)
- Suitable for all ages

## 🧪 Testing

```bash
# Install debug build
adb install app/build/outputs/apk/debug/app-debug.apk

# Install release build (after signing)
adb install app/build/outputs/apk/release/app-release.apk
```

## 📂 Project Structure

```
app/
├── src/main/
│   ├── java/studio/astroturf/baht/
│   │   ├── ads/              # AdMob integration
│   │   ├── ui/               # Compose UI screens
│   │   │   ├── coinFlip/
│   │   │   ├── diceRoll/
│   │   │   ├── luckyDraw/
│   │   │   ├── numberGenerator/
│   │   │   ├── passwordGenerator/
│   │   │   ├── wheelOfFortune/
│   │   │   └── ...
│   │   └── MainActivity.kt
│   └── res/                  # Resources (layouts, strings, etc.)
├── build.gradle.kts          # App build configuration
└── proguard-rules.pro        # ProGuard/R8 rules
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📧 Contact

- **Developer**: Studio Astroturf
- **Email**: [Your Email]
- **Play Store**: [Coming Soon]

## 🙏 Acknowledgments

- Material 3 Design System
- Jetpack Compose
- Google AdMob
- Android Developer Community 