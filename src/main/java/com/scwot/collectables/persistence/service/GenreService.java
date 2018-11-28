package com.scwot.collectables.persistence.service;

import com.scwot.collectables.persistence.model.Genre;

public interface GenreService {

    Genre find(final Long genreId);

    Genre save(final Genre genre);

    void delete(final Long genreId);

}
