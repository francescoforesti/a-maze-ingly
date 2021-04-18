package it.francescoforesti.service;

import it.francescoforesti.domain.Item;
import it.francescoforesti.domain.Room;
import it.francescoforesti.domain.Route;
import it.francescoforesti.exception.NoSolutionException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemsFinder {

    public Route findRouteToAnyItem(Room source, List<Item> targets) {
        Route defaultRoute = new Route(Collections.singletonList(source));
        return evaluate(Collections.singletonList(defaultRoute), targets)
                .orElseThrow(() -> new NoSolutionException("No Solution"));
    }

    private Optional<Route> evaluate(List<Route> candidates, List<Item> targets) {
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        Optional<Route> found = candidates.stream().filter(route -> {
            Room lastRoom = route.lastRoom();
            return lastRoom.containsAny(targets);
        }).findAny();
        if (found.isPresent()) {
            return found;
        } else {
            var workingCandidates = candidates.stream()
                    .flatMap(r -> r.explodeWithLinkedRooms().stream())
                    .filter(route -> !route.containsLoop())
                    .collect(Collectors.toList());
            return evaluate(workingCandidates, targets);
        }
    }

}
