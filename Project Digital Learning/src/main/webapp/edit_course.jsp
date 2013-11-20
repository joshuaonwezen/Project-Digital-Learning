<%-- 
    Document   : edit_course
    Created on : Nov 15, 2013, 5:06:44 PM
    Author     : wesley

    Todo       : pagina moet worden gesloten en onderliggende pagina moet worden gerefreshed
                 wanneer er successvol opgeslagen is
    Todo       : maximum character limiet moet er komen op de input velden
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${update == true ? 'Edit' : 'Create'} Course</title>

        <!-- DHTMLX Form -->
        <link rel="stylesheet" type="text/css" href="../resources/dhtmlx/dhtmlxForm/codebase/skins/dhtmlxform_dhx_terrace.css">
        <script src="../resources/dhtmlx/dhtmlxForm/codebase/dhtmlxcommon.js"></script>
        <script src="../resources/dhtmlx/dhtmlxForm/codebase/dhtmlxform.js"></script>
    </head>
    <body>
        <h1>
            ${update == true ? 'Edit' : 'Create'} Course
            <hr width="50%" align="left"/>
        </h1>

        <c:choose>
            <c:when test="${empty courseId}">
                <!-- Without courseId means a new course -->
                <form id="newCourse" action="new" method="post">
                </c:when>
                <c:otherwise>
                    <!-- Otherwise you are editing -->
                    <form id="editCourse" action="edit" method="post">
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
                                {type: "hidden", name: 'courseId'},
                                {type: "input", name: 'name', label: 'Name', width: 200, required: true},
                                {type: "select", name: 'levelValues', label: "Level", width: 200, offsetTop: 10, required: true, options:[
                                    {text: "Beginner", value: "Beginner", selected: '${'Beginner' == level ? 'true' : 'false'}'},
                                    {text: "Intermediate", value: "Intermediate", selected: '${'Intermediate' == level ? 'true' : 'false'}'},
                                    {text: "Advanced", value: "Advanced", selected: '${'Advanced' == level ? 'true' : 'false'}'}
                                ]},
                                {type: "select", name: 'ownerValues', label: "Owner", width: 200, offsetTop: 10, required: true, options:[
                                    <c:forEach var='user' items='${users}'>
                                      {text: '${user.firstname}' + ' ${user.lastname}', value: ${user.userId}, selected: '${user.userId == ownerId ? 'true' : 'false'}'},
                                    </c:forEach>
                                ]},
                                {type: "input", name: "description", label: "Description", rows: 3, width: 400, offsetTop: 10, required: true}
                            ]}
                    ];

                    courseForm = new dhtmlXForm("form_container", formStructure);
                    courseForm.enableLiveValidation(true);
                    courseForm.attachEvent("onButtonClick", function(id) {
                        switch (id) {
                            case 'save':
                                if (courseForm.validate()) {
                                    document.forms[0].submit();
                                }
                                break;
                        }
                    });
                    //initialize the form with input if we are updating the course
                    if ('${update}') {
                        courseForm.setItemValue("courseId", '${courseId}');
                        courseForm.setItemValue("name", '${name}');
                        courseForm.setItemValue("description", '${description}');
                    }
                </script>
                <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
                <link href="../resources/select2/select2.css" rel="stylesheet"/>
    <script src="../resources/select2/select2.js"></script>
    
        <div id="e12" style="width:50%"></div>
        Skills
                <script>
                        
        $("#e12").select2({tags:["red", "green", "blue"]});
    </script>
                
                
            </form>
    </body>
</html>