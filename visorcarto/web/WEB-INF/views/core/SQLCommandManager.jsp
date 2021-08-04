<%-- 
    Document   : SQLCommandManegerr View
    Created on : 26-04-2011, 02:30:51 PM
    Author     : Johns Castillo Valencia
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>SQL Command Manager</title>    
        <!-- ALL ExtJS Framework resources -->
        <%@include file="ExtJSScripts-ES_.jsp"%>
        <!-- Extencions & plugins -->
        <script type="text/javascript" src="/ext-3.3.1/examples/ux/RowExpander.js"></script>
        <script type="text/javascript" src="/ext-3.3.1/examples/ux/TabCloseMenu.js"></script>

        <!-- Application -->
        <script type="text/javascript" src="<c:url value="/js/core/sqlmanager/entity-types.js"/>"></script> 
        <script type="text/javascript" src="<c:url value="/js/core/sqlmanager/entity.js"/>"></script>    
        <script type="text/javascript" src="<c:url value="/js/core/sqlmanager/mainapp.js"/>"></script>   
        <style type="text/css">
            body {
                font-family:'lucida grande',tahoma,arial,sans-serif;
                font-size:11px;
            }
            #start-div h2 {
                font-size: 12px;
                color: #555;
                padding-bottom:5px;
                border-bottom:1px solid #C3D0DF;
            }
            #start-div p {
                margin: 10px 0;
            }
            
            .has-error { 
                background-color: #FF9999;
            }

            .read-only {
                background-color: #E6E6E6;
            }
        </style>
    </head>
    <body>
        <div style="display:none;">
            <!-- Start page content -->
            <div id="start-div"></div>
        </div>
    </body>
</html>

