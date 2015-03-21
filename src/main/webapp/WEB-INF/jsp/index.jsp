<%--suppress ALL --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:page title="home">


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

                </div>
            </div>

        </div>
    </jsp:body>

</t:page>