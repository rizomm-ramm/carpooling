<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--suppress ALL --%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<t:page title="Trajet">
    <h1>Trajet de ${journey.user.username}</h1>

    <h2>Etapes :</h2>
    <c:forEach items="${journey.stopOffs}" var="stopOff">
        <t:stopoff stopOff="${stopOff}" />
    </c:forEach>
</t:page>
