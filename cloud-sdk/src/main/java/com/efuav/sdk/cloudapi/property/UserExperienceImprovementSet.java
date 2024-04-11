package com.efuav.sdk.cloudapi.property;

import com.efuav.sdk.cloudapi.device.UserExperienceImprovementEnum;
import com.efuav.sdk.common.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author sean
 * @version 1.7
 * @date 2023/10/13
 */
public class UserExperienceImprovementSet extends BaseModel {

    @NotNull
    @JsonProperty("user_experience_improvement")
    private UserExperienceImprovementEnum userExperienceImprovement;

    public UserExperienceImprovementSet() {
    }

    @Override
    public String toString() {
        return "UserExperienceImprovementSet{" +
                "userExperienceImprovement=" + userExperienceImprovement +
                '}';
    }

    public UserExperienceImprovementEnum getUserExperienceImprovement() {
        return userExperienceImprovement;
    }

    public UserExperienceImprovementSet setUserExperienceImprovement(UserExperienceImprovementEnum userExperienceImprovement) {
        this.userExperienceImprovement = userExperienceImprovement;
        return this;
    }
}
