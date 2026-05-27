package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockAssemblyOrderResponseDto extends AssemblyOrderResponseDto {
    private CarResponseDto car;
}
