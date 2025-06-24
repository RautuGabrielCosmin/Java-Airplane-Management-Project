package service;

import repository.TouristRepository;
import domain.Tourist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MemoryTouristService implements TouristService {
    private static Logger logger = LoggerFactory.getLogger(MemoryTouristService.class);

    private final TouristRepository touristRepository;
    public MemoryTouristService(TouristRepository touristRepository) {
        this.touristRepository = touristRepository;
    }

    public Tourist createTourist(int id, String name) {
        logger.info("Create tourist");

        if(name == null || name.trim().isEmpty()){
            logger.warn("Cannot create tourist with the name of the tourist being empty");
            throw new IllegalArgumentException("Cannot create tourist with the name of the tourist being empty");
        }
        Tourist existing = touristRepository.findById(id);
        if(existing != null){
            logger.warn ("Tourist with id {} already exists", id);
            throw new IllegalArgumentException("Tourist with id " + id + " already exists");
        }
        Tourist newTourist = new Tourist(id, name);
        Tourist savedTourist = touristRepository.save(newTourist);
        logger.info("Tourist with id {} has been created", id);
        return savedTourist;
    }

    public Tourist findById(int id) {
        logger.info("Finding tourist with id {}", id);
        Tourist tourist = touristRepository.findById(id);
        if(tourist == null){
            logger.warn("Tourist with id {} does not exist", id);
        }else{
            logger.info("Tourist with id {} has been found", id);
        }
        return tourist;
    }

    public List<Tourist> findAll() {
        logger.info("Finding all tourists");
        return touristRepository.findAll();
    }

    public Tourist updateTourist(int id, String newTourist) {
        logger.info("Updating tourist with id {}", id);
        Tourist tourist = touristRepository.findById(id);
        if(tourist == null){
            logger.warn("Tourist with id {} does not exist", id);
            return null;
        }
        tourist.setName(newTourist);
        Tourist savedTourist = touristRepository.save(tourist);
        logger.info("Tourist with id {} has been updated", id);
        return savedTourist;
    }

    public Tourist deleteTourist(int id) {
        logger.info("Deleting tourist with id {}", id);
        Tourist deleted = touristRepository.findById(id);
        if(deleted == null){
            logger.warn("Tourist with id {} does not exist", id);
            return null;
        }else{
            logger.info("Tourist with id {} has been deleted", id);
            touristRepository.delete(id);
        }
        return deleted;
    }
}
