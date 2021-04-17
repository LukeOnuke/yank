package com.lukeonuke.yank;

import com.lukeonuke.yank.exception.ForbiddenException;

import java.util.Objects;

public class YankUtil {
    public static void validateNotNull(Object obj) throws ForbiddenException {
        if(Objects.isNull(obj)){
            throw new ForbiddenException();
        }
    }
}
