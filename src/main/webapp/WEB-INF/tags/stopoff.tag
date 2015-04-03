<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@tag description="StopOff Page template" pageEncoding="UTF-8" %>
<%@attribute name="stopOff" required="true" type="fr.rizomm.ramm.model.StopOff" %>
<%@attribute name="adminMode" %>

<c:set var="loggedUser" value="" />
<sec:authorize access="isAuthenticated()">
    <sec:authentication property="principal.username" var="loggedUser"/>
</sec:authorize>
<div class="panel panel-default">
    <div class="panel-heading">
        <h5>

            <span class="badge">
                <span data-toggle="tooltip" data-placement="top"
                       title="Nombre de places disponibles">${stopOff.numberOfRemainingReservation()} / ${stopOff.availableSeats}</span>

                <c:if test="${not adminMode and stopOff.numberOfRemainingReservation() > 0 and stopOff.journey.user.username != loggedUser}">
                     <a href="#" data-toggle="modal" data-target="#register-modal" data-remaining-reservations="${stopOff.numberOfRemainingReservation()}" data-stopoff-id="${stopOff.id}" style="color: white;">Réserver</a>
                </c:if>
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
                    <div class="col-md-8">
                        <h3>Participants :</h3>
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th width="20%">Nom</th>
                                <th width="10%">Places</th>
                                <th width="10%">Statut</th>
                                <th width="10%">Payé</th>
                                <th width="50%">Commentaire</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${stopOff.reservations}" var="reservations">
                                <tr>
                                    <td>${reservations.user.username}</td>
                                    <td>${reservations.seats}</td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${reservations.status == 'WAITING'}">
                                                <a class="link-popover glyphicon glyphicon-hourglass"
                                                      data-toggle="popover"
                                                      title="Changer le status"
                                                      data-html="true"
                                                      data-placement="top"
                                                        role="button"
                                                      data-content="<center>
                                                            <a href='/stopoff/status/user?stopOffId=${stopOff.id}&passengerId=${reservations.user.username}&status=VALIDATED'>
                                                                <span class='glyphicon glyphicon-ok' title='Valider'></span>
                                                            </a>
                                                            <a href='/stopoff/status/user?stopOffId=${stopOff.id}&passengerId=${reservations.user.username}&status=REFUSED'><span style='padding-left:15px;' class='glyphicon glyphicon-remove' title='Refuser'></a></span>
                                                            </center>"></a>
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
                                        ${reservations.description}
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="col-md-4">
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
                    <div class="col-md-8">
                        <h3>Participants :</h3>
                        <c:if test="${stopOff.reservations.size() == 0}">
                            <i>Aucun passager n'a encore été enregistré</i>
                        </c:if>
                        <ul>
                            <c:forEach items="${stopOff.reservations}" var="reservations">
                                <c:if test="${reservations.status == 'VALIDATED'}">
                                    <li>${reservations.user.username} <i>(${reservations.seats})</i></li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="row">
            <div class="col-md-6">
                <b>Départ :</b>
                <hr/>
                <div>
                    <span class="glyphicon glyphicon-calendar"></span> <b><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${stopOff.departurePoint.date}" /></b>
                </div>
                <div>
                    <span class="glyphicon glyphicon-map-marker"></span> ${stopOff.departurePoint.address}
                </div>
                <div>
                    <span class="glyphicon glyphicon-info-sign"></span> ${stopOff.departurePoint.description}
                </div>

            </div>
            <div class="col-md-6">
                <b>Arrivée :</b>
                <hr/>
                <div>
                    <span class="glyphicon glyphicon-calendar"></span> <b><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${stopOff.arrivalPoint.date}" /></b>
                </div>
                <div>
                    <span class="glyphicon glyphicon-map-marker"></span> ${stopOff.arrivalPoint.address}
                </div>
                <div>
                    <span class="glyphicon glyphicon-info-sign"></span> ${stopOff.arrivalPoint.description}
                </div>
            </div>
        </div>
    </div>

    <c:if test="${not adminMode}">
    <div class="modal fade" id="register-modal" tabindex="-1" role="dialog" aria-labelledby="register-modal-label" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="register-modal-label">Réserver</h4>
                </div>
                <div class="modal-body">
                    <form:form action="/stopoff/book" role="form" modelAttribute="bookSeatForm" method="POST">
                        <div class="form-group">
                            <label for="seats">Nombre de places</label>
                            <form:select path="seats" cssClass="form-control" />
                        </div>
                        <div class="form-group">
                            <label for="comment">Commentaire</label>
                            <form:textarea path="comment" cssClass="form-control" placeholder="Commentaire" />
                        </div>

                        <form:hidden path="stopOffId" />
                        <button type="submit" class="btn btn-default">Réserver</button>
                    </form:form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    </c:if>
    <script type="text/javascript">
        //triggered when modal is about to be shown
        $('#register-modal').on('show.bs.modal', function(e) {

            //get data-id attribute of the clicked element
            var stopOffId = $(e.relatedTarget).data('stopoff-id');
            var remainingReservations = $(e.relatedTarget).data('remaining-reservations');

            //populate the textbox
            $(e.currentTarget).find('input[name="stopOffId"]').val(stopOffId);

            $(e.currentTarget).find('select[id="seats"]').html("");
            for(var i = 1; i <= remainingReservations; i++) {
                $(e.currentTarget).find('select[id="seats"]').append(new Option(i, i));
            }
        });
    </script>

</div>