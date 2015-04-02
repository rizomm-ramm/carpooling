<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@tag description="StopOff Page template" pageEncoding="UTF-8" %>
<%@attribute name="stopOff" required="true" type="fr.rizomm.ramm.model.StopOff" %>
<%@attribute name="adminMode" %>

<div class="panel panel-default">
    <div class="panel-heading">
        <h5>
            <c:if test="${adminMode}">
                <span class="badge" data-toggle="tooltip" data-placement="top"
                      title="Places en attente de votre validation">
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
            <c:choose>
                <c:when test="${adminMode}">
                    <div class="col-lg-8">
                        <h3>Participants :</h3>
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th width="20%">Nom</th>
                                <th width="10%">Statut</th>
                                <th width="10%">Payé</th>
                                <th width="60%">Commentaire</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <c:forEach items="${stopOff.reservations}" var="reservations">
                                    <td>${reservations.user.username}</td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${reservations.status == 'WAITING'}">
                                                <span class="glyphicon glyphicon-hourglass"></span>
                                            </c:when>
                                            <c:when test="${reservations.status == 'VALIDATED'}">
                                                <span class="glyphicon glyphicon-ok"></span>
                                            </c:when>
                                            <c:when test="${reservations.status == 'REFUSED'}">
                                                <span class="glyphicon glyphicon-remove"></span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${reservations.payed}">
                                                <span class="glyphicon glyphicon-ok"></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="glyphicon glyphicon-remove"></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>

                                    </td>
                                </c:forEach>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="col-lg-4">
                        <h3>Légende :</h3>
                        Statut :
                        <ul>
                            <li><span class="glyphicon glyphicon-hourglass"></span> En attente de votre validation</li>
                            <li><span class="glyphicon glyphicon-ok"></span> Passager que vous avez validé</li>
                            <li><span class="glyphicon glyphicon-remove"></span> Passager que vous avez refusé</li>
                        </ul>
                        Payé :
                        <ul>
                            <li><span class="glyphicon glyphicon-ok"></span> Paiement reçu</li>
                            <li><span class="glyphicon glyphicon-remove"></span> Paiement non reçu</li>
                        </ul>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="col-lg-8">
                        <h3>Participants :</h3>
                        <c:if test="${stopOff.reservations.size() == 0}">
                            <i>Aucun passager n'a encore été enregistré</i>
                        </c:if>
                        <ul>
                            <c:forEach items="${stopOff.reservations}" var="reservations">
                                <c:if test="${reservations.status == 'VALIDATED'}">
                                    <li>${reservations.user.username}</li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </div>
                </c:otherwise>
            </c:choose>
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