package com.javamentor.springcrudsecuritybootfrom1.transferObject;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class NewUserRequest {
    @Size(min = 2, max = 30, message ="Minimum 2 and maximum 30 characters")
    private String username;

    @Size(min = 2, max = 30, message = "Minimum 2 and maximum 30 characters")
    private String firstName;

    @Size(min = 2, max = 30, message = "Minimum 2 and maximum 30 characters")
    private String lastName;

    @Min(value = 0, message = "The age cannot be less than 0.")
    private int age;

    private String roles;

    //@Size(min = 5, max = 100, message ="Minimum 5 and maximum 15 characters")
    private String password;

}
