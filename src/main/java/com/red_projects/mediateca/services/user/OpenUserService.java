package com.red_projects.mediateca.services.user;

import com.red_projects.mediateca.communication.requests.RegisterRequest;
import com.red_projects.mediateca.entities.utils.TokenAction;
import com.red_projects.mediateca.entities.utils.UserStatus;
import com.red_projects.mediateca.repositories.UserRepository;
import com.red_projects.mediateca.entities.Token;
import com.red_projects.mediateca.entities.utils.AccessLevel;
import com.red_projects.mediateca.entities.User;
import com.red_projects.mediateca.security.PasswordRequirements;
import com.red_projects.mediateca.security.PasswordValidator;
import com.red_projects.mediateca.utils.AuthStatus;
import com.red_projects.mediateca.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class OpenUserService {

    private final UserRepository userRepository;

    @Autowired
    public OpenUserService(UserRepository userRepository) {
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
        User user = new User(userInfo.getFirstName(), userInfo.getLastName());
        user.setEmailAddress(userInfo.getEmailAddress());
        user.setUsername(userInfo.getUsername());
        user.setStatus(UserStatus.UNVERIFIED.name());
        user.setPassword(SecurityUtil.bCryptEncode(userInfo.getPassword()));

        // generate verification code
        String passcode = SecurityUtil.generateVerificationCode();
        Token token = new Token(TokenAction.VERIFY_EMAIL, user.getId());
        token.setEncryptedCode(SecurityUtil.bCryptEncode(passcode));

        // if first user created make as admin
        if (userRepository.countByAccessLevel(AccessLevel.OWNER.name()) < 1) {
            user.setAccessLevel(AccessLevel.OWNER.name());
        }

        // insert
        userRepository.save(user);

        // queue verification email
        System.out.println("VerificationCode: " + SecurityUtil.base64Encode(passcode));
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
                    if (user.getAccessLevel().contentEquals(AccessLevel.ADMIN.name())) return AuthStatus.SUCCESS_ADMIN;
                    else if (user.getAccessLevel().contentEquals(AccessLevel.OWNER.name())) return AuthStatus.SUCCESS_OWNER;
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
                    user.setStatus(UserStatus.ACTIVE.name());
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
            if (user.getStatus().contentEquals(UserStatus.ACTIVE.name())) {
                return user.getEmailAddress();
            }
            return AuthStatus.UNVERIFIED_DEACTIVATED.name();
        }
        return AuthStatus.FAILED.name();
    }


    public boolean resetPasswordRequest(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // check if user
            if (user.getStatus().contentEquals(UserStatus.ACTIVE.name())) {
                // submit reset password email request
            }
        }
        return false;
    }

}
