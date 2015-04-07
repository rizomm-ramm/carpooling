<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:page title="Profil - Edition" notifications="${notifications}">

  <fmt:formatDate pattern="MM/dd/yyyy HH:mm" value="<%=new java.util.Date()%>" var="nowDate"/>
  <c:set var="date" value="<%=new java.util.Date()%>"/>
  <c:if test="${not empty journeyForm.date}">
    <fmt:formatDate pattern="MM/dd/yyyy HH:mm" value="${journeyForm.date}" var="dateToCompare"/>
    <c:if test="${not dateToCompare lt nowDate}">
      <c:set var="date" value="${journeyForm.date}"/>
    </c:if>
  </c:if>

  <fmt:formatDate pattern="MM/dd/yyyy HH:mm" value="${date}" var="formattedJourneyDate"/>
  <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${stopOff.departurePoint.date}" var="departureDate"/>
  <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${stopOff.arrivalPoint.date}" var="arrivalDate"/>

  <c:set var="unmodifiable" value="false" />
  <c:choose>
    <c:when test="${stopOff.status == 'INITIALIZED'}">
      <div class="alert alert-warning alert-dismissible" role="alert">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <strong>Ce trajet n'est pas activé et n'apparaît donc pas dans la recherche!</strong>
        <p>
          Pour l'activer, veuillez remplir les informations suivantes :
        <ul>
          <li>Le nombre de places disponibles (supérieur à 0)</li>
          <li>Le prix du trajet (supérieur à 0)</li>
        </ul>
          Vous pouvez ensuite cliquez sur le bouton "Valider" si vous souhaitez que le trajet apparaisse dans la recherche.
        </p>
      </div>
    </c:when>
    <c:when test="${stopOff.reservations.size() == 0}">
      <div class="alert alert-warning alert-dismissible" role="alert">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <strong>Ce trajet est activé, mais aucune resérvation n'a été effectuée!</strong>
        <p>
          Vous pouvez modifier tous les paramètres de ce trajet.
        </p>
      </div>
    </c:when>
    <c:otherwise>
      <c:set var="unmodifiable" value="true" />
      <div class="alert alert-info alert-dismissible" role="alert">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <strong>Ce trajet est activé, mais au moins une resérvation a été effectuée!</strong>
        <p>
          Vous ne pouvez plus modifier les informations relatives à celui ci.
        </p>
      </div>
    </c:otherwise>
  </c:choose>

  <form:form cssClass="form-horizontal" role="form" modelAttribute="stopOff" method="POST">
    <div class="row">
      <div class="col-md-6">
        <h3 class="page-header">Départ</h3>
        <div class="form-group">
          <form:label path="departurePoint.date" cssClass="col-sm-4 control-label">Date de départ :</form:label>
          <div class="col-sm-8">
            <div class='input-group date'>
              <form:input cssClass="form-control" path="departurePoint.date" id="departure_date" value="${departureDate}" disabled="${unmodifiable}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>

          </div>
        </div>
        <div class="form-group">
          <form:label path="departurePoint.address" id="departure_label"
                      cssClass="col-sm-4 control-label">Adresse :</form:label>
          <div class="col-sm-8">
            <form:input cssClass="form-control" path="departurePoint.address" onfocus="geolocate()" id="departure"
                        placeholder="Addresse de départ" disabled="${unmodifiable}" />

            <form:hidden path="departurePoint.latitude" id="departure_latitude"/>
            <form:hidden path="departurePoint.longitude" id="departure_longitude"/>

            <form:errors path="departurePoint.address" cssStyle="color:red;"/>
          </div>
        </div>
        <div class="form-group">
          <form:label path="departurePoint.description"
                      cssClass="col-sm-4 control-label">Commentaires :</form:label>
          <div class="col-sm-8">
            <form:textarea cssClass="form-control" path="departurePoint.description"
                        placeholder="Informations complémentaires sur le départ" disabled="${unmodifiable}" />

            <form:errors path="departurePoint.description" cssStyle="color:red;"/>
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <h3 class="page-header">Arrivée</h3>
        <div class="form-group">
          <form:label path="arrivalPoint.date" cssClass="col-sm-4 control-label">Date d'arrivée :</form:label>
          <div class="col-sm-8">
            <div class='input-group date'>
              <form:input cssClass="form-control" path="arrivalPoint.date" id="arrival_date" value="${arrivalDate}" disabled="${unmodifiable}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>

          </div>
        </div>
        <div class="form-group">
          <form:label path="arrivalPoint.address" id="arrival_label" cssClass="col-sm-4 control-label">Adresse :</form:label>
          <div class="col-sm-8">
            <form:input cssClass="form-control" path="arrivalPoint.address" onfocus="geolocate()" id="arrival"
                        placeholder="Addresse d'arrivée" disabled="${unmodifiable}"/>

            <form:hidden path="arrivalPoint.latitude" id="arrival_latitude"/>
            <form:hidden path="arrivalPoint.longitude" id="arrival_longitude"/>

            <form:errors path="arrivalPoint.address" cssStyle="color:red;"/>
          </div>
        </div>
        <div class="form-group">
          <form:label path="arrivalPoint.description"
                      cssClass="col-sm-4 control-label">Commentaires :</form:label>
          <div class="col-sm-8">
            <form:textarea cssClass="form-control" path="arrivalPoint.description"
                           placeholder="Informations complémentaires sur l'arrivée" disabled="${unmodifiable}" />

            <form:errors path="arrivalPoint.description" cssStyle="color:red;"/>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-md-12">
        <h3 class="page-header">Informations</h3>
        <div class="form-group">
          <form:label path="price" cssClass="col-sm-2 control-label">Prix :</form:label>
          <div class="col-sm-4">
            <form:input cssClass="form-control" path="price" placeholder="Prix" disabled="${unmodifiable}"/>

            <form:errors path="price" cssStyle="color:red;"/>
          </div>
          <form:label path="price" cssClass="col-sm-2 control-label">Places disponibles :</form:label>
          <div class="col-sm-4">
            <form:input cssClass="form-control" type="number" path="availableSeats" placeholder="Places disponibles" disabled="${unmodifiable}"/>

            <form:errors path="availableSeats" cssStyle="color:red;"/>
          </div>
        </div>
        <div class="form-group text-center">
          <form:hidden path="status"/>

          <input type="submit" class="btn btn-default" formaction="/stopoff/update" formmethod="post"
                 value="Mettre à jour" <c:if test="${unmodifiable}">disabled="disabled"</c:if>/>
        </div>
      </div>
    </div>

    <script type="text/javascript">
      var autocompleteDeparture, autocompleteArrival;


      $(function () {
        $("form :input").on("keypress", function (e) {
          return e.keyCode != 13;
        });

        autocompleteDeparture = new google.maps.places.Autocomplete(
                /** @type {HTMLInputElement} */(document.getElementById('departure')),
                {types: ['geocode']});

        autocompleteArrival = new google.maps.places.Autocomplete(
                /** @type {HTMLInputElement} */(document.getElementById('arrival')),
                {types: ['geocode']});
        // When the user selects an address from the dropdown,
        // populate the address fields in the form.
        google.maps.event.addListener(autocompleteDeparture, 'place_changed', function () {
          $("#departure_latitude").val(autocompleteDeparture.getPlace().geometry.location.D);
          $("#departure_longitude").val(autocompleteDeparture.getPlace().geometry.location.k);
        });

        google.maps.event.addListener(autocompleteArrival, 'place_changed', function () {
          $("#arrival_latitude").val(autocompleteArrival.getPlace().geometry.location.D);
          $("#arrival_longitude").val(autocompleteArrival.getPlace().geometry.location.k);
        });

        $('#departure_date').datetimepicker({
          locale: 'fr',
          format: 'DD/MM/YYYY HH:mm',
          defaultDate: '${formattedJourneyDate}',
          minDate: '${nowDate}'
        });

        $('#arrival_date').datetimepicker({
          locale: 'fr',
          format: 'DD/MM/YYYY HH:mm',
          defaultDate: '${formattedJourneyDate}',
          minDate: '${nowDate}'
        });
      });

      function geolocate() {
        if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(function (position) {
            var geolocation = new google.maps.LatLng(
                    position.coords.latitude, position.coords.longitude);
            var circle = new google.maps.Circle({
              center: geolocation,
              radius: position.coords.accuracy
            });
            autocompleteDeparture.setBounds(circle.getBounds());
            autocompleteArrival.setBounds(circle.getBounds());
          });
        }
      }
    </script>
  </form:form>
</t:page>
