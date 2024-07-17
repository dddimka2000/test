package com.CMS.kinoCMS.user.Controllers;

import com.CMS.kinoCMS.admin.models.Cinema;
import com.CMS.kinoCMS.admin.models.Hall;
import com.CMS.kinoCMS.admin.models.Schedule;
import com.CMS.kinoCMS.admin.services.CinemaService;
import com.CMS.kinoCMS.admin.services.FilmService;
import com.CMS.kinoCMS.admin.services.HallService;
import com.CMS.kinoCMS.admin.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/cinemas")
public class CinemaUserController {
    private final CinemaService cinemaService;
    private final ScheduleService scheduleService;
    private final HallService hallService;
    private final FilmService filmService;

    @Autowired
    public CinemaUserController(CinemaService cinemaService, ScheduleService scheduleService, HallService hallService, FilmService filmService) {
        this.cinemaService = cinemaService;
        this.scheduleService = scheduleService;
        this.hallService = hallService;
        this.filmService = filmService;
    }

    @GetMapping()
    public String cinemas(Model model) {
        List<Cinema> cinemas = cinemaService.findAll();
        model.addAttribute("cinemas", cinemas);
        return "users-part/cinemas/cinemas";
    }

    @GetMapping("/{id}")
    public String cinema(Model model, @PathVariable long id) {
        Optional<Cinema> cinema = cinemaService.findById(id);
        cinema.ifPresent(value -> model.addAttribute("cinema", value));

        List<Schedule> filteredSchedules = scheduleService.getFilteredSchedules(id, null, null, null);
        Map<LocalDate, List<Schedule>> schedulesByDate = scheduleService.groupSchedulesByDate(filteredSchedules);

        List<Map.Entry<LocalDate, List<Schedule>>> sortedSchedulesByDate = schedulesByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .toList();

        List<Hall> halls = hallService.findAll().stream()
                .sorted(Comparator.comparing(Hall::getNumber))
                .toList();

        model.addAttribute("cinema", cinema.orElseThrow());
        model.addAttribute("schedulesByDate", sortedSchedulesByDate);
        model.addAttribute("films", filmService.findAll());
        model.addAttribute("halls", halls);
        model.addAttribute("images", cinema.orElseThrow().getImages());

        return "users-part/cinemas/cinema-info";
    }

    @GetMapping("/hall/{hallId}")
    public String hallSchedule(@PathVariable long hallId, Model model) {
        Optional<Hall> hall = hallService.findById(hallId);
        hall.ifPresent(value -> model.addAttribute("hall", value));

        List<Schedule> filteredSchedules = scheduleService.getFilteredSchedules(null, null, hallId, null);
        Map<LocalDate, List<Schedule>> schedulesByDate = scheduleService.groupSchedulesByDate(filteredSchedules);

        List<Map.Entry<LocalDate, List<Schedule>>> sortedSchedulesByDate = schedulesByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .toList();

        model.addAttribute("schedulesByDate", sortedSchedulesByDate);
        model.addAttribute("films", filmService.findAll());
        model.addAttribute("images", hall.orElseThrow().getImages());

        return "users-part/cinemas/halls/hall-info";
    }
}