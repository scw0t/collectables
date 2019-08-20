package com.scwot.collectables.controller;

import com.scwot.collectables.filesystem.ReleaseMetadata;
import org.springframework.stereotype.Controller;

@Controller
public class DataBaseController {


    public void save(final ReleaseMetadata rm) {

        System.out.println(rm);
        System.out.println();


    }
}
