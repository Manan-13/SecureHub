package com.petproject.Securehub.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class CommonUtils{

    public static String getCurrentUser(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}