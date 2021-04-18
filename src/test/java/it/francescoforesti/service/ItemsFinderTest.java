package it.francescoforesti.service;

import static it.francescoforesti.utils.DomainUtils.createRoom;
import static it.francescoforesti.utils.DomainUtils.linkRooms;
import static org.junit.jupiter.api.Assertions.assertEquals;

import it.francescoforesti.domain.Item;
import it.francescoforesti.domain.Room;
import it.francescoforesti.domain.Route;
import java.util.Collections;
import java.util.List;

import it.francescoforesti.exception.NoSolutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ItemsFinderTest {

    private final ItemsFinder solver = new ItemsFinder();

    @Test
    public void findRouteToAnyItem_noSolution_throwsNoSolutionException() {
        Room start = createRoom("start");

        Assertions.assertThrows(NoSolutionException.class, () ->
                solver.findRouteToAnyItem(start, Collections.singletonList(new Item("object1"))));
    }

    @Test
    public void findRouteToAnyItem_oneObjectInStartRoom_returnsRouteWithStartRoom() {
        Room start = createRoom("start", new Item("object1"));

        var result = solver.findRouteToAnyItem(start, Collections.singletonList(new Item("object1")));

        Route expectedRoute = new Route(Collections.singletonList(start));
        assertEquals(expectedRoute, result);
    }

    @Test
    public void findRouteToAnyItem_multipleObjectsInStartRoom_returnsRouteWithStartRoom() {
        Room start = createRoom("start", new Item("object1"), new Item("object2"));

        var result = solver.findRouteToAnyItem(start, List.of(new Item("object1"), new Item("object2")));

        Route expectedRoute = new Route(Collections.singletonList(start));
        assertEquals(expectedRoute, result);
    }

    @Test
    public void findRouteToAnyItem_singleRoute_returnsThatRoute() {

        Room start = createRoom("start");
        Room room1 = createRoom("room1", new Item("ignored"));
        Room room2 = createRoom("room2", new Item("object1"));

        linkRooms(start, room1);
        linkRooms(room1, room2);

        var result = solver.findRouteToAnyItem(start, List.of(new Item("object1")));

        Route expectedRoute = new Route(List.of(start, room1, room2));
        assertEquals(expectedRoute, result);
    }

    @Test
    public void findRouteToAnyItem_multipleChoices_returnsShortestRoute() {

        Room start = createRoom("start");
        Room room1 = createRoom("room1");
        Room room2 = createRoom("room2");
        Room room3 = createRoom("room3", new Item("object1"));
        Room room4 = createRoom("room4");

        linkRooms(start, room1);
        linkRooms(room1, room2);
        linkRooms(room2, room3);
        linkRooms(room3, room4);
        linkRooms(start, room4);

        var result = solver.findRouteToAnyItem(start, List.of(new Item("object1")));

        Route expectedRoute = new Route(List.of(start, room4, room3));
        assertEquals(expectedRoute, result);
    }
}
