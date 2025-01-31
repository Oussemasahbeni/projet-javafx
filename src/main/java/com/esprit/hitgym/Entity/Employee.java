package com.esprit.hitgym.Entity;

import com.esprit.hitgym.controller.employees.EmployeesDetailCardController;
import com.esprit.hitgym.controller.employees.EmployeesPanelController;
import com.esprit.hitgym.helpers.CustomDate;
import com.esprit.hitgym.service.CommonService;
import com.esprit.hitgym.view.CustomMenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.Date;

public class Employee extends Person {

    private String designation;
    private java.sql.Date joiningDate;
    private String year;
    private String month;
    private CustomDate customDate;

    //    private AccessLevel accessLevel;
    private int salary;
    private int id;
    private int access;

    /**/
    private CustomMenuButton actionbtn;
    private MenuItem item1 = new MenuItem("View");
    private MenuItem item2 = new MenuItem("Remove");
    /**/

    public CustomMenuButton getActionbtn() {
        return actionbtn;
    }

    public void setActionbtn(CustomMenuButton actionbtn) {
        this.actionbtn = actionbtn;
    }

    public Employee(java.sql.Date joiningDate, String firstName, String lastName, String email, String phoneNumber, String cinNumber, String designation, int salary, int id, CustomMenuButton actionbtn) {
        super(firstName, lastName, email, "gender", phoneNumber, "Username", "password", cinNumber);
        this.designation = designation;
        this.joiningDate = joiningDate;
        this.salary = salary;
        this.id = id;
        this.actionbtn = actionbtn;
        this.actionbtn.setStyle("-fx-background-color: #00C2FF; -fx-background-radius: 12px;");
        this.actionbtn.setTextFill(Paint.valueOf("White"));
        actionbtn.getItems().addAll(item1, item2);
        item1.setOnAction(event ->
        {
            EmployeesDetailCardController.FullName = actionbtn.getFullName();
            EmployeesDetailCardController.Phone = actionbtn.getPhone();
            EmployeesDetailCardController.Gender = actionbtn.getGender();
            EmployeesDetailCardController.Emails = actionbtn.getEmail();
            EmployeesDetailCardController.Username = actionbtn.getUsername();
            EmployeesDetailCardController.Designation = actionbtn.getDesignation();
            EmployeesDetailCardController.Salary = String.valueOf(actionbtn.getSalary()) + "TND. ";
            try {
                EmployeesPanelController.view();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        item2.setOnAction(event ->
        {

            EmployeesPanelController.deletingId = actionbtn.getButtonId();
            CommonService.deleteData("employees", EmployeesPanelController.deletingId);
        });
        customDate = new CustomDate(joiningDate);

        this.month = customDate.getMonthName();
        this.year = customDate.getYear();
    }

    public Employee(java.sql.Date joiningDate, String firstName, String lastName, String email, String phoneNumber, String cinNumber, String designation, int salary, int id, String gender, String username, String password) {
        super(firstName, lastName, email, gender, phoneNumber, username, password, cinNumber);
        this.designation = designation;
        this.joiningDate = joiningDate;
        this.salary = salary;
        this.id = id;

        customDate = new CustomDate(joiningDate);

        this.month = customDate.getMonthName();
        this.year = customDate.getYear();
    }

//    public Employee(String firstName, String lastName, String email, String gender, String phoneNumber, String userName, String password, String nicNumber, String designation, java.sql.Date joiningDate, int salary, int id, String salt, int access) {
//        super(firstName, lastName, email, gender, phoneNumber, userName, password, nicNumber);
//        this.designation = designation;
//        this.joiningDate = joiningDate;
//        this.salary = salary;
//        this.id = id;
//        this.salt = salt;
//        this.access = access;
//
//    }

    public Employee() {
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }


    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public java.sql.Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = (java.sql.Date) joiningDate;
    }

    /*
        public AccessLevel getAccessLevel() {
            return accessLevel;
        }

        public void setAccessLevel(AccessLevel accessLevel) {
            this.accessLevel = accessLevel;
        }
    */
    public String getlowerfirstname() {
        return getFirstName().toLowerCase();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setJoiningDate(java.sql.Date joiningDate) {
        this.joiningDate = joiningDate;
    }


    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }
}
