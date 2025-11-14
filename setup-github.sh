#!/bin/bash

echo "================================"
echo "GitHub Setup Script"
echo "================================"
echo ""

cd /Users/Pavan.Vovveti1/HeaderRemovalDemo

# Initialize git repository
echo "📦 Initializing Git repository..."
git init

# Add all files
echo "📝 Adding files to Git..."
git add .

# Create initial commit
echo "💾 Creating initial commit..."
git commit -m "Initial commit: Android Header Removal Demo

- WebView header interception
- X-Requested-With removal functionality
- GitHub Actions build workflow
- Complete project structure"

# Set main branch
echo "🌿 Setting main branch..."
git branch -M main

echo ""
echo "✅ Git repository initialized!"
echo ""
echo "================================"
echo "Next Steps:"
echo "================================"
echo ""
echo "1. Go to GitHub.com and create a new repository"
echo "   - Repository name: HeaderRemovalDemo"
echo "   - Make it Public (so GitHub Actions works for free)"
echo "   - DON'T initialize with README"
echo ""
echo "2. Copy the repository URL (it will look like):"
echo "   https://github.com/YOUR_USERNAME/HeaderRemovalDemo.git"
echo ""
echo "3. Run these commands (replace YOUR_USERNAME):"
echo ""
echo "   git remote add origin https://github.com/YOUR_USERNAME/HeaderRemovalDemo.git"
echo "   git push -u origin main"
echo ""
echo "4. GitHub Actions will automatically:"
echo "   - Build your APK"
echo "   - Make it available for download"
echo ""
echo "================================"
