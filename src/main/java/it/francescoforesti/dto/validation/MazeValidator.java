package it.francescoforesti.dto.validation;

import it.francescoforesti.dto.MazeQuestDTO;
import it.francescoforesti.dto.RoomDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MazeValidator implements ConstraintValidator<MazeValidation, MazeQuestDTO> {

    public static final String INVALID_ROOM_LINK = "Found room id without corresponding room";
    public static final String INVALID_START_ROOM = "Start room is not in rooms list";
    public static final String INVALID_BIDIRECTIONAL_LINK = "Room %d has links that are not bidirectional";

    @Override
    public boolean isValid(MazeQuestDTO maze, ConstraintValidatorContext constraintValidatorContext) {

        var roomsIds = maze.getRooms()
                .stream()
                .map(RoomDTO::getId)
                .collect(Collectors.toSet());

        boolean startRoomOk = startRoomExistsInRoomList(maze.getStartRoom(), roomsIds, constraintValidatorContext);
        boolean linkedRoomsExist = allLinkedRoomIdsExistAsRooms(maze, roomsIds, constraintValidatorContext);
        boolean linksAreBidirectional = allLinksAreBidirectional(maze, constraintValidatorContext);
        return startRoomOk &&
                linkedRoomsExist &&
                linksAreBidirectional;
    }

    private boolean startRoomExistsInRoomList(Integer startRoom, Set<Integer> roomsIds, ConstraintValidatorContext context) {
        boolean result = true;
        boolean startingRoomFoundInMaze = roomsIds.stream()
                .anyMatch(roomId -> roomId.equals(startRoom));
        if (!startingRoomFoundInMaze) {
            context
                    .buildConstraintViolationWithTemplate(INVALID_START_ROOM)
                    .addPropertyNode("startRoom").addConstraintViolation();
            result = false;
        }
        return result;
    }

    private boolean allLinkedRoomIdsExistAsRooms(MazeQuestDTO maze, Set<Integer> roomsIds, ConstraintValidatorContext context) {
        boolean result = true;
        boolean foundRoomIdWithoutRoom = maze.getRooms().stream()
                .flatMap(r -> r.links().stream())
                .anyMatch(roomId -> !roomsIds.contains(roomId));
        if (foundRoomIdWithoutRoom) {
            context
                    .buildConstraintViolationWithTemplate(INVALID_ROOM_LINK)
                    .addConstraintViolation();
            result = false;
        }
        return result;
    }

    private boolean allLinksAreBidirectional(MazeQuestDTO maze, ConstraintValidatorContext context) {
        boolean result = true;
        var roomsById = maze.getRooms().stream()
                .collect(Collectors.toMap(RoomDTO::getId, Function.identity()));
        for (Integer id : roomsById.keySet()) {
            RoomDTO room = roomsById.get(id);
            boolean allRoomLinksAreBidirectional =
                    Stream.of(room.getEast(), room.getNorth(), room.getWest(), room.getSouth())
                            .filter(Objects::nonNull)
                            .map(roomsById::get)
                            .filter(Objects::nonNull)
                            .allMatch(linkedRoom -> Arrays.asList(
                                    linkedRoom.getEast(),
                                    linkedRoom.getNorth(),
                                    linkedRoom.getWest(),
                                    linkedRoom.getSouth()).contains(id)
                            );
            if (!allRoomLinksAreBidirectional) {
                context
                        .buildConstraintViolationWithTemplate(String.format(INVALID_BIDIRECTIONAL_LINK, id))
                        .addConstraintViolation();
            }
            result &= allRoomLinksAreBidirectional;
        }

        return result;
    }
}