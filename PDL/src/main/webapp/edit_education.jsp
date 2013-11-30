<!--
@author Shahin Mokhtar
-->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index_nl_NL" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="../resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="../resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="../resources/bootstrap/dist/js/alert.js"></script>
         <!-- Moment JS-->
        <script src="../resources/moment/moment-m.js" type="text/javascript"></script>
        <!-- Company Style -->
        <link rel="Shortcut Icon" href="../resources/images/favicon.ico" type="image/x-icon">
        <link rel="Icon" href="../resources/images/favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="../resources/css/editmode.css">

        <title><c:if test="${isUpdate == true}"><fmt:message key="edit.popup.edit"/><fmt:message key="education.educatio"/> - Info Support</c:if>
            <c:if test="${isUpdate == false}"><fmt:message key="edit.popup.create"/><fmt:message key="education.educatio"/> - Info Support</c:if></title>
        <style type="text/css">
            textarea {
            }
            textarea {
                min-width: 350px; 
                min-height: 100px;
                max-width: 300px; 
                max-height: 300px;
            }
        </style>
    </head>
    <body onload="validateForm()">
        <div class="header">
            <h1>
                <c:if test="${isUpdate == true}"><fmt:message key="edit.popup.edit"/> <fmt:message key="education.education"/></c:if>
                <c:if test="${isUpdate == false}"><fmt:message key="edit.popup.create"/> <fmt:message key="education.education"/></c:if>
            </h1>
        </div>
        <c:choose>
            <c:when test="${empty educationId}">
                <!-- education id = empty, new education -->
                <form id="newEducation" action="new" method="post">
                </c:when>
                <c:otherwise>
                    <!-- else edit education -->
                    <form id="editEducation" action="edit" method="post">
                    </c:otherwise>
                </c:choose>

                <!-- Bootstrap alerts -->
                <c:if test="${educationCreated == true}">
                    <div class="alert alert-success" style="margin-left:20px;margin-right:20px">
                        <a class="close" data-dismiss="alert">×</a>
                        <strong><fmt:message key="popup.done"/></strong> <fmt:message key="education.edit.popup.create"/>
                    </div>
                </c:if>
                <c:if test="${educationUpdated == true}">
                    <div class="alert alert-success" style="margin-left:20px;margin-right:20px">
                        <a class="close" data-dismiss="alert">×</a>
                        <strong><fmt:message key="popup.done"/></strong> <fmt:message key="education.edit.popup.update"/>
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

                        <input type="hidden" name="educationId" id="educationId" value="${educationId}">

                        <div class="form-group" id="formGroupDateFrom" style="width:50%">
                            <label for="dateFrom"><fmt:message key="education.from"/></label>
                            <input type="date" class="form-control" id="dateFrom" name="dateFrom" onchange="validateForm()" value="${dateFrom}">
                        </div>
                        
                        <div class="form-group" id="formGroupDateTill" style="width:50%">
                            <label for="dateTill"><fmt:message key="education.till"/></label>
                            <input type="date" class="form-control" id="dateTill" name="dateTill" onchange="validateForm()" value="${dateTill}">
                        </div>

                        <div class="form-group" id="formGroupName" style="width:100%">
                            <label for="name"><fmt:message key="education.name"/></label>
                            <input type="text" class="form-control" id="name" name="name" onchange="validateForm()" value="${name}">
                        </div>
                        
                        <div class="form-group" id="formGroupProfession" style="width:100%">
                            <label for="profession"><fmt:message key="education.profession"/></label>
                            <input type="text" class="form-control" id="profession" name="profession" onchange="validateForm()" value="${profession}">
                        </div>
                        
                        <div class="form-group" id="formGroupUser" style="width:100%">
                            <input type="hidden" class="form-control" id="user" name="user" value="${loggedInUserId}">
                        </div>

                    </div>

                    <div class="rightContainer">

                        <div class="form-group" id="formGroupDescription" style="width:100%">
                            <label for="description"><fmt:message key="education.description"/></label>
                            <textarea class="form-control" id="description" name="description" onchange="validateForm()">${description}</textarea>
                        </div>

                    </div>
            </form>
        </div>
        <script>
            //initialize the form with variables if available
            document.getElementById('educationId').value = '${educationId}';
            console.log('date is: ' + moment('${dateFrom}', "YYYY-MM-DD"));
            console.log('date is: ' + moment('${dateTill}', "YYYY-MM-DD"));
            document.getElementById('name').value = '${name}';
            document.getElementById('profession').value = '${profession}';
            document.getElementById('description').value = '${description}';

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
                var regexRegular = '^[a-zA-Z0-9_ ]{1,25}$';
                var regexDesc = '^[a-zA-Z0-9_ ]{1,300}$';

                errors = "";
                
                //dateFrom
                var dateFrom = document.getElementById('dateFrom').value;
                var dateMomentFrom = moment(dateFrom);
                
                if (!dateFrom || !dateMomentFrom.isValid()) {
                    setValidated('formGroupDateFrom', false);
                    errors += 'Date from may not be empty. ';
                }
                else {
                    setValidated('formGroupDateFrom', true);
                }
                
                //dateTill
                var dateTill = document.getElementById('dateTill').value;
                var dateMomentTill = moment(dateTill);
                
                if (!dateTill || !dateMomentTill.isValid()) {
                    setValidated('formGroupDateTill', false);
                    errors += 'Date till may not be empty. ';
                }
                else {
                    setValidated('formGroupDateTill', true);
                }
                
                
                
                //name
                var name = document.getElementById('name').value;
                if (!name || !name.match(regexRegular)) {
                    setValidated('formGroupName', false);
                    errors += 'Name must be 1-25 characters in size';
                }
                else {
                    setValidated('formGroupName', true);
                }
                
                //profession
                var profession = document.getElementById('profession').value;
                if (!profession || !profession.match(regexRegular)) {
                    setValidated('formGroupProfession', false);
                    errors += 'Profession must be 1-25 characters in size';
                }
                else {
                    setValidated('formGroupProfession', true);
                }
                
                //description
                var description = document.getElementById('description').value;
                if (!description || !description.match(regexDesc)) {
                    setValidated('formGroupDescription', false);
                    errors += 'Description must be 1-300 characters in size. ';
                }
                else {
                    setValidated('formGroupDescription', true);
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
                    if (!document.getElementById('educationId').value) {
                        document.getElementById('newEducation').submit();
                    }
                    else {
                        document.getElementById('editEducation').submit();
                    }
                }
            }
        </script>
        <hr style="width:100%;margin-top:370px"/>
        <div style="float:right;margin-right:20px;margin-top:-10px">

            <button type="button" class="btn btn-default" onclick="closeWindow()"><fmt:message key="edit.popup.cancel"/></button>
            <button type="button" class="btn btn-primary" onclick="saveForm()"><fmt:message key="edit.popup.save"/></button>
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
                        <p><fmt:message key="edit.popup.confirmation.message"/></p>
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