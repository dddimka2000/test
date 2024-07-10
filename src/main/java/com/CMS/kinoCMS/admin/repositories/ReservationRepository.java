package com.CMS.kinoCMS.admin.repositories;

import com.CMS.kinoCMS.admin.models.Reservation;
import com.CMS.kinoCMS.admin.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findBySchedule(Schedule schedule);
}
