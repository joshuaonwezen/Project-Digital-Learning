<%-- 
    Document   : files
    Created on : Dec 9, 2013, 12:01:05 PM
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
        <!-- Moment JS-->
        <script src="resources/moment/moment-m.js" type="text/javascript"></script>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Documents - Info Support</title>
    </head>
    <body>
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
                    <li  class="active"><a href="/PDL/courses"><fmt:message key="navbar.course"/></a></li>
                        <c:if test="${loggedInIsAdmin || loggedInIsTeacher || loggedInIsManager == true}">
                        <li class="dropdown" class="active">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">Management <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="/PDL/management">Management</a></li>
                                <li class="divider"></li>
                                <li><a href="/PDL/i18n">Internationalisation</a></li>
                            </ul>
                        </li> 
                        </c:if>
                    <li><a href="/PDL/profile?id=${loggedInUserId}"><fmt:message key="navbar.profile"/></a></li>
                    <c:if test="${loggedInIsAdmin || loggedInIsManager == true}">
                    <li><a href="/PDL/vga">VGA</a></li>
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
        <div id="main">
            <c:if test="${course.owner.userId == loggedInUserId}">
                <button type="button" class="btn btn-default" id="btnShowModalUpload" onClick="$('#modalUpload').modal('show')"><fmt:message key="documents.upload.files"/></button>
            </c:if>
            <table class="table" id="tblDocuments" name="tblDocuments">
                <tr><th><fmt:message key="documents.name"/></th><th><fmt:message key="documents.modefied"/></th><th><fmt:message key="documents.size"/></th><c:if test="${course.owner.userId == loggedInUserId}"><th>Manage</th></c:if></tr>
                <c:forEach var="file" items="${files}">
                    <c:if test="${file.visible || (file.owner.userId == loggedInUserId)}">
                    <tr>
                        <td><a href="#">${file.name}</a></td>
                        <td><script>document.write(moment('${file.lastEdited}').fromNow());</script></td>
                        <td>
                            <script>
                            var fileSize = ${file.fileSize/1024};
                            var metric = ' KB';
                            if (fileSize > 1024){
                                metric = ' MB';
                                fileSize = fileSize/1024;
                            }
                            if (fileSize > 1024){
                                metric = ' GB';
                                fileSize = fileSize/1024;
                            }
                            document.write(Math.round(fileSize) + metric);
                            </script>
                        </td>
                        <td>
                            <c:if test="${file.owner.userId == loggedInUserId}">
                        <form id="formVisibilityFile" action="changeFileVisibility" method="post" style="margin: 0; padding: 0;display:inline">
                            <input type="hidden" id="fileToChangeVisibility" name="fileToChangeVisibility" value="${file.fileId}"/>
                            <input type="hidden" id="courseId" name="courseId" value="${course.courseId}"/>
                            
                            <button type="submit" class="btn btn-default btn-xs <c:if test="${file.visible}">active </c:if>" 
                                    <button type="submit" class="btn btn-default btn-xs <c:if test="${file.visible}">active </c:if>" 
                                    <c:if test="${file.visible}">title="Visible" data-placement="bottom" data-toggle="tooltip"</c:if>
                                    <c:if test="${!file.visible}">title="Invisible" data-placement="bottom" data-toggle="tooltip"</c:if>
                                    id="btnChangeVisibility">
                                <span class="glyphicon glyphicon-eye-open"></span>
                            </button>
                        </form>
                        <form id="formDeleteFile" action="deleteFile" method="post" style="margin: 0; padding: 0;display:inline">
                            <input type="hidden" id="fileToDelete" name="fileToDelete"/>
                            <input type="hidden" id="courseId" name="courseId" value="${course.courseId}"/>
                            <button type="button" class="btn btn-default btn-xs" id="btnDelete" title="Delete" data-placement="bottom" data-toggle="tooltip" onclick="$('#confirmDelete').modal('show');document.getElementById('fileToDelete').value='${file.fileId}';">
                                <span class="glyphicon glyphicon-trash"></span>
                            </button>
                        </form>
                            </c:if>
                        </td>
                    </tr>
                    </c:if>
                </c:forEach>
            </table>
        </div>
        <!-- Modal Dialog for uploading documents -->
        <div class="modal fade" id="modalUpload">
            <div class="modal-dialog">
                <form id="formFileUpload" action="uploadFiles" method="post" enctype="multipart/form-data">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="modalTitle"><fmt:message key="documents.upload"/></h4>
                    </div>
                    <div class="modal-body">
                        <div class="alert alert-danger" id="uploadSizeError" style="display:none">
                        <a class="close" data-dismiss="alert">×</a>
                        <fmt:message key="documents.limit"/>
                    </div>
                        <p id="modalBodyText"><fmt:message key="documents.upload.select"/></p>
                            <input type="hidden" id="courseId" name="courseId" value="${course.courseId}"/>
                            
                            <input type="file" id="file" name="file" multiple/>
                        <div class="progress progress-striped active" id="progress" style="display:none">
                                    <div class="progress-bar"  role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
                                    <span class="sr-only">Loading...</span>
                                </div>
                                </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" id="btnClose" class="btn btn-default" data-dismiss="modal"><fmt:message key="documents.cancel"/></button>
                        <button type="button" id="btnSubmit" class="btn btn-primary" onclick="uploadFiles()">Upload</button>
                    </div>
                    </form>
                            
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
        <script>
            //block the upload button and show that the upload is in progress
            function uploadFiles(){
                 //block the button and show a loader
                document.getElementById('progress').style.display = 'block';
                document.getElementById('modalBodyText').style.display = 'none';
                document.getElementById('file').style.display = 'none';
                document.getElementById('btnSubmit').disabled = true;
                document.getElementById('btnClose').disabled = true;
                document.getElementById('modalTitle').innerHTML = 'Upload In Progress';
                
                document.getElementById('formFileUpload').submit();
            }
            //deleting a document
            function deleteFile(){
                document.getElementById('formDeleteFile').submit();
            }
            
            var uploadError;
            if ('${uploadSizeError}' !== ''){
                uploadError = true;
            }
            else{
                uploadError = false;
            }
            
            if (uploadError){
                document.getElementById('uploadSizeError').style.display = 'block';
                $('#modalUpload').modal('show');
            }
        </script>
                <!-- Modal Dialog for Deleting -->
        <div class="modal fade" id="confirmDelete">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title"><fmt:message key="documents.delete"/></h4>
                    </div>
                    <div class="modal-body">
                        <p><fmt:message key="documents.delete.confirm"/></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="documents.cancel"/></button>
                        <button type="button" class="btn btn-primary" onclick="deleteFile()"><fmt:message key="edit.popup.yes"/></button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
    </body>
</html>