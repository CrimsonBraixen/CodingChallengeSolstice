package com.crimsonbraixen.codingchallengesolstice.POJO;

import lombok.Data;

@Data
public class Address {

    public String street;
    public String city;
    public String state;
    public String country;
    public String zipCode;

    @Override
    public String toString() {
        return street + ", \n" + city + ", " + state + " " + zipCode + ", " + country;
    }
}
