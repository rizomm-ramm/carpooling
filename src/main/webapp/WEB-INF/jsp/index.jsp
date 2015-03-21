<%--suppress ALL --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:page title="home">
        <jsp:attribute name="scripts">
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
    </jsp:attribute>

    <jsp:body>
        <h2 class="page-header">Covoiturage</h2>

        <div role="tabpanel">

            <!-- Nav tabs -->
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#search" aria-controls="home" role="tab" data-toggle="tab">Recherche un trajet</a></li>
                <li role="presentation"><a href="#create" aria-controls="profile" role="tab" data-toggle="tab">Proposer un trajet</a></li>
            </ul>

            <!-- Tab panes -->
            <div class="tab-content">
                <div role="tabpanel" class="tab-pane active" id="search">

                </div>
                <div role="tabpanel" class="tab-pane" id="create">
                    <form:form cssClass="form-horizontal" role="form" modelAttribute="journeyForm" action="/journey/create" method="POST">
                        <form:errors cssStyle="color:red;"/>
                        <div class="form-group">
                            <form:label path="departure.address" id="departure_label" cssClass="col-sm-2 control-label">Départ :</form:label>
                            <div class="col-sm-6">
                                <form:input cssClass="form-control" path="departure.address" onfocus="geolocate()" id="departure" placeholder="Addresse de départ" />

                                <form:hidden path="departure.latitude" id="departure_latitude"  />
                                <form:hidden path="departure.longitude" id="departure_longitude"  />

                                <form:errors path="departure.address" cssStyle="color:red;"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <form:label path="arrival.address" id="arrival_label" cssClass="col-sm-2 control-label">Arrivée :</form:label>
                            <div class="col-sm-6">
                                <form:input cssClass="form-control" path="arrival.address" onfocus="geolocate()" id="arrival" placeholder="Addresse d'arrivée" />
                                <form:hidden path="arrival.latitude" id="arrival_latitude"  />
                                <form:hidden path="arrival.longitude" id="arrival_longitude"  />

                                <form:errors path="arrival.address" cssStyle="color:red;"/>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-default">Enregistrer</button>
                    </form:form>
                </div>
            </div>

        </div>
    </jsp:body>

</t:page>