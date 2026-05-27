package org.example.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.domain.exceptions.EntityNotFoundException;
import org.example.grpc.*;
import org.example.infrastructure.persistence.entities.cars.CarEntity;
import org.example.repository.jpa.CarJpaRepository;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class CarGrpcService extends CarGrpcServiceGrpc.CarGrpcServiceImplBase {
    private final CarJpaRepository carJpaRepository;
    private final ModelMapper modelMapper;

    @Override
    public void getAllCars(GetAllCarsRequest request,
                           StreamObserver<GetAllCarsResponse> responseObserver) {
        log.info("gRPC getAllCars called");
        List<CarResponse> cars = carJpaRepository.findAll()
                .stream()
                .map(this::toGrpcResponse)
                .toList();

        GetAllCarsResponse response = GetAllCarsResponse.newBuilder()
                .addAllCars(cars)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCarById(GetCarByIdRequest request,
                           StreamObserver<CarResponse> responseObserver) {
        log.info("gRPC getCarById called: {}", request.getId());
        try {
            CarEntity car = carJpaRepository.findById(UUID.fromString(request.getId()))
                    .orElseThrow(() -> new EntityNotFoundException("Car not found"));
            responseObserver.onNext(toGrpcResponse(car));
            responseObserver.onCompleted();
        } catch (EntityNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }
    }

    private CarResponse toGrpcResponse(CarEntity entity) {
        return CarResponse.newBuilder()
                .setId(entity.getId().toString())
                .setBrand(entity.getCarModel().getBrand())
                .setModel(entity.getCarModel().getModel())
                .setColor(entity.getColor())
                .setPrice(entity.getCarModel().getPrice())
                .setBodyType(entity.getCarModel().getBodyType().name())
                .setFuelType(entity.getCarModel().getFuelType().name())
                .setEnginePower(entity.getCarModel().getEnginePower())
                .setEngineCapacity(entity.getCarModel().getEngineCapacity())
                .setDriveType(entity.getCarModel().getDriveType().name())
                .build();
    }
}
