# GitHub Enterprise - APK Build Setup

This guide is optimized for GitHub Enterprise accounts.

## Benefits of Enterprise Account

✅ **Private repositories** with GitHub Actions included
✅ **More build minutes** and concurrent jobs
✅ **Better security** and compliance features
✅ **Self-hosted runners** option (if needed)
✅ **Advanced admin controls**

---

## Quick Setup for Enterprise

### Step 1: Initialize Git Repository

Open Terminal and run:

```bash
cd /Users/Pavan.Vovveti1/HeaderRemovalDemo
chmod +x setup-github.sh
./setup-github.sh
```

### Step 2: Create Repository (Enterprise)

**Option A: Personal Repository**
1. Go to your GitHub Enterprise instance
2. Click "+" → "New repository"
3. Name: `HeaderRemovalDemo`
4. Choose **Private** or **Public** (both work with Enterprise)
5. DON'T initialize with README
6. Click "Create repository"

**Option B: Organization Repository**
1. Go to your Organization
2. Repositories → "New repository"
3. Owner: Select your organization
4. Name: `HeaderRemovalDemo`
5. Choose visibility (Private recommended)
6. Click "Create repository"

### Step 3: Push to Enterprise GitHub

Your enterprise URL might look like:
- `https://github.yourcompany.com`
- `https://github.enterprise.yourcompany.com`

Replace the URL below with your actual enterprise URL and username:

```bash
cd /Users/Pavan.Vovveti1/HeaderRemovalDemo

# Add remote (use your enterprise URL)
git remote add origin https://github.yourcompany.com/YOUR_USERNAME/HeaderRemovalDemo.git

# Or for organization:
# git remote add origin https://github.yourcompany.com/YOUR_ORG/HeaderRemovalDemo.git

# Push code
git push -u origin main
```

### Step 4: Enable GitHub Actions (if needed)

Some enterprise instances require admin approval:

1. Go to repository → **Settings**
2. Click **Actions** → **General**
3. Ensure "Allow all actions and reusable workflows" is selected
4. If disabled, contact your GitHub admin

---

## Enterprise-Specific Considerations

### 1. Self-Hosted Runners (Optional)

If your enterprise uses self-hosted runners, update the workflow:

```yaml
# .github/workflows/build-apk.yml
jobs:
  build:
    runs-on: [self-hosted, android]  # Use your runner labels
```

### 2. Private Repository Benefits

With enterprise private repos:
- Your code stays confidential
- Actions work the same as public
- No build minute limits (usually)
- Better security compliance

### 3. Authentication

**Use SSH (Recommended for Enterprise)**

```bash
# Generate SSH key (if you don't have one)
ssh-keygen -t ed25519 -C "your.email@company.com"

# Copy public key
cat ~/.ssh/id_ed25519.pub

# Add to GitHub Enterprise:
# Settings → SSH and GPG keys → New SSH key

# Use SSH URL instead:
git remote add origin git@github.yourcompany.com:YOUR_USERNAME/HeaderRemovalDemo.git
git push -u origin main
```

**Use Personal Access Token**

```bash
# Create token:
# GitHub Enterprise → Settings → Developer settings → Personal access tokens

# Use token when pushing:
git remote add origin https://YOUR_TOKEN@github.yourcompany.com/YOUR_USERNAME/HeaderRemovalDemo.git
git push -u origin main
```

### 4. Enterprise Proxy Settings

If your enterprise uses a proxy:

```bash
# Configure git proxy
git config --global http.proxy http://proxy.company.com:8080
git config --global https.proxy https://proxy.company.com:8080

# Or set environment variables
export HTTP_PROXY=http://proxy.company.com:8080
export HTTPS_PROXY=https://proxy.company.com:8080
```

---

## Download APK (Enterprise)

After pushing:

1. Go to your repository
2. **Actions** tab
3. Click on workflow run
4. Wait for green checkmark ✅ (3-5 minutes)
5. Scroll to **Artifacts** section
6. Click **"app-debug"** to download
7. Unzip to get `app-debug.apk`

---

## Enterprise Workflow Features

### Advanced Build Configuration

The workflow can be customized for enterprise needs:

```yaml
# .github/workflows/build-apk.yml
jobs:
  build:
    runs-on: ubuntu-latest
    timeout-minutes: 30  # Enterprise can have higher limits

    steps:
    - name: Checkout code
      uses: actions/checkout@v3
      with:
        token: ${{ secrets.ENTERPRISE_PAT }}  # If needed

    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'

    - name: Build APK
      run: ./gradlew assembleDebug
      env:
        # Add enterprise-specific env vars if needed
        COMPANY_PROXY: ${{ secrets.COMPANY_PROXY }}
```

### Notifications

Add Slack/Teams notifications (if your enterprise uses them):

```yaml
- name: Notify on success
  if: success()
  uses: slackapi/slack-github-action@v1
  with:
    webhook-url: ${{ secrets.SLACK_WEBHOOK }}
```

---

## Security Best Practices for Enterprise

### 1. Code Scanning

Enable security features:
- Settings → Security → Code scanning
- Enable Dependabot alerts
- Enable Secret scanning

### 2. Branch Protection

Protect main branch:
- Settings → Branches → Add rule
- Require pull request reviews
- Require status checks to pass

### 3. Secrets Management

Store sensitive data in secrets:
- Settings → Secrets and variables → Actions
- Add signing keys, tokens, etc.

---

## Troubleshooting Enterprise

### Actions Not Running

Check with your admin if:
- Actions are enabled for your organization
- Your repository has permissions
- Network/firewall rules allow GitHub Actions

### Build Failures

1. Check Actions logs for errors
2. Verify enterprise proxy settings
3. Ensure self-hosted runners (if used) have Android SDK

### Authentication Issues

- Use SSH keys for easier authentication
- Or use Personal Access Token with repo scope
- Check with IT if VPN is required

---

## Advanced: Signing APKs for Release

For production builds with enterprise:

1. **Generate signing keys**:
```bash
keytool -genkey -v -keystore release.keystore -alias app -keyalg RSA -keysize 2048 -validity 10000
```

2. **Add secrets to GitHub**:
   - `KEYSTORE_FILE` (base64 encoded)
   - `KEYSTORE_PASSWORD`
   - `KEY_ALIAS`
   - `KEY_PASSWORD`

3. **Update workflow** to sign APK

---

## Summary for Enterprise Users

### Advantages:
✅ Private repositories with Actions
✅ More build resources
✅ Better security controls
✅ Compliance features
✅ Organization management

### Quick Commands:

```bash
# Initialize
cd /Users/Pavan.Vovveti1/HeaderRemovalDemo
./setup-github.sh

# Push to enterprise (replace URL)
git remote add origin https://github.yourcompany.com/YOUR_USERNAME/HeaderRemovalDemo.git
git push -u origin main

# Watch build
# Go to: https://github.yourcompany.com/YOUR_USERNAME/HeaderRemovalDemo/actions

# Download APK from Artifacts section
```

---

## Need Help?

Contact your GitHub Enterprise administrator for:
- Enabling Actions
- Increasing build minutes
- Setting up self-hosted runners
- Network/proxy configuration
- Security policies

**Everything else works the same as standard GitHub!** 🚀
