# Vibentra - Firebase Login & Logout Tracking Setup Guide

This guide is saved for you so you can follow the steps whenever you are ready.

---

### Step 1: Open Firebase Console
1. Go to: **[https://console.firebase.google.com/](https://console.firebase.google.com/)**
2. Sign in with your Google account.

---

### Step 2: Create a Project
1. Click **"Add project"**.
2. Project name: `Vibentra` (or any name you like).
3. Click **Continue** -> disable Google Analytics (optional, for faster setup) -> **Create project**.

---

### Step 3: Add Android App
1. Click the **Android** icon (`</>`).
2. Package name (must match app ID):
   ```
   echo.music.iad1tya
   ```
3. Click **Register app**.

---

### Step 4: Download & Replace `google-services.json`
1. Download the `google-services.json` file.
2. Replace the file at:
   ```
   d:\Code\VIBENTRA V1\app\google-services.json
   ```

---

### Step 5: Enable Firestore Database
1. In Firebase Console left menu, go to **Build** -> **Firestore Database**.
2. Click **Create database**.
3. Choose location (e.g. `asia-south1`) -> Click **Next**.
4. Select **Start in test mode** -> Click **Create**.

---

### Step 6: Continue with the Assistant
Once you have replaced `app/google-services.json`, simply open this chat and say:
> *"I added the google-services.json file, let's proceed."*

We will handle all code changes in the background:
- **Zero UI changes** (your app interface remains 100% identical).
- **Zero data loss** (all playlists, songs, and history remain 100% safe).
