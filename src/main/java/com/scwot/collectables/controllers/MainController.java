package com.scwot.collectables.controllers;

import javafx.fxml.FXML;
import org.springframework.stereotype.Component;

@Component
public class MainController {

    @FXML
    void handleTestButtonAction() {
        System.out.println("test");
    }

}
