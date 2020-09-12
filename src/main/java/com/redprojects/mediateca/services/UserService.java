package com.redprojects.mediateca.services;

import com.redprojects.mediateca.communication.requests.RegisterRequest;
import com.redprojects.mediateca.entities.User;
import com.redprojects.mediateca.repositories.UserRepository;
import com.redprojects.mediateca.security.PasswordRequirements;
import com.redprojects.mediateca.security.PasswordValidator;
import com.redprojects.mediateca.utils.AuthStatus;
import com.redprojects.mediateca.utils.SecurityUtil;
import com.redprojects.mediateca.utils.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {


    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUsernameUnique(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isEmpty();
    }

    public boolean isValidPassword(String password) {
        // password
        PasswordRequirements requirements = SecurityUtil.passwordRequirements;
        PasswordValidator passwordValidator = new PasswordValidator(requirements, password);
        return passwordValidator.passesRequirements();
    }


    public boolean createUser(RegisterRequest userInfo) {
        // generate verification code
        String verificationCode = SecurityUtil.generateVerificationCode();

        User newUser = new User(userInfo.getFirstName(), userInfo.getLastName());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        newUser.setId(UUID.randomUUID().toString());
        newUser.setEmailAddress(userInfo.getEmailAddress());
        newUser.setUsername(userInfo.getUsername());
        newUser.setStatus(UserStatus.UNVERIFIED.toString());
        newUser.setVerificationCode(SecurityUtil.bCryptEncode(verificationCode));
        newUser.setCreationDate(timestamp);
        newUser.setLastUpdated(timestamp);
        newUser.setPassword(SecurityUtil.bCryptEncode(userInfo.getPassword()));

        // if first user created make as admin
        if (userRepository.countByAdminTrue() < 1) { newUser.setAdmin(true); }
        // insert
        userRepository.save(newUser);

        // queue verification email
        System.out.println("VerificationCode: " + SecurityUtil.base64Encode(verificationCode));
        // TODO: CREATE EMAIL REQUESTER

        return true;
    }

    public AuthStatus authenticateUser(String username, String password) {
        Optional<User> opUser = userRepository.findByUsername(username);
        if (opUser.isPresent()) {
            User user = opUser.get();
            // check if user has is not unverified or deactivated
            if (user.getStatus().contentEquals(UserStatus.ACTIVE.toString())) {
                boolean matches = SecurityUtil.bCryptCompare(password, user.getPassword());
                if (matches) {
                    if (user.isAdmin()) return AuthStatus.SUCCESS_ADMIN;
                    else return AuthStatus.SUCCESS;
                }
                else
                    return AuthStatus.FAILED;
            }
            return AuthStatus.UNVERIFIED_DEACTIVATED;
        }
        return AuthStatus.FAILED;
    }

    public AuthStatus verifyUserEmail(String email, String verificationCode) {
        if (verificationCode.length() > 0) {
            ArrayList<User> users = userRepository.findByEmailAddress(email);
            for (User user : users) {
                boolean matches = SecurityUtil.bCryptCompare(verificationCode, user.getVerificationCode());
                if (matches) {
                    user.setStatus(UserStatus.ACTIVE.toString());
                    user.setVerificationCode(null);
                    userRepository.save(user);
                    return AuthStatus.SUCCESS;
                }
            }
        }
        return AuthStatus.FAILED;
    }

    public String authenticateCredentialsForEmail(String username) {
        Optional<User> opUser = userRepository.findByUsername(username);
        if (opUser.isPresent()) {
            User user = opUser.get();
            // check if user has is not unverified or deactivated
            if (user.getStatus().contentEquals(UserStatus.ACTIVE.toString())) {
                return user.getEmailAddress();
            }
            return AuthStatus.UNVERIFIED_DEACTIVATED.toString();
        }
        return AuthStatus.FAILED.toString();
    }

    public boolean resetPasswordRequest(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // check if user
            if (user.getStatus().contentEquals(UserStatus.ACTIVE.toString())) {
                // submit reset password email request
            }
        }
        return false;
    }



}
