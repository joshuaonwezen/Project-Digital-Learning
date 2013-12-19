<!--
@author Shahin Mokhtar
-->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index" />
<html  lang="${language}">
    <head>
        <title>Management</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width">

        <!-- Company Style -->
        <link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon">
        <link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon">
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bootstrap/dist/js/alert.js"></script>
        <!-- DHTMLX General -->
        <script src="resources/dhtmlx/dhtmlxTabbar/codebase/dhtmlxcommon.js"></script>
        <!-- DHTMLX Tabbar -->
        <link rel="stylesheet" type="text/css" href="resources/dhtmlx/dhtmlxTabbar/codebase/dhtmlxtabbar.css">
        <script src="resources/dhtmlx/dhtmlxTabbar/codebase/dhtmlxtabbar.js"></script>
        <script src="resources/dhtmlx/dhtmlxTabbar/codebase/dhtmlxtabbar_start.js"></script>
        <!-- DHTMLX Grid -->
        <link rel="stylesheet" type="text/css" href="resources/dhtmlx/dhtmlxGrid/codebase/dhtmlxgrid.css">
        <script src="resources/dhtmlx/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>        
        <script src="resources/dhtmlx/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>  
        <script src="resources/dhtmlx/dhtmlxGrid/codebase/ext/dhtmlxgrid_start.js"></script>
        <!-- DHTMLX Menu -->
        <script src="resources/dhtmlx/dhtmlxMenu/codebase/dhtmlxmenu.js"></script>
        <script src="resources/dhtmlx/dhtmlxMenu/codebase/ext/dhtmlxmenu_ext.js"></script>
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
                        <li><a href="/PDL/management">Management</a></li>
                        </c:if>
                    <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                    <c:if test="${loggedInIsAdmin || loggedInIsManager == true}">
                    <li><a href="/PDL/vga">VGA</a></li>
                    </c:if>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Settings <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="index.jsp">Logout</a></li>
                            <li class="divider"></li>
                        </ul>
                    </li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </nav>
        <!-- eof navbar-->
    </body>
</html>