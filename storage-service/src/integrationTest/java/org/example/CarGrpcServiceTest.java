package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.CarGrpcServiceGrpc;
import org.example.grpc.GetAllCarsRequest;
import org.example.grpc.GetAllCarsResponse;
import org.example.grpc.GetCarByIdRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarGrpcServiceTest extends BaseIntegrationTest {

    private ManagedChannel channel;
    private CarGrpcServiceGrpc.CarGrpcServiceBlockingStub stub;

    @BeforeEach
    void setUp() {
        channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        stub = CarGrpcServiceGrpc.newBlockingStub(channel);
    }

    @AfterEach
    void tearDown() {
        channel.shutdown();
    }

    @Test
    void getAllCars_shouldReturnList() {
        GetAllCarsResponse response = stub.getAllCars(
                GetAllCarsRequest.newBuilder().build()
        );
        assertNotNull(response);
        assertNotNull(response.getCarsList());
    }

    @Test
    void getCarById_shouldReturnNotFound_whenInvalidId() {
        assertThrows(Exception.class, () -> {
            stub.getCarById(GetCarByIdRequest.newBuilder()
                    .setId("00000000-0000-0000-0000-000000000000")
                    .build());
        });
    }
}