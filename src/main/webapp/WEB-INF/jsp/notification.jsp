<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${notifications}" var="notification">
  <c:set var="eyeStatus" value="open" />

  <c:if test="${notification.status == 'UNREAD'}">
    <c:set var="eyeStatus" value="close" />
  </c:if>

  <c:set var="href" value="#" />

  <c:if test="${not empty notification.link}">
    <c:set var="href" value="${notification.link}" />
  </c:if>
  <li class="alert-${notification.type.name().toLowerCase()}" style="opacity: 0.6;">
    <a href="${href}">
      <div>
        <span class="glyphicon glyphicon-eye-${eyeStatus}"></span>
        ${notification.message}
      </div>
      <div class="text-right" style="font-size: 10px;">
        <abbr class="timeago" title="${notification.date}">${notification.date}</abbr>
      </div>
    </a>
  </li>
</c:forEach>
