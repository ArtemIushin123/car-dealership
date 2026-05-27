package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomAssemblyOrderResponseDto extends AssemblyOrderResponseDto {
    private CarModelResponseDto carModel;
}
