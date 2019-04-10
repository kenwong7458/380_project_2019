<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MyWebClassroom</title>
    </head>
    <body>
        <security:authorize access="hasAnyRole('TEACHER','STUDENT')">
            <c:url var="logoutUrl" value="/logout" />
            <form action="${logoutUrl}" method="POST">
                <input type="submit" value="Log out" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>
        </security:authorize>
        
        <security:authorize access="!hasAnyRole('TEACHER','STUDENT')">
            <c:url var="loginUrl" value="/login" />
            <form action="${loginUrl}" method="GET">
                <input type="submit" value="Log in" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>
        </security:authorize>
        
        <security:authorize access="hasRole('TEACHER')">
            <a href="<c:url value="/user" />"> Manage User Accounts</a><br/><br/>
        </security:authorize>
        
        <h3>Course Index</h3>
        <security:authorize access="!hasAnyRole('TEACHER','STUDENT')">
            <i>Login to see more information of each lecture.</i><br/>
        </security:authorize>
            
        <security:authorize access="hasAnyRole('TEACHER')">
            <a href="<c:url value="/classroom/create" />">Create a Lecture </a><br/><br/>
        </security:authorize>
            
        <c:choose>
            <c:when test="${fn:length(lectureDatabase)== 0}">
                <i>There are no lectures in the system</i>
            </c:when>
                
            <c:otherwise>
                <i>There is/are ${fn:length(lectureDatabase)} lecture(s) in total.</i><br/><br/>
                <c:forEach items="${lectureDatabase}" var="lecture">
                    <b><i># <c:out value="${lecture.tag}" /></i></b><br/>
                    Lecture ${lecture.id}:
                    <a href="<c:url value="/classroom/view/${lecture.id}" />">
                        <c:out value="${lecture.name}" /></a>
                    <br/>
                    <security:authorize access="hasAnyRole('TEACHER')">
                    [<a href="<c:url value="/classroom/edit/${lecture.id}" />">Edit</a>]
                    </security:authorize>
                    <security:authorize access="hasRole('TEACHER')">
                        [<a href="<c:url value="/classroom/delete/${lecture.id}" />">Delete</a>]
                    </security:authorize>
                    <br/><br/>
                </c:forEach>
            </c:otherwise>    
        </c:choose>
    </body>
</html>
