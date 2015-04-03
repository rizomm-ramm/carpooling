<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:page title="Profile - Trajets" notifications="${notifications}">
    <c:set var="type" value="driver" />
    <c:if test="${not empty param.type and (param.type == 'passenger')}">
        <c:set var="type" value="passenger" />
    </c:if>

    <div role="tabpanel">

        <!-- Nav tabs -->
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="control-label"><h4>Mes trajets en tant que : </h4></li>
            <li role="presentation" class="<c:if test="${type == 'driver'}">active</c:if>" style="padding-left: 50px;">
                <a href="#driver" aria-controls="driver" role="tab" data-toggle="tab"><span class="glyphicon glyphicon-list"></span> Conduteur</a>
            </li>
            <li role="presentation" class="<c:if test="${type == 'passenger'}">active</c:if>"> <a href="#passenger" aria-controls="passenger" role="tab" data-toggle="tab"><span class="glyphicon glyphicon-list"></span> Passager</a></li>
        </ul>

        <!-- Tab panes -->
        <div class="tab-content" style="padding-top: 15px;">
            <div role="tabpanel" class="tab-pane <c:if test="${type == 'driver'}">active</c:if>" id="driver">
                <c:if test="${user.journeys.size() == 0}" >
                    <i>Vous n'avez créé aucun trajet.</i>
                </c:if>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <c:forEach items="${user.journeys}" var="journey" varStatus="status">
                        <div class="panel panel-default" >
                            <div class="panel-heading" role="tab" id="journey-head-${status.index}">
                                <div class="row panel-title" role="button" data-toggle="collapse" data-parent="#accordion" href="#journey-${status.index}"
                                     aria-expanded="true" aria-controls="journey-${status.index}">
                                    <div class="col-md-2 text-left">
                                        <span class="glyphicon glyphicon-calendar"></span> <b><fmt:formatDate pattern="dd/MM/yyyy" value="${journey.stopOffs[0].departurePoint.date}" /></b>
                                    </div>
                                    <div class="col-md-4 text-right">
                                            ${journey.stopOffs[0].departurePoint.address}
                                    </div>
                                    <div class="col-md-2 text-center">
                                        <span class="glyphicon glyphicon-arrow-right"></span>
                                    </div>
                                    <div class="col-md-4 text-left">
                                            ${journey.stopOffs[0].arrivalPoint.address}
                                    </div>
                                </div>
                            </div>
                            <div id="journey-${status.index}" class="panel-collapse collapse
                    <c:if test="${param.driverStoffOffid == journey.id or (empty param.driverStoffOffid and status.first)}">in</c:if>"
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
            </div>

            <div role="tabpanel" class="tab-pane <c:if test="${type == 'passenger'}">active</c:if>" id="passenger">
                <c:if test="${user.stopOffReservations.size() == 0}" >
                    <i>Vous n'avez effectué aucune réservation.</i>
                </c:if>
                <t:reservation reservations="${user.getActiveReservations()}" />
                <hr />
                <h3><span class="glyphicon glyphicon-folder-open"></span> Archives</h3>
                <t:reservation reservations="${user.getArchiveReservations()}" />
            </div>
        </div>

    </div>

</t:page>