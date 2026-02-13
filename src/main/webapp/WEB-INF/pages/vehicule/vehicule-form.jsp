<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="itu.framework.backoffice.entities.Vehicule" %>
<%
    Vehicule vehicule = (Vehicule) request.getAttribute("vehicule");
    boolean isUpdate = vehicule != null;
%>
<!DOCTYPE html>
<html lang="fr" data-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= isUpdate ? "Modifier" : "Nouveau" %> Véhicule - Backoffice</title>
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
                <a href="${pageContext.request.contextPath}/api/vehicules" class="nav-item active">
                    <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M5 17h14v-5H5v5z"/>
                        <path d="M18 17a2 2 0 100 4 2 2 0 000-4zm-12 0a2 2 0 100 4 2 2 0 000-4z"/>
                        <path d="M5 9L2 5h20l-3 4H5z"/>
                    </svg>
                    Véhicules
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
                <h1><%= isUpdate ? "Modifier" : "Nouveau" %> Véhicule</h1>
                <p>Remplissez les informations du véhicule</p>
            </div>

            <div class="card">
                <div class="card-header">
                    <h3 class="card-title"><%= isUpdate ? "Modification" : "Ajout" %> d'un véhicule</h3>
                    <p class="card-description">Renseignez les caractéristiques du véhicule</p>
                </div>

                <form method="post" action="${pageContext.request.contextPath}/api/vehicules<%= isUpdate ? "/" + vehicule.getId() : "" %>">
                    <% if (isUpdate) { %>
                    <input type="hidden" name="id" value="<%= vehicule.getId() %>">
                    <% } %>

                    <div class="form-grid">
                        <div class="form-group">
                            <label class="form-label">Référence</label>
                            <input type="text" 
                                   class="form-input" 
                                   name="ref" 
                                   placeholder="Ex: VH-001" 
                                   maxlength="10"
                                   value="<%= isUpdate ? vehicule.getRef() : "" %>"
                                   required>
                            <small style="color: var(--text-secondary); font-size: 0.875rem;">Maximum 10 caractères</small>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Nombre de places</label>
                            <input type="number" 
                                   class="form-input" 
                                   name="nbrPlace" 
                                   min="1" 
                                   placeholder="Ex: 5"
                                   value="<%= isUpdate ? vehicule.getNbrPlace() : "" %>"
                                   required>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Type de carburant</label>
                            <select class="form-select" name="typeCarburant" required>
                                <option value="">-- Choisir un type --</option>
                                <option value="D" <%= isUpdate && "D".equals(vehicule.getTypeCarburant()) ? "selected" : "" %>>Diesel (D)</option>
                                <option value="ES" <%= isUpdate && "ES".equals(vehicule.getTypeCarburant()) ? "selected" : "" %>>Essence (ES)</option>
                                <option value="EL" <%= isUpdate && "EL".equals(vehicule.getTypeCarburant()) ? "selected" : "" %>>Électrique (EL)</option>
                                <option value="H" <%= isUpdate && "H".equals(vehicule.getTypeCarburant()) ? "selected" : "" %>>Hybride (H)</option>
                            </select>
                        </div>
                    </div>

                    <div class="btn-group">
                        <button type="submit" class="btn primary">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="margin-right: 0.5rem;">
                                <path d="M19 21H5a2 2 0 01-2-2V5a2 2 0 012-2h11l5 5v11a2 2 0 01-2 2z"/>
                                <polyline points="17 21 17 13 7 13 7 21"/>
                                <polyline points="7 3 7 8 15 8"/>
                            </svg>
                            <%= isUpdate ? "Mettre à jour" : "Enregistrer" %>
                        </button>
                        <a href="${pageContext.request.contextPath}/api/vehicules" class="btn" style="text-decoration: none;">Annuler</a>
                    </div>
                </form>
            </div>

            <footer class="copyright">
                Copyright &copy; 2026 Backoffice. Template par <a href="https://templatemo.com" target="_blank" rel="nofollow">TemplateMo</a>
            </footer>
        </main>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/templatemo-crypto-script.js"></script>
</body>
</html>
