<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>List All Users</title>
    </head>
    <body>
        <h1>This is the R-part I of CRUD: LIST ALL</h1>
        
        <table border="1" class="table">
                    <tr>
                        <td><strong>User ID</strong></td>
                        <td><strong>First name</strong></td>
                        <td><strong>Last name</strong></td>
                        <td><strong>Email address</strong></td>
                        
                    </tr>
            <c:forEach var="user" items="${users}">
                     <tr>
                           <td>${user.userId}</td>
                           <td>${user.firstname}</td>
                           <td>${user.lastname}</td>
                           
                    </tr>
            </c:forEach>
   </table>
                    <br>                
   <form method="post" action="loginController">                    
    <input type="submit" value="Back to menu"/>
   </form>
    </body>
</html>
