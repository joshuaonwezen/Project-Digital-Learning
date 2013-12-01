<!--
@author Shahin Mokhtar
-->
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="index" />
<link rel="Shortcut Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<link rel="Icon" href="resources/images/favicon.ico" type="image/x-icon"></link>
<html  lang="${language}">
    <title>${firstname} ${lastname} - CV</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />

    <link rel="stylesheet" type="text/css" href="resources/css/cvreset.css" media="all" /> 
    <link rel="stylesheet" type="text/css" href="resources/css/cv.css" media="all" />

</head>
<body>
    <div id="doc2" class="yui-t7">
        <div id="inner">

            <div id="hd">
                <div class="yui-gc">
                    <div class="yui-u first">
                        <h1>${firstname} ${lastname}</h1>
                        <h2>${position}</h2>
                    </div>

                    <div class="yui-u">
                        <div class="contact-info">
                            <h3><a href="mailto:${emailAddress}">${emailAddress}</a></h3>
                        </div>
                    </div>
                </div>
            </div>

            <div id="bd">
                <div id="yui-main">
                    <div class="yui-b">


                        <c:if test="${workList.size() > 0}">
                            <div class="yui-gf">

                                <div class="yui-u first">
                                    <h2><fmt:message key="work.work"/></h2>
                                </div>

                                <div class="yui-u">
                                    <c:forEach var="tempWork" items="${workList}">
                                        <div class="job">
                                            <h2>${tempWork.name} </h2>
                                            <h3>${tempWork.profession}</h3>
                                            <h4>${tempWork.dateFrom} - ${tempWork.dateTill}</h4>
                                            <p>${tempWork.description}</p>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${projectList.size() > 0}">
                            <div class="yui-gf">

                                <div class="yui-u first">
                                    <h2><fmt:message key="project.project"/></h2>
                                </div>

                                <div class="yui-u">
                                    <c:forEach var="tempProject" items="${projectList}">
                                        <div class="job">
                                            <h2>${tempProject.name}</h2>
                                            <h3>${tempProject.profession}</h3>
                                            <h4>${tempProject.dateFrom} - ${tempProject.dateTill}</h4>
                                            <p>${tempProject.description}</p>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${educationList.size() > 0}">
                            <div class="yui-gf">

                                <div class="yui-u first">
                                    <h2><fmt:message key="education.education"/></h2>
                                </div>

                                <div class="yui-u">
                                    <c:forEach var="tempEducation" items="${educationList}">
                                        <div class="job">
                                            <h2>${tempEducation.name}</h2>
                                            <h3>${tempEducation.profession}</h3>
                                            <h4>${tempEducation.dateFrom} - ${tempEducation.dateTill}</h4>
                                            <p>${tempEducation.description}</p>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${skillList.size() > 0}">
                            <div class="yui-gf">
                                <div class="yui-u first">
                                    <h2><fmt:message key="skill.skill"/></h2>
                                </div>
                                <div class="yui-u">
                                    <c:forEach var="tempSkill" items="${skillList}">
                                        <ul class="talent">
                                            <li>${tempSkill.name}</li>
                                        </ul>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:if>

                    </div>
                </div>
            </div>

            <div id="ft">
                <p>${firstname} ${lastname} &mdash; <a href="mailto:${emailAddress}">${emailAddress}</a> &mdash;</p>
            </div>

        </div>


    </div>


</body>
</html>

