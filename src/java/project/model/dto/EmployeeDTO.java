package project.model.dto;

/**
 *
 * @author KhoaLe
 */
public class EmployeeDTO {
    private int employeeId;
    private String name;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String role;
    private String workingHours;
    private int ownerId;
    private int managerId;
    private int accountId;
    
    // Manager and Owner names for display purposes
    private String managerName;
    private String ownerName;
    private String accountUsername;

    // Default constructor
    public EmployeeDTO() {}

    // Constructor with essential fields
    public EmployeeDTO(String name, String firstName, String lastName, String phone, 
                      String email, String role, String workingHours) {
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.workingHours = workingHours;
    }

    // Full constructor
    public EmployeeDTO(int employeeId, String name, String firstName, String lastName, 
                      String phone, String email, String role, String workingHours, 
                      int ownerId, int managerId, int accountId) {
        this.employeeId = employeeId;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.workingHours = workingHours;
        this.ownerId = ownerId;
        this.managerId = managerId;
        this.accountId = accountId;
    }

    // Getters and Setters
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public int getManagerId() { return managerId; }
    public void setManagerId(int managerId) { this.managerId = managerId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getAccountUsername() { return accountUsername; }
    public void setAccountUsername(String accountUsername) { this.accountUsername = accountUsername; }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", workingHours='" + workingHours + '\'' +
                ", ownerId=" + ownerId +
                ", managerId=" + managerId +
                ", accountId=" + accountId +
                '}';
    }
}