package com.red_projects.mediateca.entities.utils;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.util.ArrayList;

public class UserDataList {

    private ArrayList<UserData> userDataList;

    public UserDataList() {
        userDataList = new ArrayList<UserData>();
    }

    public void addUserData(UserData userData) {
        userDataList.add(userData);
    }

    public ArrayList<UserData> getUserDataList() {
        return userDataList;
    }

    public void setUserDataList(ArrayList<UserData> userDataList) {
        this.userDataList = userDataList;
    }

    public JsonArray toJsonArray(String detailLevel) {
        if (detailLevel.contentEquals("condensed")) {
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (UserData userData : userDataList) {
                jsonArrayBuilder.add(userData.toCondensedJsonObject());
            }
            return jsonArrayBuilder.build();
        }
        else if (detailLevel.contentEquals("full")) {
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (UserData userData : userDataList) {
                jsonArrayBuilder.add(userData.toJsonObject());
            }
            return jsonArrayBuilder.build();
        }
        else
            return null;
    }
}
