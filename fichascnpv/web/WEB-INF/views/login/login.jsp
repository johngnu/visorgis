<%--
    Document   : login
    Created on : Feb 12, 2018, 6:07:39 PM
    Author     : John Castillo
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
        <link rel="shortcut icon" type="image/png" href="<c:url value="/img/logo.png"/>"/>
        <title>Login</title>

        <!-- Bootstrap core CSS     -->
        <link href="<c:url value="/css/bootstrap.min.css"/>" media="all" rel="stylesheet" type="text/css" />
        <!-- Main Style CSS         -->
        <link href="<c:url value="/css/style.css"/>" media="all" rel="stylesheet" type="text/css" />
        <!-- Fonts and icons        -->
        <link href="<c:url value="/css/font-awesome/css/font-awesome.min.css"/>" rel="stylesheet" type="text/css" />
    </head>
    <body class="login2">
        <div class="login-wrapper">
            <a href="<c:url value="/index"/>"><img style="width: 100px" src="<c:url value="/img/logo.png"/>" /></a>
            <form action="<c:url value="/j_spring_security_check"/>" method="post" id="__login_fomr"> 
                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="icon-user"></i></span><input class="form-control"  autocomplete="off" name="j_username" placeholder="Username or email" type="text">
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <span class="input-group-addon"><i class="icon-lock"></i></span><input class="form-control" name="j_password" placeholder="Password or pin" type="password">
                    </div>
                </div>
                <input class="btn btn-lg btn-primary btn-block" type="submit" value="Login">
                <a href="<c:url value="/index"/>">Home</a> | <a href="<c:url value="/resetaccout"/>">Reset account</a> | <a href="<c:url value="/register"/>">Sign up</a>
            </form>

            <c:if test="${!empty key}">
                <div class="alert alert-danger">
                    <button class="close" data-dismiss="alert" type="button">&times;</button>User or password wrong.  try again or contact with system admin.
                </div>
            </c:if>

            <c:if test="${empty key}">
                <div class="alert alert-warning">
                    <button class="close" data-dismiss="alert" type="button">&times;</button>Exclusive use of admin.
                </div>
            </c:if>
        </div>

    </body>
    <!--   Core JS Files   -->
    <script src="<c:url value="/js/jquery-3.2.1.min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/js/bootstrap.min.js"/>" type="text/javascript"></script>
    <!-- Material Dashboard DEMO methods, don't include it in your project! -->
    <script src="<c:url value="/js/jquery.validate.js"/>" type="text/javascript"></script>
    <!-- Local scripts code --> 
    <script type="text/javascript">
        $(document).ready(function () {

            $("#__login_fomr").validate({
                rules: {
                    j_username: {
                        required: true
                    },
                    j_password: {
                        required: true
                    }
                }
            });
        });
    </script>  
</html>