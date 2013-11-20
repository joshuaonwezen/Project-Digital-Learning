<%-- 
    Document   : edit_user
    Created on : Nov 10, 2013, 9:40:25 PM
    Author     : wesley
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- DHTMLX Form -->
        <link rel="stylesheet" type="text/css" href="../resources/dhtmlx/dhtmlxForm/codebase/skins/dhtmlxform_dhx_terrace.css">
        <script src="../resources/dhtmlx/dhtmlxForm/codebase/dhtmlxcommon.js"></script>
        <script src="../resources/dhtmlx/dhtmlxForm/codebase/dhtmlxform.js"></script>
        <!-- Bootstrap (Alert messages) -->
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
        <link href="../resources/bootstrap/css/bootstrap.min.css" rel="stylesheet"> 
        <script src="../resources/bootstrap/js/bootstrap.min.js" ></script>
        <script src="../resources/bootstrap/js/alert.js"></script>
        <!-- Company Style -->
        <link rel="Shortcut Icon" href="../resources/images/favicon.ico" type="image/x-icon">
        <link rel="Icon" href="../resources/images/favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="../resources/css/style-m.css">

        <title>${isUpdate == true ? 'Edit' : 'Create'} User - Info Support</title>
    </head>
    <body>
        <div class="header">
            <h1>
                ${isUpdate == true ? 'Edit' : 'Create'} User
            </h1>
        </div>
        <c:choose>
            <c:when test="${empty userId}">
                <!-- Without userId means a new user -->
                <form id="newUser" action="new" method="post">
            </c:when>
            <c:otherwise>
                <!-- Otherwise you are editing -->
                <form id="editUser" action="edit" method="post">
            </c:otherwise>
         </c:choose>
                    
         <!-- Bootstrap alerts -->
         <c:if test="${userCreated == true}">
             <div class="alert alert-success">  
                 <a class="close" data-dismiss="alert">×</a>
                 <strong>Done!</strong> New User created.
             </div>
         </c:if>
         <c:if test="${userUpdated == true}">
             <div class="alert alert-success">
                 <a class="close" data-dismiss="alert">×</a>
                 <strong>Done!</strong> User is successfully updated.
             </div>
         </c:if>
         <c:if test="${errors != null}">
             <script>
                 //concat the errors in a var
                 var errors = '<strong>Oh snap! </strong>';
                              
                 <c:forEach var="error" items="${errors}" varStatus="count">
                     errors += '${error}';
                 </c:forEach>
             </script>
             <div class="alert alert-danger">
                 <a class="close" data-dismiss="alert">×</a>
                 <script>
                     document.write(errors);
                 </script>
             </div>
         </c:if>
             <div id="validationAlert"></div>
         <!-- End of Bootstrap alerts -->
         
         <div id="form_container"></div>
         <script>
             var userForm, formData;
             //json format structure: we use this to initialize our form
             formStructure = [
                 {type: "settings", position: "label-top"},
                 {type: "block", width: 800, height: 500, list: [
                         {type: "hidden", name: 'userId'},
                         {type: "input", name: 'username', label: 'Username', style:'width:350px;height:25px;'},
                         {type: "input", name: "firstname", label: "First Name", style:'width:350px;height:25px;', offsetTop: 20},
                         {type: "input", name: "lastname", label: "Last Name", style:'width:350px;height:25px;', offsetTop: 20},
                         {type: "input", name: "emailAddress", label: "E-mail address", style:'width:350px;height:25px;', offsetTop: 20},
                         {type: "newcolumn", offset: 45},
                         {type: "input", name: "position", label: "Position", style:'width:350px;height:25px;'},
                         {type: "password", name: "password", label: "Password", style:'width:350px;height:25px;', offsetTop: 20},
                         {type: "checkbox", name: "isAdmin", label: "Administrator", checked: true, offsetTop: 20, position: "label-right"}
                     ]}
             ];
             //create our userform
             userForm = new dhtmlXForm("form_container", formStructure);
             userForm.setFontSize('14px');
             
             //use the same validations that are used on the server side
             var errors;
             function validateForm(){
                 var regexUsername = '[a-zA-Z]{1,100}';
                 var regexRegular = '.{1,100}';
                 var regexEmailAddress = '[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[a-zA-Z]{2,4}';
                 var regexPassword = '.{7,100}';
                 
                 errors = "";
                 //username
                 if (!userForm.getItemValue('username')){
                     errors += 'Username may not be empty. ';
                 }
                 else if (!userForm.getItemValue('username').match(regexUsername)){
                     errors += 'Username must be 1-100 characters in size and no spaces or special characters are allowed. ';
                 }
                 //first name
                 if (!userForm.getItemValue('firstname')){
                     errors += 'First Name may not be empty. ';
                 }
                 else if (!userForm.getItemValue('firstname').match(regexRegular)){
                     errors += 'First Name must be 1-100 characters in size. ';
                 }
                 //last name
                 if (!userForm.getItemValue('lastname')){
                     errors += 'Last Name may not be empty. ';
                 }
                 else if (!userForm.getItemValue('lastname').match(regexRegular)){
                     errors += 'Last Name must be 1-100 characters in size. ';
                 }
                 //emailaddress
                 if (!userForm.getItemValue('emailAddress')){
                     errors += 'Email address may not be empty. ';
                 }
                 else if (!userForm.getItemValue('emailAddress').match(regexEmailAddress)){
                     errors += 'Email address is not valid. ';
                 }
                 //position
                 if (!userForm.getItemValue('position')){
                     errors += 'Position may not be empty. ';
                 }
                 else if (!userForm.getItemValue('position').match(regexRegular)){
                     errors += 'Position must be 1-100 characters in size. ';
                 }
                 //password
                 if (!userForm.getItemValue('password')){
                     errors += 'Password may not be empty. ';
                 }
                 else if (!userForm.getItemValue('password').match(regexPassword)){
                     errors += 'Password must be 7-100 characters in size. ';
                 }
                 
                 if (errors){
                     return false;
                 }
                 else{
                     return true;
                 }
                 
             }
             //save button press
             function buttonClicked(){
                 if (!validateForm()){
                     document.getElementById('validationAlert').innerHTML = '<div class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><strong>Oh snap!</strong> ' + errors + '</div>';
                 }
                 else{
                     document.getElementById('validationAlert').innerHTML = '';
                     console.log('success');
                     document.forms[0].submit();
                 }
             }
             //initialize the form with values if available
             userForm.setItemValue("userId", '${userId}');
             userForm.setItemValue("username", '${username}');
             userForm.setItemValue("firstname", '${firstname}');
             userForm.setItemValue("lastname", '${lastname}');
             userForm.setItemValue("emailAddress", '${emailAddress}');
             userForm.setItemValue("position", '${position}');
             userForm.setItemValue("isAdmin", ${isAdmin == true ? true : false});
             userForm.setItemValue("password", '${password}');
             
             //function to refresh the parent window on save/close to reflect the updated data in the grid
             window.onunload = function(){
                window.opener.location.reload();
             };
             
         </script>
         </form>
             <div class="hr"><hr /></div>
             
             <button class="buttonSave" onclick="buttonClicked()">Save</button>
             <button class="buttonClose" onclick="if (confirm('Are you sure you want to cancel? Any unsaved changes will be lost.'))
                    window.close()">Cancel</button>
    </body>
</html>