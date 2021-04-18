package it.francescoforesti.dto;

import it.francescoforesti.domain.Item;
import it.francescoforesti.domain.Room;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {

    @NotNull
    private Integer id;
    @NotNull
    private String name;
    private Integer north;
    private Integer south;
    private Integer east;
    private Integer west;
    @Builder.Default
    private List<Item> objects = new ArrayList<>();

    public Room toRoom() {
        return Room.builder().id(id).name(name).items(objects).build();
    }

    public Set<Integer> links() {
        return Stream.of(north, south, west, west)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    public static List<Room> createRooms(List<RoomDTO> dtos) {
        Map<Integer, Room> roomsById = dtos.stream()
                .map(RoomDTO::toRoom)
                .collect(Collectors.toMap(Room::getId, Function.identity()));
        return linkRooms(dtos, roomsById);
    }

    private static List<Room> linkRooms(List<RoomDTO> dtos, Map<Integer, Room> rooms) {
        dtos.forEach(dto -> {
            Room room = rooms.get(dto.id);
            Stream.of(dto.getNorth(), dto.getEast(), dto.getSouth(), dto.getWest())
                    .filter(Objects::nonNull)
                    .map(rooms::get)
                    .filter(Objects::nonNull)
                    .forEach(link -> room.getLinkedRooms().add(link));
        });
        return new ArrayList<>(rooms.values());
    }
}
