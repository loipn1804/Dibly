package com.dibs.dibly.daocontroller;

import android.content.Context;

import com.dibs.dibly.application.MyApplication;

import java.util.List;

import greendao.Comment;
import greendao.CommentDao;

/**
 * Created by USER on 7/10/2015.
 */
public class CommentController {

    private static CommentDao getCommentDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCommentDao();
    }

    public static void insert(Context context, Comment comment) {
        getCommentDao(context).deleteByKey(comment.getComment_id());
        getCommentDao(context).insert(comment);
    }

    public static List<Comment> getAll(Context context) {
        return getCommentDao(context).queryRaw(" order by comment_id desc");
    }

    public static void clearById(Context context, Long id) {
        getCommentDao(context).deleteByKey(id);
    }

    public static void clearAll(Context context) {
        getCommentDao(context).deleteAll();
    }
}
