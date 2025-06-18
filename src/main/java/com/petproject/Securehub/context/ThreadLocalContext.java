//package com.petproject.Securehub.context;
//
//public class ThreadLocalContext {
//    private static final ThreadLocal<String> userHolder = new ThreadLocal<>();
//
//    public static void setCurrentUser(String user) {
//        userHolder.set(user);
//    }
//
//    public static String getCurrentUser() {
//        return userHolder.get();
//    }
//
//    public static void clear() {
//        userHolder.remove();
//    }
//}