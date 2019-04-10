<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Lecture</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout" />
        <form action="${logoutUrl}" method="POST">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
        <h3>Lecture #${lecture.id}: <c:out value="${lecture.name}" /></h3>
        <br /><br />
        <i> <b># <c:out value="${lecture.tag}" /></b></i><br /><br />
        <b>Description: </b><br/>
        <c:out value="${lecture.description}" /><br /><br />
        
        <c:if test="${fn:length(lecture.attachments) > 0}">
            <b>Attachments:</b><br/>
            
            <c:forEach items="${lecture.attachments}" var="attachment" varStatus="status">
                <ul>
                    <li>
                        <a href="<c:url value="/classroom/view/${lecture.id}/attachment/${attachment.name}" />">
                        <c:out value="${attachment.name}" /></a>
                    </li>
                </ul>
                
            </c:forEach><br /><br />
        </c:if>
        <a href="<c:url value="/classroom/listLecture" />">Return to lecture list</a>
    </body>
</html>
