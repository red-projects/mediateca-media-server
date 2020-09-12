package com.redprojects.mediateca.security;

public class PasswordValidator {

    private PasswordRequirements requirements;
    private String password;

    public PasswordValidator(PasswordRequirements requirements, String password) {
        this.requirements = requirements;
        this.password = password;
    }

    public boolean checkLengthRequirement() {
        return password.length() >= requirements.getLengthRequired();
    }

    public boolean checkLowerCaseRequirement() {
        if (requirements.isLowerCaseRequired()) {
            return password.matches(".*[a-z].*");
        }
        return true;
    }

    public boolean checkUpperCaseRequirement() {
        if (requirements.isUpperCaseRequired()) {
            return password.matches(".*[A-Z].*");
        }
        return true;
    }

    public boolean checkNumberRequirement() {
        if (requirements.isNumberRequired()) {
            return password.matches(".*[0-9].*");
        }
        return true;
    }

    public boolean checkSymbolRequirement() {
        if (requirements.isSymbolRequired()) {
            return password.matches(".*[-+_!@#$%^&*.,?].*");
        }
        return true;
    }

    public boolean passesRequirements() {
        return checkLengthRequirement() && checkLowerCaseRequirement()
                && checkUpperCaseRequirement() && checkNumberRequirement()
                && checkSymbolRequirement();
    }

}
