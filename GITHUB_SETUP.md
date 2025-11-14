# GitHub Actions APK Build Setup

This guide will help you automatically build your Android APK using GitHub Actions (cloud-based, no local Android Studio needed).

## Prerequisites

- GitHub account (free - sign up at https://github.com)
- Git installed on your Mac (check by running `git --version` in Terminal)

---

## Step-by-Step Instructions

### Step 1: Initialize Git Repository

Open Terminal and run:

```bash
cd /Users/Pavan.Vovveti1/HeaderRemovalDemo
chmod +x setup-github.sh
./setup-github.sh
```

This will initialize the git repository and create your first commit.

---

### Step 2: Create GitHub Repository

1. **Go to GitHub**: https://github.com
2. **Sign in** (or create account if you don't have one)
3. **Click the "+" icon** in top-right corner → "New repository"
4. **Fill in details**:
   - Repository name: `HeaderRemovalDemo`
   - Description: "Android app to remove X-Requested-With header from WebView"
   - **Important**: Choose "Public" (GitHub Actions is free for public repos)
   - **Important**: Do NOT check "Initialize with README"
   - Click "Create repository"

---

### Step 3: Push Code to GitHub

After creating the repository, you'll see a page with commands. Copy your repository URL.

In Terminal, run (replace `YOUR_USERNAME` with your actual GitHub username):

```bash
cd /Users/Pavan.Vovveti1/HeaderRemovalDemo

# Add GitHub as remote
git remote add origin https://github.com/YOUR_USERNAME/HeaderRemovalDemo.git

# Push code
git push -u origin main
```

**Enter your GitHub credentials when prompted.**

---

### Step 4: Watch GitHub Actions Build Your APK

1. **Go to your repository** on GitHub
2. **Click the "Actions" tab** at the top
3. You should see a workflow running: "Build Android APK"
4. **Click on the workflow** to watch it build
5. **Wait 3-5 minutes** for the build to complete

---

### Step 5: Download Your APK

Once the build is complete (green checkmark ✅):

1. **Click on the completed workflow run**
2. **Scroll down** to "Artifacts" section
3. **Click "app-debug"** to download
4. You'll get a `.zip` file
5. **Unzip it** to get `app-debug.apk`

---

### Step 6: Install APK on Android Phone

#### Transfer APK to Your Phone:

**Option A: Email**
- Email the APK to yourself
- Open email on your Android phone
- Download the APK

**Option B: Cloud Storage**
- Upload to Google Drive / Dropbox
- Open on phone and download

**Option C: USB Cable**
- Connect phone to Mac
- Transfer APK file using Android File Transfer

#### Install APK:

1. **Open the APK file** on your Android phone
2. **Allow "Install from Unknown Sources"** (if prompted)
   - Settings → Security → Unknown Sources → Enable
   - Or: Settings → Apps → Special Access → Install Unknown Apps → Enable for your file manager
3. **Tap "Install"**
4. **Open the app** and test!

---

## Troubleshooting

### Git not installed

Install Homebrew, then install git:
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
brew install git
```

### GitHub Actions build fails

1. Go to Actions tab
2. Click on failed workflow
3. Check the error logs
4. Common issues:
   - Missing gradlew file (already included)
   - Missing wrapper files (already included)
   - Syntax errors in gradle files

### Can't find "Actions" tab

- Make sure your repository is Public (not Private)
- GitHub Actions is free for public repositories
- For private repos, you need a paid plan

### Authentication issues when pushing

Use a Personal Access Token instead of password:

1. Go to: GitHub → Settings → Developer Settings → Personal Access Tokens → Tokens (classic)
2. Generate new token
3. Select scopes: `repo` (all)
4. Use token as password when pushing

---

## How GitHub Actions Works

1. **You push code** to GitHub
2. **GitHub Actions detects** the push
3. **Cloud server starts** (Ubuntu Linux)
4. **Installs Java & Android SDK** automatically
5. **Runs gradle build** to create APK
6. **Uploads APK** as artifact for download
7. **All this happens in the cloud** - no local setup needed!

---

## Manual Trigger

You can also manually trigger a build:

1. Go to repository → Actions tab
2. Click "Build Android APK" workflow
3. Click "Run workflow" button
4. Select branch: main
5. Click green "Run workflow" button

---

## Automatic Builds

Every time you push code to GitHub, it will automatically:
- Build a new APK
- Make it available for download
- Show build status (success/failure)

---

## Advanced: Create a Release

To create a permanent download link:

1. Go to repository → Releases
2. Click "Create a new release"
3. Tag version: `v1.0.0`
4. Release title: "Initial Release"
5. Upload the `app-debug.apk` file
6. Click "Publish release"

Now anyone can download your APK from the releases page!

---

## Need Help?

Common commands:

```bash
# Check git status
git status

# View commit history
git log --oneline

# View remote URL
git remote -v

# Force push (if needed)
git push -f origin main

# Create new commit after changes
git add .
git commit -m "Update: description of changes"
git push
```

---

## Summary

1. Run `./setup-github.sh` to initialize git
2. Create repository on GitHub.com
3. Push code: `git push -u origin main`
4. Wait for Actions to build APK
5. Download APK from Artifacts
6. Install on Android phone
7. Test the app!

**No Android Studio needed! Everything builds in the cloud! 🚀**
