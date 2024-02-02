package com.markp.FileToQueue.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductMessage {

    private String productName;

    private String productCategory;

    private String productDescription;

    private BigDecimal productPrice;

    private int productStock;

    private String productImageFilename;

    private String productImageFilepath;
}
