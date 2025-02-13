// In file: com/esprit/hitgym/service/BmiService.java

package com.esprit.hitgym.service;

import com.esprit.hitgym.Entity.BMI;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.esprit.hitgym.utils.Datasource.getConnection;

public class BmiService {

    public boolean add(BMI bmi, int fk_customer_id) {

        PreparedStatement queryStatement = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    INSERT INTO bmi (id, weight, recorded_date, fk_customer_id, recorded_month, height, bmi_value)
                    VALUES (?,?,?,?,?,?,?);
                    """);

            queryStatement.setInt(1, bmi.getId());
            queryStatement.setDouble(2, bmi.getWeight());
            queryStatement.setDate(3, bmi.getRecordedDate());
            queryStatement.setInt(4, fk_customer_id);
            queryStatement.setString(5, bmi.getRecordedMonth());
            queryStatement.setDouble(6, bmi.getHeight());
            queryStatement.setDouble(7, bmi.getBMI());

            queryStatement.executeUpdate();
            return true;


        } catch (SQLException e) {
            System.out.println("Error : " + e);
            return false;
        }

    }

    public ResultSet findAll() {

        PreparedStatement queryStatement = null;
        ResultSet bmiRs = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    SELECT * FROM bmi;
                    """);
            bmiRs = queryStatement.executeQuery();

            while (bmiRs.next()) {
                System.out.println(bmiRs.getInt(1));
                System.out.println(bmiRs.getString(2));
                System.out.println(bmiRs.getString(3));
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }

        return bmiRs;

    }

    public ResultSet findBmiByCustomerId(int customerId) {
        PreparedStatement queryStatement = null;
        ResultSet bmiRs = null;

        try {
            queryStatement = getConnection().prepareStatement("""
                    SELECT * FROM bmi WHERE fk_customer_id = ?
                    """);
            queryStatement.setInt(1, customerId);
            bmiRs = queryStatement.executeQuery();


        } catch (SQLException e) {
            System.out.println("Error : " + e);
        }

        return bmiRs;
    }
}