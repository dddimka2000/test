package com.CMS.kinoCMS.services;

import com.CMS.kinoCMS.admin.models.Film;
import com.CMS.kinoCMS.admin.repositories.FilmRepository;
import com.CMS.kinoCMS.admin.services.FilmService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private FilmService filmService;

    @Test
    public void shouldReturnCurrentFilms() {
        LocalDate today = LocalDate.now();
        List<Film> films = getFilms();

        Mockito.when(filmRepository.findByDateBeforeOrDate(today, today)).thenReturn(films);

        List<Film> result = filmService.getCurrentFilms();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(films.size(), result.size());
        Assertions.assertEquals(films, result);
    }

    @Test
    public void shouldReturnUpcomingFilms() {
        LocalDate today = LocalDate.now();
        List<Film> films = getFilms();

        Mockito.when(filmRepository.findByDateAfterOrDateIsNull(today)).thenReturn(films);

        List<Film> result = filmService.getUpcomingFilms();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(films.size(), result.size());
        Assertions.assertEquals(films, result);
    }

    @Test
    public void testGetPrePremieresFilms() {

        boolean prePremiere = true;
        Film film1 = new Film();
        film1.setId(1L);
        film1.setName("Film 1");
        film1.setPrePremiere(true);

        Film film2 = new Film();
        film2.setId(2L);
        film2.setName("Film 2");
        film2.setPrePremiere(true);

        List<Film> prePremiereFilms = Arrays.asList(film1, film2);

        Mockito.when(filmRepository.findByIsPrePremiere(prePremiere)).thenReturn(prePremiereFilms);


        List<Film> result = filmService.getPrePremieresFilms(prePremiere);


        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(prePremiereFilms, result);
    }

    @Test
    public void testGetPrePremieresFilms_NoPrePremieres() {

        boolean prePremiere = false;
        Mockito.when(filmRepository.findByIsPrePremiere(prePremiere)).thenReturn(List.of());

        List<Film> result = filmService.getPrePremieresFilms(prePremiere);


        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }


    @Test
    public void shouldSaveFilm() {
        Film film = getFilm(1L);

        filmService.save(film);

        Mockito.verify(filmRepository, Mockito.times(1)).save(film);
    }

    @Test
    public void shouldReturnFilmById() {
        Long id = 1L;
        Film film = getFilm(id);

        Mockito.when(filmRepository.findById(id)).thenReturn(Optional.of(film));

        Optional<Film> result = filmService.findById(id);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(id, result.get().getId());
        Assertions.assertEquals(film, result.get());
    }

    private Film getFilm(Long id) {
        Film film = new Film();
        film.setId(id);
        film.setName("Film " + id);
        film.setDescription("Description " + id);

        return film;
    }

    private List<Film> getFilms() {
        Film firstFilm = getFilm(1L);
        Film secondFilm = getFilm(2L);
        return List.of(firstFilm, secondFilm);
    }
}
