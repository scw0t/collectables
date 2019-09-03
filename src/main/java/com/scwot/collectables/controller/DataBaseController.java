package com.scwot.collectables.controller;

import com.scwot.collectables.file.metadata.ReleaseScopeMetadata;
import org.springframework.stereotype.Controller;

@Controller
public class DataBaseController {


    public void save(final ReleaseScopeMetadata rm) {

        System.out.println(rm);
        System.out.println();


    }
}
