<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr" data-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur - Backoffice</title>
    <script>
        (function() {
            const savedTheme = localStorage.getItem('theme') || 'dark';
            document.documentElement.setAttribute('data-theme', savedTheme);
        })();
    </script>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Instrument+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/templatemo-crypto-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/templatemo-crypto-pages.css">
</head>
<body>
    <button class="mobile-menu-toggle" id="mobileMenuToggle">
        <div class="hamburger">
            <span></span>
            <span></span>
            <span></span>
        </div>
    </button>

    <div class="sidebar-overlay" id="sidebarOverlay"></div>

    <div class="dashboard">
        <aside class="sidebar" id="sidebar">
            <div class="logo">
                <div class="logo-icon">BO</div>
                <span class="logo-text">Backoffice</span>
            </div>

            <nav class="nav-section">
                <div class="nav-label">Menu Principal</div>
                <a href="${pageContext.request.contextPath}/" class="nav-item">
                    <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/>
                        <polyline points="9 22 9 12 15 12 15 22"/>
                    </svg>
                    Accueil
                </a>
                <a href="${pageContext.request.contextPath}/api/reservations/form" class="nav-item">
                    <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                        <line x1="16" y1="2" x2="16" y2="6"/>
                        <line x1="8" y1="2" x2="8" y2="6"/>
                        <line x1="3" y1="10" x2="21" y2="10"/>
                    </svg>
                    Réservations
                </a>
            </nav>

            <div class="sidebar-footer">
                <div class="theme-toggle">
                    <div class="theme-toggle-label">
                        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <circle cx="12" cy="12" r="5"/>
                            <line x1="12" y1="1" x2="12" y2="3"/>
                            <line x1="12" y1="21" x2="12" y2="23"/>
                            <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/>
                            <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/>
                            <line x1="1" y1="12" x2="3" y2="12"/>
                            <line x1="21" y1="12" x2="23" y2="12"/>
                            <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/>
                            <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/>
                        </svg>
                        Mode Clair
                    </div>
                    <div class="theme-switch" id="themeSwitch"></div>
                </div>
            </div>
        </aside>

        <main class="main-content">
            <div class="page-header">
                <h1>Erreur</h1>
                <p>Une erreur s'est produite</p>
            </div>

            <div class="card" style="text-align: center; padding: 3rem;">
                <div style="margin-bottom: 2rem;">
                    <svg width="80" height="80" viewBox="0 0 24 24" fill="none" stroke="var(--loss)" stroke-width="1.5">
                        <circle cx="12" cy="12" r="10"/>
                        <line x1="15" y1="9" x2="9" y2="15"/>
                        <line x1="9" y1="9" x2="15" y2="15"/>
                    </svg>
                </div>

                <h2 style="color: var(--text-primary); margin-bottom: 1rem;">Oups ! Une erreur est survenue</h2>

                <div class="card" style="background: rgba(194, 120, 120, 0.15); border: 1px solid var(--loss); margin: 1.5rem 0; text-align: left;">
                    <div style="display: flex; align-items: flex-start; gap: 12px; padding: 1rem;">
                        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="var(--loss)" stroke-width="2" style="flex-shrink: 0; margin-top: 2px;">
                            <circle cx="12" cy="12" r="10"/>
                            <line x1="12" y1="8" x2="12" y2="12"/>
                            <line x1="12" y1="16" x2="12.01" y2="16"/>
                        </svg>
                        <p style="color: var(--loss); margin: 0; word-break: break-word;">
                            <%= request.getAttribute("error-message") != null ? request.getAttribute("error-message") : "Erreur inconnue" %>
                        </p>
                    </div>
                </div>

                <div class="btn-group" style="justify-content: center; margin-top: 2rem;">
                    <%
                        String link = (String) request.getAttribute("link");
                        if (link != null && !link.isEmpty()) {
                    %>
                    <a href="<%= link %>" class="btn primary" style="text-decoration: none; display: inline-flex; align-items: center; gap: 0.5rem;">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <line x1="19" y1="12" x2="5" y2="12"/>
                            <polyline points="12 19 5 12 12 5"/>
                        </svg>
                        Retour
                    </a>
                    <% } %>
                    <a href="${pageContext.request.contextPath}/" class="btn" style="text-decoration: none; display: inline-flex; align-items: center; gap: 0.5rem;">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/>
                            <polyline points="9 22 9 12 15 12 15 22"/>
                        </svg>
                        Accueil
                    </a>
                </div>
            </div>

            <footer class="copyright">
                Copyright &copy; 2026 Backoffice. Template par <a href="https://templatemo.com" target="_blank" rel="nofollow">TemplateMo</a>
            </footer>
        </main>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/templatemo-crypto-script.js"></script>
</body>
</html>
