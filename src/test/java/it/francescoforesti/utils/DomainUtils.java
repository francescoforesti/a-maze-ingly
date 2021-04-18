package it.francescoforesti.utils;

import it.francescoforesti.domain.Item;
import it.francescoforesti.domain.Room;

import java.util.List;
import java.util.Random;

public class DomainUtils {

    public static void linkRooms(Room room1, Room room2) {
        room1.getLinkedRooms().add(room2);
        room2.getLinkedRooms().add(room1);
    }

    public static Room createRoom(String name, Item... objects) {
        return Room.builder()
                .id(new Random().nextInt())
                .name(name)
                .items(List.of(objects))
                .build();
    }
}
