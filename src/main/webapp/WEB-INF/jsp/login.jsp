<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:page title="login">
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form action="login" method="POST">
        <div class="form-group">
            <label for="j_username">Nom d'utilisateur</label>
            <input type="text" class="form-control" id="j_username" name="j_username" value="${j_username}" placeholder="Nom d'utilisateur">
        </div>
        <div class="form-group">
            <label for="j_password">Mot de passe</label>
            <input type="password" class="form-control" id="j_password" name="j_password" value="${j_password}" placeholder="Mot de passe">
        </div>
        <input type="submit" class="btn btn-default" value="Connexion" formaction="/login" />
        <c:if test="${empty j_username and empty j_password}">
            <input type="submit" class="btn btn-default" value="S'enregistrer" formaction="/register" />
        </c:if>
    </form>
</t:page>