package com.cox.oss.pet.controller;

import com.cox.oss.pet.services.PetsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapi.example.api.PetsApi;
import org.openapi.example.model.Pet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Slf4j
public class PetsController implements PetsApi {

    private PetsService petsService;

    @Override
    public ResponseEntity<Void> createPets(Pet pet) {
        petsService.createPet(pet);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<Pet>> listPets(Integer limit) {
        return ResponseEntity.ok(petsService.findAll());
    }

    @Override
    public ResponseEntity<Pet> showPetById(String petId) {
        return ResponseEntity.ok(petsService.findById(UUID.fromString(petId)));
    }

    @Override
    public ResponseEntity<Pet> updatePetById(Pet pet) {
        return ResponseEntity.ok(petsService.updateById(pet));
    }
}
