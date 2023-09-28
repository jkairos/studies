package com.cox.oss.pet.services;

import com.cox.oss.pet.repository.PetsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapi.example.model.Pet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class PetsService {
    private final PetsRepository repository;

    public Pet createPet(Pet pet){
        log.info("createPet[{}]", pet);
        return repository.createPet(pet);
    }

    public List<Pet> findAll(){
        log.info("findAll");
        return repository.findAll();
    }

    public Pet findById(UUID id){
        log.info("findById[{}]", id);
        return repository.findById(id);
    }

    public Pet updateById(Pet pet){
        return repository.updateById(pet);
    }
}
