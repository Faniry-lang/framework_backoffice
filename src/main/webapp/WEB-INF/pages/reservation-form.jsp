<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="itu.framework.backoffice.entities.Hotel" %>

<!DOCTYPE html>
<html>
<head>
    <title>Créer une réservation</title>
</head>
<body>

<h2>Nouvelle réservation</h2>

<form method="post" action="/api/reservation/save">

    <label>Client ID :</label><br>
    <input type="text" name="idClient" required><br><br>

    <label>Nombre de passagers :</label><br>
    <input type="number" name="nbPassager" min="1" required><br><br>

    <label>Hôtel :</label><br>
    <select name="idHotel" required>
        <option value="">-- Choisir un hôtel --</option>
        <%
            List<Hotel> hotels = (List<Hotel>) request.getAttribute("hotels");
            if (hotels != null) {
                for (Hotel h : hotels) {
        %>
        <option value="<%= h.getId() %>">
            <%= h.getNom() %>
        </option>
        <%
                }
            }
        %>
    </select><br><br>

    <label>Date et heure d’arrivée :</label><br>
    <!-- Format ISO pour LocalDateTime -->
    <input type="datetime-local" name="dateHeureArrivee" required><br><br>

    <button type="submit">Enregistrer</button>
</form>

</body>
</html>
