package com.markp.FileToQueue.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FileActions {

    private String productName;

    private String productCategory;

    private String productDescription;

    private BigDecimal productPrice;

    private int productStock;

    private String productImageFilename;
}
