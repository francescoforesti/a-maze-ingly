package it.francescoforesti.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = "name")
public class Room {

    private Integer id;
    private String name;

    @Builder.Default
    private List<Room> linkedRooms = new ArrayList<>();

    @Builder.Default
    private List<Item> items = new ArrayList<>();

    public boolean containsAny(List<Item> items) {
        return !containedItems(items).isEmpty();
    }

    public List<Item> containedItems(List<Item> otherItems) {
        return items.stream()
                .filter(otherItems::contains)
                .collect(Collectors.toList());
    }
}
