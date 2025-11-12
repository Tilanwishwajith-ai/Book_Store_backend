/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csacoursework.Models;

/**
 *
 * @author Katana
 */


import java.util.UUID;

public class Author {
    private UUID id;
    private String name;
    private String biography;

    public Author() {
        this.id = UUID.randomUUID();
    }

    public Author(String name, String biography) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.biography = biography;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}