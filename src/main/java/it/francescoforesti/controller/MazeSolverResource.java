package it.francescoforesti.controller;

import it.francescoforesti.domain.MazeSolution;
import it.francescoforesti.dto.MazeQuestDTO;
import it.francescoforesti.dto.MazeSolutionDTO;
import it.francescoforesti.service.MazeSolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "v1/maze")
public class MazeSolverResource {

    @Autowired
    private MazeSolverService service;

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String solveMaze(@RequestBody @Valid MazeQuestDTO input) {
        MazeSolution solved = service.solve(input.toDomain());
        return MazeSolutionDTO.from(solved).asPrintableString();
    }

}
