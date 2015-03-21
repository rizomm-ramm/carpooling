<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@attribute name="title" required="true" %>
<%@attribute name="scripts" fragment="true"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <title>Carpooling - ${title}</title>

    <%--css--%>
    <link rel="stylesheet" type="text/css" href="/resources/css/bootstrap.min.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="/resources/css/bootstrap-theme.min.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css" media="all"/>

    <%--javascript--%>
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true&libraries=places"></script>
</head>
<body>
<div id="pageheader">
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/">HOME</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <sec:authorize access="isAuthenticated()">
                        <li><a href="/logout">[ <sec:authentication property="principal.username" /> ] Se déconnecter</a></li>
                    </sec:authorize>

                    <sec:authorize access="isAnonymous()">
                        <li><a href="/login">Se connecter</a></li>
                    </sec:authorize>
                </ul>
            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>
</div>
<div id="body">
    <div class="row">
        <div class="col-md-12">
            <jsp:doBody/>
        </div>
    </div>
</div>

<div id="pagescripts">
    <jsp:invoke fragment="scripts" />
</div>
</body>
</html>