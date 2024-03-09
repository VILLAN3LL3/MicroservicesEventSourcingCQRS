package com.villan3ll3.estore.ProductsService.core.data;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="productlookup")
public class ProductLookupEntity implements Serializable {

    @Id
    private String productId;
    @Column(unique = true)
    private String title;
}
