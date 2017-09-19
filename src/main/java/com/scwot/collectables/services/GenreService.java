package com.scwot.collectables.services;

import com.scwot.collectables.entities.Genre;

public interface GenreService {

    Genre find(final Long genreId);

    Genre save(final Genre genre);

    void delete(final Long genreId);

}
