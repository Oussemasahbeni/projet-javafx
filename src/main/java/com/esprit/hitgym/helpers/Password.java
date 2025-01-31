package com.esprit.hitgym.helpers;

import com.esprit.hitgym.service.CustomerService;
import com.esprit.hitgym.service.EmployeeService;
import com.esprit.hitgym.utils.SecurityUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Password {


    public static String isCustomerOrEmployee;

    private final CustomerService customerService;
    private final EmployeeService employeeService;

    public Password() {
        this.customerService = new CustomerService();
        this.employeeService = new EmployeeService();
    }

    private static void checkCustomerEmployee() {

    }

    public static String[] makeFinalPassword(String password) {

        String changedPassword = DigestUtils.sha3_256Hex(password);
        SecureRandom secureRandom = null;
        String[] passSalt = new String[2];

        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error! " + e);
        }

        assert secureRandom != null;
        int tempRandom1 = secureRandom.nextInt(100000, 999999);
        String random1 = Integer.toString(tempRandom1);
        changedPassword = changedPassword + random1;

        passSalt[0] = random1;
        passSalt[1] = changedPassword;

        return passSalt;
    }

    public boolean verifyPassword(String email, String enteredPassword) {


        try {
            String storedPasswordHash = "";

            if (isCustomerOrEmployee.equals("customer")) {
                storedPasswordHash = customerService.findUserPassword(email);
            } else if (isCustomerOrEmployee.equals("employee")) {
                storedPasswordHash = employeeService.findEmployeePassword(email);
            }

            if (SecurityUtil.checkPassword(enteredPassword, storedPasswordHash)) {
                System.out.println("Access granted.");
                return true;
            } else {
                System.out.println("Wrong password");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
