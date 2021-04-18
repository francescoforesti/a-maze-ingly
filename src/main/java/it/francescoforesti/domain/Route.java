package it.francescoforesti.domain;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Route {

    private List<Room> rooms;

    public Route(List<Room> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            throw new IllegalArgumentException("init room list can not be null");
        }

        for (int i = 0; i < rooms.size() - 1; i++) {
            Room room = rooms.get(i);
            Room nextRoomInRoute = rooms.get(i + 1);
            if (!room.getLinkedRooms().contains(nextRoomInRoute)) {
                throw new IllegalArgumentException(
                    String.format("Rooms %s and room %s are not linked", room, nextRoomInRoute));
            }
        }

        this.rooms = rooms;
    }

    public Route concat(Route other) {
        List<Room> newRooms = new ArrayList<>(rooms);
        if (!isLinkedTo(other)) {
            throw new IllegalStateException("Cannot concatenate disconnected routes");
        }
        newRooms.addAll(
            other.firstRoom().equals(lastRoom()) ?
                other.rooms.subList(1, other.rooms.size()) :
                other.rooms
        );
        return new Route(newRooms);
    }

    public Route append(Room room) {
        return concat(new Route(Collections.singletonList(room)));
    }

    public List<Route> explodeWithLinkedRooms() {
        return lastRoom().getLinkedRooms().stream()
            .map(this::append)
            .collect(Collectors.toList());
    }

    public boolean containsLoop() {
        return rooms.size() > rooms.stream().mapToInt(Room::getId).distinct().count();
    }

    private boolean isLinkedTo(Route other) {
        Room otherFirst = other.firstRoom();
        Room thisLast = lastRoom();
        return otherFirst.equals(thisLast) ||
            thisLast.getLinkedRooms().contains(otherFirst);
    }

    public Room firstRoom() {
        return rooms.get(0);
    }

    public Room lastRoom() {
        return rooms.get(rooms.size() - 1);
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

}
