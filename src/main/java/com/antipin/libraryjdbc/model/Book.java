package com.antipin.libraryjdbc.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "books")
public class Book {

    @Id
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    private int publicationYear;
}
