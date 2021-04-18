package it.francescoforesti.service;

import static it.francescoforesti.utils.DomainUtils.createRoom;
import static it.francescoforesti.utils.DomainUtils.linkRooms;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.francescoforesti.domain.Item;
import it.francescoforesti.domain.Maze;
import it.francescoforesti.domain.Room;
import it.francescoforesti.domain.Route;
import java.util.ArrayList;
import java.util.List;

import it.francescoforesti.exception.NoSolutionException;
import org.junit.jupiter.api.Test;

class MazeSolverServiceTest {

    private final MazeSolverService service = new MazeSolverService(new ItemsFinder());

    @Test
    void solve_feasibleSolution_returnsRoute() {

        Room start = createRoom("start");
        Room room1 = createRoom("room1", new Item("object1"));
        Room room2 = createRoom("room2");
        Room room3 = createRoom("room3", new Item("object2"));
        Room room4 = createRoom("room4");

        linkRooms(start, room1);
        linkRooms(room1, room2);
        linkRooms(room2, room3);
        linkRooms(start, room4);

        var result = service.solve(Maze.builder()
            .startingRoom(start)
            .rooms(List.of(start, room1, room2, room3, room4))
            .itemsToBeCollected(new ArrayList<>(List.of(new Item("object1")
                , new Item("object2")))
            ).build());

        Route expectedRoute = new Route(List.of(start, room1, room2, room3));
        assertEquals(expectedRoute, result.getFullRoute());
    }

    @Test
    void solve_noSolution_throwsNoSolutionException() {

        Room start = createRoom("start");
        Room room1 = createRoom("room1");
        Room room2 = createRoom("room2", new Item("object1"));
        Room room3 = createRoom("room3", new Item("object2"));
        Room room4 = createRoom("room4");

        linkRooms(start, room1);
        linkRooms(room1, room2);
        linkRooms(room3, room4);

        Maze maze = Maze.builder()
            .startingRoom(start)
            .rooms(List.of(start, room4, room1, room2, room3))
            .itemsToBeCollected(new ArrayList<>(List.of(new Item("object1")
                , new Item("object2")))
            ).build();

        assertThrows(NoSolutionException.class, () -> service.solve(maze));
    }

    @Test
    void solve_objectsInDisconnectedRoutes_goesBackAndForth() {

        Room start = createRoom("start");
        Room room1 = createRoom("room1");
        Room room2 = createRoom("room2", new Item("object1"));
        Room room3 = createRoom("room3", new Item("object2"));
        Room room4 = createRoom("room4");

        linkRooms(start, room1);
        linkRooms(room1, room2);
        linkRooms(room3, room4);
        linkRooms(start, room4);

        var result = service.solve(Maze.builder()
                .startingRoom(start)
                .rooms(List.of(start, room4, room1, room2, room3))
                .itemsToBeCollected(new ArrayList<>(List.of(new Item("object1")
                        , new Item("object2")))
                ).build());

        Route expectedRoute = new Route(List.of(start, room1, room2, room1, start, room4, room3));
        assertEquals(expectedRoute, result.getFullRoute());
    }
}