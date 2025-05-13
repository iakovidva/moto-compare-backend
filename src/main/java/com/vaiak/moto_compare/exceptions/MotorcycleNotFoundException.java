package com.vaiak.moto_compare.exceptions;

public class MotorcycleNotFoundException extends RuntimeException {

    public MotorcycleNotFoundException() {
    }

    public MotorcycleNotFoundException(Long motorcycleId) {
        super("Motorcycle with id: " + motorcycleId + " not found");
    }

    public MotorcycleNotFoundException(String manufacturer) {
        super("Motorcycles with manufacturer: " + manufacturer + " not found");
    }
}
