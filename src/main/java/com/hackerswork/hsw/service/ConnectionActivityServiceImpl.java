package com.hackerswork.hsw.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.hackerswork.hsw.dto.ActivityDTO;
import com.hackerswork.hsw.dto.ConnectionActivityDTO;
import com.hackerswork.hsw.dto.ConnectionDTO;
import com.hackerswork.hsw.enums.Activity;
import com.hackerswork.hsw.service.activity.ActivityQueryService;
import com.hackerswork.hsw.service.connection.ConnectionQueryService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConnectionActivityServiceImpl implements ConnectionActivityService {

    private final ConnectionQueryService connectionQueryService;
    private final ActivityQueryService activityQueryService;

    @Override
    public List<ConnectionActivityDTO> findConnectionsByPerson(Long personId) {
        var connections = connectionQueryService.findConnections(personId);
        var personActivities = activityQueryService.list(connections.stream().map(
            ConnectionDTO::getConnectionId).collect(
            Collectors.toList()));

        var persons = personActivities.stream().map(a ->
            ConnectionActivityDTO.builder()
                .personId(a.getPersonId())
                .userName(a.getUserName())
                .name(a.getName())
                .activity(getActivity(a.getLastActivityTime()))
                .pinned(isPinned(connections, a))
                .build()
        ).collect(Collectors.toList());

        var onlinePersons = persons.stream()
            .filter(p -> p.getActivity().equals(Activity.ONLINE) && nonNull(p.getName()))
            .sorted(Comparator.comparing(ConnectionActivityDTO::getName)).collect(Collectors.toList());

        var offlinePersons = persons.stream()
            .filter(p -> p.getActivity().equals(Activity.OFFLINE) && nonNull(p.getName()))
            .sorted(Comparator.comparing(ConnectionActivityDTO::getName)).collect(Collectors.toList());

        var others = persons.stream()
            .filter(p -> isNull(p.getName()))
            .sorted(Comparator.comparing(ConnectionActivityDTO::getUserName)).collect(Collectors.toList());

        var resultLs = new ArrayList<ConnectionActivityDTO>();
        resultLs.addAll(onlinePersons);
        resultLs.addAll(offlinePersons);
        resultLs.addAll(others);

        return resultLs;
    }

    private boolean isPinned(List<ConnectionDTO> connections, ActivityDTO a) {
        return connections.stream().filter(c -> c.getConnectionId().equals(a.getPersonId()))
            .findFirst().orElse(ConnectionDTO.builder().build()).isPinned();
    }

    private Activity getActivity(Long lastActivityTime) {
        return Activity.findBy(lastActivityTime);
    }
}
