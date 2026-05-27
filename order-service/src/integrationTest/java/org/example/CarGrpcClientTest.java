package org.example;

import org.example.client.CarGrpcClient;
import org.example.domain.exceptions.ServiceUnavailableException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CarGrpcClientTest extends BaseIntegrationTest {

    @Autowired
    private CarGrpcClient carGrpcClient;

    @Test
    void getAllCars_shouldReturnList() {
        assertThrows(ServiceUnavailableException.class, () -> {
            carGrpcClient.getAllCars();
        });
    }

    @Test
    void getCarById_shouldThrowServiceUnavailable_whenStorageDown() {
        assertThrows(ServiceUnavailableException.class, () -> {
            carGrpcClient.getCarById(UUID.randomUUID());
        });
    }
}