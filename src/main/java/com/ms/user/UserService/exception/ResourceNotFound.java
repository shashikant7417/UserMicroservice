package com.ms.user.UserService.exception;

public class ResourceNotFound extends RuntimeException {

    public ResourceNotFound(){
        super("Resource Not Found on the server!!");
    }

    public ResourceNotFound(String message){
        super(message);
    }

}
