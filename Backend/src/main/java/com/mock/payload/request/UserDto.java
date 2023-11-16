package com.mock.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private int id;
    @NotBlank
    @Size(min = 4, max = 24, message = "Username must be of 6-24 characters!")
    @Pattern(regexp = "\\S+", message = "Username cannot contain whitespace!")
    private String username;
    @NotEmpty
    @Size(min = 2, max = 10, message = "FirstName must be of 6-24 characters!")
    private String firstName;
    @NotEmpty
    @Size(min = 2, max = 10, message = "LastName must be of 6-24 characters!")
    private String lastName;
    @NotBlank
    @Email(message = "Email address is not valid")
    @Pattern(regexp = "\\S+", message = "Username cannot contain whitespace!")
    private String email;

    private String password;
    @NotEmpty
    @Size(min=9, max = 11, message = "PhoneNumber must be of 9-11 characters!" )
    private String phone;
    private String avatar="default.png";
    private boolean status=true;

    private Set<RoleDto> roles = new HashSet<>();

    @JsonIgnore
    public String getPassword(){
        return this.password;
    }
    @JsonProperty
    public void setPassword(String password){
        this.password=password;
    }

}
