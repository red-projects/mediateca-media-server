package com.red_projects.mediateca.communication.response;

public class Messages {

    public static class Failure {

        public final static String USER_CREATION = "Error Creating New User";

        public final static String PASSWORD_REQUIREMENT = "Password Does Not Meet Requirements";

        public final static String INVALID_USERNAME = "Username Is Associated With Another Account";

        public final static String INVALID_CREDENTIALS = "Invalid Credentials";

        public final static String USER_STATUS = "Account Has Not Been Verified Or Is Deactivated";

        public final static String EMAIL_VERIFICATION = "Verification Code Is Invalid Or Incorrect";

        public final static String BAD_REQUEST = "Bad Request";

        public final static String GET_USER_INFO = "Could Not Retrieve User Information";

        public final static String PROMOTE_USER = "Could Not Promote User";

        public final static String RESET_PASSWORD = "Could Not Reset User Password";

        public final static String DEMOTE_USER = "Could Not Demote User";

        public final static String DELETE_USER = "Could Not Promote User";

    }

    public static class Success {

        public final static String USER_REGISTRATION = "User Successfully Registered";

        public final static String EMAIL_VERIFICATION = "User Email Successfully Verified";

        public final static String PROMOTE_USER = "User Password Successfully Reset";

        public final static String RESET_PASSWORD = "User Password Successfully Reset";

        public final static String RESET_PASSWORD_REQUEST = "User Password Reset Request Received";

        public final static String DEMOTE_USER = "User Successfully Demoted";

        public final static String DELETE_USER = "User Successfully Deleted";

    }
}
