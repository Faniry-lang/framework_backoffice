<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="itu.framework.backoffice.entities.Vehicule" %>
<%
    Vehicule vehicule = (Vehicule) request.getAttribute("vehicule");
%>
<!DOCTYPE html>
<html lang="fr" data-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fiche Véhicule <%= vehicule != null ? vehicule.getRef() : "" %> - Backoffice</title>
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
            <% if (vehicule != null) { %>
            <div class="page-header">
                <div>
                    <h1>Fiche Véhicule</h1>
                    <p>Détails du véhicule <%= vehicule.getRef() %></p>
                </div>
                <a href="${pageContext.request.contextPath}/api/vehicules/form?id=<%= vehicule.getId() %>" class="btn primary">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="margin-right: 0.5rem;">
                        <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
                        <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
                    </svg>
                    Modifier
                </a>
            </div>

            <div class="card">
                <div class="card-header">
                    <div class="coin-cell">
                        <div class="coin-icon" style="width: 64px; height: 64px; background: var(--accent-gradient);">
                            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#1c1c1e" stroke-width="2">
                                <path d="M5 17h14v-5H5v5z"/>
                                <path d="M18 17a2 2 0 100 4 2 2 0 000-4zm-12 0a2 2 0 100 4 2 2 0 000-4z"/>
                                <path d="M5 9L2 5h20l-3 4H5z"/>
                            </svg>
                        </div>
                        <div>
                            <h2 class="card-title" style="margin-bottom: 0.25rem;"><%= vehicule.getRef() %></h2>
                            <p class="card-description">Véhicule #<%= vehicule.getId() %></p>
                        </div>
                    </div>
                </div>

                <div class="stats-grid" style="grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1.5rem; padding: 1.5rem;">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, rgba(184, 115, 51, 0.1), rgba(184, 115, 51, 0.05));">
                            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="var(--accent-copper)" stroke-width="2">
                                <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/>
                            </svg>
                        </div>
                        <div>
                            <div class="stat-label">Référence</div>
                            <div class="stat-value"><%= vehicule.getRef() %></div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, rgba(76, 175, 80, 0.1), rgba(76, 175, 80, 0.05));">
                            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#4caf50" stroke-width="2">
                                <path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/>
                                <circle cx="9" cy="7" r="4"/>
                                <path d="M23 21v-2a4 4 0 00-3-3.87"/>
                                <path d="M16 3.13a4 4 0 010 7.75"/>
                            </svg>
                        </div>
                        <div>
                            <div class="stat-label">Nombre de places</div>
                            <div class="stat-value"><%= vehicule.getNbrPlace() %></div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, rgba(33, 150, 243, 0.1), rgba(33, 150, 243, 0.05));">
                            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#2196f3" stroke-width="2">
                                <path d="M12 2v20M17 5H9.5a3.5 3.5 0 000 7h5a3.5 3.5 0 010 7H6"/>
                            </svg>
                        </div>
                        <div>
                            <div class="stat-label">Type de carburant</div>
                            <div class="stat-value">
                                <span class="status-badge" style="font-size: 1.25rem; padding: 0.375rem 0.75rem;">
                                    <%= vehicule.getTypeCarburant() %>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="card" style="margin-top: 1.5rem;">
                <div class="card-header">
                    <h3 class="card-title">Informations détaillées</h3>
                </div>
                
                <table class="market-table">
                    <tbody>
                        <tr>
                            <td style="font-weight: 600; width: 40%;">ID</td>
                            <td><%= vehicule.getId() %></td>
                        </tr>
                        <tr>
                            <td style="font-weight: 600;">Référence</td>
                            <td><%= vehicule.getRef() %></td>
                        </tr>
                        <tr>
                            <td style="font-weight: 600;">Capacité</td>
                            <td><%= vehicule.getNbrPlace() %> places</td>
                        </tr>
                        <tr>
                            <td style="font-weight: 600;">Carburant</td>
                            <td>
                                <%
                                    String carburant = vehicule.getTypeCarburant();
                                    String carburantLabel = "";
                                    switch(carburant) {
                                        case "D": carburantLabel = "Diesel"; break;
                                        case "ES": carburantLabel = "Essence"; break;
                                        case "EL": carburantLabel = "Électrique"; break;
                                        case "H": carburantLabel = "Hybride"; break;
                                        default: carburantLabel = carburant;
                                    }
                                %>
                                <%= carburantLabel %> (<%= carburant %>)
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <div class="btn-group" style="margin-top: 1.5rem;">
                <a href="${pageContext.request.contextPath}/api/vehicules/form?id=<%= vehicule.getId() %>" class="btn primary" style="text-decoration: none;">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="margin-right: 0.5rem;">
                        <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
                        <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
                    </svg>
                    Modifier ce véhicule
                </a>
                <a href="${pageContext.request.contextPath}/api/vehicules" class="btn" style="text-decoration: none;">Retour à la liste</a>
            </div>

            <% } else { %>
            <div class="card">
                <div style="text-align: center; padding: 3rem; color: var(--text-secondary);">
                    <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" style="margin-bottom: 1rem; opacity: 0.5;">
                        <circle cx="12" cy="12" r="10"/>
                        <line x1="12" y1="8" x2="12" y2="12"/>
                        <line x1="12" y1="16" x2="12.01" y2="16"/>
                    </svg>
                    <h2>Véhicule introuvable</h2>
                    <p style="margin-top: 0.5rem;">Le véhicule demandé n'existe pas.</p>
                    <a href="${pageContext.request.contextPath}/api/vehicules" class="btn primary" style="margin-top: 1.5rem; text-decoration: none;">Retour à la liste</a>
                </div>
            </div>
            <% } %>

            <footer class="copyright">
                Copyright &copy; 2026 Backoffice. Template par <a href="https://templatemo.com" target="_blank" rel="nofollow">TemplateMo</a>
            </footer>
        </main>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/templatemo-crypto-script.js"></script>
</body>
</html>
