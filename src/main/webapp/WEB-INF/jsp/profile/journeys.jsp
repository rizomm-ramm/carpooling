<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:page title="Profile - Trajets">
    <c:if test="${param.id}">

    </c:if>
    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
        <c:forEach items="${user.journeys}" var="journey" varStatus="status">
            <div class="panel panel-default" >
                <div class="panel-heading" role="tab" id="journey-head-${status.index}">
                    <div class="row panel-title" role="button" data-toggle="collapse" data-parent="#accordion" href="#journey-${status.index}"
                         aria-expanded="true" aria-controls="journey-${status.index}">
                        <div class="col-lg-5 text-right">
                            ${journey.stopOffs[0].departurePoint.address}
                        </div>
                        <div class="col-lg-2 text-center">
                            <span class="glyphicon glyphicon-arrow-right"></span>
                        </div>
                        <div class="col-lg-5 text-left">
                            ${journey.stopOffs[0].arrivalPoint.address}
                        </div>
                    </div>
                </div>
                <div id="journey-${status.index}" class="panel-collapse collapse
                    <c:if test="${param.id == journey.id or (empty param.id and status.first)}">in</c:if>"
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