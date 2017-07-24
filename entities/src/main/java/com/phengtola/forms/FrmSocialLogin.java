package com.phengtola.forms;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Created by tolapheng on 7/24/17.
 */
public class FrmSocialLogin {

    private String email;

    @JsonProperty("image_url")
    private String imageUrl;

    private String username;

    @NotBlank(message = "Social Id is required.")
    @JsonProperty("social_id")
    private String socialId;

    @NotBlank(message = "Social Type is required")
    @JsonProperty("social_type")
    @Size(max=1, message="socialType must be one character")
    private String socialType;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getSocialType() {
        return socialType;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
