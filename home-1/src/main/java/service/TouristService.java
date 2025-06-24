package service;

import domain.Tourist;

import java.io.IOException;
import java.util.List;

public interface TouristService {
    Tourist createTourist(int id, String name) throws IOException;
    Tourist findById(int id) throws IOException;
    List<Tourist> findAll() throws IOException;
    Tourist updateTourist(int id, String newName) throws IOException;
    Tourist deleteTourist(int id) throws IOException;
}
