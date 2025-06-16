<%-- 
    Document   : register
    Created on : Jun 12, 2025, 1:50:21 PM
    Author     : Khanh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="css/register.css">
    </head>
    <body>

        <div class="image-box"></div>

        <div class="container">
            <form action="MainController" method="POST">
                User Name:<input type="text" name="userName" required="" minlength="2" maxlength="20"/>
                </br>Email Address:<input type="text" name="emailAddress" required="" maxlength="250" minlength="2"/>
                </br>Phone:<input type="text" name="phone" required=""/>
                </br>Password:<input type="password" name="password" required="" minlength="8" maxlength="30"/>
                </br>Confirm password:<input type="password" name="confirm" required=""/>

                <p>Đã có tài khoản?  <a href="login.jsp">Login</a></p>
                </br><input type="submit" name="action" value="Create"/>

            </form>
        </div>
    </body>
</html>
