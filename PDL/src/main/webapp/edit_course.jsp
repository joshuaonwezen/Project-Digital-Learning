<%-- 
    Document   : edit_course
    Created on : Nov 15, 2013, 5:06:44 PM
    Author     : wesley

    TODO:
        1. kleurvalidatie voor skills
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index" />
<html  lang="${language}">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="../resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="../resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="../resources/bootstrap/dist/js/alert.js"></script>
        <!-- Select2 -->
        <link href="../resources/select2/select2.css" rel="stylesheet"/>
        <script src="../resources/select2/select2.js"></script>
        <!-- Company Style -->
        <link rel="Shortcut Icon" href="../resources/images/favicon.ico" type="image/x-icon">
        <link rel="Icon" href="../resources/images/favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href="../resources/css/editmode.css">

        <title><c:if test="${isUpdate == true}"><fmt:message key="edit.popup.edit"/><fmt:message key="course.course"/> - Info Support</c:if>
            <c:if test="${isUpdate == false}"><fmt:message key="edit.popup.create"/><fmt:message key="course.course"/> - Info Support</c:if></title>
    </head>
    <body onload="validateForm()">
        <div class="header">
            <h1>
                <c:if test="${isUpdate == true}"><fmt:message key="edit.popup.edit"/> <fmt:message key="course.course"/></c:if>
                <c:if test="${isUpdate == false}"><fmt:message key="edit.popup.create"/> <fmt:message key="course.course"/></c:if>
            </h1>
        </div>
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

                <!-- Bootstrap alerts -->
                <c:if test="${courseCreated == true}">
                    <div class="alert alert-success" style="margin-left:20px;margin-right:20px">
                        <a class="close" data-dismiss="alert">×</a>
                        <strong><fmt:message key="popup.done"/></strong> <fmt:message key="course.edit.popup.new.create"/>
                    </div>
                </c:if>
                <c:if test="${courseUpdated == true}">
                    <div class="alert alert-success" style="margin-left:20px;margin-right:20px">
                        <a class="close" data-dismiss="alert">×</a>
                        <strong><fmt:message key="popup.done"/></strong> <fmt:message key="course.edit.popup.update"/>
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

                        <input type="hidden" id="courseId" name="courseId">

                        <div class="form-group" id="formGroupName" style="width:100%">
                            <label for="name"><fmt:message key="course.name"/></label>
                            <input type="text" class="form-control" id="name" name="name" onchange="validateForm()" placeholder="<fmt:message key="placeholder.name"/>">
                        </div>
                        <div class="form-group" id="formGroupLevel" style="width:100%">
                            <label for="levelValues"><fmt:message key="course.level"/></label>
                            <select class="form-control" id="levelValues" name="levelValues" onchange="validateForm()">
                                <option value="Beginner" ${'Beginner' == level ? 'selected' : ''}>Beginner</option>
                                <option value="Intermediate" ${'Intermediate' == level ? 'selected' : ''}>Intermediate</option>
                                <option value="Advanced" ${'Advanced' == level ? 'selected' : ''}>Advanced</option>
                            </select>
                        </div>
                        <div class="form-group" id="formGroupOwner" style="width:100%">
                            <label for="ownerValues"><fmt:message key="course.owner"/></label>
                            <select class="form-control" id="ownerValues" name="ownerValues" onchange="validateForm()">
                                <c:forEach var='user' items='${users}'>
                                    <option value="${user.userId}" ${user.userId == owner.userId ? 'selected' : ''}>${user.firstname} ${user.lastname}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group has-success" id="formGroupSkills" style="width:100%">
                            <label for="skills"><fmt:message key="course.skills"/></label>
                            <input type="hidden" id="tagSkills" name="tagSkills" onchange="validateForm()" placeholder="&nbsp;<fmt:message key="placeholder.skills.seperated"/>" style="width:100%">
                            <script>
                                //set all available skills from the database in the multiselect
                                var arrSkills = new Array();
                                <c:forEach var='skill' items='${skills}'>
                                    arrSkills.push('${skill.name}');
                                </c:forEach>
                                $('#tagSkills').select2({tags: arrSkills, tokenSeparators: [",", " "]});
                            </script>
                        </div>
                    </div>

                    <div class="rightContainer">

                        <div class="form-group" id="formGroupDescription" style="width:100%">
                            <label for="description"><fmt:message key="course.description"/></label>
                            <textarea class="form-control" rows="4" id="description" name="description" onchange="validateForm()" placeholder="<fmt:message key="placeholder.description"/>" style="resize: none"></textarea>
                        </div>
                        <div class="form-group" id="formGroupIsVisible" style="width:100%">
                            <label for="isVisible"><fmt:message key="course.visable"/></label><br/>
                            <input type="checkbox" id="isVisible" name="isVisible"> Visible
                        </div>
                    </div>
            </form>
        </div>
        <script>
            //initialize the form with variables if available
            document.getElementById('courseId').value = '${courseId}';
            document.getElementById('name').value = '${name}';
            document.getElementById('description').value = '${description}';
            document.getElementById('isVisible').checked = ${isVisible == true ? true : false};
            var arrayCourseSkills = '${courseSkills}'.split(',');
$('#tagSkills').select2('val', [arrayCourseSkills]);


            //now set the courseSkills

            //close window
            function closeWindow() {
                console.log('canceling');
                $('#myModal').modal('show')
            }

            //function to refresh the parent window to reflect the updated data in the grid
            var isModified = false; // true when the form was submitted
            window.onunload = function() {
                if (isModified){ // only on save/edit
                    window.opener.location.reload();
                }
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
                var regexRegular = '^.{1,100}$';

                errors = "";
                //name
                var name = document.getElementById('name').value;
                if (!name || !name.match(regexRegular)) {
                    setValidated('formGroupName', false);
                    errors += 'Name must be 1-100 characters in size. ';
                }
                else {
                    setValidated('formGroupName', true);
                }
                //level
                var level = document.getElementById('levelValues').value;
                if (!level) {
                    setValidated('formGroupLevel', false);
                    errors += 'Level must be chosen. ';
                }
                else {
                    setValidated('formGroupLevel', true);
                }
                //owner
                var owner = document.getElementById('ownerValues').value;
                if (!owner) {
                    setValidated('formGroupOwner', false);
                    errors += 'Owner must be chosen. ';
                }
                else {
                    setValidated('formGroupOwner', true);
                }
                //description
                var description = document.getElementById('description').value;
                if (!description || !description.match(regexRegular)) {
                    setValidated('formGroupDescription', false);
                    errors += 'Description must be 1-100 characters in size. ';
                }
                else {
                    setValidated('formGroupDescription', true);
                }
                //skills
                var skills = $('#tagSkills').val();
                if (!skills) {
                    //@todo setValidated('formGroupSkills', false);
                    errors += 'Please choose at least one skill. ';
                }
                else {
                    //@todo setValidated('formGroupSkills', true);
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
                    isModified = true; // set true so we have to refresh the parent when closing
                    document.getElementById('validationAlert').innerHTML = '';
                    //check to see which form we need to submit (edit or new)
                    if (!document.getElementById('courseId').value) {
                        document.getElementById('newCourse').submit();
                    }
                    else {
                        document.getElementById('editCourse').submit();
                    }
                }
            }
        </script>
        <hr style="width:100%;margin-top:370px"/>
        <div style="float:right;margin-right:20px;margin-top:-10px;margin-bottom:10px">

            <button type="button" class="btn btn-default" onclick="closeWindow()"><fmt:message key="edit.popup.cancel"/></button>
            <button type="button" class="btn btn-primary" onclick="saveForm()"><fmt:message key="edit.popup.save"/></button>
        </div>
        <!-- Modal Dialog for Canceling -->
        <div class="modal fade" id="myModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title"><fmt:message key="edit.popup.unsaved"/></h4>
                    </div>
                    <div class="modal-body">
                        <p><fmt:message key="edit.popup.confirmation.message"/></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="edit.popup.continue"/></button>
                        <button type="button" class="btn btn-primary" onclick="window.close()"><fmt:message key="edit.popup.yes"/></button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
</body>