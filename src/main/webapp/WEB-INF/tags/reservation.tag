<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@tag description="StopOffReservation Page template" pageEncoding="UTF-8" %>
<%@attribute name="reservations" required="true" type="java.util.List" %>

<c:set var="rand"><%= java.lang.Math.round(java.lang.Math.random() * 100000) %></c:set>

<div class="panel-group" id="accordion-reservation-${rand}" role="tablist" aria-multiselectable="true">
    <c:forEach items="${reservations}" var="reservation" varStatus="status">
        <c:set var="panelColor" value="default" />
        <c:choose>
            <c:when test="${reservation.status == 'WAITING'}">
                <c:set var="panelColor" value="warning" />
            </c:when>
            <c:when test="${reservation.status == 'VALIDATED'}">
                <c:set var="panelColor" value="success" />
            </c:when>
            <c:when test="${reservation.status == 'REFUSED'}">
                <c:set var="panelColor" value="danger" />
            </c:when>
        </c:choose>
        <div class="panel panel-${panelColor}" >
            <div class="panel-heading" role="tab" id="journey-head-${status.index}">
                <div class="row panel-title" role="button" data-toggle="collapse" data-parent="#accordion-reservation-${rand}" href="#reservation-${rand}-${status.index}"
                     aria-expanded="true" aria-controls="reservation-${status.index}">
                    <div class="col-md-2 text-left">
                        <span class="glyphicon glyphicon-calendar"></span> <b><fmt:formatDate pattern="dd/MM/yyyy" value="${reservation.stopOff.departurePoint.date}" /></b>
                    </div>
                    <div class="col-md-4 text-right">
                            ${reservation.stopOff.departurePoint.address}
                    </div>
                    <div class="col-md-2 text-center">
                        <span class="glyphicon glyphicon-arrow-right"></span>
                    </div>
                    <div class="col-md-4 text-left">
                            ${reservation.stopOff.arrivalPoint.address}
                    </div>
                </div>
            </div>
            <div id="reservation-${rand}-${status.index}" class="panel-collapse collapse
                        <c:if test="${param.passengerStoffOffid == reservation.stopOff.id or (empty param.passengerStoffOffid and status.first)}">in</c:if>"
                 role="tabpanel" aria-labelledby="journey-head-${status.index}">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-3">
                            <div class="page-header">Etat de la réservation</div>

                            <ul style="margin: 0; padding-left: 7px;">
                                <li>
                                    <span class="glyphicon glyphicon-credit-card"></span>
                                    <c:choose>
                                        <c:when test="${reservation.payed}">
                                            Réglée
                                        </c:when>
                                        <c:otherwise>
                                            Non réglée
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                                <li>
                                    <c:choose>
                                        <c:when test="${reservation.status == 'WAITING'}">
                                            <span class="glyphicon glyphicon-hourglass"></span> En attente de validation
                                        </c:when>
                                        <c:when test="${reservation.status == 'VALIDATED'}">
                                            <span class="glyphicon glyphicon-ok"></span> Validée
                                        </c:when>
                                        <c:when test="${reservation.status == 'REFUSED'}">
                                            <span class="glyphicon glyphicon-remove"></span> Refusée
                                        </c:when>
                                    </c:choose>
                                </li>
                                <li>
                                        ${reservation.seats} place(s)
                                </li>
                                <li>
                                    Commentaire :
                                </li>
                            </ul>
                            <p>
                                    ${reservation.comment}
                            </p>
                        </div>
                        <div class="col-md-9">
                            <t:stopoff stopOff="${reservation.stopOff}"/>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </c:forEach>
</div>