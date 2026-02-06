<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="itu.framework.backoffice.entities.Reservation" %>

<!DOCTYPE html>
<html>
<head>
    <title>Succès</title>
</head>
<body>

<%
    Reservation reservation = (Reservation) request.getAttribute("reservation");
%>

<h2>Réservation enregistrée avec succès ✅</h2>

<ul>

</ul>

<a href="${pageContext.request.contextPath}/api/reservation/form">
    Nouvelle réservation
</a>

</body>
</html>
