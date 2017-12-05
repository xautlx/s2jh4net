package com.entdiy.dev.guide.web;

import com.entdiy.security.AuthUserDetails;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/dev")
public class DevelopController {

    private final static Logger logger = LoggerFactory.getLogger(DevelopController.class);

    @RequiresRoles(AuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard() {
        return "dev/dashboard";
    }
}

