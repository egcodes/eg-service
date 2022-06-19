package com.hackerswork.hsw.service.connection;

import com.hackerswork.hsw.persistence.entity.Connection;
import java.util.List;

public interface ConnectionQueryService {

    Connection findByPersonId(Long personId, Long connectionId);

    List<Long> findConnections(Long personId);
}
