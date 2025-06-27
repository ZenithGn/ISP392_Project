<%-- 
    Document   : Login
    Created on : Jun 12, 2025, 9:42:32 AM
    Author     : Khanh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Trang đăng nhập</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="css/login.css">
         <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
         <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    </head>
    <body>
        <!-- Display error message if any -->
            
        <!-- Login Form -->
        
            <a href="homepage.jsp">
            <button class="back-button"><i class="fa fa-arrow-left"></i>Back</button>
        </a>
            
           

            

            
                 <section class="wrapper">
                     
      <div class="form signup">
        <header>Signup</header>
        <form action="MainController" method="POST">
          <input type="text" placeholder="Full name" required />
          <input type="text" placeholder="Email address" required />
          <input type="text" placeholder="Phone" required />
          <input type="password" placeholder="Password" required />
          <input type="password" placeholder="Confirm Password" required />
          
          <div class="checkbox"> 
            <input type="checkbox" id="signupCheck" />
            <label for="signupCheck">I accept all terms & conditions</label>
          </div>
          <input type="submit" name="action" value="Signup" />
        </form>
      </div>
      <div class="form login">
        <header>Login</header>
        <form action="MainController" method="POST">
          <input type="text" name="phone" required placeholder="Phone"/> 
                <input type="password" name="password" required placeholder="Password"/>
          <a href="forgotPassword.jsp">Forgot password?</a>
          <input type="submit" name="action" value="Login" />
        </form>
        
        <% String error = (String) request.getAttribute("error"); %>
            <% if (error != null) { %>
                <div style="color: red; margin-bottom: 10px;">
                    <%= error %>
                </div>
            <% } %>
      </div>
      <script>
        const wrapper = document.querySelector(".wrapper"),
          signupHeader = document.querySelector(".signup header"),
          loginHeader = document.querySelector(".login header");
        loginHeader.addEventListener("click", () => {
          wrapper.classList.add("active");
        });
        signupHeader.addEventListener("click", () => {
          wrapper.classList.remove("active");
        });
      </script>
    </section>
  </body>
</html>