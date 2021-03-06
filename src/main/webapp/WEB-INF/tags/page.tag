<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@attribute name="title" required="true" %>
<%@attribute name="notifications" type="java.util.List" %>
<%@attribute name="errors" type="java.util.List" %>
<%@attribute name="scripts" fragment="true" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Carpooling - ${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <%--css--%>
    <link rel="stylesheet" type="text/css" href="/resources/css/bootstrap.min.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="/resources/css/bootstrap-theme.min.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="/resources/css/bootstrap-datetimepicker.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css" media="all"/>

    <%--javascript--%>
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.timeago.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.timeago.fr.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/resources/js/moment.js"></script>
    <script type="text/javascript" src="/resources/js/datetimepicker.js"></script>

    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true&libraries=places"></script>
</head>
<body>
    <div class="container">
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
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false" id="notification-block">
                                        <span class="badge" style="font-size: 16px;" id="notification-badge">
                                            <span id="notification-number">0</span>
                                            <span class="glyphicon glyphicon-bell"></span>
                                        </span>
                                        <span class="caret"></span></a>
                                    <ul class="dropdown-menu" role="menu" style="padding: 0;" id="notifications">
                                    </ul>
                                </li>

                                <li class="dropdown">
                                    <a href="/profile" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
                                        <span class="glyphicon glyphicon-user"></span> <sec:authentication property="principal.username" />
                                        <span class="caret"></span></a>
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="/profile/informations">Mon profil</a></li>
                                        <li><a href="/profile/journeys">Mes trajets</a></li>
                                        <li><a href="/profile/messages">Mes messages</a></li>
                                        <li class="divider"></li>
                                        <li><a href="/logout">Se déconnecter</a></li>
                                    </ul>
                                </li>
                            </sec:authorize>

                            <sec:authorize access="isAnonymous()">
                                <li><a href="/login">Se connecter</a></li>
                            </sec:authorize>
                        </ul>
                    </div><!-- /.navbar-collapse -->
                </div><!-- /.container-fluid -->
            </nav>
        </div>

        <c:if test="${errors.size() > 0}">
            <div class="alert alert-danger alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <ul>
                    <c:forEach items="${errors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
        <c:if test="${notifications.size() > 0}">
            <div class="alert alert-info alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <ul>
                    <c:forEach items="${notifications}" var="notification">
                        <li>${notification}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
        <jsp:doBody/>

        <div id="pagescripts">
            <jsp:invoke fragment="scripts"/>
            <script type="text/javascript">
                $(function () {
                    $('[data-toggle="tooltip"]').tooltip();
                    $('[data-toggle="popover"]').popover();
                    $('.link-popover').click(function(){
                        $('.link-popover').not(this).popover('hide'); //all but this
                    });
                    $("abbr.timeago").timeago();
                })

                <sec:authorize access="isAuthenticated()">
                    setUnreadNotificationsCount();
                    window.setInterval(function(){
                        setUnreadNotificationsCount();
                    }, 5000);

                    function setUnreadNotificationsCount() {
                        $.get( "/notification/unread/count", function( data ) {
                            $("#notification-number" ).html( data );

                            if(data > 0) {
                                $("#notification-badge").addClass("alert-danger");
                            } else {
                                $("#notification-badge").removeClass("alert-danger");
                            }
                        });
                    }

                    $("#notification-block").hover(function() {
                        $.get( "/notification/", function( data ) {
                            $("#notifications").html( data );
                            $("abbr.timeago").timeago();
                        });
                    }, function() {
                        $.get( "/notification/allread");

                        $("#notification-number" ).html(0);
                        $("#notification-badge").removeClass("alert-danger");
                    });

                </sec:authorize>
            </script>
        </div>
    </div>
</body>
</html>