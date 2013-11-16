<%-- 
    Document   : edit_user
    Created on : Nov 10, 2013, 9:40:25 PM
    Author     : wesley

    Todo       : pagina moet worden gesloten en onderliggende pagina moet worden gerefreshed
                 wanneer er successvol opgeslagen is
    Todo       : maximum character limiet moet er komen op de input velden
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- DHTMLX Form -->
        <link rel="stylesheet" type="text/css" href="../resources/dhtmlx/dhtmlxForm/codebase/skins/dhtmlxform_dhx_terrace.css">
        <script src="../resources/dhtmlx/dhtmlxForm/codebase/dhtmlxcommon.js"></script>
        <script src="../resources/dhtmlx/dhtmlxForm/codebase/dhtmlxform.js"></script>
        <title>${update == true ? 'Edit' : 'Create'} User - Info Support</title>
    </head>
    <body>
        <h1>
            ${update == true ? 'Edit' : 'Create'} User
            <hr width="50%" align="left"/>
        </h1>

        <c:choose>
            <c:when test="${empty userId}">
                <!-- Without userId mean a new user -->
                <form id="newUser" action="new" method="post">
                </c:when>
                <c:otherwise>
                    <!-- Otherwise you are editing -->
                    <form id="editUser" action="edit" method="post">
                    </c:otherwise>
                </c:choose> 

                <c:if test="${errors != null}">
                    <!-- Mochten er foutmeldingen zijn, dan worden ze hier getoond -->
                    <c:forEach var="error" items="${errors}">
                        <p><font color="red">"${error}"</font></p>
                        </c:forEach>
                    </c:if>

                <div id="form_container" style="width:300px;height:200px;"></div>

                <script>
                    var userForm, formData;

                    //json format structure: we use this to initialize our form
                    formStructure = [
                        {type: "settings", position: "label-top"},
                        {type: "block", width: 500, list: [
                                {type: "hidden", name: 'userId'},
                                {type: "input", name: 'username', label: 'Username', width: 200, required: true},
                                {type: "input", name: "firstname", label: "First Name", width: 200, offsetTop: 10, required: true},
                                {type: "input", name: "lastname", label: "Last Name", width: 200, offsetTop: 10, required: true},
                                {type: "input", name: "emailAddress", label: "E-mail address", width: 200, offsetTop: 10, validate: "ValidEmail", required: true},
                                {type: "button", name: "save", value: "Save", offsetTop: 20},
                                {type: "newcolumn", offset: 50},
                                {type: "input", name: "position", label: "Position", width: 200, required: true},
                                {type: "password", name: "password", label: "Password", width: 200, offsetTop: 10, required: true},
                                {type: "checkbox", name: "isAdmin", label: "Administrator", checked: true, offsetTop: 10, position: "label-right"}
                            ]}
                    ];

                    userForm = new dhtmlXForm("form_container", formStructure);
                    userForm.enableLiveValidation(true);
                    userForm.attachEvent("onButtonClick", function(id) {
                        switch (id) {
                            case 'save':
                                if (userForm.validate()) {
                                    document.forms[0].submit();
                                    //@todo
                                    //setTimeout(console.log('workds'), 2000);
                                }
                                break;
                        }
                    });
                    //initialize the form with input if we are updating the user
                    if ('${update}') {
                        userForm.setItemValue("userId", '${userId}');
                        userForm.setItemValue("username", '${username}');
                        userForm.setItemValue("firstname", '${firstName}');
                        userForm.setItemValue("lastname", '${lastName}');
                        userForm.setItemValue("emailAddress", '${emailAddress}');
                        userForm.setItemValue("position", '${position}');
                        userForm.setItemValue("isAdmin", ${isAdmin == true ? true : false});
                        userForm.setItemValue("password", '${password}');
                    }
                </script>
            </form>
    </body>
</html>