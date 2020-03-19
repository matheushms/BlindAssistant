package com.blindassistant.googleglass.Exceptions;

import java.io.IOException;

public class SendException extends IOException {

    public String toString(){
        return "Error when send the image";
    }

}