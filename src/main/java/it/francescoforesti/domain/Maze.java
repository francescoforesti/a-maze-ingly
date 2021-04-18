package it.francescoforesti.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class Maze {

    @Builder.Default
    private List<Room> rooms = new ArrayList<>();

    private Room startingRoom;

    @Builder.Default
    private List<Item> itemsToBeCollected = new ArrayList<>();

}
