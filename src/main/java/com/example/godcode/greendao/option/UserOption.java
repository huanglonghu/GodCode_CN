package com.example.godcode.greendao.option;

import com.example.godcode.greendao.entity.User;
import com.example.godcode.greendao.gen.UserDao;
import com.example.godcode.ui.base.GodCodeApplication;
import com.example.godcode.utils.LogUtil;

/**
 * Created by Administrator on 2018/7/30.
 */

public class UserOption {

    private UserOption() {
    }

    private static UserOption defaultInstance;

    public static UserOption getInstance() {

        UserOption userOption = defaultInstance;
        if (defaultInstance == null) {
            synchronized (UserOption.class) {
                if (defaultInstance == null) {
                    userOption = new UserOption();
                    defaultInstance = userOption;
                }
            }
        }
        return userOption;
    }

    public void addUser(User user) {
            UserDao userDao = GodCodeApplication.getInstance().getDaoSession().getUserDao();
            userDao.insert(user);
    }

    public User querryUser(int userId) {
        UserDao userDao = GodCodeApplication.getInstance().getDaoSession().getUserDao();
        User user = userDao.queryBuilder()
                .where(UserDao.Properties.UserId.eq(userId))
                .unique();
        return user;
    }

    public void updateUser(User user){
        UserDao userDao = GodCodeApplication.getInstance().getDaoSession().getUserDao();
        userDao.update(user);
    }

}
