package com.esprit.hitgym.helpers;

import com.esprit.hitgym.service.CommonService;

import java.util.ArrayList;

public class Username {

    public static boolean checkUsername(String username) {

        ArrayList<String> allUsernames = CommonService.findAllUsernames();

        int i = 0;

        for (String s : allUsernames) {

            if (s.equals(username)) {
                System.out.println("Username found!");

                if (i <= CommonService.customersListCount) {
                    Password.isCustomerOrEmployee = "customer";
                    return true;
                } else if (i > CommonService.employeesListCount) {
                    Password.isCustomerOrEmployee = "employee";
                }

            }
            i++;
        }
        return false;
    }

}
