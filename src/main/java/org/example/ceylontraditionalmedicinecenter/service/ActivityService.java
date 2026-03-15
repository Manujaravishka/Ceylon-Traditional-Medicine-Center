package org.example.ceylontraditionalmedicinecenter.service;

import org.example.ceylontraditionalmedicinecenter.dto.ActivityDTO;

import java.util.List;

public interface ActivityService {
    int saveActivity(ActivityDTO activityDTO);

    int updateActivity(Long id, ActivityDTO activityDTO);

    int deleteActivity(Long id);

    List<ActivityDTO> getAllActivity();
}
