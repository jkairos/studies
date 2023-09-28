package com.cox.oss.pet.repository;

import org.openapi.example.model.Pet;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class PetsRepository {
    private Map<UUID, Pet> data = new HashMap<>();

    public Pet createPet(Pet pet){
        UUID id = UUID.randomUUID();
        pet.setId(id);
        data.put(id, pet);
        return pet;
    }

    public List<Pet> findAll(){
        return data.values().stream().collect(Collectors.toList());
    }

    public Pet findById(UUID id){
        return data.get(id);
    }

    public Pet updateById(Pet pet){
        data.put(pet.getId(), pet);
        return pet;
    }
}
