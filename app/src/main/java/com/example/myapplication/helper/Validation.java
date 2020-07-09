package com.example.myapplication.helper;

public class Validation {
    private String phoneVal = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";

    public boolean isEmpty(String value) {
        if (value.equalsIgnoreCase("")) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isValidPhone(String value){
        if(!value.matches(phoneVal)){
            return true;
        }
        else {
            return false;
        }
    }
    public String lastLetter(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        str = str.trim();
        return str;
    }
    public String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length()-1);
    }
}
