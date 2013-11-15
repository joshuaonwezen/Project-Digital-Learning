<%-- 
    Document   : users
    Created on : Nov 4, 2013, 1:03:00 PM
    Author     : wesley
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Management</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width">

        <!-- DHTMLX General -->
        <script src="resources/dhtmlx/dhtmlxTabbar/codebase/dhtmlxcommon.js"></script>
        <!-- DHTMLX Tabbar -->
        <link rel="stylesheet" type="text/css" href="resources/dhtmlx/dhtmlxTabbar/codebase/dhtmlxtabbar.css">
        <script src="resources/dhtmlx/dhtmlxTabbar/codebase/dhtmlxtabbar.js"></script>
        <script src="resources/dhtmlx/dhtmlxTabbar/codebase/dhtmlxtabbar_start.js"></script>
        <!-- DHTMLX Grid -->
        <link rel="stylesheet" type="text/css" href="resources/dhtmlx/dhtmlxGrid/codebase/dhtmlxgrid.css">
        <script src="resources/dhtmlx/dhtmlxGrid/codebase/dhtmlxgrid.js"></script>        
        <script src="resources/dhtmlx/dhtmlxGrid/codebase/dhtmlxgridcell.js"></script>  
        <script src="resources/dhtmlx/dhtmlxGrid/codebase/ext/dhtmlxgrid_start.js"></script>
        <!-- DHTMLX Menu -->
        <script src="resources/dhtmlx/dhtmlxMenu/codebase/dhtmlxmenu.js"></script>
        <script src="resources/dhtmlx/dhtmlxMenu/codebase/ext/dhtmlxmenu_ext.js"></script>
    </head>
    <body>
        <div id="usersGrid"></div>
        <br>
        <script>

            //contextual menu settings for grid
            usersMenu = new dhtmlXMenuObject();
            usersMenu.setIconsPath("resources/dhtmlx/dhtmlxMenu/samples/common/images/");
            usersMenu.setSkin('dhx_terrace');
            usersMenu.renderAsContextMenu();
            usersMenu.attachEvent("onClick", onButtonClick);
            usersMenu.loadXML("resources/dhtmlx/dhtmlxMenu/structures/users.xml");

            //grid settings
            usersGrid = new dhtmlXGridObject('usersGrid');
            usersGrid.enableAutoHeight(true);
            usersGrid.enableAutoWidth(true);
            usersGrid.setImagePath("resources/dhtmlx/dhtmlxGrid/codebase/imgs/");
            usersGrid.setSkin('dhx_terrace');
            usersGrid.enableContextMenu(usersMenu);
            usersGrid.setHeader("Employee ID, Username, First Name, Last Name, E-mail address, Position, Admin");
            //way in which text has to be aligned
            usersGrid.setColAlign("left,left,left,left,left,left,center");
            //int=integer, str=string
            usersGrid.setColSorting("int,str,str,str,str,str,str");
            //ro=readonly, ch=checkbox
            usersGrid.setColTypes("ro,ro,ro,ro,ro,ro,ch");
            //disable cell editing
            usersGrid.enableEditEvents(false, false, false);
            //disable checkbox editing
            usersGrid.attachEvent("onEditCell", function(stage, rId, cInd, nValue, oValue) {
                return false;
            });
            usersGrid.init();

            //now lets build a javascript array with our users for the grid
            var users = new Array();

            <c:forEach var='user' items='${users}'>
            var row = ['${user.userId}', '${user.username}', '${user.firstname}', '${user.lastname}', '${user.emailAddress}', '${user.position}', '${user.isAdmin}'];
            users.push(row);
            </c:forEach>

            //set data in grid
            usersGrid.parse(users, "jsarray");

            //event handling for contextual menu
            function onButtonClick(menuitemId) {
                var data = usersGrid.contextID.split("_");
                var rowIndex = data[0];
                var columnIndex = data[1];

                switch (menuitemId) {
                    case "new":
                        openUserWindow(null);
                        break;
                    case "view":
                        openUserProfile(usersGrid.cells(rowIndex, 0).getValue());
                        break;
                    case "edit":
                        openUserWindow(usersGrid.cells(rowIndex, 0).getValue());
                        break;
                    case "delete":
                        deleteUser(usersGrid.cells(rowIndex, 0).getValue());
                        break;
                }
            }
            //variables for the sizes and location (center) of a popup
            var popupWidth = 800;
            var popupHeight = 500;
            var popupLeft = (screen.width / 2) - (popupWidth / 2);
            var popupTop = (screen.height / 2) - (popupHeight / 2);

            // view profile
            function openUserProfile(userId) {
                var uri = "profile?id=";
                //edit a user
                if (userId !== null) {
                    uri += userId;
                }
                
                window.open(uri, "menubar=no" +
                        ",width=" + popupWidth + ",height=" + popupHeight +
                        ",top=" + popupTop + ",left=" + popupLeft);
            }

            //window for creating/editing a user
            function openUserWindow(userId) {
                var uri = "users/edit?userId=";
                //edit a user
                if (userId !== null) {
                    uri += userId;
                }

                //now open a new window for creating/editing a user
                window.open(uri, "_blank", "menubar=no" +
                        ",width=" + popupWidth + ",height=" + popupHeight +
                        ",top=" + popupTop + ",left=" + popupLeft);
            }

            function deleteUser(userId) {
                if (confirm('Weet u het zeker dat u deze gebruiker wil verwijderen?'))
                    window.location = 'users/delete?userId=' + userId;
            }

        </script>         

        <div id="tabbar"></div>
        <script>
            tabbar = new dhtmlXTabBar("tabbar", "top");
            tabbar.setSkin('dhx_terrace');
            tabbar.setImagePath("resources/dhtmlx/dhtmlxTabbar/codebase/imgs/");
            //we load our tabbar from an xml file (because it works best)
            tabbar.loadXML("resources/dhtmlx/dhtmlxTabbar/structures/admin.xml", function() {
                tabbar.setTabActive("t1");
                tabbar.setContent('t1', 'usersGrid');
            });
        </script>
    </body>
</html>