<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--suppress ALL --%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<t:page title="Etape">
  <h1 class="page-header">Résultat de votre recherche :</h1>

  <c:forEach items="${matchingStopOffs}" var="matchingStopOff">
    <h2>Départ à <fmt:formatNumber maxFractionDigits="2" value="${matchingStopOff.value / 1000}"/> kms de vous</h2>
    <t:stopoff stopOff="${matchingStopOff.key}" />
  </c:forEach>

</t:page>
