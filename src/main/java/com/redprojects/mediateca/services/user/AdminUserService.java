package com.redprojects.mediateca.services.user;

import com.redprojects.mediateca.entities.User;
import com.redprojects.mediateca.entities.UserData;
import com.redprojects.mediateca.entities.UserDataList;
import com.redprojects.mediateca.repositories.UserRepository;
import com.redprojects.mediateca.utils.ActionStatus;
import com.redprojects.mediateca.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.json.JsonObject;
import java.util.ArrayList;
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
        } else {
            return null;
        }
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

    public ActionStatus promoteUser(String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setAdmin(true);
            userRepository.save(user);
            return ActionStatus.SUCCEEDED;
        } else {
            return ActionStatus.FAILED;
        }
    }

    public ActionStatus resetUserPassword(String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setPassword(SecurityUtil.bCryptEncode(SecurityUtil.generatePasscode(32, true)));
            userRepository.save(user);
            return ActionStatus.SUCCEEDED;
        } else {
            return ActionStatus.FAILED;
        }
    }

    public ActionStatus demoteUser(String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setAdmin(false);
            userRepository.save(user);
            return ActionStatus.SUCCEEDED;
        } else {
            return ActionStatus.FAILED;
        }
    }

    public ActionStatus deleteUser(String userId) {
        Optional<User> opUser = userRepository.findById(userId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            if (!user.isAdmin()) {
                userRepository.delete(user);
                return ActionStatus.SUCCEEDED;
            } else {
                return ActionStatus.FAILED;
            }
        } else {
            return ActionStatus.FAILED;
        }
    }
}
