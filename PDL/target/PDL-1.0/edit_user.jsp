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
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="../resources/bootstrap/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="../resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="../resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="../resources/bootstrap/dist/js/alert.js"></script>
        <!-- Company Style -->
        <link rel="Shortcut Icon" href="../resources/images/favicon.ico" type="image/x-icon">
        <link rel="Icon" href="../resources/images/favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="../resources/css/style-m.css">

        <title>${isUpdate == true ? 'Edit' : 'Create'} User - Info Support</title>
    </head>
    <body onload="validateForm()">
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
                    <div class="alert alert-success" style="margin-left:20px;margin-right:20px">
                        <a class="close" data-dismiss="alert">×</a>
                        <strong>Done!</strong> New User created.
                    </div>
                </c:if>
                <c:if test="${userUpdated == true}">
                    <div class="alert alert-success" style="margin-left:20px;margin-right:20px">
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
                    <div class="alert alert-danger" style="margin-left:20px;margin-right:20px">
                        <a class="close" data-dismiss="alert">×</a>
                        <script>
                            document.write(errors);
                        </script>
                    </div>
                </c:if>
                <div id="validationAlert" style="margin-left:20px;margin-right:20px"></div>
                <!-- End of Bootstrap alerts -->

                <div id="form_container">
                    <div class="leftContainer">

                        <input type="hidden" id="userId" name="userId">
                        <div class="form-group" id="formGroupUsername" style="width:100%">
                            <label for="username">Username</label>
                            <input type="text" class="form-control" id="username" name="username" onchange="validateForm()" placeholder="Enter username">
                        </div>
                        <div class="form-group" id="formGroupFirstname" style="width:100%">
                            <label for="firstname">First Name</label>
                            <input type="text" class="form-control" id="firstname" name="firstname" onchange="validateForm()" placeholder="Enter first name">
                        </div>
                        <div class="form-group" id="formGroupLastname" style="width:100%">
                            <label for="lastname">Last Name</label>
                            <input type="text" class="form-control" id="lastname" name="lastname" onchange="validateForm()" placeholder="Enter last name">
                        </div>
                        <div class="form-group" id="formGroupEmailAddress" style="width:100%">
                            <label for="emailAddress">Email Address</label>
                            <input type="text" class="form-control" id="emailAddress" name="emailAddress" onchange="validateForm()" placeholder="Enter email address">
                        </div>
                    </div>

                    <div class="rightContainer">

                        <div class="form-group" id="formGroupPassword" style="width:100%">
                            <label for="password">Password</label>
                            <input type="password" class="form-control" id="password" name="password" onchange="validateForm()" placeholder="Enter a password">
                        </div>
                        <div class="form-group" id="formGroupPosition" style="width:100%">
                            <label for="position">Position</label>
                            <input type="text" class="form-control" id="position" name="position" onchange="validateForm()" placeholder="Enter position">
                        </div>
                        <div class="form-group" id="formGroupUserRights" style="width:100%">
                            <label for="userRights">User Rights</label><br/>
                            <input type="checkbox" id="isAdmin" name="isAdmin"> Administrator
                        </div>
                    </div>
            </form>
        </div>
        <script>
            //initialize the form with variables if available
            document.getElementById('userId').value = '${userId}';
            document.getElementById('username').value = '${username}';
            document.getElementById('firstname').value = '${firstname}';
            document.getElementById('lastname').value = '${lastname}';
            document.getElementById('emailAddress').value = '${emailAddress}';
            document.getElementById('position').value = '${position}';
            document.getElementById('isAdmin').checked = ${isAdmin == true ? true : false};
            document.getElementById('password').value = '${password}';
            
            //close window
            function closeWindow() {
                console.log('canceling');
                $('#myModal').modal('show')
            }
            
            //function to refresh the parent window on save/close to reflect the updated data in the grid
            window.onunload = function() {
                window.opener.location.reload();
            };
            
            //set input color for validations
            function setValidated(id, isValidated) {
                if (isValidated) {
                    document.getElementById(id).className = 'form-group has-success';
                }
                else {
                    document.getElementById(id).className = 'form-group has-error';
                }
            }
            //use the same validations that are used on the server side
            function validateForm() {
                var regexUsername = '^.[a-zA-Z]{1,100}$';
                var regexRegular = '^.{1,100}$';
                var regexEmailAddress = '[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\.[a-zA-Z]{2,4}';
                var regexPassword = '^.{7,100}$';

                errors = "";
                //username
                var username = document.getElementById('username').value;
                if (!username || !username.match(regexUsername)) {
                    setValidated('formGroupUsername', false);
                    errors += 'Username must be 1-100 characters in size and no spaces or special characters are allowed. ';
                }
                else {
                    setValidated('formGroupUsername', true);
                }
                //first name
                var firstName = document.getElementById('firstname').value;
                if (!firstname || !firstName.match(regexRegular)) {
                    setValidated('formGroupFirstname', false);
                    errors += 'First Name must be 1-100 characters in size. ';
                }
                else {
                    setValidated('formGroupFirstname', true);
                }
                //last name
                var lastName = document.getElementById('lastname').value;
                if (!lastName || !lastName.match(regexRegular)) {
                    setValidated('formGroupLastname', false);
                    errors += 'Last Name must be 1-100 characters in size. ';
                }
                else {
                    setValidated('formGroupLastname', true);
                }
                //emailAddress
                var emailAddres = document.getElementById('emailAddress').value;
                if (!emailAddres || !emailAddres.match(regexEmailAddress)) {
                    setValidated('formGroupEmailAddress', false);
                    if (!emailAddress) {
                        errors += 'Email address may not be empty. ';
                    }
                    else {
                        errors += 'Email address is not valid. ';
                    }

                }
                else {
                    setValidated('formGroupEmailAddress', true);
                }
                //position
                var position = document.getElementById('position').value;
                if (!position || !position.match(regexRegular)) {
                    setValidated('formGroupPosition', false);
                    errors += 'Position must be 1-100 characters in size. ';
                }
                else {
                    setValidated('formGroupPosition', true);
                }
                //password
                var password = document.getElementById('password').value;
                if (!password || !password.match(regexPassword)) {
                    setValidated('formGroupPassword', false);
                    errors += 'Password must be 7-100 characters in size. ';
                }
                else {
                    setValidated('formGroupPassword', true);
                }

                //return true if there are errors
                if (errors) {
                    return true;
                }
                else {
                    return false;
                }
            }
            //save button press
            function saveForm() {
                if (validateForm()) {
                    document.getElementById('validationAlert').innerHTML = '<div class="alert alert-danger"><a class="close" data-dismiss="alert">×</a><strong>Oh snap!</strong> ' + errors + '</div>';
                }
                else {
                    document.getElementById('validationAlert').innerHTML = '';
                    //check to see which form we need to submit (edit or new)
                    if (!document.getElementById('userId').value){
                        document.getElementById('newUser').submit();
                    }
                    else {
                        document.getElementById('editUser').submit();
                    }
                }
            }
        </script>
        <hr style="width:100%;margin-top:370px"/>
        <div style="float:right;margin-right:20px;margin-top:-10px">

            <button type="button" class="btn btn-default" onclick="closeWindow()">Cancel</button>
            <button type="button" class="btn btn-primary" onclick="saveForm()">Save</button>
        </div>
        <!-- Modal Dialog for Canceling -->
        <div class="modal fade" id="myModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Unsaved data</h4>
                    </div>
                    <div class="modal-body">
                        <p>Are you sure you want to cancel? Any unsaved changes will be lost.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Continue editing</button>
                        <button type="button" class="btn btn-primary" onclick="window.close()">Yes</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
</body>
</html>