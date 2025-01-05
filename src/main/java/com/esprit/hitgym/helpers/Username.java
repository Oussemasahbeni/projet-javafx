package com.esprit.hitgym.helpers;

import com.esprit.hitgym.db.DatabaseFunctions;

import java.util.ArrayList;

public class Username {

    public static boolean checkUsername(String username) {

        ArrayList<String> allUsernames = DatabaseFunctions.getAllUsernames();

        int i = 0;

        for (String s : allUsernames) {

            if (s.equals(username)) {
                System.out.println("Username found!");

                if (i <= DatabaseFunctions.customersListCount) {
                    Password.isCustomerOrEmployee = "customer";
                    return true;
                } else if (i > DatabaseFunctions.employeesListCount) {
                    Password.isCustomerOrEmployee = "employee";
                }

            }
            i++;
        }
        return false;
    }

}
