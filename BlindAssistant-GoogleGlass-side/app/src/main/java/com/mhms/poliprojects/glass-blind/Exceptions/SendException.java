package com.mhms.poliprojects.toblindpeopleimersion.Exceptions;

import java.io.IOException;

public class SendException extends IOException {

    public String toString(){
        return "Error when send the image";
    }

}