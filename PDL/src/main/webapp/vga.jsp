<%-- 
    Document   : vga
    Created on : Dec 11, 2013, 1:32:38 PM
    Author     : wesley
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index" />

<!DOCTYPE html>
<html  lang="${language}">
    <head>
        <!--Company Style-->
        <link rel="stylesheet" type="text/css" href="resources/css/homepage.css">
        <link rel="icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
        <!-- Bootstrap-->
        <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
        <link rel="stylesheet" href="resources/bootstrap/dist/css/bootstrap.min.css">
        <script src="resources/bootstrap/dist/js/bootstrap.min.js"></script>
        <script src="resources/bootstrap/dist/js/alert.js"></script>
        <!-- Select2 -->
        <link href="resources/select2/select2.css" rel="stylesheet"/>
        <script src="resources/select2/select2.js"></script>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home - Info Support</title>
    </head>
    <body
        <c:if test="${empty userVGAStatuses && !editedPSkills && !suggested}">
            onload="$('#modalStartSweep').modal('show');"
        </c:if>
        <c:if test="${empty userVGAStatuses && editedPSkills}">
            onload="document.getElementById('alertUpdateSuccessfull').style.display = 'block';
                        $('#modalEditPeriodicSweep').modal('show');
                        $('#modalStartSweep').modal('hide');"
        </c:if>
        >

        <!--Start nav bar-->
        <nav class="navbar navbar-inverse" role="navigation">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/PDL/homepage"><img src="resources/images/Logo.png"></a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="margin-top:12px">
                <ul class="nav navbar-nav">
                    <li><a href="/PDL/homepage">Home</a></li>
                    <li><a href="/PDL/courses"><fmt:message key="navbar.course"/></a></li>
                        <c:if test="${loggedInIsAdmin || loggedInIsTeacher || loggedInIsManager == true}">
                        <li class="dropdown" class="active">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Management <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="/PDL/management">Management</a></li>
                                <li class="divider"></li>
                                <li><a href="/PDL/i18n_nl">Internationalisation</a></li>
                            </ul>
                        </li> 
                        </c:if>
                    <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                        <c:if test="${loggedInIsAdmin || loggedInIsManager == true}">
                        <li class="active"><a href="/PDL/vga">VGA</a></li>
                        </c:if>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><fmt:message key="navbar.settings"/> <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="index.jsp"><fmt:message key="navbar.logout"/></a></li>
                            <li class="divider"></li>
                            <li>
                                <a >
                                    <form>
                                        <select id="language" name="language" onchange="submit()">
                                            <option value="en_US" ${language == 'en_US' ? 'selected' : ''}>English</option>
                                            <option value="nl_NL" ${language == 'nl_NL' ? 'selected' : ''}>Nederlands</option>
                                        </select>
                                    </form>
                                </a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </nav>
        <!-- eof navbar-->

                <div id="main_left">
             <button type="button" class="btn btn-default" id="btnShowModalStartSweep" onClick="$('#modalStartSweep').modal('show')" style="width:150px"><fmt:message key="vga.start.new.sweep"/></button>
           
            
            </div>
        <div id="main_right">
        <c:if test="${readyToSuggest == true}">
        <div id="main">
            <div class="alert alert-info" id="alertCanBeApplied">
                <a class="close" data-dismiss="alert">×</a>
                <h4>Ready to Apply!</h4> You can choose to suggest the output to the Users or just watch this output.
                <p>
                     <form id="applySweep" action="applySweep" method="post">
                    <button type="submit" class="btn btn-info" id="btnApplySweep">Apply Sweep</button>
                     </form>
                </p>
             </div>
        </c:if>
            <c:if test="${suggested == true}">
                    <div class="alert alert-success">
                        <a class="close" data-dismiss="alert">×</a>
                        <strong><fmt:message key="popup.done"/></strong> Users Activity Feed succesfully updated with the suggestions.
                    </div>
                </c:if>
            
            
            <c:if test="${!empty userVGAStatuses}">
                <table class="table" id="messagesOverview" name="messagesOverview">
                    <tr><th><fmt:message key="user.user"/></th><th>Status</th><th><fmt:message key="course.description"/></th></tr>
                            <c:forEach var="output" items="${userVGAStatuses}">
                        <tr id="${output.user.username}"><td>${output.user.firstname} ${output.user.lastname}</td><td>${output.status}</td><td>${output.statusDescription}</td></tr>
                        <script>
                            //set color on row
                            if ('${output.status}' === 'Skill Owner') {
                                document.getElementById('${output.user.username}').className = 'success';
                            }
                            else if ('${output.status}' === 'Busy acquiring Skill') {
                                document.getElementById('${output.user.username}').className = 'active';
                            }
                            else {
                                document.getElementById('${output.user.username}').className = 'warning';
                            }
                        </script>
                    </c:forEach>
                </table>
            </c:if>
        </div>
        </div>
        <!-- Modal Dialog for Starting a New Sweep -->
        <div class="modal fade" id="modalStartSweep">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="modalTitle"><fmt:message key="vga.initiate"/></h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal" role="form" id="formVGASweep" action="doSweep" method="post">
                            <p id="modalBodyText"><fmt:message key="vga.initiate.msg"/> </p>
                            <div class="form-group">
                                <label id="skillsLabel" for="skills" class="col-sm-2 control-label"><fmt:message key="skill.skill"/></label>
                                <div class="col-sm-10" id="divSkills">
                                    <input type="hidden" id="tagSkills" onkeyup="toggleStartButton()" onchange="toggleStartButton()" name="tagSkills" placeholder="&nbsp;Enter a Skill" style="width:100%">
                                    <script>
                                        //set all available skills from the database in the multiselect
                                        var arrSkills = new Array();
                                        <c:forEach var='skill' items='${skills}'>
                                        arrSkills.push('${skill.name}');
                                        </c:forEach>
                                        $('#tagSkills').select2({tags: arrSkills, tokenSeparators: [",", " "], createSearchChoice: false, maximumSelectionSize: 1});
                                    </script>

                                </div>
                            </div>
                        </form>
                        <div class="progress progress-striped active" id="progress" style="display:none">
                            <div class="progress-bar"  role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                                <span class="sr-only">Loading...</span>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-link" id="btnEditPeriodicSweep" style="float:left;" onClick="$('#modalEditPeriodicSweep').modal('show');
                                $('#modalStartSweep').modal('hide')">Set Skills for Periodic Sweep</button>
                        <button type="button" class="btn btn-default" id="btnCancel" data-dismiss="modal"><fmt:message key="documents.cancel"/></button>
                        <button type="button" class="btn btn-primary" id="btnStartSweep" onclick="doSweep();" disabled><fmt:message key="vga.start"/></button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->

        <!-- Modal Dialog for Editing Period Sweep -->
        <div class="modal fade" id="modalEditPeriodicSweep">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="modalTitle">Set Skills for Periodic Sweep</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal" role="form" id="formVGAPeriodic" action="updatePeriodic" method="post">
                            <div class="alert alert-success" style="display:none" id="alertUpdateSuccessfull">
                                <a class="close" data-dismiss="alert">×</a>
                                <strong>Success!</strong> The VGA has updated Skills to check for.
                            </div>
                            <p id="modalBodyText">The Skills you provide in this window are prerequisites for all employees. They will be checked for every first day of the month until an employee acquired it. </p>

                            <div class="form-group">
                                <label id="skillsLabel" for="skills" class="col-sm-2 control-label"><fmt:message key="skill.skill"/></label>
                                <div class="col-sm-10" id="divSkills">
                                    <input type="hidden" id="tagSkillsPeriodic" name="tagSkillsPeriodic" placeholder="&nbsp;Enter Skills" style="width:100%">
                                    <script>
                                        //set all available skills from the database in the multiselect
                                        var arrSkills = new Array();
                                        <c:forEach var='skill' items='${skills}'>
                                        arrSkills.push('${skill.name}');
                                        </c:forEach>
                                        $('#tagSkillsPeriodic').select2({tags: arrSkills, tokenSeparators: [",", " "], createSearchChoice: false});
                                    </script>

                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-link" id="btnCreateSweep" style="float:left;" onclick="$('#modalStartSweep').modal('show');
                                $('#modalEditPeriodicSweep').modal('hide')">Initiate new VGA sweep</button>
                        <button type="button" class="btn btn-default" id="btnCancelP" data-dismiss="modal"><fmt:message key="documents.cancel"/></button>
                        <button type="button" class="btn btn-primary" id="btnUpdatePeriodicSweep" onclick="doPeriodic();">Update</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
        <script>
            //set the skills in the multiselect that need to be checked for periodically
            var arraySkills = new Array();
            <c:forEach var="test" items="${skillsToCheckByVGA}">
            arraySkills.push('${test}');
            </c:forEach>
            $('#tagSkillsPeriodic').select2('val', [arraySkills]);

            // block the start button if there is no input in the skills feld
            function toggleStartButton() {
                var skills = $('#tagSkills').val();
                if (!skills) {
                    document.getElementById('btnStartSweep').disabled = true;
                }
                else {
                    document.getElementById('btnStartSweep').disabled = false;
                }
            }

            //submit button is clicked
            function doSweep() {
                //block the button and show a loader
                document.getElementById('progress').style.display = 'block';
                document.getElementById('modalBodyText').style.display = 'none';
                document.getElementById('skillsLabel').style.display = 'none';
                document.getElementById('divSkills').style.display = 'none';
                document.getElementById('btnStartSweep').disabled = true;
                document.getElementById('btnCancel').disabled = true;
                document.getElementById('btnEditPeriodicSweep').disabled = true;
                document.getElementById('modalTitle').innerHTML = 'VGA In Progress';

                document.getElementById('formVGASweep').submit();
            }

            //submit button for updating skills is clicked
            function doPeriodic() {
                document.getElementById('btnUpdatePeriodicSweep').disabled = true;
                document.getElementById('btnCancelP').disabled = true;
                document.getElementById('btnCreateSweep').disabled = true;

                document.getElementById('formVGAPeriodic').submit();
            }
        </script>
    </body>
</html>