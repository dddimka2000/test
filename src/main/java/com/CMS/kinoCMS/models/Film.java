package com.CMS.kinoCMS.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 4000, message = "Description should not be bigger than 4000 symbols")
    @NotEmpty(message = "Description should not be empty")
    private String description;

    private String mainImage;

    @Size(max = 255, message = "Link should be less than 255 symbols")
    @NotEmpty(message = "Insert the link, it should not be empty")
    private String link;

    @Size(max = 255, message = "Name should be less than 255 characters")
    @NotEmpty(message = "Name shouldn't be empty")
    private String name;

    private LocalDate date;

    private String types;

    // ------------- SEO Block

    @Size(max=255, message = "Url should be less than 255 characters")
    @NotEmpty(message = "Url shouldn't be empty")
    private String urlSEO;

    @Size(max=255, message = "Title should be less than 255 characters")
    @NotEmpty(message = "Title shouldn't be empty")
    private String titleSEO;

    @Size(max=255, message = "Keywords should be less than 255 characters")
    @NotEmpty(message = "Keywords shouldn't be empty")
    private String keywordsSEO;

    @Size(max=4000, message = "Description should be less than 4000 characters")
    @NotEmpty(message = "Description shouldn't be empty")
    private String descriptionSEO;

}
