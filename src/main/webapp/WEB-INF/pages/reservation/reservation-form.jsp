<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="itu.framework.backoffice.entities.Hotel" %>
<%@ page import="itu.framework.backoffice.dtos.ReservationDTO" %>
<!DOCTYPE html>
<html lang="fr" data-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Réservations - Backoffice</title>
    <script>
        (function() {
            const savedTheme = localStorage.getItem('theme') || 'dark';
            document.documentElement.setAttribute('data-theme', savedTheme);
        })();
    </script>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Instrument+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/assets/css/templatemo-crypto-style.css">
    <link rel="stylesheet" href="/assets/css/templatemo-crypto-pages.css">
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
                <a href="${pageContext.request.contextPath}/api/reservations/form" class="nav-item active">
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
                <h1>Réservations</h1>
                <p>Gérez les réservations d'hôtel</p>
            </div>

            <%
                String message = (String) request.getAttribute("message");
                if(message != null && !message.isEmpty()) {
            %>
            <div class="card" style="background: rgba(184, 115, 51, 0.15); border: 1px solid #b87333; margin-bottom: 1.5rem;">
                <div style="display: flex; align-items: center; gap: 12px; padding: 0.5rem;">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#b87333" stroke-width="2">
                        <path d="M22 11.08V12a10 10 0 11-5.93-9.14"/>
                        <polyline points="22 4 12 14.01 9 11.01"/>
                    </svg>
                    <p style="color: var(--text-primary); margin: 0;"><%= message %></p>
                </div>
            </div>
            <%
                }
            %>

            <!-- Formulaire de création -->
            <div class="card" style="margin-bottom: 2rem;">
                <div class="card-header">
                    <h3 class="card-title">Nouvelle Réservation</h3>
                    <p class="card-description">Remplissez les détails de la réservation</p>
                </div>

                <form method="post" action="${pageContext.request.contextPath}/api/reservations/save">
                    <div class="form-grid">
                        <div class="form-group">
                            <label class="form-label">ID Client</label>
                            <input type="text" class="form-input" name="idClient" placeholder="Entrez l'ID du client" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Nombre de Passagers</label>
                            <input type="number" class="form-input" name="nbPassager" min="1" placeholder="1" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Hôtel</label>
                            <select class="form-select" name="idHotel" required>
                                <option value="">-- Choisir un hôtel --</option>
                                <%
                                    List<Hotel> hotels = (List<Hotel>) request.getAttribute("hotels");
                                    if (hotels != null) {
                                        for (Hotel h : hotels) {
                                %>
                                <option value="<%= h.getId() %>"><%= h.getNom() %></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Date et Heure d'Arrivée</label>
                            <input type="datetime-local" class="form-input" name="dateHeureArrivee" required>
                        </div>
                    </div>

                    <div class="btn-group">
                        <button type="submit" class="btn primary">
                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="margin-right: 0.5rem;">
                                <path d="M19 21H5a2 2 0 01-2-2V5a2 2 0 012-2h11l5 5v11a2 2 0 01-2 2z"/>
                                <polyline points="17 21 17 13 7 13 7 21"/>
                                <polyline points="7 3 7 8 15 8"/>
                            </svg>
                            Enregistrer
                        </button>
                        <a href="${pageContext.request.contextPath}/" class="btn" style="text-decoration: none;">Annuler</a>
                    </div>
                </form>
            </div>

            <!-- Liste des réservations -->
            <div class="card">
                <div class="card-header">
                    <h3 class="card-title">Liste des Réservations</h3>
                    <p class="card-description">Toutes les réservations enregistrées</p>
                </div>

                <table class="market-table">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Client</th>
                            <th>Hôtel</th>
                            <th class="text-right">Passagers</th>
                            <th class="text-right">Date d'arrivée</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<ReservationDTO> reservations = (List<ReservationDTO>) request.getAttribute("reservations");
                            if (reservations != null && !reservations.isEmpty()) {
                                int index = 1;
                                for (ReservationDTO r : reservations) {
                        %>
                        <tr>
                            <td><span class="coin-rank"><%= index++ %></span></td>
                            <td>
                                <div class="coin-cell">
                                    <div class="coin-icon" style="background: var(--accent-gradient);">
                                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#1c1c1e" stroke-width="2">
                                            <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/>
                                            <circle cx="12" cy="7" r="4"/>
                                        </svg>
                                    </div>
                                    <div>
                                        <div class="coin-name"><%= r.getId_client() %></div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <div class="coin-cell">
                                    <div class="coin-icon" style="background: linear-gradient(135deg, #6b8e6b, #8fb88f);">
                                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#1c1c1e" stroke-width="2">
                                            <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/>
                                        </svg>
                                    </div>
                                    <div>
                                        <div class="coin-name"><%= r.getNom_hotel() %></div>
                                    </div>
                                </div>
                            </td>
                            <td class="text-right"><span style="color: var(--accent-copper); font-weight: 600;"><%= r.getNb_passager() %></span></td>
                            <td class="text-right"><span style="color: var(--text-secondary);"><%= r.getFormatedDate() %></span></td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="5" style="text-align: center; padding: 2rem; color: var(--text-secondary);">
                                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" style="margin-bottom: 1rem; opacity: 0.5;">
                                    <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                                    <line x1="16" y1="2" x2="16" y2="6"/>
                                    <line x1="8" y1="2" x2="8" y2="6"/>
                                    <line x1="3" y1="10" x2="21" y2="10"/>
                                </svg>
                                <p>Aucune réservation pour le moment</p>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </div>

            <footer class="copyright">
                Copyright &copy; 2026 Backoffice. Template par <a href="https://templatemo.com" target="_blank" rel="nofollow">TemplateMo</a>
            </footer>
        </main>
    </div>

    <script src="${pageContext.request.contextPath}/static/assets/js/templatemo-crypto-script.js"></script>
</body>
</html>
