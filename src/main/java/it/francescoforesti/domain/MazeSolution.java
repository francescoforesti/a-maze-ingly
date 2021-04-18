package it.francescoforesti.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MazeSolution {

    private Route fullRoute;
    private List<Item> itemsToCollect = new ArrayList<>();

    public void add(Route route, List<Item> foundItems) {
        fullRoute = fullRoute == null ? route : fullRoute.concat(route);
        itemsToCollect.addAll(foundItems);
    }
}
