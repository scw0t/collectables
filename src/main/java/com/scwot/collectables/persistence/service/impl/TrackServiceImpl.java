package com.scwot.collectables.persistence.service.impl;

import com.scwot.collectables.persistence.model.Track;
import com.scwot.collectables.persistence.repository.TrackRepository;
import com.scwot.collectables.persistence.service.TrackService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Verify.verifyNotNull;

@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;

    @Override
    public Track find(final Long trackId) {
        return trackRepository.getOne(verifyNotNull(trackId));
    }

    @Override
    public Track save(final Track track) {
        return trackRepository.saveAndFlush(verifyNotNull(track));
    }

    @Override
    public void delete(final Long trackId) {
        trackRepository.deleteById(verifyNotNull(trackId));
    }
}
