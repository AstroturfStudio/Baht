# Play Store Release Checklist for Baht

## ✅ Completed
- [x] Release build configuration with ProGuard/R8
- [x] App signing configuration
- [x] Production AdMob IDs configured
- [x] Proper manifest permissions
- [x] Version info (1.0, versionCode 1)

## 🔧 Technical Preparation

### 1. Generate Signing Key
```bash
./generate-keystore.sh
```

### 2. Build Release APK/AAB
```bash
# Build APK for testing
./gradlew assembleRelease

# Build AAB for Play Store (recommended)
./gradlew bundleRelease
```

### 3. Test Release Build
- Install release APK on device
- Test all randomizer features
- Verify ads are working
- Check app performance

## 📱 Play Store Assets Needed

### App Icon
- **Requirement**: 512x512 PNG
- **Current**: Using adaptive icon (needs high-res version)
- **Location**: Upload to Play Console

### Screenshots (Required)
- **Phone**: Minimum 2, maximum 8 screenshots
- **Tablet**: Recommended for better visibility
- **Sizes**: 16:9 or 9:16 aspect ratio, minimum 320px

### Feature Graphic
- **Size**: 1024x500 pixels
- **Format**: PNG or JPEG
- **Usage**: Shown in Play Store when featured

### App Description

#### Short Description (80 chars max)
"Ultimate randomizer toolkit with dice, coins, wheels, and generators"

#### Full Description (4000 chars max)
```
🎲 BAHT - Your Ultimate Randomizer Toolkit

Make decisions easier with our comprehensive collection of randomizer tools!

✨ FEATURES:
• 🎯 Lucky Draw - Random selection from custom lists
• 🪙 Coin Flip - Classic heads or tails
• 🎲 Dice Roll - 1-6 dice with smooth animations
• 🔢 Number Generator - Custom range random numbers
• ⚖️ Weighted Random - Fair selection based on contributions
• 🎡 Wheel of Fortune - Spin to choose from options
• ⚖️ Weighted Wheel - Probability-based wheel spinner
• 🔐 Password Generator - Secure random passwords
• 📝 List Splitter - Divide lists into random groups
• 🔀 List Shuffler - Randomize list order

🎨 BEAUTIFUL DESIGN:
• Smooth animations and transitions
• Intuitive user experience

🎯 PERFECT FOR:
• Decision making
• Games and activities
• Team assignments
• Random selections
• Password generation

Download now and let randomness decide! 🎲
```

### Categories
- **Primary**: Tools
- **Secondary**: Productivity

### Content Rating
- Target age: Everyone
- Contains ads: Yes

## 📋 Pre-Launch Checklist

### Code Quality
- [ ] All features tested
- [ ] No debug code in release
- [ ] ProGuard rules optimized
- [ ] Memory leaks checked
- [ ] Performance optimized

### Legal & Privacy
- [ ] Privacy policy created (required for apps with ads)
- [ ] Terms of service (optional)
- [ ] Data safety form completed
- [ ] Content rating questionnaire

### Store Optimization
- [ ] App title optimized for search
- [ ] Keywords researched
- [ ] Screenshots showcase key features
- [ ] Description includes relevant keywords

## 🚀 Release Process

### 1. Upload to Play Console
1. Create app in Play Console
2. Upload AAB file
3. Add store listing assets
4. Complete content rating
5. Set pricing (Free)
6. Choose countries/regions

### 2. Review Process
- Google review typically takes 1-3 days
- Address any policy violations
- Monitor for approval

### 3. Post-Launch
- Monitor crash reports
- Check user reviews
- Track download metrics
- Plan updates

## 📞 Support Information

### Contact Details
- **Developer**: Your Name/Studio Astroturf
- **Email**: Your support email
- **Website**: Your website (optional)

### Privacy Policy URL
- Required for apps with ads
- Should cover AdMob data collection
- Host on website or GitHub Pages

---

**Next Steps**: 
1. Run `./generate-keystore.sh`
2. Build release with `./gradlew bundleRelease`
3. Create Play Store assets
4. Upload to Play Console 