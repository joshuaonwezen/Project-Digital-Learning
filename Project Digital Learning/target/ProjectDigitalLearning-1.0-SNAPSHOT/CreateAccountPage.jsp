<%-- 
    Document   : CreateAccountPage
    Created on : 5-nov-2013, 19:13:39
    Author     : Joshua
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<LINK href="resources/css/forms.css" rel="stylesheet" type="text/css">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Account</title>
    </head>
    
<script type="text/javascript">
function validateform() {
    //vars
    var username = document.forms["Create"]["username"].value;
    var first = document.forms["Create"]["firstname"].value;
    var last = document.forms["Create"]["lastname"].value;
    var pass1 = document.forms["Create"]["password1"].value;
    var pass2 = document.forms["Create"]["password2"].value;
    var isFilled=true;
    var msg="";
  
    //Check if empty
    if(username == "") {
        msg+="You need to fill in a username\n";
        isFilled=false;
    }
    if(first == "") {
        msg+="You need to fill in the users first name\n";
        isFilled=false;
    }
    if(last == "") {
        msg+="You need to fill in the users last name\n";
        isFilled=false;
    }
    if(pass1 == "") {
        msg+="You need to fill in the users password\n";
        isFilled=false;
    }
    if(pass2 == "") {
        msg+="You need to confirm the users password\n";
        isFilled=false;
    }
    if (username.length < 3 || username.length > 15) {
        msg+="The username is too short, it needs to be between 3 and 15 characters\n";
        isFilled=false;
    }
    if (pass1 != pass2) {
        msg+="The passwords you've entered do not match\n";
        isFilled=false;
    }
    if (pass1.length < 4) {
        msg+="The password you have entered is too short, it needs to be longer than 4 characters\n";
        isFilled=false;
    }
    //email check
    //var atpos=email.indexOf("@");
    //var dotpos=email.lastIndexOf(".");
    //if (atpos<1 || dotpos<atpos+2 || dotpos+2>=email.length) {
     //   alert("Not a valid e-mail address");
        //return false;
    //}
    
    if(!isFilled) {
        alert(msg);
    }
    return isFilled;
}
</script>
<form onsubmit="return validateform();" name ="Create" method="post" action="Create Account">
    <div class="createaccountdetails"><table name="createaccountdetails">
        <tr><th>Create User</th></tr>
        <tr><td>Username<br>
        <input type="textfield"  name="username" value="${userName}" placeholder="username" required autofocus /></td></tr>
        <tr><td>First Name<br>
        <input type="textfield"  name="firstname" value="${firstName}" placeholder="first name"/></td></tr>
        <tr><td>Last Name<br>
        <input type="textfield"  name="lastname" value="${lastName}" placeholder="last name" /></td></tr>
        <tr><td>Password<br>
        <input type="password"  name="password1" value="${password}" placeholder="password"/></td></tr>
        <tr><td>Confirm Password<br>
        <input type="password" name="password2" placeholder="password"/></td></tr>
        <tr><td>Select a role<br>
        <select name="userRole" value="${userRole}">
            <option>Employee</option>
            <option>Teacher</option>
            <option>Manager</option>
        </select></td></tr>
        <tr><th><input type="submit" value="Create Account" name="Create Account"/></th></tr>
    </table></div>
</form>           

</html>
