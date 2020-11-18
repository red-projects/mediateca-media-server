package com.red_projects.mediateca.services.user;

import com.red_projects.mediateca.entities.User;
import com.red_projects.mediateca.entities.utils.AccessLevel;
import com.red_projects.mediateca.entities.utils.UserData;
import com.red_projects.mediateca.entities.utils.UserDataList;
import com.red_projects.mediateca.entities.utils.UserStatus;
import com.red_projects.mediateca.repositories.UserRepository;
import com.red_projects.mediateca.utils.ActionStatus;
import com.red_projects.mediateca.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminUserService {

    private final UserRepository userRepository;

    @Autowired
    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserData getUserInfo(String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            return new UserData(user);
        }
        return null;
    }

    public UserDataList getUserList() {
        List<User> userList = userRepository.findAll();
        UserDataList userDataList = new UserDataList();
        if (!userList.isEmpty()) {
            for (User user : userList) {
                userDataList.addUserData(new UserData((user)));
            }
            return userDataList;
        }
        return null;
    }


    public ActionStatus resetUserPassword(String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setPassword(SecurityUtil.bCryptEncode(SecurityUtil.generatePasscode(32, true)));
            userRepository.save(user);
            return ActionStatus.SUCCEEDED;
        }
        return ActionStatus.FAILED;
    }


    public ActionStatus reactivateUser(String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (user.getStatus().contentEquals(UserStatus.DEACTIVATED.name())) {
                user.setStatus(UserStatus.ACTIVE.name());
                userRepository.save(user);
                return ActionStatus.SUCCEEDED;
            }
        }
        return ActionStatus.FAILED;
    }


    public ActionStatus deactivateUser(String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (!user.getAccessLevel().contentEquals(AccessLevel.ADMIN.name()) && !user.getAccessLevel().contentEquals(AccessLevel.OWNER.name())) {
                if (user.getStatus().contentEquals(UserStatus.ACTIVE.name())) {
                    user.setStatus(UserStatus.DEACTIVATED.name());
                    userRepository.save(user);
                    return ActionStatus.SUCCEEDED;
                }
            }
        }
        return ActionStatus.FAILED;
    }


    public ActionStatus deleteUser(String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (!user.getAccessLevel().contentEquals(AccessLevel.ADMIN.name()) && !user.getAccessLevel().contentEquals(AccessLevel.OWNER.name())) {
                userRepository.delete(user);
                return ActionStatus.SUCCEEDED;
            }
        }
        return ActionStatus.FAILED;
    }
}
