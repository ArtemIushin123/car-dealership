package org.example.dto.response;

import lombok.Data;

@Data
public class CarDto {
    private String id;
    private String brand;
    private String model;
    private String color;
    private int price;
    private String bodyType;
    private String fuelType;
    private int enginePower;
    private int engineCapacity;
    private String driveType;
}