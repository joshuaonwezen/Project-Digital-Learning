
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>${courseName}</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width">

        <!-- Company Style -->
        <link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
        <link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
        <link rel="stylesheet" type="text/css" href="resources/css/style.css">
        
                <div class="Header">
            <ul>
                <li><a class="button" href="homepage.jsp">Home</a></li>
                    <c:if test="${loggedInIsAdmin == false}">
                    <li><a class="button" href="/Project%20Digital%20Learning/profile?id=${loggedInUserId}">My Profile</a></li>
                    </c:if>
                <li><a class="button" href="#courses">Courses</a></li>
                    <c:if test="${loggedInIsAdmin == true}">
                    <li><a class="button" href="/Project Digital Learning/management">Management</a></li>
                    </c:if>
                <li><a class="button" href="index.jsp">LogOut</a></li>
            </ul>
        </div>
    
    <body>
        <div class="CoursePage" align=center>
            <h1>${name}Placeholder++<br></h1>
            <h1>Teacher: ${owner}Placeholder<br></h1>
            <h6>Description:<br>${courseDescription}Dit is de course Placeholder en hier leer je shit over shit<br></h6><br>
            <table>
            <form action="#documents">
                <tr><td><input class="coursebutton" type="submit" value="" style="background-image: url(resources/images/documents_icon.png);background-repeat: no-repeat;width:105px;height:105px;"></td>
            </form>     
            <form action="#userlist">
                <td><input class="coursebutton" type="submit" value="" style="background-image: url(resources/images/users_icon.png);background-repeat: no-repeat;width:105px;height:105px;border-color:#EFF8FB;"></tr></td>         
            </form>
                <tr></tr>
                <tr><td><h7>&nbsp;&nbsp;&nbsp;&nbsp;Documents</h7></td><br>
                <td><h7>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Userlist</h7></tr></td>
            <form action="#virtualclassroom">
                <tr><td><input class="coursebutton" type="submit" value="" style="background-image: url(resources/images/virtualclassroom_icon.png);background-repeat: no-repeat;width:105px;height:105px;"></td>
            </form>
            <form action="/courses">
                <td><input class="coursebutton" type="submit" value="" style="background-image: url(resources/images/unenroll_icon.png);background-repeat: no-repeat;width:105px;height:105px;"></tr></td>
            </form>
                <tr><td><h8>Virtual Classroom</h8></td>
                <td><h7>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Unenroll</h7></tr></td>
            </table>
        </div>
    </body>
</html>
