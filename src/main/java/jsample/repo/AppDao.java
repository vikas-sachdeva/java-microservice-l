package jsample.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import jsample.model.Application;

@Repository
public interface AppDao extends ReactiveMongoRepository<Application, String> {

}