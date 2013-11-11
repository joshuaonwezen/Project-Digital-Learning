<%-- 
    Document   : CreateAccount
    Created on : 9-okt-2013, 14:04:14
    Author     : Joshua
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<LINK href="resources/css/forms.css" rel="stylesheet" type="text/css">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    
    <script type="text/javascript">
function validateform() {
    //vars
    var username = document.forms["Login"]["username"].value;
    var pass = document.forms["Login"]["password"].value;
    var isFilled=true;
    var msg="";
  
    //Check if empty
    if(username == "") {
        msg+="You need to fill in a username\n";
        isFilled=false;
    }    
    if(pass == "") {
        msg+="You need to fill in a password\n";
        isFilled=false;
    }
    if(!isFilled) {
        alert(msg);
    }
    return isFilled;
}
</script>
    
<form onsubmit="return validateform();" name="Login" method="post" action="Login">
    <div class="logindetails"><table class="createaccountdetails2">
        <tr><th>Sign in</th></tr>
        <tr><td>Username<br>
        <input type="text" name="username" /><br><br>
                Password<br>
        <input type="password" name="password" /> </td></tr>
        <tr><th><input type="submit" value="Login" name="Login" /></th></tr>
    </table></div>
</form>           



</html>
