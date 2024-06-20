package com.CMS.kinoCMS.controllers;

import com.CMS.kinoCMS.models.Film;
import com.CMS.kinoCMS.services.FileUploadService;
import com.CMS.kinoCMS.services.FilmService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/films")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class FilmsController {
    private static final Logger logger = LogManager.getLogger(FilmsController.class);

    private final FileUploadService fileUploadService;
    private final FilmService filmService;

    @Autowired
    public FilmsController(FileUploadService fileUploadService, FilmService filmService) {
        this.fileUploadService = fileUploadService;
        this.filmService = filmService;
    }

    @GetMapping
    public String films(Model model) {
        logger.info("Entering films method");
        List<Film> currentFilms = filmService.getCurrentFilms();
        List<Film> upcomingFilms = filmService.getUpcomingFilms();
        model.addAttribute("currentFilms", currentFilms);
        model.addAttribute("upcomingFilms", upcomingFilms);
        logger.info("Exiting films method with {} current films and {} upcoming films", currentFilms.size(), upcomingFilms.size());
        return "films/films";
    }

    @GetMapping("/add")
    public String addFilm(Model model) {
        logger.info("Entering addFilm method");
        model.addAttribute("film", new Film());
        model.addAttribute("allFilmTypes", List.of("2D", "3D", "IMax"));
        logger.info("Exiting addFilm method");
        return "films/film-add";
    }

    @PostMapping("/add")
    public String addFilmPost(@Valid Film film,
                              BindingResult bindingResult,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam("filmTypes") List<String> filmTypes,
                              RedirectAttributes redirectAttributes) {
        logger.info("Entering addFilmPost method with film: {}", film);
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors in addFilmPost: {}", bindingResult.getAllErrors());
            return "films/film-add";
        }
        try {
            if (file != null && !file.isEmpty()) {
                String resultFileName = fileUploadService.uploadFile(file);
                film.setMainImage(resultFileName);
                logger.info("Uploaded file with name: {}", resultFileName);
            }
            film.setTypes(String.join(",", filmTypes));
            filmService.save(film);
            logger.info("Film saved with id: {}", film.getId());
            redirectAttributes.addFlashAttribute("message", "Фильм успешно добавлен!");
        } catch (IOException e) {
            logger.error("Error uploading file: ", e);
            redirectAttributes.addFlashAttribute("message", "Ошибка загрузки файла: " + e.getMessage());
            return "redirect:/add-film";
        }
        logger.info("Exiting addFilmPost method");
        return "redirect:/admin/films";
    }

    @GetMapping("/edit/{id}")
    public String filmEdit(@PathVariable(value = "id") long id, Model model) {
        logger.info("Entering filmEdit method with id: {}", id);
        Optional<Film> optionalFilm = filmService.findById(id);
        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            model.addAttribute("film", film);
            model.addAttribute("allFilmTypes", List.of("2D", "3D", "IMAX"));
            model.addAttribute("selectedFilmTypes", film.getTypes() != null ? film.getTypes().split(",") : new String[]{});
            logger.info("Exiting filmEdit method with film: {}", film);
            return "films/film-edit";
        } else {
            logger.warn("Film with ID {} not found", id);
            model.addAttribute("message", "Фильм с ID " + id + " не найден.");
            return "redirect:/admin/films";
        }
    }

    @PostMapping("/edit/{id}")
    public String editFilm(@PathVariable("id") Long id,
                           @Valid Film film,
                           BindingResult bindingResult,
                           @RequestParam(value = "file", required = false) MultipartFile file,
                           @RequestParam(value = "filmTypes", required = false) List<String> filmTypes,
                           RedirectAttributes redirectAttributes) {
        logger.info("Entering editFilm method with id: {}", id);
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors in editFilm: {}", bindingResult.getAllErrors());
            return "films/film-edit";
        }

        try {
            Film existingFilm = filmService.findById(id)
                    .orElseThrow(() -> {
                        logger.error("Invalid film Id: {}", id);
                        return new IllegalArgumentException("Invalid film Id:" + id);
                    });

            if (file != null && !file.isEmpty()) {
                String resultFileName = fileUploadService.uploadFile(file);
                existingFilm.setMainImage(resultFileName);
                logger.info("Uploaded new file for film with id: {}", id);
            }

            existingFilm.setName(film.getName());
            existingFilm.setDescription(film.getDescription());
            existingFilm.setLink(film.getLink());
            if(film.getDate() != null) {
                existingFilm.setDate(film.getDate());
            }
            existingFilm.setUrlSEO(film.getUrlSEO());
            existingFilm.setTitleSEO(film.getTitleSEO());
            existingFilm.setKeywordsSEO(film.getKeywordsSEO());
            existingFilm.setDescriptionSEO(film.getDescriptionSEO());

            if (filmTypes != null) {
                String typesString = String.join(",", filmTypes);
                existingFilm.setTypes(typesString);
            } else {
                existingFilm.setTypes("");
            }

            filmService.save(existingFilm);
            logger.info("Film updated with id: {}", id);
            redirectAttributes.addFlashAttribute("message", "Фильм успешно обновлен!");
        } catch (IOException e) {
            logger.error("Error uploading file: ", e);
            redirectAttributes.addFlashAttribute("message", "Ошибка загрузки файла: " + e.getMessage());
        }

        logger.info("Exiting editFilm method");
        return "redirect:/admin/films";
    }
}
