<%--suppress ALL --%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<t:page title="Trajet">
    <h1>Trajet de ${journey.user.username}</h1>

    <h2>Etapes :</h2>
    <c:forEach items="${journey.stopOffs}" var="stopOff">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h5><span class="badge">${stopOff.availableSeats} <span class="glyphicon glyphicon-ok"></span></span> | <span class="badge badge-default">${stopOff.price} &euro;</span>

                ${stopOff.departurePoint.address} <span class="glyphicon glyphicon-arrow-right"></span> ${stopOff.arrivalPoint.address} <span
                        class="pull-right"><b>${stopOff.distance / 1000} kms</b></span></h5>
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-lg-12">
                        Places disponibles : ${stopOff.availableSeats} | Prix du trajet : ${stopOff.price} &euro;
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
    </c:forEach>
</t:page>
