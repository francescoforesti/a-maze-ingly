package it.francescoforesti.dto;

import it.francescoforesti.domain.Item;
import it.francescoforesti.domain.MazeSolution;
import it.francescoforesti.domain.Room;
import it.francescoforesti.domain.Route;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MazeSolutionDTO {

    private Route fullRoute;
    private List<Item> itemsToCollect = new ArrayList<>();

    public static MazeSolutionDTO from(MazeSolution solved) {
        MazeSolutionDTO result = new MazeSolutionDTO();
        result.setFullRoute(solved.getFullRoute());
        result.setItemsToCollect(solved.getItemsToCollect());
        return result;
    }

    private static final String HEADER = "ID     Room             Object collected\n";
    public String asPrintableString() {
        StringBuilder sb = new StringBuilder();
        sb
                .append(HEADER)
                .append(String.format("%s\n", "-".repeat(HEADER.length()-1)));
        for (Room r : fullRoute.getRooms()) {
            sb.append(String.format("%-8d",r.getId()))
                    .append(String.format("%-15s\t",r.getName()));
            String foundItemNames = r.containedItems(itemsToCollect).stream()
                    .map(Item::getName)
                    .reduce((a, b) -> String.join(", ", a, b))
                    .orElse("None");
            sb.append(foundItemNames)
                    .append('\n');
        }
        return sb.toString();
    }
}
