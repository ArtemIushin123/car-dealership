package org.example.client;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.grpc.*;
import org.springframework.stereotype.Component;

import org.example.domain.exceptions.ServiceUnavailableException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CarGrpcClient {
    @GrpcClient("storage-service")
    private CarGrpcServiceGrpc.CarGrpcServiceBlockingStub carStub;

    public List<CarResponse> getAllCars() {
        try {
            log.info("gRPC call: getAllCars");
            GetAllCarsResponse response = carStub
                    .withDeadlineAfter(5, TimeUnit.SECONDS)
                    .getAllCars(GetAllCarsRequest.newBuilder().build());
            return response.getCarsList();
        } catch (StatusRuntimeException e) {
            log.error("gRPC getAllCars failed: {}", e.getStatus());
            throw new ServiceUnavailableException("Storage service unavailable");
        }
    }

    public CarResponse getCarById(UUID id) {
        try {
            log.info("gRPC call: getCarById {}", id);
            return carStub
                    .withDeadlineAfter(5, TimeUnit.SECONDS)
                    .getCarById(GetCarByIdRequest.newBuilder()
                            .setId(id.toString())
                            .build());
        } catch (StatusRuntimeException e) {
            log.error("gRPC getCarById failed: {}", e.getStatus());
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new EntityNotFoundException("Car not found");
            }
            throw new ServiceUnavailableException("Storage service unavailable");
        }
    }
}
