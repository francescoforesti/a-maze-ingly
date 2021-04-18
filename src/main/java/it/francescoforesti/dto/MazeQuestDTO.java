package it.francescoforesti.dto;

import it.francescoforesti.domain.Item;
import it.francescoforesti.domain.Maze;
import it.francescoforesti.domain.Room;
import it.francescoforesti.dto.validation.MazeValidation;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MazeValidation
public class MazeQuestDTO {

    @NotNull
    @Size(min = 1)
    @Valid
    private List<RoomDTO> rooms = new ArrayList<>();
    @NotNull
    private Integer startRoom;
    @NotNull
    @Size(min = 1)
    private List<String> objects = new ArrayList<>();

    public Maze toDomain() {

        List<Room> rooms = RoomDTO.createRooms(this.rooms);
        return Maze.builder()
            .itemsToBeCollected(objects.stream()
                .map(Item::new)
                .collect(Collectors.toList()))
            .rooms(rooms)
            .startingRoom(rooms.stream()
                .filter(r -> Objects.equals(r.getId(), startRoom))
                .findFirst()
                .get())
            .build();
    }
}
