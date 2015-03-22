<%--suppress ALL --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:page title="home">
        <jsp:attribute name="scripts">
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
        <h3 class="page-header">Départ</h3>
        <div class="row">
            <div class="col-lg-3">
                Adresse :
            </div>
            <div class="col-lg-9">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-3">
                Horaire :
            </div>
            <div class="col-lg-9">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-3">
                Note complémentaire :
            </div>
            <div class="col-lg-9">
            </div>
        </div>
        <h3 class="page-header">Arrivée</h3>
        <div class="row">
            <div class="col-lg-3">
                Adresse :
            </div>
            <div class="col-lg-9">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-3">
                Horaire :
            </div>
            <div class="col-lg-9">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-3">
                Note complémentaire :
            </div>
            <div class="col-lg-9">
            </div>
        </div>
        <h3 class="page-header">Configuration du trajet</h3>
        <div class="row">
            <div class="col-lg-3">
                Nombre de passagers :
            </div>
            <div class="col-lg-9">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-3">
                Prix par passager :
            </div>
            <div class="col-lg-9">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-3">
                Taille des bagages :
            </div>
            <div class="col-lg-9">
            </div>
        </div>
    </jsp:body>
</t:page>