package service;

import domain.Tourist;

import java.util.List;

public interface TouristService {
    Tourist createTourist(int id, String name);
    Tourist findById(int id);
    List<Tourist> findAll();
    Tourist updateTourist(int id, String newName);
    Tourist deleteTourist(int id);
}
