package com.efy.exception;

import com.efy.enums.FileErrorEnum;

/**
 * @Project xmbb
 * @Date 2020/9/13 19:23
 * @Created by Efy
 * @Description TODO
 */
public class FileInvalidException extends Exception{
    public String errorCode;
    public String errorMsg;

    public FileInvalidException(FileErrorEnum err) {
        new FileInvalidException(err.code,err.desc);
    }

    public FileInvalidException(FileErrorEnum err,Throwable cause) {
        new FileInvalidException(err.code,err.desc,cause);
    }

    public FileInvalidException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public FileInvalidException(String errorCode,String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "FileInvalidException{" +
                "errorCode='" + errorCode + '\'' +
                "errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
