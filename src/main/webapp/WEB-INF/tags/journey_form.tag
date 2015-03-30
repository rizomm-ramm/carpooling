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
        Précision : <form:input cssClass="form-control" type="text" path="departure.precision" /> kms
        <form:errors path="departure.precision" cssStyle="color:red;"/>
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
        Précision : <form:input  cssClass="form-control" type="text" path="arrival.precision" /> kms
        <form:errors path="arrival.precision" cssStyle="color:red;"/>
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
</form:form>