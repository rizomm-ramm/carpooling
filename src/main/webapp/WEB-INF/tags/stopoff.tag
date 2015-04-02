<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@tag description="StopOff Page template" pageEncoding="UTF-8" %>
<%@attribute name="stopOff" required="true" type="fr.rizomm.ramm.model.StopOff" %>
<%@attribute name="adminMode" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <h5>
            <c:if test="${adminMode}">
                <span class="badge" data-toggle="tooltip" data-placement="top" title="Places en attente de votre validation">
                    ${stopOff.availableSeats} <span class="glyphicon glyphicon-hourglass"></span>
                </span>
            </c:if>
            <span class="badge" data-toggle="tooltip" data-placement="top" title="Places disponibles">
                ${stopOff.availableSeats} <span class="glyphicon glyphicon-ok"></span>
            </span>
            |
            <span class="badge badge-default" data-toggle="tooltip" data-placement="top" title="Prix du trajet">
                ${stopOff.price} &euro;
            </span>

            ${stopOff.departurePoint.address} <span
                    class="glyphicon glyphicon-arrow-right"></span> ${stopOff.arrivalPoint.address} <span
                    class="pull-right"><b>${stopOff.distance / 1000} kms</b>
                <c:if test="${adminMode}"> <a href="/stopoff/edit/${stopOff.id}"><span
                        class="glyphicon glyphicon-edit"></span> </a>
                </c:if></span></h5>
    </div>
    <div class="panel-body">
        <div class="row">
            <div class="col-lg-12">
                <h3>Participants :</h3>
                <ul>
                    <c:forEach items="${stopOff.passengers}" var="passenger">
                        <li>${passenger.username}</li>
                    </c:forEach>
                    <li><span class="glyphicon glyphicon-ok" data-toggle="tooltip" data-placement="top" title="Validé"></span> Test 1</li>
                    <li><span class="glyphicon glyphicon-hourglass" data-toggle="tooltip" data-placement="top" title="En attente"></span> Test 1</li>
                </ul>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-6">
                <b>Départ :</b>
                <hr/>
                <div>
                    <span class="glyphicon glyphicon-calendar"></span> Heure estimée :
                </div>
                <div>
                    ${stopOff.departurePoint.address}
                </div>
                <div>
                    <span class="glyphicon glyphicon-info-sign"></span> ${stopOff.departurePoint.description}
                </div>

            </div>
            <div class="col-lg-6">
                <b>Arrivée :</b>
                <hr/>
                <div>
                    <span class="glyphicon glyphicon-calendar"></span> Heure estimée :
                </div>
                <div>
                    ${stopOff.arrivalPoint.address}
                </div>
                <div>
                    <span class="glyphicon glyphicon-info-sign"></span> ${stopOff.arrivalPoint.description}
                </div>
            </div>
        </div>
    </div>

</div>