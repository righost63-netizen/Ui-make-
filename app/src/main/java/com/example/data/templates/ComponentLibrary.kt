package com.example.data.templates

import com.example.data.model.CodeSnippet

object ComponentLibrary {

    val DEFAULT_SNIPPETS = listOf(
        // Buttons
        CodeSnippet(
            id = "btn-gradient",
            prefix = "btn",
            title = "Gradient Action Button",
            category = "Buttons",
            htmlCode = """<button class="btn-gradient"><span>Get Started</span> →</button>""",
            cssCode = """.btn-gradient {
  background: linear-gradient(135deg, #6366F1 0%, #22D3EE 100%);
  color: #ffffff;
  border: none;
  border-radius: 12px;
  padding: 14px 24px;
  font-size: 15px;
  font-weight: 600;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  box-shadow: 0 4px 18px rgba(99, 102, 241, 0.35);
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}
.btn-gradient:active {
  transform: scale(0.97);
}"""
        ),
        CodeSnippet(
            id = "btn-glass",
            prefix = "glassbtn",
            title = "Glassmorphic Button",
            category = "Buttons",
            htmlCode = """<button class="btn-glass">✨ Explore Design</button>""",
            cssCode = """.btn-glass {
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(255, 255, 255, 0.15);
  color: #EEF1F8;
  border-radius: 12px;
  padding: 12px 20px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
}"""
        ),

        // Cards
        CodeSnippet(
            id = "card-glass",
            prefix = "glass",
            title = "Glassmorphic Card",
            category = "Cards",
            htmlCode = """<div class="glass-card">
  <div class="glass-badge">PRO</div>
  <h3>Linear Glass Card</h3>
  <p>Ultra-refined backdrop blur with subtle borders and balanced typography.</p>
</div>""",
            cssCode = """.glass-card {
  background: rgba(14, 17, 24, 0.7);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 20px;
  padding: 22px;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.4);
}
.glass-badge {
  display: inline-block;
  background: rgba(99, 102, 241, 0.2);
  color: #22D3EE;
  border: 1px solid rgba(34, 211, 238, 0.3);
  padding: 3px 8px;
  border-radius: 20px;
  font-size: 11px;
  font-weight: 700;
  margin-bottom: 12px;
}"""
        ),
        CodeSnippet(
            id = "card-metric",
            prefix = "card",
            title = "Fintech Metric Card",
            category = "Cards",
            htmlCode = """<div class="metric-card">
  <div class="metric-header">
    <span class="label">Monthly Revenue</span>
    <span class="pill positive">+18.4%</span>
  </div>
  <div class="metric-value">${'$'}48,250.00</div>
  <div class="metric-footer">vs. ${'$'}40,750 last month</div>
</div>""",
            cssCode = """.metric-card {
  background: #0E1118;
  border: 1px solid rgba(255, 255, 255, 0.07);
  border-radius: 18px;
  padding: 20px;
}
.metric-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #8B93A7;
}
.metric-value {
  font-size: 28px;
  font-weight: 700;
  color: #EEF1F8;
  margin: 10px 0 6px 0;
  letter-spacing: -0.5px;
}
.pill.positive {
  background: rgba(52, 211, 153, 0.15);
  color: #34D399;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}
.metric-footer {
  font-size: 12px;
  color: #8B93A7;
}"""
        ),

        // Navbars
        CodeSnippet(
            id = "nav-floating",
            prefix = "nav",
            title = "Floating Glass Bottom Nav",
            category = "Navbars",
            htmlCode = """<nav class="floating-nav">
  <button class="nav-item active"><span class="icon">🏠</span><span class="text">Home</span></button>
  <button class="nav-item"><span class="icon">🧭</span><span class="text">Explore</span></button>
  <button class="nav-item"><span class="icon">⚡</span><span class="text">Activity</span></button>
  <button class="nav-item"><span class="icon">👤</span><span class="text">Profile</span></button>
</nav>""",
            cssCode = """.floating-nav {
  position: fixed;
  bottom: 20px;
  left: 20px;
  right: 20px;
  height: 60px;
  background: rgba(21, 25, 38, 0.85);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 22px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.5);
  z-index: 100;
}
.nav-item {
  background: none;
  border: none;
  color: #8B93A7;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  cursor: pointer;
  padding: 6px 14px;
  border-radius: 14px;
  transition: all 0.2s ease;
}
.nav-item.active {
  background: rgba(99, 102, 241, 0.2);
  color: #22D3EE;
}
.nav-item .icon { font-size: 18px; }
.nav-item .text { font-size: 10px; font-weight: 600; }"""
        ),

        // Inputs
        CodeSnippet(
            id = "input-search",
            prefix = "input",
            title = "Glass Search Field",
            category = "Inputs",
            htmlCode = """<div class="search-field">
  <span class="search-icon">🔍</span>
  <input type="text" placeholder="Search templates, styles, components..." />
</div>""",
            cssCode = """.search-field {
  background: #0E1118;
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 14px;
  display: flex;
  align-items: center;
  padding: 12px 16px;
  gap: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}
.search-field input {
  background: transparent;
  border: none;
  color: #EEF1F8;
  font-size: 14px;
  outline: none;
  width: 100%;
}
.search-field input::placeholder {
  color: #8B93A7;
}"""
        ),

        // Toggles
        CodeSnippet(
            id = "toggle-gradient",
            prefix = "toggle",
            title = "Gradient Switch Toggle",
            category = "Toggles",
            htmlCode = """<label class="switch-gradient">
  <input type="checkbox" checked />
  <span class="slider-round"></span>
</label>""",
            cssCode = """.switch-gradient {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 28px;
}
.switch-gradient input { opacity: 0; width: 0; height: 0; }
.slider-round {
  position: absolute;
  cursor: pointer;
  top: 0; left: 0; right: 0; bottom: 0;
  background-color: #151926;
  border: 1px solid rgba(255, 255, 255, 0.1);
  transition: .3s;
  border-radius: 34px;
}
.slider-round:before {
  position: absolute;
  content: "";
  height: 20px;
  width: 20px;
  left: 3px;
  bottom: 3px;
  background-color: #EEF1F8;
  transition: .3s;
  border-radius: 50%;
}
.switch-gradient input:checked + .slider-round {
  background: linear-gradient(135deg, #6366F1, #22D3EE);
}
.switch-gradient input:checked + .slider-round:before {
  transform: translateX(22px);
}"""
        ),

        // Modals & Sheets
        CodeSnippet(
            id = "bottom-sheet",
            prefix = "sheet",
            title = "Interactive Bottom Sheet",
            category = "Bottom sheets",
            htmlCode = """<div class="bottom-sheet">
  <div class="sheet-handle"></div>
  <h3 class="sheet-title">Payment Confirmation</h3>
  <p class="sheet-desc">Confirm ${'$'}120.00 transfer to Alex Morgan.</p>
  <button class="btn-gradient" style="width:100%; justify-content:center; margin-top:16px;">Confirm Payment</button>
</div>""",
            cssCode = """.bottom-sheet {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #0E1118;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 26px 26px 0 0;
  padding: 16px 24px 32px 24px;
  box-shadow: 0 -10px 40px rgba(0, 0, 0, 0.6);
}
.sheet-handle {
  width: 38px;
  height: 4px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
  margin: 0 auto 18px auto;
}
.sheet-title { font-size: 18px; font-weight: 700; color: #EEF1F8; }
.sheet-desc { font-size: 13px; color: #8B93A7; margin-top: 4px; }"""
        ),

        // Charts
        CodeSnippet(
            id = "chart-bars",
            prefix = "chart",
            title = "Dynamic Weekly Bar Chart",
            category = "Charts",
            htmlCode = """<div class="chart-wrapper">
  <div class="chart-col"><div class="bar-fill" style="height: 40%"></div><span>M</span></div>
  <div class="chart-col"><div class="bar-fill" style="height: 65%"></div><span>T</span></div>
  <div class="chart-col"><div class="bar-fill active" style="height: 95%"></div><span>W</span></div>
  <div class="chart-col"><div class="bar-fill" style="height: 55%"></div><span>T</span></div>
  <div class="chart-col"><div class="bar-fill" style="height: 80%"></div><span>F</span></div>
  <div class="chart-col"><div class="bar-fill" style="height: 35%"></div><span>S</span></div>
  <div class="chart-col"><div class="bar-fill" style="height: 50%"></div><span>S</span></div>
</div>""",
            cssCode = """.chart-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  height: 140px;
  background: #0E1118;
  border: 1px solid rgba(255, 255, 255, 0.07);
  border-radius: 18px;
  padding: 18px;
}
.chart-col {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  flex: 1;
}
.bar-fill {
  width: 14px;
  background: #151926;
  border-radius: 6px;
  transition: all 0.3s;
}
.bar-fill.active {
  background: linear-gradient(180deg, #22D3EE, #6366F1);
  box-shadow: 0 0 14px rgba(34, 211, 238, 0.4);
}
.chart-col span { font-size: 11px; color: #8B93A7; }"""
        ),

        // Avatars
        CodeSnippet(
            id = "avatar-badge",
            prefix = "avatar",
            title = "Avatar with Live Status Badge",
            category = "Avatars",
            htmlCode = """<div class="avatar-container">
  <div class="avatar-ring">
    <div class="avatar-img">EV</div>
  </div>
  <span class="status-indicator"></span>
</div>""",
            cssCode = """.avatar-container {
  position: relative;
  display: inline-block;
  width: 54px;
  height: 54px;
}
.avatar-ring {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  padding: 2px;
  background: linear-gradient(135deg, #6366F1, #22D3EE);
}
.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: #0E1118;
  color: #EEF1F8;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 18px;
}
.status-indicator {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: #34D399;
  border: 2px solid #07080C;
}"""
        )
    )

    /**
     * Emmet-style expansion: e.g. div.card>h3+p, ul>li*4, button.btn
     */
    fun expandEmmet(query: String): String? {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return null

        // Quick shortcode check
        DEFAULT_SNIPPETS.find { it.prefix.equals(trimmed, ignoreCase = true) }?.let {
            return it.htmlCode
        }

        return when {
            trimmed.matches(Regex("ul>li\\*(\\d+)")) -> {
                val count = trimmed.substringAfter("li*").toIntOrNull() ?: 3
                buildString {
                    append("<ul>\n")
                    for (i in 1..count) {
                        append("  <li>Item $i</li>\n")
                    }
                    append("</ul>")
                }
            }
            trimmed == "div.card>h3+p" -> {
                """<div class="card">
  <h3>Card Title</h3>
  <p>Card description text goes here.</p>
</div>"""
            }
            trimmed.startsWith("div.") -> {
                val className = trimmed.removePrefix("div.")
                """<div class="$className">\n  \n</div>"""
            }
            trimmed.startsWith("button.") -> {
                val className = trimmed.removePrefix("button.")
                """<button class="$className">Click Me</button>"""
            }
            trimmed.startsWith("input:") -> {
                val type = trimmed.removePrefix("input:")
                """<input type="$type" placeholder="Enter $type..." />"""
            }
            else -> null
        }
    }
}
