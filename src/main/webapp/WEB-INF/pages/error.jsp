<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Erreur</title>
</head>
<body>

<h2>❌ Erreur lors de l’enregistrement</h2>

<p style="color:red;">
    <%= request.getAttribute("error-message") %>
</p>

<a href="${pageContext.request.contextPath}/api/reservation/form">
    Retour au formulaire
</a>

</body>
</html>
