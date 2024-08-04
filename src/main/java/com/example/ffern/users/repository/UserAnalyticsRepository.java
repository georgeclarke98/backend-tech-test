package com.example.ffern.users.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAnalyticsRepository extends CrudRepository<UserAnalyticsEntity, Long> {}
