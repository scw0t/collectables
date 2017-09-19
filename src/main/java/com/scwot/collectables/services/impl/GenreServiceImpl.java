package com.scwot.collectables.services.impl;

import com.scwot.collectables.entities.Genre;
import com.scwot.collectables.repository.GenreRepository;
import com.scwot.collectables.services.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.google.common.base.Verify.verifyNotNull;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GenreServiceImpl implements GenreService {

    private GenreRepository genreRepository;

    @Override
    public Genre find(Long genreId) {
        return genreRepository.findOne(verifyNotNull(genreId));
    }

    @Override
    public Genre save(Genre genre) {
        return genreRepository.save(verifyNotNull(genre));
    }

    @Override
    public void delete(Long genreId) {
        genreRepository.delete(verifyNotNull(genreId));
    }
}
