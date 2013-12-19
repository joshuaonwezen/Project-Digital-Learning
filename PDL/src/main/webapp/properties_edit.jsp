<%-- 
    Document   : properties_edit
    Created on : 8-dec-2013, 21:13:55
    Author     : Martijn
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bootstrap/dist/js/alert.js"></script>
        <title>Courses - Info Support</title>
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
                        <li  class="active"><a href="/PDL/management">Management</a></li>
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
                <form class="navbar-form navbar-right" role="search" id="searchCourse" action="searchCourse">
                    <div class="form-group">
                        <input type="text" name="searchQuery" id="searchQuery" class="form-control" placeholder="<fmt:message key="searchbar.search.course"/>">
                    </div>
                    <button type="submit" class="btn btn-default"><fmt:message key="navbar.search"/></button>
                </form>
            </div><!-- /.navbar-collapse -->
        </nav>
        <title>JSP Page</title>
   
    
        <h1>Properties</h1>
        <table>
            <c:forEach items="${prop}" var="properties">
                <tr>
                    <td>${prop.key}</td>
                    <td>${prop.value}</td>
                </tr>
            </c:forEach>
        </table>   
        
    
    <script type="text/javascript">
function update(){
    try{
        Properties p = new Properties();
        File file=new File("/WEB-INF/classes/tempindex_nl_NL.properties");
        p.store(new FileOutputStream(file),null);
        Enumeration en = p.keys();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String val = request.getParameter(pr.get(key))
            p.setProperty(key, val);
            p.store(new FileOutputStream(file),null);
        }
        
        System.out.println("Operation completly successfuly!");
        }

    catch(IOException e){
    System.out.println(e.getMessage());

}

    </script>
    <table border="1" style="margin-left: 10px">
        <tr>
            <th align=left>Property Key</th>
            <th align=left>Property Value</th>
        </tr>
        <%
            ServletContext context = getServletContext();
            InputStream inStream = context.getResourceAsStream("/WEB-INF/classes/index_nl_NL.properties");
            Properties pr = new Properties();
            pr.load(inStream);
            Enumeration en = pr.keys();
            while (en.hasMoreElements()) {%>
        <tr>
            <%String key = (String) en.nextElement();%>
            <td><%=key%></td>
            <td><input type="input" name="PROPERTY VALUE" value="<%=pr.get(key)%>" onclick="editRecord(<%=pr.get(key)%>);" ></td>
            
        </tr>
        <%}
        %>
    </table>
    <br/>
    <input type=submit value="Update" onClick="update()" href="/PDL/properties_edit" style="margin-left: 10px">
    <input type="button" value="Back" onclick="history.back()"> 
    </body>
</html>
