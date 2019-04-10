<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MyWebClassroom</title>
    </head>
    <body>
        <c:url var="loginUrl" value="/login" />
        <form action="${loginUrl}" method="GET">
            <input type="submit" value="Log in" />
        </form>
        
        <h3>Register an Account</h3>
        <form:form method="POST" enctype="multipart/form-data" modelAttribute="registerAccountForm">
            <form:label path="username">Username</form:label><br/>
            <form:input type="text" path="username" /><br/><br/>
            
            <form:label path="email">Email</form:label><br/>
            <form:input type="email" path="email" /><br/><br/>
            
            <form:label path="password">Password</form:label><br/>
            <form:input type="password" path="password" /><br/><br/>
            
            <form:label path="cPassword">Confirm your password</form:label><br/>
            <form:input type="password" path="cPassword" /><br/><br/>
            
            <form:label path="roles"></form:label><br/>
            <form:checkbox path="roles" value="ROLE_STUDENT" required="required" />I am not a robot.<br/>
            
            <input type="submit" value="Submit" />

        </form:form>
    </body>
</html>

