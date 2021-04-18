package it.francescoforesti.dto.validation;

import static it.francescoforesti.dto.validation.MazeValidator.*;
import static it.francescoforesti.utils.DtoUtils.createRoom;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.francescoforesti.dto.MazeQuestDTO;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MazeQuestDTOValidationTest {

    private Validator validator;

    @BeforeEach
    public void before() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validation_whenValid_returnsTrue() {
        var startRoom = createRoom("start");
        var room1 = createRoom("room1");
        startRoom.setNorth(room1.getId());
        room1.setSouth(startRoom.getId());

        MazeQuestDTO mazeQuestDTO = MazeQuestDTO.builder()
            .rooms(List.of(startRoom, room1))
            .startRoom(startRoom.getId())
            .objects(Collections.singletonList("object"))
            .build();

        assertTrue(validator.validate(mazeQuestDTO).isEmpty());
    }

    @Test
    public void validation_whenStartRoomIdNotPresentInRooms_returnsFalse() {
        var startRoom = createRoom("start");
        var room1 = createRoom("room1");

        MazeQuestDTO mazeQuestDTO = MazeQuestDTO.builder()
            .rooms(List.of(startRoom, room1))
            .startRoom(-1)
            .objects(Collections.singletonList("object"))
            .build();

        Set<ConstraintViolation<MazeQuestDTO>> violations = validator.validate(mazeQuestDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(INVALID_START_ROOM)));
    }

    @Test
    public void validation_whenRoomLinkedToNonExistentRoomId_returnsFalse() {
        var startRoom = createRoom("start");
        var room1 = createRoom("room1");
        room1.setNorth(-1);

        MazeQuestDTO mazeQuestDTO = MazeQuestDTO.builder()
            .rooms(List.of(startRoom, room1))
            .startRoom(startRoom.getId())
            .objects(Collections.singletonList("object"))
            .build();

        Set<ConstraintViolation<MazeQuestDTO>> violations = validator.validate(mazeQuestDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(INVALID_ROOM_LINK)));
    }

    @Test
    public void validation_whenNotAllRoomLinksAreBidirectional_returnsFalse() {
        var startRoom = createRoom("start");
        var room1 = createRoom("room1");
        var room2 = createRoom("room2");
        var room3 = createRoom("room3");

        startRoom.setNorth(room1.getId());
        room1.setSouth(startRoom.getId());

        room1.setEast(room2.getId());
        room2.setWest(room1.getId());

        room1.setWest(room3.getId());
        room3.setEast(room1.getId());

        room2.setNorth(room3.getId());

        MazeQuestDTO mazeQuestDTO = MazeQuestDTO.builder()
                .rooms(List.of(startRoom, room1, room2, room3))
                .startRoom(startRoom.getId())
                .objects(Collections.singletonList("object"))
                .build();

        Set<ConstraintViolation<MazeQuestDTO>> violations = validator.validate(mazeQuestDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(String.format(INVALID_BIDIRECTIONAL_LINK, room2.getId()))));
    }
}
