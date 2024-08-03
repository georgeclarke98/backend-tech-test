package com.example.ffern.waitlist;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitlistRepository extends CrudRepository<WaitlistEntity, Long> {}
