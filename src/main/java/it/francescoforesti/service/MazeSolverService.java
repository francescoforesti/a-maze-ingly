package it.francescoforesti.service;

import it.francescoforesti.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MazeSolverService {

    private final ItemsFinder solver;

    @Autowired
    public MazeSolverService(ItemsFinder solver) {
        this.solver = solver;
    }

    public MazeSolution solve(Maze input) {
        MazeSolution solution = new MazeSolution();
        Room currentRoom = input.getStartingRoom();

        List<Item> items = input.getItemsToBeCollected();
        while (!items.isEmpty()) {
            Route pathToOneOrMoreItems = solver.findRouteToAnyItem(currentRoom, input.getItemsToBeCollected());
            currentRoom = pathToOneOrMoreItems.lastRoom();
            List<Item> foundItems = currentRoom.containedItems(input.getItemsToBeCollected());
            solution.add(pathToOneOrMoreItems, foundItems);
            items.removeAll(foundItems);
        }

        return solution;
    }

}
