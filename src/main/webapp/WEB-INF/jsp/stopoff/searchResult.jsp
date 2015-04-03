<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--suppress ALL --%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<t:page title="Etape" notifications="${notifications}">
  <t:journey_form journeyForm="${journeyForm}" creation="false" />
  <h1 class="page-header" id="result">Résultat de votre recherche :</h1>

  <c:if test="${matchingStopOffs.size() == 0}">
    <i>Aucun résultat correspond à votre recherche.</i>
  </c:if>

  <c:forEach items="${matchingStopOffs}" var="matchingStopOff">
    <h2>Départ à <fmt:formatNumber maxFractionDigits="2" value="${matchingStopOff.key.departureDistance / 1000}"/> kms de vous
    <span class="pull-right"> Arrivée à <fmt:formatNumber maxFractionDigits="2" value="${matchingStopOff.key.arrivalDistance / 1000}"/> kms de vous</span></h2>
    <t:stopoff stopOff="${matchingStopOff.value}" />
  </c:forEach>

</t:page>
