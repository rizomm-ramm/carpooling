<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Journey form template" pageEncoding="UTF-8" %>
<%@attribute name="journeyForm" required="true" type="fr.rizomm.ramm.form.SimpleJourneyForm" %>
<%@attribute name="creation" required="true"  %>

<form:form cssClass="form-horizontal" role="form" modelAttribute="journeyForm" method="POST">
  <div class="form-group">
    <form:label path="departure.address" id="departure_label" cssClass="col-sm-2 control-label">Départ :</form:label>
    <div class="col-sm-10">
      <form:input cssClass="form-control" path="departure.address" onfocus="geolocate()" id="departure" placeholder="Addresse de départ" />

      <c:if test="${not creation}" >
        <div class="row" style="padding: 10px">
          <div class="col-lg-2 col-lg-offset-6 text-right control-label">
            Précision :
          </div>
          <div class="col-lg-2">
            <form:input  cssClass="form-control" type="text" path="departure.precision" />
            <form:errors path="departure.precision" cssStyle="color:red;"/>
          </div>
          <div class="col-lg-2 control-label" style="text-align: left;">
            kms
          </div>
        </div>
      </c:if>
      <form:hidden path="departure.latitude" id="departure_latitude"  />
      <form:hidden path="departure.longitude" id="departure_longitude"  />

      <form:errors path="departure.address" cssStyle="color:red;"/>
    </div>
  </div>
  <div class="form-group">
    <form:label path="arrival.address" id="arrival_label" cssClass="col-sm-2 control-label">Arrivée :</form:label>
    <div class="col-sm-10">
      <form:input cssClass="form-control" path="arrival.address" onfocus="geolocate()" id="arrival" placeholder="Addresse d'arrivée" />

      <c:if test="${not creation}" >
        <div class="row" style="padding: 10px">
          <div class="col-lg-2 col-lg-offset-6 text-right control-label">
            Précision :
          </div>
          <div class="col-lg-2">
            <form:input  cssClass="form-control" type="text" path="arrival.precision" />
            <form:errors path="arrival.precision" cssStyle="color:red;"/>
          </div>
          <div class="col-lg-2 control-label" style="text-align: left;">
            kms
          </div>
        </div>
      </c:if>
      <form:hidden path="arrival.latitude" id="arrival_latitude"  />
      <form:hidden path="arrival.longitude" id="arrival_longitude"  />

      <form:errors path="arrival.address" cssStyle="color:red;"/>
    </div>
  </div>
  <div class="form-group text-center">
    <c:if test="${creation}" >
      <input type="submit" class="btn btn-default" formaction="/journey/initialize" value="Créer" />
    </c:if>
    <input type="submit" class="btn btn-default" formaction="/stopoff/search" formmethod="get" value="Chercher" />
  </div>

  <script type="text/javascript">
    var autocompleteDeparture, autocompleteArrival;


    $(function() {
      $("form :input").on("keypress", function(e) {
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