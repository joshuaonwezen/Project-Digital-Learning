<%-- 
    Document   : vga
    Created on : Dec 11, 2013, 1:32:38 PM
    Author     : wesley
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
        <title>Home - Info Support</title>
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
                        <c:if test="${loggedInIsAdmin == true}">
                        <li><a href="/PDL/management">Management</a></li>
                        </c:if>
                    <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
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

        <div id="main">
            <button type="button" class="btn btn-default" id="btnShowModalStartSweep" onClick="$('#modalStartSweep').modal('show')">Start new sweep</button>
        </div>
        <!-- Modal Dialog for Starting a New Sweep -->
        <div class="modal fade" id="modalStartSweep">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Initiate new VGA sweep</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal" role="form" id="formVGASweep" action="createSweep" method="post">
                        <p>Please select a Skill to start a new search for. </p>
                        <div class="form-group">
                            <label for="skills" class="col-sm-2 control-label">Skill</label>
                            <div class="col-sm-10">
                                <input type="hidden" id="tagSkills" name="tagSkills" placeholder="&nbsp;Enter a Skill" style="width:100%">
                                <script>
                                    //set all available skills from the database in the multiselect
                                    var arrSkills = new Array();
                                    <c:forEach var='skill' items='${skills}'>
                                    arrSkills.push('${skill.name}');
                                    </c:forEach>
                                    $('#tagSkills').select2({tags: arrSkills, tokenSeparators: [",", " "], createSearchChoice: false, multiple: false});
                                </script>
                            </div>
                        </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-primary">Start</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
    </body>
</html>