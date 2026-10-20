package com.example.data.templates

data class TemplateInfo(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val color: Long,
    val htmlContent: String,
    val cssContent: String,
    val jsContent: String
)

object ProjectTemplates {

    val BLANK = TemplateInfo(
        id = "blank",
        name = "Blank Starter",
        description = "Minimal clean canvas with mobile viewport meta and starter styles",
        category = "Starter",
        color = 0xFF00D2FF,
        htmlContent = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Mobile UI Starter</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="screen-container">
    <header class="app-header">
      <div class="status-indicator"></div>
      <h1 class="title">Mobile UI Studio</h1>
      <p class="subtitle">Your creative canvas awaits</p>
    </header>

    <main class="content-body">
      <div class="card">
        <div class="icon-circle">⚡</div>
        <h2>Ready to build?</h2>
        <p>Edit <code>index.html</code>, <code>style.css</code>, and <code>script.js</code> in the editor to customize this screen.</p>
        <button id="actionBtn" class="btn primary-btn">Click Me</button>
      </div>

      <div id="resultBox" class="result-box hidden">
        <p id="resultText">Hello from JavaScript!</p>
      </div>
    </main>
  </div>
  <script src="script.js"></script>
</body>
</html>""",
        cssContent = """* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
  -webkit-tap-highlight-color: transparent;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
  background-color: #0d1117;
  color: #f0f6fc;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.screen-container {
  max-width: 440px;
  margin: 0 auto;
  width: 100%;
  padding: 24px 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.app-header {
  margin-top: 16px;
  margin-bottom: 24px;
}

.status-indicator {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #00d2ff;
  box-shadow: 0 0 12px #00d2ff;
  margin-bottom: 12px;
}

.title {
  font-size: 26px;
  font-weight: 700;
  letter-spacing: -0.5px;
  color: #ffffff;
}

.subtitle {
  font-size: 14px;
  color: #8b949e;
  margin-top: 4px;
}

.card {
  background: #161b22;
  border: 1px solid #30363d;
  border-radius: 20px;
  padding: 24px;
  text-align: center;
  box-shadow: 0 10px 24px rgba(0,0,0,0.4);
}

.icon-circle {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: rgba(0, 210, 255, 0.1);
  color: #00d2ff;
  font-size: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px auto;
}

.card h2 {
  font-size: 20px;
  margin-bottom: 8px;
}

.card p {
  font-size: 14px;
  color: #8b949e;
  line-height: 1.5;
  margin-bottom: 20px;
}

code {
  background: #21262d;
  color: #58a6ff;
  padding: 2px 6px;
  border-radius: 6px;
  font-family: monospace;
}

.btn {
  width: 100%;
  padding: 14px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.primary-btn {
  background: linear-gradient(135deg, #00d2ff 0%, #0077ff 100%);
  color: #ffffff;
  box-shadow: 0 4px 14px rgba(0, 119, 255, 0.4);
}

.primary-btn:active {
  transform: scale(0.98);
}

.result-box {
  margin-top: 20px;
  background: #1c2128;
  border-left: 4px solid #00d2ff;
  padding: 16px;
  border-radius: 8px;
}

.hidden {
  display: none;
}
""",
        jsContent = """let count = 0;
const btn = document.getElementById('actionBtn');
const resultBox = document.getElementById('resultBox');
const resultText = document.getElementById('resultText');

btn.addEventListener('click', () => {
  count++;
  resultBox.classList.remove('hidden');
  resultText.textContent = 'Button tapped ' + count + (count === 1 ? ' time!' : ' times!');
  console.log('Action button pressed. Count: ' + count);
});
"""
    )

    val LOGIN = TemplateInfo(
        id = "login",
        name = "Login Screen",
        description = "Modern authentication UI with glassmorphic styling, inputs, and validation",
        category = "Auth",
        color = 0xFFA78BFA,
        htmlContent = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Login Screen</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="auth-wrapper">
    <div class="brand-header">
      <div class="logo-mark">⚡</div>
      <h1 class="brand-title">Welcome Back</h1>
      <p class="brand-desc">Sign in to your account to continue</p>
    </div>

    <form id="loginForm" class="auth-form" onsubmit="return false;">
      <div class="form-group">
        <label for="email">Email Address</label>
        <div class="input-container">
          <input type="email" id="email" placeholder="name@example.com" required autocomplete="email">
        </div>
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <div class="input-container">
          <input type="password" id="password" placeholder="••••••••" required>
          <button type="button" id="togglePassword" class="icon-toggle">👁</button>
        </div>
      </div>

      <div class="form-extras">
        <label class="remember-label">
          <input type="checkbox" id="rememberMe" checked>
          <span>Remember me</span>
        </label>
        <a href="#" class="forgot-link" id="forgotBtn">Forgot?</a>
      </div>

      <button type="submit" id="submitBtn" class="submit-btn">
        <span id="btnText">Sign In</span>
      </button>
    </form>

    <div class="divider">
      <span>or continue with</span>
    </div>

    <div class="social-actions">
      <button class="social-btn" onclick="socialLogin('Google')">Google</button>
      <button class="social-btn" onclick="socialLogin('Apple')">Apple</button>
      <button class="social-btn" onclick="socialLogin('GitHub')">GitHub</button>
    </div>

    <div id="toast" class="toast hidden"></div>
  </div>
  <script src="script.js"></script>
</body>
</html>""",
        cssContent = """* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
  -webkit-tap-highlight-color: transparent;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
  background-color: #0b0e14;
  color: #f3f4f6;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px 16px;
}

.auth-wrapper {
  width: 100%;
  max-width: 400px;
  background: #141923;
  border: 1px solid #232c3d;
  border-radius: 28px;
  padding: 32px 24px;
  box-shadow: 0 16px 36px rgba(0, 0, 0, 0.4);
}

.brand-header {
  text-align: center;
  margin-bottom: 28px;
}

.logo-mark {
  width: 52px;
  height: 52px;
  background: linear-gradient(135deg, #a78bfa, #6366f1);
  color: white;
  font-size: 24px;
  font-weight: bold;
  border-radius: 16px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.35);
}

.brand-title {
  font-size: 24px;
  font-weight: 700;
  color: #ffffff;
}

.brand-desc {
  font-size: 13px;
  color: #94a3b8;
  margin-top: 6px;
}

.form-group {
  margin-bottom: 18px;
}

.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 500;
  color: #cbd5e1;
  margin-bottom: 6px;
}

.input-container {
  position: relative;
  display: flex;
  align-items: center;
}

.input-container input {
  width: 100%;
  background: #0d111a;
  border: 1px solid #283347;
  border-radius: 14px;
  padding: 14px 16px;
  color: #ffffff;
  font-size: 15px;
  outline: none;
  transition: all 0.2s ease;
}

.input-container input:focus {
  border-color: #818cf8;
  box-shadow: 0 0 0 3px rgba(129, 140, 248, 0.2);
}

.icon-toggle {
  position: absolute;
  right: 14px;
  background: none;
  border: none;
  color: #94a3b8;
  font-size: 16px;
  cursor: pointer;
}

.form-extras {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 22px;
  font-size: 13px;
}

.remember-label {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #94a3b8;
  cursor: pointer;
}

.forgot-link {
  color: #818cf8;
  text-decoration: none;
  font-weight: 500;
}

.submit-btn {
  width: 100%;
  padding: 15px;
  border-radius: 14px;
  background: linear-gradient(135deg, #818cf8 0%, #6366f1 100%);
  color: white;
  border: none;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 6px 18px rgba(99, 102, 241, 0.35);
  transition: all 0.2s;
}

.submit-btn:active {
  transform: scale(0.98);
}

.divider {
  display: flex;
  align-items: center;
  text-align: center;
  margin: 24px 0 18px 0;
  color: #64748b;
  font-size: 12px;
}

.divider::before, .divider::after {
  content: '';
  flex: 1;
  border-bottom: 1px solid #232c3d;
}

.divider span {
  padding: 0 12px;
}

.social-actions {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 10px;
}

.social-btn {
  background: #0d111a;
  border: 1px solid #283347;
  color: #e2e8f0;
  padding: 10px;
  border-radius: 12px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
}

.toast {
  position: fixed;
  bottom: 24px;
  left: 50%;
  transform: translateX(-50%);
  background: #10b981;
  color: white;
  padding: 10px 20px;
  border-radius: 30px;
  font-size: 14px;
  font-weight: 500;
  box-shadow: 0 8px 24px rgba(0,0,0,0.4);
  transition: opacity 0.3s;
}
""",
        jsContent = """const form = document.getElementById('loginForm');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const toggleBtn = document.getElementById('togglePassword');
const toast = document.getElementById('toast');

toggleBtn.addEventListener('click', () => {
  const isPass = passwordInput.type === 'password';
  passwordInput.type = isPass ? 'text' : 'password';
  toggleBtn.textContent = isPass ? '🙈' : '👁';
});

function showToast(msg, isSuccess = true) {
  toast.textContent = msg;
  toast.style.background = isSuccess ? '#10b981' : '#ef4444';
  toast.classList.remove('hidden');
  setTimeout(() => toast.classList.add('hidden'), 3000);
}

form.addEventListener('submit', (e) => {
  e.preventDefault();
  const email = emailInput.value.trim();
  const pass = passwordInput.value;

  if (!email || !pass) {
    showToast('Please fill all fields', false);
    return;
  }

  showToast('Welcome back, ' + email.split('@')[0] + '!');
  console.log('Login successful for:', email);
});

function socialLogin(provider) {
  showToast('Logging in with ' + provider + '...');
  console.log('OAuth triggered: ' + provider);
}
"""
    )

    val DASHBOARD = TemplateInfo(
        id = "dashboard",
        name = "Fintech Dashboard",
        description = "Sleek wallet balance, weekly spending chart, and transactions list",
        category = "Dashboard",
        color = 0xFF10B981,
        htmlContent = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Wallet Dashboard</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="dash-container">
    <header class="dash-top">
      <div class="user-info">
        <div class="avatar">AM</div>
        <div>
          <p class="greeting">Welcome Back,</p>
          <h2 class="user-name">Alex Morgan</h2>
        </div>
      </div>
      <button class="icon-badge-btn" onclick="alert('No new notifications')">🔔</button>
    </header>

    <div class="balance-card">
      <div class="card-header">
        <span>Total Balance</span>
        <button id="toggleBalance" class="toggle-eye">👁</button>
      </div>
      <h1 id="balanceAmount" class="balance-text">$24,850.50</h1>
      <div class="card-stats">
        <div class="stat-pill positive">+12.4% this month</div>
        <div class="currency-tag">USD / Vault</div>
      </div>
    </div>

    <div class="action-grid">
      <button class="action-item" onclick="handleAction('Send')">
        <div class="action-icon">↗</div>
        <span>Send</span>
      </button>
      <button class="action-item" onclick="handleAction('Receive')">
        <div class="action-icon">↙</div>
        <span>Receive</span>
      </button>
      <button class="action-item" onclick="handleAction('Analytics')">
        <div class="action-icon">📊</div>
        <span>Analytics</span>
      </button>
      <button class="action-item" onclick="handleAction('Cards')">
        <div class="action-icon">💳</div>
        <span>Cards</span>
      </button>
    </div>

    <section class="chart-section">
      <div class="section-header">
        <h3>Weekly Activity</h3>
        <span class="chart-total">$1,420 spent</span>
      </div>
      <div class="bar-chart">
        <div class="bar-col"><div class="bar" style="height: 45%;"></div><span>M</span></div>
        <div class="bar-col"><div class="bar" style="height: 70%;"></div><span>T</span></div>
        <div class="bar-col"><div class="bar active" style="height: 90%;"></div><span>W</span></div>
        <div class="bar-col"><div class="bar" style="height: 60%;"></div><span>T</span></div>
        <div class="bar-col"><div class="bar" style="height: 80%;"></div><span>F</span></div>
        <div class="bar-col"><div class="bar" style="height: 35%;"></div><span>S</span></div>
        <div class="bar-col"><div class="bar" style="height: 50%;"></div><span>S</span></div>
      </div>
    </section>

    <section class="tx-section">
      <div class="section-header">
        <h3>Recent Transactions</h3>
        <a href="#" class="view-all">See All</a>
      </div>
      <div class="tx-list">
        <div class="tx-row">
          <div class="tx-icon sub">🎬</div>
          <div class="tx-details">
            <h4>Netflix Subscription</h4>
            <p>Today, 10:24 AM</p>
          </div>
          <div class="tx-amount negative">-$15.99</div>
        </div>
        <div class="tx-row">
          <div class="tx-icon transfer">💼</div>
          <div class="tx-details">
            <h4>Design Client Payout</h4>
            <p>Yesterday, 4:15 PM</p>
          </div>
          <div class="tx-amount positive">+$2,400.00</div>
        </div>
        <div class="tx-row">
          <div class="tx-icon coffee">☕</div>
          <div class="tx-details">
            <h4>Blue Bottle Coffee</h4>
            <p>May 18, 9:10 AM</p>
          </div>
          <div class="tx-amount negative">-$6.50</div>
        </div>
      </div>
    </section>
  </div>
  <script src="script.js"></script>
</body>
</html>""",
        cssContent = """* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
  -webkit-tap-highlight-color: transparent;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
  background-color: #0b0f19;
  color: #f1f5f9;
  min-height: 100vh;
  padding: 16px;
}

.dash-container {
  max-width: 440px;
  margin: 0 auto;
}

.dash-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: #3b82f6;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 15px;
}

.greeting {
  font-size: 12px;
  color: #94a3b8;
}

.user-name {
  font-size: 17px;
  font-weight: 600;
}

.icon-badge-btn {
  background: #1e293b;
  border: 1px solid #334155;
  border-radius: 12px;
  width: 42px;
  height: 42px;
  font-size: 18px;
  cursor: pointer;
}

.balance-card {
  background: linear-gradient(135deg, #10b981 0%, #047857 100%);
  border-radius: 24px;
  padding: 24px;
  color: white;
  box-shadow: 0 12px 28px rgba(16, 185, 129, 0.25);
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  opacity: 0.9;
}

.toggle-eye {
  background: none;
  border: none;
  color: white;
  font-size: 16px;
  cursor: pointer;
}

.balance-text {
  font-size: 34px;
  font-weight: 800;
  margin: 12px 0 16px 0;
  letter-spacing: -0.5px;
}

.card-stats {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-pill {
  background: rgba(255, 255, 255, 0.2);
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.currency-tag {
  font-size: 12px;
  opacity: 0.8;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-bottom: 22px;
}

.action-item {
  background: #161f30;
  border: 1px solid #243147;
  border-radius: 16px;
  padding: 12px 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: #e2e8f0;
  cursor: pointer;
}

.action-icon {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  background: #243147;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
}

.action-item span {
  font-size: 12px;
  font-weight: 500;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.section-header h3 {
  font-size: 16px;
  font-weight: 600;
}

.chart-total, .view-all {
  font-size: 13px;
  color: #10b981;
  text-decoration: none;
}

.chart-section {
  background: #141c2b;
  border: 1px solid #233047;
  border-radius: 20px;
  padding: 18px;
  margin-bottom: 20px;
}

.bar-chart {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  height: 120px;
  padding-top: 10px;
}

.bar-col {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.bar {
  width: 14px;
  background: #243147;
  border-radius: 6px;
  transition: all 0.3s;
}

.bar.active {
  background: #10b981;
  box-shadow: 0 0 12px rgba(16, 185, 129, 0.4);
}

.bar-col span {
  font-size: 11px;
  color: #64748b;
}

.tx-section {
  background: #141c2b;
  border: 1px solid #233047;
  border-radius: 20px;
  padding: 18px;
}

.tx-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.tx-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.tx-icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  background: #1f2b3e;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.tx-details {
  flex: 1;
}

.tx-details h4 {
  font-size: 14px;
  font-weight: 600;
}

.tx-details p {
  font-size: 12px;
  color: #64748b;
}

.tx-amount {
  font-size: 14px;
  font-weight: 700;
}

.tx-amount.positive {
  color: #10b981;
}

.tx-amount.negative {
  color: #ef4444;
}
""",
        jsContent = """let isHidden = false;
const originalBalance = "$24,850.50";
const balanceEl = document.getElementById('balanceAmount');
const toggleBtn = document.getElementById('toggleBalance');

toggleBtn.addEventListener('click', () => {
  isHidden = !isHidden;
  balanceEl.textContent = isHidden ? "••••••••" : originalBalance;
  toggleBtn.textContent = isHidden ? "🙈" : "👁";
  console.log('Balance visibility toggled:', !isHidden);
});

function handleAction(name) {
  console.log('Action triggered: ' + name);
  alert(name + ' flow initiated!');
}

document.querySelectorAll('.bar-col').forEach(col => {
  col.addEventListener('click', () => {
    document.querySelectorAll('.bar').forEach(b => b.classList.remove('active'));
    col.querySelector('.bar').classList.add('active');
    console.log('Selected day:', col.querySelector('span').textContent);
  });
});
"""
    )

    val PROFILE = TemplateInfo(
        id = "profile",
        name = "Profile Screen",
        description = "User profile header, stats counters, interactive tabs, and settings toggles",
        category = "Social",
        color = 0xFFF59E0B,
        htmlContent = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>User Profile</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="profile-page">
    <div class="banner"></div>
    <div class="profile-card">
      <div class="avatar-row">
        <div class="avatar-wrap">
          <div class="avatar">EV</div>
          <span class="online-dot"></span>
        </div>
        <div class="profile-actions">
          <button class="btn follow-btn" id="followBtn">Follow</button>
          <button class="btn icon-btn" onclick="alert('Message window opened')">💬</button>
        </div>
      </div>

      <div class="bio-section">
        <h2 class="name">Elena Vance <span class="badge">PRO</span></h2>
        <p class="handle">@elenavance • San Francisco, CA</p>
        <p class="bio">Senior Mobile UI Designer & Design Systems Architect. Creating smooth interactive mobile prototypes.</p>
      </div>

      <div class="stats-bar">
        <div class="stat-item">
          <span class="stat-num" id="projCount">48</span>
          <span class="stat-label">Projects</span>
        </div>
        <div class="stat-item">
          <span class="stat-num" id="followersCount">14.2k</span>
          <span class="stat-label">Followers</span>
        </div>
        <div class="stat-item">
          <span class="stat-num">3.9k</span>
          <span class="stat-label">Stars</span>
        </div>
      </div>

      <div class="settings-list">
        <h3>Preferences</h3>
        <div class="setting-item">
          <span>Dark Mode</span>
          <label class="switch">
            <input type="checkbox" id="darkModeToggle" checked>
            <span class="slider"></span>
          </label>
        </div>
        <div class="setting-item">
          <span>Push Notifications</span>
          <label class="switch">
            <input type="checkbox" checked>
            <span class="slider"></span>
          </label>
        </div>
        <div class="setting-item">
          <span>Biometric Login</span>
          <label class="switch">
            <input type="checkbox">
            <span class="slider"></span>
          </label>
        </div>
      </div>
    </div>
  </div>
  <script src="script.js"></script>
</body>
</html>""",
        cssContent = """* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
  -webkit-tap-highlight-color: transparent;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
  background-color: #0c1017;
  color: #f1f5f9;
  min-height: 100vh;
}

body.light-mode {
  background-color: #f1f5f9;
  color: #0f172a;
}

body.light-mode .profile-card {
  background: #ffffff;
  border-color: #e2e8f0;
}

body.light-mode .handle, body.light-mode .stat-label {
  color: #64748b;
}

.banner {
  height: 110px;
  background: linear-gradient(135deg, #f59e0b 0%, #d97706 100%);
}

.profile-card {
  margin: -40px 16px 24px 16px;
  background: #151b26;
  border: 1px solid #273142;
  border-radius: 24px;
  padding: 20px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.4);
}

.avatar-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 16px;
}

.avatar-wrap {
  position: relative;
  width: 76px;
  height: 76px;
}

.avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: #f59e0b;
  color: #000;
  font-size: 26px;
  font-weight: 800;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 4px solid #151b26;
}

.online-dot {
  position: absolute;
  bottom: 4px;
  right: 4px;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: #10b981;
  border: 2px solid #151b26;
}

.profile-actions {
  display: flex;
  gap: 8px;
}

.btn {
  padding: 10px 18px;
  border-radius: 12px;
  font-weight: 600;
  font-size: 14px;
  border: none;
  cursor: pointer;
}

.follow-btn {
  background: #f59e0b;
  color: #000000;
}

.icon-btn {
  background: #232b3b;
  color: white;
}

.bio-section {
  margin-bottom: 20px;
}

.name {
  font-size: 20px;
  font-weight: 700;
}

.badge {
  font-size: 11px;
  background: #f59e0b;
  color: black;
  padding: 2px 6px;
  border-radius: 6px;
  vertical-align: middle;
}

.handle {
  font-size: 13px;
  color: #94a3b8;
  margin: 4px 0 12px 0;
}

.bio {
  font-size: 14px;
  line-height: 1.5;
  color: #cbd5e1;
}

.stats-bar {
  display: flex;
  justify-content: space-around;
  background: #0d121a;
  border-radius: 16px;
  padding: 14px;
  margin-bottom: 24px;
}

.stat-item {
  text-align: center;
}

.stat-num {
  display: block;
  font-size: 18px;
  font-weight: 700;
}

.stat-label {
  font-size: 12px;
  color: #94a3b8;
}

.settings-list h3 {
  font-size: 15px;
  margin-bottom: 12px;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #232b3b;
  font-size: 14px;
}

.switch {
  position: relative;
  display: inline-block;
  width: 44px;
  height: 24px;
}

.switch input { opacity: 0; width: 0; height: 0; }

.slider {
  position: absolute;
  cursor: pointer;
  top: 0; left: 0; right: 0; bottom: 0;
  background-color: #334155;
  transition: .3s;
  border-radius: 24px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 18px;
  width: 18px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: .3s;
  border-radius: 50%;
}

input:checked + .slider {
  background-color: #f59e0b;
}

input:checked + .slider:before {
  transform: translateX(20px);
}
""",
        jsContent = """let isFollowing = false;
const followBtn = document.getElementById('followBtn');
const followersEl = document.getElementById('followersCount');
const darkToggle = document.getElementById('darkModeToggle');

followBtn.addEventListener('click', () => {
  isFollowing = !isFollowing;
  followBtn.textContent = isFollowing ? 'Following' : 'Follow';
  followBtn.style.background = isFollowing ? '#232b3b' : '#f59e0b';
  followBtn.style.color = isFollowing ? '#ffffff' : '#000000';
  followersEl.textContent = isFollowing ? '14.3k' : '14.2k';
  console.log('Follow status changed:', isFollowing);
});

darkToggle.addEventListener('change', (e) => {
  if (e.target.checked) {
    document.body.classList.remove('light-mode');
  } else {
    document.body.classList.add('light-mode');
  }
  console.log('Dark mode active:', e.target.checked);
});
"""
    )

    val NAVIGATION = TemplateInfo(
        id = "navigation",
        name = "Mobile App Shell",
        description = "Store UI with search, category pills, product cards, heart toggle, and bottom navigation",
        category = "Navigation",
        color = 0xFF38BDF8,
        htmlContent = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>App Shell & Store</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="shell">
    <header class="top-bar">
      <div class="search-box">
        <span>🔍</span>
        <input type="text" placeholder="Search templates, icons..." id="searchInput">
      </div>
      <button class="cart-btn" onclick="alert('Cart view opened')">
        🛒<span class="badge" id="cartCount">0</span>
      </button>
    </header>

    <div class="pills-scroll">
      <button class="pill active" onclick="filterTab(this, 'All')">All</button>
      <button class="pill" onclick="filterTab(this, 'Mobile')">Mobile</button>
      <button class="pill" onclick="filterTab(this, 'UI Kits')">UI Kits</button>
      <button class="pill" onclick="filterTab(this, 'Widgets')">Widgets</button>
      <button class="pill" onclick="filterTab(this, '3D Icons')">3D Icons</button>
    </div>

    <main class="product-grid" id="productGrid">
      <div class="item-card">
        <div class="item-thumb" style="background: linear-gradient(135deg, #6366f1, #3b82f6)">📱</div>
        <div class="item-body">
          <div class="item-title-row">
            <h4>iOS Design Kit</h4>
            <button class="heart-btn" onclick="toggleHeart(this)">🤍</button>
          </div>
          <p class="item-desc">60+ native screens & components</p>
          <div class="item-footer">
            <span class="price">$29</span>
            <button class="add-btn" onclick="addToCart('iOS Design Kit')">+ Add</button>
          </div>
        </div>
      </div>

      <div class="item-card">
        <div class="item-thumb" style="background: linear-gradient(135deg, #10b981, #06b6d4)">📊</div>
        <div class="item-body">
          <div class="item-title-row">
            <h4>Finance Analytics</h4>
            <button class="heart-btn" onclick="toggleHeart(this)">🤍</button>
          </div>
          <p class="item-desc">Clean charts & wallet flows</p>
          <div class="item-footer">
            <span class="price">$19</span>
            <button class="add-btn" onclick="addToCart('Finance Analytics')">+ Add</button>
          </div>
        </div>
      </div>
    </main>

    <nav class="bottom-nav">
      <button class="nav-tab active" onclick="switchNav(this, 'Home')">
        <span>🏠</span>
        <label>Home</label>
      </button>
      <button class="nav-tab" onclick="switchNav(this, 'Explore')">
        <span>🧭</span>
        <label>Explore</label>
      </button>
      <button class="nav-tab" onclick="switchNav(this, 'Saved')">
        <span>❤️</span>
        <label>Saved</label>
      </button>
      <button class="nav-tab" onclick="switchNav(this, 'Account')">
        <span>👤</span>
        <label>Account</label>
      </button>
    </nav>
  </div>
  <script src="script.js"></script>
</body>
</html>""",
        cssContent = """* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
  -webkit-tap-highlight-color: transparent;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
  background-color: #0b0f17;
  color: #f1f5f9;
  min-height: 100vh;
}

.shell {
  max-width: 440px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  padding-bottom: 72px;
}

.top-bar {
  display: flex;
  gap: 12px;
  padding: 16px;
  align-items: center;
}

.search-box {
  flex: 1;
  background: #172030;
  border: 1px solid #28374f;
  border-radius: 14px;
  display: flex;
  align-items: center;
  padding: 10px 14px;
  gap: 8px;
}

.search-box input {
  background: none;
  border: none;
  color: white;
  font-size: 14px;
  outline: none;
  width: 100%;
}

.cart-btn {
  position: relative;
  width: 44px;
  height: 44px;
  background: #172030;
  border: 1px solid #28374f;
  border-radius: 14px;
  font-size: 18px;
  cursor: pointer;
}

.cart-btn .badge {
  position: absolute;
  top: -4px;
  right: -4px;
  background: #38bdf8;
  color: #000;
  font-size: 11px;
  font-weight: 700;
  border-radius: 10px;
  padding: 2px 6px;
}

.pills-scroll {
  display: flex;
  gap: 8px;
  padding: 0 16px 16px 16px;
  overflow-x: auto;
}

.pill {
  background: #172030;
  border: 1px solid #28374f;
  color: #94a3b8;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 500;
  white-space: nowrap;
  cursor: pointer;
}

.pill.active {
  background: #38bdf8;
  color: #0b0f17;
  font-weight: 600;
  border-color: #38bdf8;
}

.product-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
  padding: 0 16px;
}

.item-card {
  background: #141b27;
  border: 1px solid #222d3d;
  border-radius: 20px;
  overflow: hidden;
}

.item-thumb {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48px;
}

.item-body {
  padding: 16px;
}

.item-title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.heart-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
}

.item-desc {
  font-size: 13px;
  color: #8b949e;
  margin: 6px 0 14px 0;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 18px;
  font-weight: 700;
  color: #38bdf8;
}

.add-btn {
  background: #243247;
  border: none;
  color: white;
  padding: 8px 14px;
  border-radius: 10px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #0f1523;
  border-top: 1px solid #202b3d;
  display: flex;
  justify-content: space-around;
  padding: 10px 0;
}

.nav-tab {
  background: none;
  border: none;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  color: #64748b;
  cursor: pointer;
}

.nav-tab span {
  font-size: 18px;
}

.nav-tab label {
  font-size: 11px;
}

.nav-tab.active {
  color: #38bdf8;
}
""",
        jsContent = """let cart = 0;
const cartCount = document.getElementById('cartCount');

function addToCart(title) {
  cart++;
  cartCount.textContent = cart;
  console.log('Added to cart: ' + title + ', total items: ' + cart);
}

function toggleHeart(btn) {
  const isFilled = btn.textContent === '❤️';
  btn.textContent = isFilled ? '🤍' : '❤️';
  console.log('Toggled favorite');
}

function filterTab(pill, category) {
  document.querySelectorAll('.pill').forEach(p => p.classList.remove('active'));
  pill.classList.add('active');
  console.log('Filter category selected:', category);
}

function switchNav(tab, name) {
  document.querySelectorAll('.nav-tab').forEach(t => t.classList.remove('active'));
  tab.classList.add('active');
  console.log('Switched navigation tab to:', name);
}
"""
    )

    val ONBOARDING = TemplateInfo(
        id = "onboarding",
        name = "App Onboarding",
        description = "Multi-step onboarding flow with swipeable slides, indicators, and get started button",
        category = "Onboarding",
        color = 0xFFEC4899,
        htmlContent = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Onboarding</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="onboard-shell">
    <div class="slide-illustration" id="illustration">🚀</div>
    <div class="slide-content">
      <h2 id="slideTitle">Craft Without Limits</h2>
      <p id="slideDesc">Design and code interactive mobile user interfaces on the go with real-time preview.</p>
    </div>
    <div class="dots-indicator">
      <span class="dot active"></span>
      <span class="dot"></span>
      <span class="dot"></span>
    </div>
    <div class="onboard-actions">
      <button class="skip-btn" onclick="skip()">Skip</button>
      <button class="next-btn" id="nextBtn" onclick="nextSlide()">Next →</button>
    </div>
  </div>
  <script src="script.js"></script>
</body>
</html>""",
        cssContent = """* { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #07080C; color: #EEF1F8; min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 24px; }
.onboard-shell { max-width: 380px; width: 100%; text-align: center; }
.slide-illustration { width: 120px; height: 120px; margin: 0 auto 36px auto; border-radius: 36px; background: linear-gradient(135deg, rgba(99, 102, 241, 0.2), rgba(34, 211, 238, 0.2)); border: 1px solid rgba(255, 255, 255, 0.1); display: flex; align-items: center; justify-content: center; font-size: 54px; box-shadow: 0 16px 40px rgba(0,0,0,0.5); }
.slide-content h2 { font-size: 26px; font-weight: 700; margin-bottom: 12px; }
.slide-content p { font-size: 14px; color: #8B93A7; line-height: 1.6; min-height: 66px; }
.dots-indicator { display: flex; justify-content: center; gap: 8px; margin: 28px 0; }
.dot { width: 8px; height: 8px; border-radius: 4px; background: #151926; transition: all 0.3s; }
.dot.active { width: 24px; background: linear-gradient(90deg, #6366F1, #22D3EE); }
.onboard-actions { display: flex; justify-content: space-between; align-items: center; }
.skip-btn { background: none; border: none; color: #8B93A7; font-size: 14px; font-weight: 600; cursor: pointer; }
.next-btn { background: linear-gradient(135deg, #6366F1, #22D3EE); border: none; border-radius: 12px; padding: 12px 24px; color: white; font-size: 14px; font-weight: 600; cursor: pointer; box-shadow: 0 4px 16px rgba(99, 102, 241, 0.4); }""",
        jsContent = """let current = 0;
const slides = [
  { icon: '🚀', title: 'Craft Without Limits', desc: 'Design and code interactive mobile user interfaces on the go with real-time preview.' },
  { icon: '🎨', title: 'Design System Tokens', desc: 'Inspect elements, modify variables live, and export production-ready HTML and CSS.' },
  { icon: '📦', title: '100% Offline & Yours', desc: 'No accounts, zero telemetry. Save projects locally and export ZIP files anytime.' }
];
function nextSlide() {
  current = (current + 1) % slides.length;
  updateUI();
}
function skip() { current = slides.length - 1; updateUI(); }
function updateUI() {
  document.getElementById('illustration').textContent = slides[current].icon;
  document.getElementById('slideTitle').textContent = slides[current].title;
  document.getElementById('slideDesc').textContent = slides[current].desc;
  document.querySelectorAll('.dot').forEach((d, i) => d.classList.toggle('active', i === current));
  document.getElementById('nextBtn').textContent = (current === slides.length - 1) ? 'Get Started' : 'Next →';
}"""
    )

    val FOOD_DELIVERY = TemplateInfo(
        id = "food",
        name = "Food Delivery",
        description = "Restaurant menu showcase with item cards, calorie tags, and sticky cart summary",
        category = "E-Commerce",
        color = 0xFFF97316,
        htmlContent = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Food Delivery</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="food-shell">
    <header class="food-top">
      <div>
        <p class="deliver-label">Deliver To</p>
        <h3 class="location">Downtown Studio, 5th Ave ▾</h3>
      </div>
      <div class="user-avatar">🍜</div>
    </header>

    <div class="search-bar">
      <span>🔍</span>
      <input type="text" placeholder="Search burgers, sushi, ramen..." />
    </div>

    <div class="food-list">
      <div class="food-card">
        <div class="food-img">🥑</div>
        <div class="food-info">
          <h4>Avocado Crunch Bowl</h4>
          <p>Quinoa, fresh avocado, edamame</p>
          <div class="food-meta">
            <span class="price">${'$'}14.50</span>
            <button class="add-btn" onclick="addToOrder('Avocado Bowl')">+ Add</button>
          </div>
        </div>
      </div>

      <div class="food-card">
        <div class="food-img">🍔</div>
        <div class="food-info">
          <h4>Truffle Wagyu Burger</h4>
          <p>Brioche bun, smoked gouda, truffle aioli</p>
          <div class="food-meta">
            <span class="price">${'$'}18.00</span>
            <button class="add-btn" onclick="addToOrder('Truffle Burger')">+ Add</button>
          </div>
        </div>
      </div>
    </div>

    <div class="cart-bar" id="cartBar">
      <div>
        <span id="itemCount">0 items</span> • <strong id="totalPrice">${'$'}0.00</strong>
      </div>
      <button class="checkout-btn" onclick="alert('Proceeding to checkout!')">View Cart →</button>
    </div>
  </div>
  <script src="script.js"></script>
</body>
</html>""",
        cssContent = """* { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #07080C; color: #EEF1F8; min-height: 100vh; padding: 16px; padding-bottom: 90px; }
.food-shell { max-width: 420px; margin: 0 auto; }
.food-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.deliver-label { font-size: 11px; color: #8B93A7; }
.location { font-size: 15px; font-weight: 700; color: #22D3EE; }
.user-avatar { width: 40px; height: 40px; border-radius: 12px; background: #151926; display: flex; align-items: center; justify-content: center; font-size: 20px; border: 1px solid rgba(255, 255, 255, 0.1); }
.search-bar { background: #0E1118; border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 14px; padding: 10px 14px; display: flex; gap: 8px; align-items: center; margin-bottom: 20px; }
.search-bar input { background: transparent; border: none; color: white; outline: none; width: 100%; font-size: 13px; }
.food-list { display: flex; flex-direction: column; gap: 14px; }
.food-card { background: #0E1118; border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 18px; padding: 14px; display: flex; gap: 14px; }
.food-img { width: 68px; height: 68px; border-radius: 14px; background: #151926; display: flex; align-items: center; justify-content: center; font-size: 32px; }
.food-info { flex: 1; }
.food-info h4 { font-size: 15px; font-weight: 600; margin-bottom: 4px; }
.food-info p { font-size: 12px; color: #8B93A7; margin-bottom: 8px; }
.food-meta { display: flex; justify-content: space-between; align-items: center; }
.price { font-size: 15px; font-weight: 700; color: #34D399; }
.add-btn { background: linear-gradient(135deg, #6366F1, #22D3EE); border: none; border-radius: 8px; padding: 6px 12px; color: white; font-weight: 600; font-size: 12px; cursor: pointer; }
.cart-bar { position: fixed; bottom: 16px; left: 16px; right: 16px; max-width: 388px; margin: 0 auto; background: #151926; border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 18px; padding: 12px 18px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 10px 30px rgba(0,0,0,0.6); }
.checkout-btn { background: #34D399; color: #07080C; border: none; border-radius: 10px; padding: 8px 14px; font-weight: 700; font-size: 12px; cursor: pointer; }""",
        jsContent = """let count = 0;
let total = 0.0;
function addToOrder(name) {
  count++;
  total += 15.0;
  document.getElementById('itemCount').textContent = count + ' item' + (count === 1 ? '' : 's');
  document.getElementById('totalPrice').textContent = '$' + total.toFixed(2);
  console.log('Added ' + name + ' to food order.');
}"""
    )

    val FITNESS = TemplateInfo(
        id = "fitness",
        name = "Fitness & Workout",
        description = "Activity rings, steps counter, workout cards, and calorie metrics",
        category = "Health",
        color = 0xFF10B981,
        htmlContent = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Fitness Tracker</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="fit-shell">
    <header class="fit-header">
      <h2>Activity Rings</h2>
      <span>Today, May 24</span>
    </header>

    <div class="ring-card">
      <div class="ring-circle">🔥 720 / 800 kcal</div>
      <div class="ring-stats">
        <div><strong>8,450</strong><span>Steps</span></div>
        <div><strong>48 min</strong><span>Exercise</span></div>
        <div><strong>11 hrs</strong><span>Stand</span></div>
      </div>
    </div>

    <h3>Today's Workouts</h3>
    <div class="workout-item">
      <div class="wo-icon">🏃‍♂️</div>
      <div class="wo-info">
        <h4>Outdoor Run</h4>
        <p>5.24 km • 28 mins • Pace 5'20"</p>
      </div>
      <span class="wo-badge">Done</span>
    </div>

    <div class="workout-item">
      <div class="wo-icon">🧘‍♀️</div>
      <div class="wo-info">
        <h4>Evening Core Yoga</h4>
        <p>20 mins • Strength & flexibility</p>
      </div>
      <button class="start-btn" onclick="startWorkout()">Start</button>
    </div>
  </div>
  <script src="script.js"></script>
</body>
</html>""",
        cssContent = """* { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #07080C; color: #EEF1F8; min-height: 100vh; padding: 18px; }
.fit-shell { max-width: 420px; margin: 0 auto; }
.fit-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.fit-header h2 { font-size: 20px; font-weight: 700; }
.fit-header span { font-size: 12px; color: #8B93A7; }
.ring-card { background: #0E1118; border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 20px; padding: 22px; text-align: center; margin-bottom: 24px; }
.ring-circle { width: 140px; height: 140px; border-radius: 50%; border: 6px solid #22D3EE; margin: 0 auto 18px auto; display: flex; align-items: center; justify-content: center; font-size: 15px; font-weight: 700; box-shadow: 0 0 20px rgba(34, 211, 238, 0.25); }
.ring-stats { display: flex; justify-content: space-around; border-top: 1px solid rgba(255, 255, 255, 0.06); padding-top: 14px; }
.ring-stats div strong { display: block; font-size: 16px; color: #34D399; }
.ring-stats div span { font-size: 11px; color: #8B93A7; }
.workout-item { background: #0E1118; border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 16px; padding: 14px; display: flex; align-items: center; gap: 12px; margin-top: 10px; }
.wo-icon { width: 44px; height: 44px; border-radius: 12px; background: #151926; display: flex; align-items: center; justify-content: center; font-size: 20px; }
.wo-info { flex: 1; }
.wo-info h4 { font-size: 14px; font-weight: 600; }
.wo-info p { font-size: 11px; color: #8B93A7; margin-top: 2px; }
.wo-badge { background: rgba(52, 211, 153, 0.15); color: #34D399; padding: 4px 10px; border-radius: 8px; font-size: 11px; font-weight: 600; }
.start-btn { background: linear-gradient(135deg, #6366F1, #22D3EE); border: none; border-radius: 8px; padding: 6px 14px; color: white; font-size: 12px; font-weight: 600; cursor: pointer; }""",
        jsContent = """function startWorkout() {
  alert('Core Yoga workout started! Timer active.');
  console.log('Workout started: Core Yoga');
}"""
    )

    val CHAT = TemplateInfo(
        id = "chat",
        name = "Modern Chat App",
        description = "Messenger conversation thread with bubbles, active status, and input bar",
        category = "Communication",
        color = 0xFF8B5CF6,
        htmlContent = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Chat</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="chat-shell">
    <header class="chat-header">
      <div class="chat-user">
        <div class="chat-avatar">AI</div>
        <div>
          <h4>Studio Assistant</h4>
          <span class="online-status">● Online</span>
        </div>
      </div>
      <button class="call-btn" onclick="alert('Voice call simulated')">📞</button>
    </header>

    <div class="chat-thread" id="chatThread">
      <div class="msg inbound">
        <p>Hey there! Welcome to Mobile UI Studio. What are you building today?</p>
        <span class="msg-time">10:14 AM</span>
      </div>
      <div class="msg outbound">
        <p>Just testing out the new modern dark theme and live preview!</p>
        <span class="msg-time">10:15 AM</span>
      </div>
    </div>

    <form class="chat-input-row" id="chatForm" onsubmit="return false;">
      <input type="text" id="chatInput" placeholder="Type a message..." />
      <button type="submit" id="sendBtn" class="send-btn">➤</button>
    </form>
  </div>
  <script src="script.js"></script>
</body>
</html>""",
        cssContent = """* { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #07080C; color: #EEF1F8; min-height: 100vh; }
.chat-shell { max-width: 440px; margin: 0 auto; display: flex; flex-direction: column; height: 100vh; }
.chat-header { display: flex; justify-content: space-between; align-items: center; padding: 14px 18px; background: #0E1118; border-bottom: 1px solid rgba(255, 255, 255, 0.08); }
.chat-user { display: flex; align-items: center; gap: 10px; }
.chat-avatar { width: 38px; height: 38px; border-radius: 50%; background: linear-gradient(135deg, #6366F1, #22D3EE); display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 14px; }
.online-status { font-size: 11px; color: #34D399; }
.call-btn { background: #151926; border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 10px; width: 36px; height: 36px; cursor: pointer; color: white; }
.chat-thread { flex: 1; padding: 18px; overflow-y: auto; display: flex; flex-direction: column; gap: 12px; }
.msg { max-width: 78%; padding: 12px 14px; border-radius: 18px; font-size: 14px; line-height: 1.4; }
.msg.inbound { align-self: flex-start; background: #151926; border-bottom-left-radius: 4px; }
.msg.outbound { align-self: flex-end; background: linear-gradient(135deg, #6366F1, #4F46E5); color: white; border-bottom-right-radius: 4px; }
.msg-time { display: block; font-size: 10px; opacity: 0.6; margin-top: 4px; text-align: right; }
.chat-input-row { display: flex; gap: 10px; padding: 12px 16px; background: #0E1118; border-top: 1px solid rgba(255, 255, 255, 0.08); }
.chat-input-row input { flex: 1; background: #151926; border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 12px; padding: 10px 14px; color: white; outline: none; font-size: 13px; }
.send-btn { width: 40px; height: 40px; border-radius: 12px; border: none; background: #22D3EE; color: #07080C; font-size: 16px; cursor: pointer; font-weight: bold; }""",
        jsContent = """const form = document.getElementById('chatForm');
const input = document.getElementById('chatInput');
const thread = document.getElementById('chatThread');
form.addEventListener('submit', (e) => {
  e.preventDefault();
  const text = input.value.trim();
  if (!text) return;
  const bubble = document.createElement('div');
  bubble.className = 'msg outbound';
  bubble.innerHTML = '<p>' + text + '</p><span class="msg-time">Just now</span>';
  thread.appendChild(bubble);
  input.value = '';
  thread.scrollTop = thread.scrollHeight;
  console.log('Sent message: ' + text);
});"""
    )

    val MUSIC_PLAYER = TemplateInfo(
        id = "music",
        name = "Music & Podcast Player",
        description = "Album artwork, animated playback scrubber, playlist queue, and play/pause controls",
        category = "Media",
        color = 0xFF6366F1,
        htmlContent = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Music Player</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="music-shell">
    <header class="music-top">
      <span>Now Playing</span>
      <button class="queue-btn" onclick="alert('Queue sheet opened')">☰</button>
    </header>

    <div class="album-art">🎧</div>

    <div class="track-info">
      <h2>Neon Horizons</h2>
      <p>Cyberwave Collective • Midnight City</p>
    </div>

    <div class="scrubber-wrap">
      <div class="scrubber-bar"><div class="scrubber-fill" style="width: 45%;"></div></div>
      <div class="time-row"><span>1:45</span><span>3:50</span></div>
    </div>

    <div class="controls-row">
      <button class="c-btn">⏮</button>
      <button class="c-btn play-btn" id="playBtn" onclick="togglePlay()">⏸</button>
      <button class="c-btn">⏭</button>
    </div>
  </div>
  <script src="script.js"></script>
</body>
</html>""",
        cssContent = """* { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #07080C; color: #EEF1F8; min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 20px; }
.music-shell { max-width: 380px; width: 100%; text-align: center; }
.music-top { display: flex; justify-content: space-between; align-items: center; font-size: 13px; color: #8B93A7; margin-bottom: 24px; }
.queue-btn { background: none; border: none; color: white; font-size: 18px; cursor: pointer; }
.album-art { width: 220px; height: 220px; border-radius: 28px; background: linear-gradient(135deg, #6366F1, #22D3EE); margin: 0 auto 24px auto; display: flex; align-items: center; justify-content: center; font-size: 72px; box-shadow: 0 20px 50px rgba(99, 102, 241, 0.4); }
.track-info h2 { font-size: 22px; font-weight: 700; margin-bottom: 6px; }
.track-info p { font-size: 13px; color: #8B93A7; margin-bottom: 24px; }
.scrubber-wrap { margin-bottom: 28px; }
.scrubber-bar { height: 6px; background: #151926; border-radius: 6px; overflow: hidden; cursor: pointer; }
.scrubber-fill { height: 100%; background: linear-gradient(90deg, #6366F1, #22D3EE); }
.time-row { display: flex; justify-content: space-between; font-size: 11px; color: #8B93A7; margin-top: 6px; }
.controls-row { display: flex; justify-content: center; align-items: center; gap: 24px; }
.c-btn { background: none; border: none; font-size: 24px; color: white; cursor: pointer; }
.play-btn { width: 64px; height: 64px; border-radius: 50%; background: linear-gradient(135deg, #6366F1, #22D3EE); display: flex; align-items: center; justify-content: center; font-size: 28px; box-shadow: 0 8px 24px rgba(34, 211, 238, 0.35); }""",
        jsContent = """let isPlaying = true;
function togglePlay() {
  isPlaying = !isPlaying;
  document.getElementById('playBtn').textContent = isPlaying ? '⏸' : '▶';
  console.log('Playback state: ' + (isPlaying ? 'Playing' : 'Paused'));
}"""
    )

    val TRAVEL = TemplateInfo(
        id = "travel",
        name = "Travel Explorer",
        description = "Destination showcase with hero banner, filters, and booking cards",
        category = "Travel",
        color = 0xFF06B6D4,
        htmlContent = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Travel Explorer</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="travel-shell">
    <div class="travel-hero">
      <span class="destination-tag">Explore Japan</span>
      <h2>Kyoto Heritage Gardens</h2>
      <p>Serene bamboo groves & historic shrines</p>
    </div>

    <div class="popular-section">
      <h3>Popular Getaways</h3>
      <div class="card-grid">
        <div class="dest-card">
          <div class="dest-icon">🏔</div>
          <h4>Swiss Alps</h4>
          <span>${'$'}1,200 / 5 days</span>
        </div>
        <div class="dest-card">
          <div class="dest-icon">🏝</div>
          <h4>Bali Retreat</h4>
          <span>${'$'}850 / 7 days</span>
        </div>
      </div>
    </div>
    <button class="book-now-btn" onclick="alert('Booking reservation initiated!')">Book Adventure →</button>
  </div>
  <script src="script.js"></script>
</body>
</html>""",
        cssContent = """* { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #07080C; color: #EEF1F8; min-height: 100vh; padding: 18px; }
.travel-shell { max-width: 420px; margin: 0 auto; }
.travel-hero { background: linear-gradient(135deg, #06B6D4, #3B82F6); border-radius: 24px; padding: 28px 20px; color: white; margin-bottom: 24px; box-shadow: 0 12px 30px rgba(6, 182, 212, 0.3); }
.destination-tag { background: rgba(0, 0, 0, 0.25); padding: 4px 10px; border-radius: 12px; font-size: 11px; font-weight: 700; text-transform: uppercase; }
.travel-hero h2 { font-size: 24px; font-weight: 800; margin: 12px 0 6px 0; }
.travel-hero p { font-size: 13px; opacity: 0.9; }
.popular-section h3 { font-size: 16px; margin-bottom: 12px; }
.card-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-bottom: 24px; }
.dest-card { background: #0E1118; border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 16px; padding: 16px; text-align: center; }
.dest-icon { font-size: 32px; margin-bottom: 8px; }
.dest-card h4 { font-size: 14px; font-weight: 600; margin-bottom: 4px; }
.dest-card span { font-size: 11px; color: #34D399; font-weight: 600; }
.book-now-btn { width: 100%; padding: 14px; border-radius: 14px; background: linear-gradient(135deg, #6366F1, #22D3EE); color: white; border: none; font-size: 15px; font-weight: 600; cursor: pointer; }""",
        jsContent = """console.log('Travel Explorer loaded');"""
    )

    val SETTINGS_PAGE = TemplateInfo(
        id = "settings_ui",
        name = "App Settings UI",
        description = "Grouped preferences list, toggle switches, storage meters, and profile section",
        category = "Settings",
        color = 0xFF64748B,
        htmlContent = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Settings</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="set-shell">
    <h2>Settings</h2>
    <div class="user-strip">
      <div class="strip-avatar">JD</div>
      <div>
        <h4>Jordan Doe</h4>
        <span>Pro Member • Exp. 2028</span>
      </div>
    </div>

    <div class="settings-group">
      <h3>Preferences</h3>
      <div class="row"><span>Dark Theme</span><input type="checkbox" checked /></div>
      <div class="row"><span>Haptic Feedback</span><input type="checkbox" checked /></div>
      <div class="row"><span>Auto Sync Code</span><input type="checkbox" /></div>
    </div>
  </div>
  <script src="script.js"></script>
</body>
</html>""",
        cssContent = """* { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #07080C; color: #EEF1F8; min-height: 100vh; padding: 18px; }
.set-shell { max-width: 420px; margin: 0 auto; }
h2 { font-size: 22px; font-weight: 700; margin-bottom: 16px; }
.user-strip { display: flex; align-items: center; gap: 12px; background: #0E1118; border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 16px; padding: 14px; margin-bottom: 20px; }
.strip-avatar { width: 44px; height: 44px; border-radius: 12px; background: linear-gradient(135deg, #6366F1, #22D3EE); color: white; display: flex; align-items: center; justify-content: center; font-weight: 700; }
.user-strip h4 { font-size: 15px; font-weight: 600; }
.user-strip span { font-size: 11px; color: #8B93A7; }
.settings-group { background: #0E1118; border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 18px; padding: 16px; }
.settings-group h3 { font-size: 13px; color: #8B93A7; margin-bottom: 12px; text-transform: uppercase; letter-spacing: 0.5px; }
.row { display: flex; justify-content: space-between; align-items: center; padding: 12px 0; border-bottom: 1px solid rgba(255, 255, 255, 0.06); font-size: 14px; }
.row:last-child { border-bottom: none; }""",
        jsContent = """console.log('Settings initialized');"""
    )

    val LANDING_PAGE = TemplateInfo(
        id = "landing",
        name = "Product Landing Page",
        description = "High-converting mobile product landing with hero headline and download call-to-action",
        category = "Marketing",
        color = 0xFF38BDF8,
        htmlContent = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>Mobile UI Studio</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <div class="landing-shell">
    <div class="pill-badge">✨ Next Generation Studio</div>
    <h1>Build mobile UI anywhere, offline.</h1>
    <p>Professional mobile web mockup tool with instant device preview, syntax engine, and zero-cloud independence.</p>
    <button class="cta-btn" onclick="alert('Demo launched!')">Download App APK</button>
  </div>
  <script src="script.js"></script>
</body>
</html>""",
        cssContent = """* { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #07080C; color: #EEF1F8; min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 24px; text-align: center; }
.landing-shell { max-width: 400px; }
.pill-badge { display: inline-block; background: rgba(99, 102, 241, 0.2); color: #22D3EE; border: 1px solid rgba(34, 211, 238, 0.3); padding: 6px 14px; border-radius: 20px; font-size: 12px; font-weight: 700; margin-bottom: 18px; }
h1 { font-size: 32px; font-weight: 800; line-height: 1.2; margin-bottom: 14px; letter-spacing: -0.5px; }
p { font-size: 14px; color: #8B93A7; line-height: 1.6; margin-bottom: 28px; }
.cta-btn { background: linear-gradient(135deg, #6366F1, #22D3EE); border: none; border-radius: 14px; padding: 16px 28px; color: white; font-size: 16px; font-weight: 700; cursor: pointer; box-shadow: 0 8px 24px rgba(99, 102, 241, 0.4); }""",
        jsContent = """console.log('Landing page loaded');"""
    )

    val ALL_TEMPLATES = listOf(
        BLANK,
        LOGIN,
        DASHBOARD,
        PROFILE,
        NAVIGATION,
        ONBOARDING,
        FOOD_DELIVERY,
        FITNESS,
        CHAT,
        MUSIC_PLAYER,
        TRAVEL,
        SETTINGS_PAGE,
        LANDING_PAGE
    )
}

