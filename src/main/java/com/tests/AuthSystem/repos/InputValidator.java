/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tests.AuthSystem.repos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author abeer
 */
public class InputValidator {

    //TODO: Switch to spring boot validation
    public static boolean validName(String name) {
        //Not empty & more than 3 characters & less than 3 characters
        if (name == null) {
            return false;
        }
        if (!name.isEmpty() && !name.isBlank()) {
            if (name.length() > 3 && name.length() < 15) {
                return true;
            }
        }
        return false;
    }

    public static boolean validEmail(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validPassword(String pass) {
        //String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$"; //At least 8 characters and requires alphabets and numbers
        Pattern pattern = Pattern.compile(regex);
        if (pass == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(pass);
        return matcher.matches();
    }
}
