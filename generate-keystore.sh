#!/bin/bash
echo "Generating Android App Signing Keystore for Baht"
echo "=================================================="

# Prompt for keystore details
read -p "Enter keystore password: " -s KEYSTORE_PASSWORD
echo
read -p "Enter key alias (e.g., baht-release): " KEY_ALIAS
read -p "Enter key password: " -s KEY_PASSWORD
echo
read -p "Enter your name: " DEVELOPER_NAME
read -p "Enter your organization: " ORGANIZATION
read -p "Enter your city: " CITY
read -p "Enter your state/province: " STATE
read -p "Enter your country code (e.g., US): " COUNTRY

# Generate keystore
keytool -genkey -v -keystore release-key.keystore -alias $KEY_ALIAS -keyalg RSA -keysize 2048 -validity 10000 \
    -storepass $KEYSTORE_PASSWORD -keypass $KEY_PASSWORD \
    -dname "CN=$DEVELOPER_NAME, OU=$ORGANIZATION, L=$CITY, ST=$STATE, C=$COUNTRY"

echo
echo "Keystore generated successfully!"
echo "Add these to your ~/.gradle/gradle.properties or local.properties:"
echo "RELEASE_STORE_FILE=release-key.keystore"
echo "RELEASE_STORE_PASSWORD=$KEYSTORE_PASSWORD"
echo "RELEASE_KEY_ALIAS=$KEY_ALIAS"
echo "RELEASE_KEY_PASSWORD=$KEY_PASSWORD"
echo
echo "⚠️  IMPORTANT: Keep your keystore and passwords secure!"
echo "⚠️  You cannot upload a new app to Play Store without the same keystore!" 