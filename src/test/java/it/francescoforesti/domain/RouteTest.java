package it.francescoforesti.domain;

import static it.francescoforesti.utils.DomainUtils.createRoom;
import static it.francescoforesti.utils.DomainUtils.linkRooms;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RouteTest {

    private Room room1;
    private Room room2;
    private Route subject;

    @BeforeEach
    public void beforeEach() {
        room1 = createRoom("room1");
        room2 = createRoom("room2");
        linkRooms(room1, room2);
        subject = new Route(List.of(room1, room2));
    }

    @Test
    public void constructor_nullList_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Route(null));
    }

    @Test
    public void constructor_emptyRoomsList_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Route(Collections.emptyList()));
    }

    @Test
    public void constructor_roomsNotLinked_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
            new Route(List.of(room1, createRoom("notLinked"))));
    }

    @Test
    public void concat_routesNotLinkeable_throwsIllegalStateException() {
        Route disconnectedRoute = new Route(Collections.singletonList(createRoom("notLinked")));
        assertThrows(IllegalStateException.class,
            () -> subject.concat(disconnectedRoute));
    }

    @Test
    public void concat_routesLinkeable_createsConcatenationOfRoutes() {
        var room3 = createRoom("room3");
        linkRooms(room2, room3);
        Route route = new Route(List.of(room3));

        var result = subject.concat(route);

        var expectedRoute = new Route(List.of(room1, room2, room3));
        assertEquals(expectedRoute, result);
    }

    @Test
    public void append_roomIsLastOfCurrentRoute_returnsSameRoute() {
        var result = subject.append(room2);

        var expectedRoute = new Route(List.of(room1, room2));
        assertEquals(expectedRoute, result);
    }

    @Test
    public void append_roomIsLinkeable_returnsRouteWithRoomAsLast() {
        var room3 = createRoom("room3");
        linkRooms(room2, room3);
        var result = subject.append(room3);

        var expectedRoute = new Route(List.of(room1, room2, room3));
        assertEquals(expectedRoute, result);
    }

    @Test
    public void explodeWithLinkedRooms_lastRoomHasNoLinks_returnsEmptyList() {
        room2.getLinkedRooms().clear();
        var result = subject.explodeWithLinkedRooms();

        assertEquals(0, result.size());
    }

    @Test
    public void explodeWithLinkedRooms_lastRoomHasOneAdditionalLink_returnsTwoRoutes() {
        Room room3 = createRoom("room3");
        linkRooms(room2, room3);
        var result = subject.explodeWithLinkedRooms();

        assertEquals(2, result.size());
        assertEquals(new Route(List.of(room1, room2, room1)), result.get(0));
        assertEquals(new Route(List.of(room1, room2, room3)), result.get(1));
    }

    @Test
    public void containsLoop_whenRouteHasLoop_returnsTrue() {
        Route subject = new Route(List.of(room1, room2, room1));
        assertTrue(subject.containsLoop());
    }

    @Test
    public void containsLoop_whenRouteHasNoLoops_returnsFalse() {
        Route subject = new Route(List.of(room1, room2));
        assertFalse(subject.containsLoop());
    }
}
