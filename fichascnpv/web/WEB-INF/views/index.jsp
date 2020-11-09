<%-- 
    Document   : index
    Created on : Oct 14, 2020, 6:05:30 PM
    Author     : John Castillo @johngnu
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!doctype html>
<html lang="es">
    <head>
        <meta charset="utf-8">
        <meta name="author" content="@johngnu">
        <link rel="icon" type="image/png" href="img/logo.png" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

        <title>Ficha CNPV</title>

        <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
        <meta name="viewport" content="width=device-width" />

        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
    </head>

    <body>
        <div class="container-fluid">
            <h4>Mapas Ficha CNPV</h4>
            <p class="text-muted">Lista de mapas implementados</p>
            <a href="<c:url value="/amanzanado"/>">Amanzanado schema</a>
            <br/>
            <a href="<c:url value="/disperso"/>">Disperso schema</a>
        </div>

    </body>

</html>


