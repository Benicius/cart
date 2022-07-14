package com.coffee.shop.starbux.cart.domains;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRest;
    private Long id;
    private String name;
    private Double price;
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    private ItemCart itemCart;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdRest() {
        return idRest;
    }

    public void setIdRest(Long idRest) {
        this.idRest = idRest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
