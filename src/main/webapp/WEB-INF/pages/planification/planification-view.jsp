<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="itu.framework.backoffice.dtos.TrajetDetailDTO" %>
<%@ page import="itu.framework.backoffice.dtos.ReservationNonAssigneeDTO" %>
<%@ page import="itu.framework.backoffice.dtos.ReservationTrajetDTO" %>
<!DOCTYPE html>
<html lang="fr" data-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Planification - Backoffice</title>
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
                <a href="${pageContext.request.contextPath}/api/vehicules" class="nav-item">
                    <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M5 17h14v-5H5v5z"/>
                        <path d="M18 17a2 2 0 100 4 2 2 0 000-4zm-12 0a2 2 0 100 4 2 2 0 000-4z"/>
                        <path d="M5 9L2 5h20l-3 4H5z"/>
                    </svg>
                    Véhicules
                </a>
                <a href="${pageContext.request.contextPath}/api/planification/form" class="nav-item active">
                    <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="2" y="3" width="20" height="14" rx="2" ry="2"/>
                        <line x1="8" y1="21" x2="16" y2="21"/>
                        <line x1="12" y1="17" x2="12" y2="21"/>
                    </svg>
                    Planification
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
                <h1>Planification du ${dateSelectionnee}</h1>
                <p>Résultat de l'assignation automatique des véhicules</p>
            </div>

            <!-- Section 1: Trajets assignés -->
            <div class="card" style="margin-bottom: 2rem;">
                <div class="card-header">
                    <h3 class="card-title">Trajets Assignés</h3>
                    <p class="card-description">Liste des trajets planifiés avec leurs réservations</p>
                </div>

                <%
                    @SuppressWarnings("unchecked")
                    List<TrajetDetailDTO> trajetsDetails = (List<TrajetDetailDTO>) request.getAttribute("trajetsDetails");
                    if (trajetsDetails != null && !trajetsDetails.isEmpty()) {
                %>
                <div class="table-container">
                    <table class="market-table">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Véhicule</th>
                                <th>Places</th>
                                <th>Réservations</th>
                                <th>Départ</th>
                                <th>Arrivée</th>
                                <th>Durée</th>
                                <th>Distance</th>
                                <th>Détail Trajet</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int i = 0; i < trajetsDetails.size(); i++) {
                                    TrajetDetailDTO trajet = trajetsDetails.get(i);
                            %>
                            <tr>
                                <td><%= i + 1 %></td>
                                <td><strong><%= trajet.getVehiculeRef() %></strong></td>
                                <td><%= trajet.getNbrPlace() %></td>
                                <td>
                                    <%
                                        if (trajet.getReservations() != null && !trajet.getReservations().isEmpty()) {
                                            StringBuilder reservationsText = new StringBuilder();
                                            for (int j = 0; j < trajet.getReservations().size(); j++) {
                                                ReservationTrajetDTO res = trajet.getReservations().get(j);
                                                if (j > 0) reservationsText.append(", ");
                                                reservationsText.append(res.getIdClient()).append(" (").append(res.getNbPassager()).append("p)");
                                            }
                                    %>
                                    <%= reservationsText.toString() %><br>
                                    <small style="color: var(--text-secondary);">Total: <%= trajet.getNbPassagersTotal() %> passagers</small>
                                    <%
                                        } else {
                                    %>
                                    <span style="color: var(--text-secondary);">Aucune réservation</span>
                                    <%
                                        }
                                    %>
                                </td>
                                <td><%= trajet.getFormattedDepart() %></td>
                                <td><%= trajet.getFormattedArrivee() %></td>
                                <td><%= trajet.getFormattedDuree() %></td>
                                <td><%= trajet.getDistanceTotale() %> km</td>
                                <td><%= trajet.getDetailTrajetFormate() %></td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>
                <%
                    } else {
                %>
                <div style="text-align: center; padding: 3rem; color: var(--text-secondary);">
                    <svg width="60" height="60" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="margin-bottom: 1rem;">
                        <rect x="2" y="3" width="20" height="14" rx="2" ry="2"/>
                        <line x1="8" y1="21" x2="16" y2="21"/>
                        <line x1="12" y1="17" x2="12" y2="21"/>
                    </svg>
                    <p>Aucun trajet planifié pour cette date</p>
                </div>
                <%
                    }
                %>
            </div>

            <!-- Section 2: Réservations non assignées -->
            <div class="card" style="margin-bottom: 2rem;">
                <div class="card-header">
                    <h3 class="card-title">Réservations Non Assignées</h3>
                    <p class="card-description">Réservations qui n'ont pas pu être planifiées automatiquement</p>
                </div>

                <%
                    @SuppressWarnings("unchecked")
                    List<ReservationNonAssigneeDTO> reservationsNonAssignees = (List<ReservationNonAssigneeDTO>) request.getAttribute("reservationsNonAssignees");
                    if (reservationsNonAssignees == null || reservationsNonAssignees.isEmpty()) {
                %>
                <div style="text-align: center; padding: 3rem; color: #10b981; background: rgba(16, 185, 129, 0.1); border-radius: 8px;">
                    <svg width="60" height="60" viewBox="0 0 24 24" fill="none" stroke="#10b981" stroke-width="1.5" style="margin-bottom: 1rem;">
                        <path d="M22 11.08V12a10 10 0 11-5.93-9.14"/>
                        <polyline points="22 4 12 14.01 9 11.01"/>
                    </svg>
                    <p style="margin: 0; font-size: 1.1rem; font-weight: 500;">Toutes les réservations sont assignées ✓</p>
                </div>
                <%
                    } else {
                %>
                <div class="table-container">
                    <table class="market-table">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Client ID</th>
                                <th>Passagers</th>
                                <th>Hôtel</th>
                                <th>Arrivée Client</th>
                                <th>Attente Max</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                for (int i = 0; i < reservationsNonAssignees.size(); i++) {
                                    ReservationNonAssigneeDTO res = reservationsNonAssignees.get(i);
                            %>
                            <tr style="background: rgba(251, 191, 36, 0.1);">
                                <td><%= i + 1 %></td>
                                <td><%= res.getIdClient() %></td>
                                <td><%= res.getNbPassager() %></td>
                                <td><%= res.getHotelNom() %></td>
                                <td><%= res.getFormattedHeure() %></td>
                                <td><%= res.getTempsAttenteMax() %> min</td>
                            </tr>
                            <%
                                }
                            %>
                        </tbody>
                    </table>
                </div>
                <%
                    }
                %>
            </div>

            <!-- Boutons d'action -->
            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/api/planification/form" class="btn btn-secondary">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M19 12H6m0 0l6 6m-6-6l6-6"/>
                    </svg>
                    Nouvelle Date
                </a>
                <a href="${pageContext.request.contextPath}/api/reservations/form" class="btn btn-outline">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
                        <line x1="16" y1="2" x2="16" y2="6"/>
                        <line x1="8" y1="2" x2="8" y2="6"/>
                        <line x1="3" y1="10" x2="21" y2="10"/>
                    </svg>
                    Voir toutes les Réservations
                </a>
            </div>
        </main>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/templatemo-crypto-script.js"></script>
</body>
</html>
