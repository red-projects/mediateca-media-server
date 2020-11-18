package com.red_projects.mediateca.services.user;

import com.red_projects.mediateca.entities.utils.AccessLevel;
import com.red_projects.mediateca.repositories.UserRepository;
import com.red_projects.mediateca.utils.ActionStatus;
import com.red_projects.mediateca.utils.SecurityUtil;
import com.red_projects.mediateca.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerUserService {

    private final UserRepository userRepository;

    @Autowired
    public OwnerUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public ActionStatus promoteUser(String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setAccessLevel(AccessLevel.ADMIN.name());
            userRepository.save(user);
            return ActionStatus.SUCCEEDED;
        }
        return ActionStatus.FAILED;
    }


    public ActionStatus demoteUser(String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (user.getAccessLevel().contentEquals(AccessLevel.ADMIN.name())) {
                user.setAccessLevel(AccessLevel.USER.name());
                userRepository.save(user);
                return ActionStatus.SUCCEEDED;
            }
        }
        return ActionStatus.FAILED;
    }

    public ActionStatus transferOwnership(String requestUsername, String userId) {
        Optional<User> opRequestUser = userRepository.findByUsername(requestUsername);
        Optional<User> opUser = userRepository.findById(userId);
        if (opRequestUser.isPresent() && opUser.isPresent()) {
            User requestUser = opRequestUser.get();
            User user = opUser.get();
            if (user.getAccessLevel().contentEquals(AccessLevel.ADMIN.name())) {
                String verificationCode = SecurityUtil.generateVerificationCode();

                // send confirmation email to current owner
                requestUser.setVerificationCode(SecurityUtil.bCryptEncode(verificationCode));
                String requesterEmail = requestUser.getEmailAddress();

                // send notification email to potential new owner
                String userEmail = user.getEmailAddress();

                userRepository.save(user);
                return ActionStatus.SUCCEEDED;
            }
        }
        return ActionStatus.FAILED;
    }

}
