package com.cadence.app.cadence.repository;

import com.cadence.app.cadence.domain.ActivityContextEntity;
import org.springframework.data.repository.CrudRepository;

public interface ActivityContextRepository extends CrudRepository<ActivityContextEntity, Long> {

  ActivityContextEntity findByActivityId(String activityId);

}
