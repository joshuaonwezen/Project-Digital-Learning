<%-- 
    Document   : index
    Created on : Oct 29, 2013, 10:49:38 AM
    Author     : wesley
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login - Info Support</title>
        <!-- DHTMLX Form -->
        <link rel="stylesheet" type="text/css" href="resources/dhtmlx/dhtmlxForm/codebase/skins/dhtmlxform_dhx_terrace.css">
        <link rel="stylesheet" type="text/css" href="resources/css/style.css">
        <script src="resources/dhtmlx/dhtmlxForm/codebase/dhtmlxcommon.js"></script>
        <script src="resources/dhtmlx/dhtmlxForm/codebase/dhtmlxform.js"></script>
    </head>
    <body>
        <div id="header">
            <div id="header_logo">
                <img src="resources/images/Logo.png">
            </div>
            <div id="header_nav">
                <ul>
                    <li>
                        <a href="#">Settings</a>
                        <ul>
                            <li><a class="button" href="information.jsp">Information</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
        <div class="loginform">
            <c:if test="${errors != null}">
                <!-- Mochten er foutmeldingen zijn, dan worden ze hier getoond -->
                <c:forEach var="error" items="${errors}">
                    <p><font color="red">"${error}"</font></p>
                    </c:forEach>
                </c:if>
            <form id="login" action="login" method="post">
                <div id="form_container" style="width:300px;height:200px;"></div>

                <script>
                    var userForm, formData;

                    //json format structure: we use this to initialize our form
                    formStructure = [
                        {type: "settings", position: "label-top"},
                        {type: "block", width: 500, list: [
                                {type: "input", name: 'username', label: 'Username', width: 200, required: true},
                                {type: "password", name: "password", label: "Password", width: 200, offsetTop: 10, required: true},
                                {type: "button", name: "login", value: "Login", offsetTop: 20}
                            ]}
                    ];

                    loginForm = new dhtmlXForm("form_container", formStructure);
                    loginForm.enableLiveValidation(true);
                    loginForm.attachEvent("onButtonClick", function(id) {
                        switch (id) {
                            case 'login':
                                if (loginForm.validate()) {
                                    document.forms[0].submit();
                                }
                                break;
                        }
                    });
                </script>
            </form>
        </div>
    </body>
</html>
