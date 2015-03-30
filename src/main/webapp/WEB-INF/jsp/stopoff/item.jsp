<%--suppress ALL --%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<t:page title="Etape">
    <h1>Trajet de ${stopOff.journey.user.username}</h1>

    <t:stopoff stopOff="${stopOff}" />
</t:page>
