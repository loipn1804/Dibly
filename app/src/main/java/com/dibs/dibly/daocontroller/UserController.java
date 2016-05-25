package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.ObjectUser;
import greendao.ObjectUserDao;

/**
 * Created by USER on 7/6/2015.
 */
public class UserController {

    private static ObjectUserDao getUserDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getObjectUserDao();
    }

    public static void insertOrUpdate(Context context, ObjectUser objectUser) {
        List<ObjectUser> users = UserController.getUserById(context, objectUser.getUser_id());
        if (users.size() == 0) {
            getUserDao(context).insert(objectUser);
        } else {
            ObjectUser user = users.get(0);
            user.setUser_id(objectUser.getUser_id());
            user.setGroup_id(objectUser.getGroup_id());
            user.setEmail(objectUser.getEmail());
            user.setIsConsumer(objectUser.getIsConsumer());
            user.setConsumer_id(objectUser.getConsumer_id());
            user.setFirst_name(objectUser.getFirst_name());
            user.setLast_name(objectUser.getLast_name());
            user.setPhone(objectUser.getPhone());
            user.setGender(objectUser.getGender());
            user.setProfile_image(objectUser.getProfile_image());
            user.setFacebook_id(objectUser.getFacebook_id());

            getUserDao(context).update(user);
        }
    }

    public static void update(Context context, ObjectUser objectUser) {
        getUserDao(context).update(objectUser);
    }

    public static List<ObjectUser> getAllUsers(Context context) {
        return getUserDao(context).loadAll();
    }

    public static boolean isLogin(Context context) {
        List<ObjectUser> users = getUserDao(context).loadAll();
        if (users.size() == 0) {
            return false;
        } else {
            if (users.get(0).getFacebook_id() != null && users.get(0).getFacebook_id().trim().length() > 0 && !users.get(0).getFacebook_id().equals("null")) {
                return true;
            }

            if (users.get(0).getF_verified_phone() == 0) {
                return false;
            }
            return true;
        }
    }

    public static boolean isExist(Context context) {
        List<ObjectUser> users = getUserDao(context).loadAll();
        if (users.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isLoginByFb(Context context) {
        List<ObjectUser> users = getUserDao(context).loadAll();
        if (users.size() == 0) {
            return false;
        } else {
            if (users.get(0).getFacebook_id().length() == 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static List<ObjectUser> getUserById(Context context, Long user_id) {
        return getUserDao(context).queryRaw(" WHERE USER_ID = ?", user_id + "");
    }

    public static ObjectUser getCurrentUser(Context context) {
        List<ObjectUser> list = getUserDao(context).loadAll();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static void clearAllUsers(Context context) {
        getUserDao(context).deleteAll();
    }
}
