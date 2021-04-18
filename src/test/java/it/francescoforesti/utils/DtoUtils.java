package it.francescoforesti.utils;

import it.francescoforesti.dto.RoomDTO;
import java.util.Random;

public class DtoUtils {

    public static RoomDTO createRoom(String name) {
        return RoomDTO.builder()
            .id(new Random().nextInt())
            .name("start")
            .build();
    }

}
