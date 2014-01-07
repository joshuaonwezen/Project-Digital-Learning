<%-- 
    Document   : i18n
    Created on : 18-dec-2013, 11:18:42
    Author     : Martijn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index" />

<!DOCTYPE html>
<html  lang="${language}">
    <head>
        <!--Company Style-->
        <link rel="stylesheet" type="text/css" href="resources/css/homepage.css">
        <link rel="icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bootstrap/dist/js/alert.js"></script>
        <!-- Moment JS-->
        <script src="resources/moment/moment-m.js" type="text/javascript"></script>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Internationalisation - Info Support</title>
    </head>
    <body>
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
                <a class="navbar-brand" href="/PDL/homepage"><img src="resources/images/Logo.png"></a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="margin-top:12px">
                <ul class="nav navbar-nav">
                    <li><a href="/PDL/homepage">Home</a></li>
                    <li><a href="/PDL/courses"><fmt:message key="navbar.course"/></a></li>
                        <c:if test="${loggedInIsAdmin || loggedInIsTeacher || loggedInIsManager == true}">
                        <li class="dropdown active">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Management <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="/PDL/management">Management</a></li>
                                <li class="divider"></li>
                                <li><a href="/PDL/i18n_nl">Internationalisation</a></li>
                            </ul>
                        </li> 
                        </c:if>
                    <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                        <c:if test="${loggedInIsAdmin || loggedInIsManager == true}">
                        <li><a href="/PDL/vga">VGA</a></li>
                        </c:if>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><fmt:message key="navbar.settings"/> <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="index.jsp"><fmt:message key="navbar.logout"/></a></li>
                            <li class="divider"></li>
                            <li><a href="#">Help</a></li>
                            <li><a href="#"><fmt:message key="navbar.problem"/></a></li>
                            <li>
                                <a >
                                    <form>
                                        <select id="language" name="language" onchange="submit()">
                                            <option value="en_US" ${language == 'en_US' ? 'selected' : ''}>English</option>
                                            <option value="nl_NL" ${language == 'nl_NL' ? 'selected' : ''}>Nederlands</option>
                                        </select>
                                    </form>
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </nav>
        <!-- eof navbar-->

        <div class="main">
            <div id="main_left">
                <ul class="nav nav-pills nav-stacked" style="width:150px">
      <li>
                        <a href="/PDL/i18n_nl">
                            Dutch <span class="pull-right"><img src="resources/images/dutch_flag.png"/></span>
                        </a>
                    </li>
                    <li  class="active">
                        <a href="/PDL/i18n_en">
                            English <span class="pull-right"><img src="resources/images/uk_flag.png"/></span>
                        </a>
                    </li>
                </ul><br/>
             <button type="submit" class="btn btn-default" onclick="document.getElementById('updateI18n').submit();" style="width:150px"><fmt:message key="edit.popup.save"/></button>
            </div>

            <div id="main_right">
            <c:choose>
                <c:when test="${needsUpdate}">
                    <div class="alert alert-warning" id="alertNeedsUpdate">
                        <a class="close" data-dismiss="alert">×</a>
                        <h4><fmt:message key="i18n.trans.new"/>!</h4> <fmt:message key="i18n.trans.last"/> <script>document.write(moment('${lastUpdatedOn}').fromNow());</script> <fmt:message key="course.teacher"/>  
                        <c:choose>
                            <c:when test="${loggedInUsername == lastUpdatedBy.username}"><fmt:message key="i18n.trans.you"/>.</c:when>
                            <c:otherwise>${lastUpdatedBy.firstname} ${lastUpdatedBy.lastname}.</c:otherwise>
                        </c:choose>
                        <fmt:message key="i18n.trans.apply"/>      
                        <p>
                            <button type="button" class="btn btn-warning" onclick="$('#modalUpdateI18N').modal('show')"><fmt:message key="i18n.trans.apply.button"/></button>
                        </p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info" id="alertNoUpdateNeeded">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:choose>
                            <c:when test="${lastUpdatedOn == null && lastAppliedOn != null}">
                                <strong><fmt:message key="i18n.trans.no"/>!</strong> <fmt:message key="i18n.trans.no.last"/> <script>document.write(moment('${lastAppliedOn}').fromNow());</script> <fmt:message key="course.teacher"/>  
                                <c:choose>
                                    <c:when test="${loggedInUsername == lastAppliedBy.username}"><fmt:message key="i18n.trans.you"/>.</c:when>
                                    <c:otherwise>${lastAppliedBy.firstname} ${lastAppliedBy.lastname}.</c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <strong><fmt:message key="i18n.trans.no"/>!</strong> <fmt:message key="i18n.trans.no.done"/>.
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:otherwise>
            </c:choose>
            <form id="updateI18n" action="updateEnglish" method="post">
                        <table class="table table-bordered table-condensed">
                            <th>Key</th><th>English Translation</th>
                                <c:forEach var="property" items="${i18nProperties}">
                                <tr>
                                    <td width="25%">${property.key}</td>
                                    <td width="75%"><input type="input"  id="${property.key}" name="${property.key}" value="${property.value}" style="border:none;width:100%;"></td>
                                </tr>
                            </c:forEach>
                        </table>
            </form>

            </div>
        </div>

        <!-- Modal Dialog for Updating Translations -->
        <div class="modal fade" id="modalUpdateI18N">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="modalTitle"><fmt:message key="i18n.dialog.title"/></h4>
                    </div>
                    <form role="form" id="applyTranslations" action="applyTranslations" method="post">
                        <div class="modal-body">
                            <p id="modalBodyText1"><fmt:message key="i18n.dialog.text"/></p>
                            <p id="modalBodyText2"><fmt:message key="i18n.dialog.password"/></p>
                            <div class="alert alert-danger" style="display:none" id="alertIncorrectPassword">
                                <a class="close" data-dismiss="alert">×</a>
                                <strong>Oh snap!</strong> <fmt:message key="i18n.dialog.password.incorrect"/>
                            </div>
                            <p id="modalBodyText3" style="display:none"><fmt:message key="i18n.dialog.password.restart"/></p>
                            <br/>
                            <div class="form-group" id="formGroupPassword" style="width:100%">
                                <label for="password"><fmt:message key="user.password"/></label>
                                <input type="password" class="form-control" id="password" name="password" onkeyup="toggleApplyButton()" placeholder="<fmt:message key="placeholder.password"/>">
                            </div>
                            <div class="progress progress-striped active" id="progress" style="display:none">
                                <div class="progress-bar progress-bar-danger"  role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                                    <span class="sr-only">Loading...</span>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" id="btnCancel" class="btn btn-default" data-dismiss="modal"><fmt:message key="edit.popup.cancel"/></button>
                            <button type="button" id="btnUpdate" class="btn btn-danger" onclick="applyTranslations()" disabled><fmt:message key="i18n.trans.apply.button"/></button>
                        </div>
                    </form>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
        <script>
            if ('${verificationFailed}') {
                console.log('password incorrect');
                document.getElementById('alertIncorrectPassword').style.display = 'block';
                $('#modalUpdateI18N').modal('show')
            }


            function applyTranslations() {
                //block the button and show a loader
                document.getElementById('progress').style.display = 'block';
                document.getElementById('modalBodyText1').style.display = 'none';
                document.getElementById('modalBodyText2').style.display = 'none';
                document.getElementById('modalBodyText3').style.display = 'block';
                document.getElementById('btnUpdate').disabled = true;
                document.getElementById('btnCancel').disabled = true;
                document.getElementById('formGroupPassword').style.display = 'none';
                document.getElementById('alertIncorrectPassword').style.display = 'none';
                document.getElementById('modalTitle').innerHTML = 'Server Restarted';

                //submit form
                document.getElementById('applyTranslations').submit();
            }

            // block the apply button if there is no input in the password feld
            function toggleApplyButton() {
                if (document.getElementById('password').value.length > 0) {
                    console.log('not empty');
                    document.getElementById('btnUpdate').disabled = false;
                }
                else {
                    console.log('empty');
                    document.getElementById('btnUpdate').disabled = true;
                }
            }

        </script>
    </body>
</html>
