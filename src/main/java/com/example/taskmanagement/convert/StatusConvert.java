package com.example.taskmanagement.convert;

import com.example.taskmanagement.api.Status;
import org.springframework.stereotype.Component;

@Component
public class StatusConvert {

    public Status convertStatus(String status){

        switch(status){
            case "Ready" -> {
                return Status.Ready;
            }
            case "Progress" -> {
                return Status.Progress;
            }
            case "Done" -> {
                return Status.Done;
            }
        }

        return null;
    }
}
