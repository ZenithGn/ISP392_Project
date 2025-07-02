<%@page import="java.util.List"%>
<%@page import="project.model.dao.ServiceDAO"%>
<%@page import="project.model.dto.ServiceDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    ServiceDAO dao = new ServiceDAO();
    List<ServiceDTO> services = dao.getAllServices();
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Manage Services</title>
        <link rel="stylesheet" href="css/manageService.css">
    </head>
    <body>
        <div id="main-content">
            <h1>Service Management</h1>
            <button onclick="openPopup()">Create New Service</button>
            <br><br>
            <table>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Actions</th>
                </tr>
                <%
                    for (ServiceDTO service : services) {
                %>
                <tr>
                <form action="MainController" method="get" style="display:inline;">
                    <td><%= service.getServiceId()%></td>
                    <td>                        
                        <input type="text" name="name" value="<%= service.getServiceName() != null ? service.getServiceName() : ""%>">
                    </td>
                    <td>
                        <input type="text" name="price" value="<%= service.getPrice() != null ? service.getPrice() : ""%>">
                    </td>
                    <td>
                        <input type="hidden" name="action" value="updateService">
                        <input type="hidden" name="id" value="<%= service.getServiceId()%>">
                        <button type="submit">Update</button>
                </form>
                <form action="MainController" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="deleteService">
                        <input type="hidden" name="id" value="<%= service.getServiceId()%>">
                        <button type="submit">Delete</button>
                    </td>
                </form>
                </tr>
                <% }%>
            </table>
        </div>

        <!-- Create Popup -->
        <div id="popup-overlay" class="overlay">
            <div class="popup">
                <h2>Create Service</h2>
                <form action="MainController" method="post">
                    <input type="hidden" name="action" value="createService">
                    <input type="text" name="serviceName" placeholder="Service Name" required><br><br>
                    <input type="text" name="servicePrice" placeholder="Price" required><br><br>
                    <input type="number" name="garageId" placeholder="GarageID" min="1" max="4" required><br><br>
                    <input type="hidden" name="isActive" value="true">
                    <button type="submit">Create</button>
                    <button type="button" onclick="closePopup()">Cancel</button>
                </form>
            </div>
        </div>

        <script>
            function openPopup() {
                document.getElementById('popup-overlay').style.display = 'flex';
                document.getElementById('main-content').classList.add('blur');
            }

            function closePopup() {
                document.getElementById('popup-overlay').style.display = 'none';
                document.getElementById('main-content').classList.remove('blur');
            }
        </script>
    </body>
</html>