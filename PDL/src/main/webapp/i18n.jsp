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


        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>i18n</title>
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
                    <li class="active"><a href="/PDL/homepage">Home</a></li>
                    <li><a href="/PDL/courses"><fmt:message key="navbar.course"/></a></li>
                        <c:if test="${loggedInIsAdmin || loggedInIsTeacher || loggedInIsManager == true}">
                        <li><a href="/PDL/management">Management</a></li>
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
            <form id="updateI18n" action="update" method="post">
                <table border="1" style="margin-left: 10px">
                    <h4 style="margin-left: 10px">Nederlands</h4>
                    <th>Key</th><th>Value</th>
                        <c:forEach var="property" items="${i18nPropertiesNL}">
                        <tr>
                            <td>${property.key}</td>
                            <td><input type="input" id="${property.key}" name="${property.key}" value="${property.value}" onclick="editRecord(${property.value});" ></td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
                <table border="1" style="margin-left: 10px">
                    <h4 style="margin-left: 10px">English</h4>
                    <th>Key</th><th>Value</th>
                        <c:forEach var="property" items="${i18nPropertiesEN}">
                        <tr>
                            <td>${property.key}</td>
                            <td><input type="input" id="${property.key}" name="${property.key}" value="${property.value}" onclick="editRecord(${property.value});" ></td>
                        </tr>
                    </c:forEach>
                </table>
                <button type="submit" class="btn btn-primary" ><fmt:message key="edit.popup.save"/></button>
            </form>
            
            
        </div>

        <!-- Modal Dialog for Updating Translations -->
        <div class="modal fade" id="modalUpdateI18N">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="modalTitle">Apply I18N Translations</h4>
                    </div>
                    <form role="form" id="applyTranslations" action="applyTranslations" method="post">
                    <div class="modal-body">
                        <p id="modalBodyText1">To apply the new translations the server needs to be restarted. This means that all users are disconnected and will be unable to login for at least five minutes. It is recommended to do this as least as possible at times where as minimal users are online. Inform your employees if necessary.</p>
                        <p id="modalBodyText2">Please enter your password to continue.</p>
                        <p id="modalBodyText3" style="display:none">The server is restarting. The updates should be applied within five minutes. You will need to login manually again.</p>
                        <br/>
                        <div class="form-group" id="formGroupPassword" style="width:100%">
                            <label for="password"><fmt:message key="user.password"/></label>
                            <input type="password" class="form-control" id="password" name="password" placeholder="<fmt:message key="placeholder.password"/>">
                        </div>
                        <div class="progress progress-striped active" id="progress" style="display:none">
                                    <div class="progress-bar progress-bar-danger"  role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                                    <span class="sr-only">Loading...</span>
                                </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" id="btnCancel" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        <button type="button" id="btnUpdate" class="btn btn-danger" onclick="applyTranslations()">Apply</button>
                    </div>
                    </form>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
        <script>
            function applyTranslations(){
                //block the button and show a loader
                document.getElementById('progress').style.display = 'block';
                document.getElementById('modalBodyText1').style.display = 'none';
                document.getElementById('modalBodyText2').style.display = 'none';
                document.getElementById('modalBodyText3').style.display = 'block';
                document.getElementById('btnUpdate').disabled = true;
                document.getElementById('btnCancel').disabled = true;
                document.getElementById('formGroupPassword').style.display = 'none';
                document.getElementById('modalTitle').innerHTML = 'Server Restarted';
                
                //submit form
               document.getElementById('applyTranslations').submit();
            }
            
        </script>
    </body>
</html>
