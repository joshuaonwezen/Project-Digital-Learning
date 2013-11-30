<%-- 
    Document   : index
    Created on : Oct 29, 2013, 10:49:38 AM
    Author     : wesley
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index_nl_NL" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bootstrap/dist/js/alert.js"></script>
        <!-- Company style -->
        <link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
        <link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>

        <title>Login - Info Support</title>
    </head>
    <body>
        <!-- MADE IN HOLLAND -->
        <!--Start nav bar-->
        <nav class="navbar navbar-inverse" role="navigation">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.jsp"><img src="resources/images/Logo.png"></a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="margin-top:12px">
            </div><!-- /.navbar-collapse -->
        </nav>
        <!-- eof navbar-->
        <c:if test="${errors != null}">
            <script>
                //concat the errors in a var
                var errors = '<strong>Oh snap! </strong>';

                <c:forEach var="error" items="${errors}" varStatus="count">
                errors += '${error}';
                </c:forEach>
            </script>
            <div class="alert alert-danger" style="margin-left:20px;margin-right:20px">
                <a class="close" data-dismiss="alert">×</a>
                <script>
                    document.write(errors);
                </script>
            </div>
        </c:if>

        <div id="validationAlert" style="margin-left:20px;margin-right:20px"></div>
        <form id="login" action="login" method="post">



            <div id="form_container" style="margin-left:auto; margin-right:auto;width:350px;margin-top:100px">
                <div class="form-group" id="formGroupUsername" style="width:100%">
                    <label for="username">Username</label>
                    <div class="input-group">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
                        <input type="text" class="form-control" id="username" name="username" placeholder="Enter your username">
                    </div>
                </div>
                <div class="form-group" id="formGroupPassword" style="width:100%">
                    <label for="password">Password</label>
                    <div class="input-group">
                        <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Enter your password">
                </div>
                </div>

            </div>

            <div id="form_container" style="margin-left:auto; margin-right:auto;width:350px">
                <button type="button" class="btn btn-primary" style="width:100%;" onclick="login()">Login</button>
            </div>
            <div class="well" style="margin-top:150px"><center>You can only login with an InfoSupport account.<br/> No account yet?</br>Contact the InfoSupport Admin.</center></div>
            
            <script>
                //use the same validations that are used on the server side
                function validateForm() {
                    errors = "";
                    //username
                    var username = document.getElementById('username').value;
                    if (!username) {
                        errors += 'Username may not be empty. ';
                    }
                    //password
                    var password = document.getElementById('password').value;
                    if (!password) {
                        errors += 'Password may not be empty. ';
                    }

                    //return true if there are errors
                    if (errors) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                //login button press
                function login() {
                    if (validateForm()) {
                        document.getElementById('validationAlert').innerHTML = '<div class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><strong>Oh snap!</strong> ' + errors + '</div>';
                    }
                    else {
                        document.getElementById('validationAlert').innerHTML = '';
                        //check to see which form we need to submit (edit or new)
                        document.getElementById('login').submit();
                    }
                }

            </script>



        </form>
    </div>
</body>
</html>
