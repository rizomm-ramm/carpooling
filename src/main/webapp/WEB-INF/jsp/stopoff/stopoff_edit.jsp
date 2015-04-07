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



  <form:form cssClass="form-horizontal" role="form" modelAttribute="stopOff" method="POST">
    <div class="form-group">
      <form:label path="departurePoint.date" cssClass="col-sm-2 control-label">Date de départ :</form:label>
      <div class="col-sm-10">
        <div class='input-group date'>
          <form:input cssClass="form-control" path="departurePoint.date" id="departure_date" value="${departureDate}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
        </div>

      </div>
    </div>
    <div class="form-group">
      <form:label path="departurePoint.address" id="departure_label"
                  cssClass="col-sm-2 control-label">Départ :</form:label>
      <div class="col-sm-10">
        <form:input cssClass="form-control" path="departurePoint.address" onfocus="geolocate()" id="departure"
                    placeholder="Addresse de départ"/>

        <form:hidden path="departurePoint.latitude" id="departure_latitude"/>
        <form:hidden path="departurePoint.longitude" id="departure_longitude"/>

        <form:errors path="departurePoint.address" cssStyle="color:red;"/>
      </div>
    </div>
    <div class="form-group">
      <form:label path="arrivalPoint.date" cssClass="col-sm-2 control-label">Date d'arrivée :</form:label>
      <div class="col-sm-10">
        <div class='input-group date'>
          <form:input cssClass="form-control" path="arrivalPoint.date" id="arrival_date" value="${arrivalDate}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
        </div>

      </div>
    </div>
    <div class="form-group">
      <form:label path="arrivalPoint.address" id="arrival_label" cssClass="col-sm-2 control-label">Arrivée :</form:label>
      <div class="col-sm-10">
        <form:input cssClass="form-control" path="arrivalPoint.address" onfocus="geolocate()" id="arrival"
                    placeholder="Addresse d'arrivée"/>

        <form:hidden path="arrivalPoint.latitude" id="arrival_latitude"/>
        <form:hidden path="arrivalPoint.longitude" id="arrival_longitude"/>

        <form:errors path="arrivalPoint.address" cssStyle="color:red;"/>
      </div>
    </div>
    <div class="form-group text-center">
      <c:if test="${creation}">
        <input type="submit" class="btn btn-default" formaction="/journey/initialize" value="Créer"/>
      </c:if>
      <input type="submit" class="btn btn-default" formaction="/stopoff/search#result" formmethod="get"
             value="Chercher"/>
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
