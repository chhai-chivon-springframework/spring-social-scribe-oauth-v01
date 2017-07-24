package com.phengtola.forms;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by tolapheng on 7/24/17.
 */
public class FrmWebLogin {

    @NotBlank(message = "Email is required!")
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
