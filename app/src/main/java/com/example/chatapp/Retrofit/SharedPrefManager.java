package com.example.chatapp.Retrofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.chatapp.Activities.LoginActivity;
import com.example.chatapp.Dtos.GroupDto;
import com.example.chatapp.Dtos.UserProfileDto;
import com.example.chatapp.Model.Conservation.Conservation;
import com.example.chatapp.Model.User.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_FIRSTNAME = "keyfirstname";
    private static final String KEY_LASTNAME = "keylastname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_ID = "keyid";
    private static final String KEY_PASSWORD = "keypassword";
    private static final String KEY_BIO = "keybio";
    private static final String CURRENT_USER = "currentuser";
    private static final String CURRENT_CONSERVATION = "CURRENT_CONSERVATION";
    private static final String CURRENT_GROUP = "CURRENT_GROUP";
    private static final String LIST_CONSERVATION = "LIST_CONSERVATION";
    private static final String LIST_GROUP = "LIST_GROUP";
    private static final String CHANGE = "CHANGE";

    private static SharedPrefManager mInstance;
    private static Context ctx;
    private Gson gson;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ctx = context;
        gson = new Gson();
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getId());
        editor.putString(KEY_FIRSTNAME, user.getFirstName());
        editor.putString(KEY_LASTNAME, user.getLastName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_BIO, user.getBio());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FIRSTNAME, null) != null;
    }

    public UserProfileDto getUser() {
        String userJson = sharedPreferences.getString(CURRENT_USER, null);
        if (userJson != null) {
            Type type = new TypeToken<UserProfileDto>() {
            }.getType();
            return gson.fromJson(userJson, type);
        }
        return null;
    }


    public Conservation getCurrentConservation() {
        String conservation = sharedPreferences.getString(CURRENT_CONSERVATION, null);
        if (conservation != null) {
            Type type = new TypeToken<Conservation>() {
            }.getType();
            return gson.fromJson(conservation, type);
        }
        return null;
    }

    public void saveCurrentConservation(Conservation conservation) {
        String userJson = gson.toJson(conservation);
        editor.putString(CURRENT_CONSERVATION, userJson);
        editor.apply();
    }


    public List<Conservation> getListConservation() {
        String conservation = sharedPreferences.getString(LIST_CONSERVATION, null);
        if (conservation != null) {
            Type type = new TypeToken<List<Conservation>>() {
            }.getType();
            return gson.fromJson(conservation, type);
        }
        return null;
    }

    public void saveListConservation(List<Conservation> conservation) {
        String userJson = gson.toJson(conservation);
        editor.putString(LIST_CONSERVATION, userJson);
        editor.apply();
    }


    public List<String> getListGroupId() {
        String groups = sharedPreferences.getString(LIST_GROUP, null);
        if (groups != null) {
            Type type = new TypeToken<List<String>>() {
            }.getType();
            return gson.fromJson(groups, type);
        }
        return null;
    }

    public void saveListGroupId(List<String> groups) {
        String groupsJson = gson.toJson(groups);
        editor.putString(LIST_GROUP, groupsJson);
        editor.apply();
    }

    public void setChange(Boolean value) {
        editor.putBoolean(CHANGE, value);
        editor.apply();
    }

    public Boolean getChange() {
        return sharedPreferences.getBoolean(CHANGE,false);
    }

    public void saveCurrentGroup(GroupDto groupDto) {
        String userJson = gson.toJson(groupDto);
        editor.putString(CURRENT_GROUP, userJson);
        editor.apply();
    }

    public void clear() {
        editor.putString(CURRENT_GROUP, null);
        editor.putString(LIST_CONSERVATION, null);
        editor.putString(LIST_GROUP, null);
        editor.apply();
    }

    public GroupDto getCurrentGroup() {
        String groupDto = sharedPreferences.getString(CURRENT_GROUP, null);
        if (groupDto != null) {
            Type type = new TypeToken<GroupDto>() {
            }.getType();
            return gson.fromJson(groupDto, type);
        }
        return null;
    }

    public void clearUser() {
        editor.remove(CURRENT_USER).apply();
    }

    public void saveUser(UserProfileDto user) {
        String userJson = gson.toJson(user);
        editor.putString(CURRENT_USER, userJson);
        editor.apply();
    }

    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(ctx, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
        WebSocketManager.getInstance(ctx).disconnect();
//        ctx.startActivities(new Intent[]{new Intent(ctx, LoginActivity.class)});
    }


}
