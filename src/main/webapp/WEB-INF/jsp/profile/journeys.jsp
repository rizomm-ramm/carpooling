<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:page title="Profile - Trajets">
    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
        <c:forEach items="${user.journeys}" var="journey" varStatus="status">
            <div class="panel panel-default">
                <div class="panel-heading" role="tab" id="journey-head-${status.index}">
                    <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion" href="#journey-${status.index}"
                           aria-expanded="true" aria-controls="journey-${status.index}">
                            Trajet ${status.index}
                        </a>
                    </h4>
                </div>
                <div id="journey-${status.index}" class="panel-collapse collapse <c:if test="${status.first}">in</c:if>"
                     role="tabpanel" aria-labelledby="journey-head-${status.index}">
                    <div class="panel-body">
                        <c:forEach items="${journey.stopOffs}" var="stopOff">
                            <t:stopoff stopOff="${stopOff}" adminMode="true"/>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>


</t:page>