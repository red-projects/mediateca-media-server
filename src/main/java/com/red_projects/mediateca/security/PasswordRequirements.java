package com.red_projects.mediateca.security;

public class PasswordRequirements {

    private int lengthRequired;
    private boolean upperCaseRequired;
    private boolean lowerCaseRequired;
    private boolean numberRequired;
    private boolean symbolRequired;


    public PasswordRequirements() {
        this.lengthRequired = 8;
        this.upperCaseRequired = false;
        this.lowerCaseRequired = false;
        this.upperCaseRequired = false;
    }

    public int getLengthRequired() {
        return lengthRequired;
    }

    public void setLengthRequired(int lengthRequired) {
        this.lengthRequired = lengthRequired;
    }

    public boolean isUpperCaseRequired() {
        return upperCaseRequired;
    }

    public void setUpperCaseRequired(boolean upperCaseRequired) {
        this.upperCaseRequired = upperCaseRequired;
    }

    public boolean isLowerCaseRequired() {
        return lowerCaseRequired;
    }

    public void setLowerCaseRequired(boolean lowerCaseRequired) {
        this.lowerCaseRequired = lowerCaseRequired;
    }

    public boolean isNumberRequired() {
        return numberRequired;
    }

    public void setNumberRequired(boolean numberRequired) {
        this.numberRequired = numberRequired;
    }

    public boolean isSymbolRequired() {
        return symbolRequired;
    }

    public void setSymbolRequired(boolean symbolRequired) {
        this.symbolRequired = symbolRequired;
    }
}
