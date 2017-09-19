package com.scwot.collectables.services.impl;

import com.scwot.collectables.entities.Track;
import com.scwot.collectables.repository.TrackRepository;
import com.scwot.collectables.services.TrackService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.google.common.base.Verify.verifyNotNull;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;

    @Override
    public Track find(final Long trackId) {
        return trackRepository.findOne(verifyNotNull(trackId));
    }

    @Override
    public Track save(final Track track) {
        return trackRepository.saveAndFlush(verifyNotNull(track));
    }

    @Override
    public void delete(final Long trackId) {
        trackRepository.delete(verifyNotNull(trackId));
    }
}
