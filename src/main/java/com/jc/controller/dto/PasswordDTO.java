package com.jc.controller.dto;

import lombok.Data;

@Data
public class PasswordDTO {
    private String username;
    private String password;
    private String newPassword;
}
