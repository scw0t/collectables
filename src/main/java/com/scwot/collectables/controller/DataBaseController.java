package com.scwot.collectables.controller;

import com.scwot.collectables.adapter.ReleaseAdapter;
import com.scwot.collectables.file.metadata.ReleaseScopeMetadata;
import org.springframework.stereotype.Controller;

@Controller
public class DataBaseController {


    public void save(final ReleaseScopeMetadata rm) {

        final ReleaseAdapter releaseAdapter = new ReleaseAdapter();
        releaseAdapter.convert(rm);


        System.out.println(rm);
        System.out.println();


    }
}
