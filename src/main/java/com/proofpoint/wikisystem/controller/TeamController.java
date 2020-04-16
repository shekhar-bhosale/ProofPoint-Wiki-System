package com.proofpoint.wikisystem.controller;

import com.proofpoint.wikisystem.model.Team;
import com.proofpoint.wikisystem.payload.CreateTeamDto;
import com.proofpoint.wikisystem.payload.UpdateTeamDto;
import com.proofpoint.wikisystem.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.proofpoint.wikisystem.util.Constants.STATUS_FAILED_WITH_MESSAGE;
import static com.proofpoint.wikisystem.util.Constants.STATUS_SUCCESS;

@Slf4j
@RestController
@RequestMapping("/wikisystem/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> create(@RequestBody final CreateTeamDto payload) {

        try {
            log.info("Received request to create team");
            log.info("Payload:" + payload.toString());

            teamService.create(payload.getTeamId(), payload.isAdmin());
            return new ResponseEntity<>(STATUS_SUCCESS, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(STATUS_FAILED_WITH_MESSAGE + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Team> read(@RequestParam final String teamId) {
        log.info("Received request to read team");
        Team output = teamService.read(teamId);

        if (output != null) {
            return new ResponseEntity<>(output, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(output, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<String> update(@RequestParam final String teamId, @RequestBody final UpdateTeamDto payload) {
        log.info("Received request to update team");
        String output = teamService.update(teamId, payload);

        if (output != null) {
            return new ResponseEntity<>(output, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(output, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<String> delete(@RequestParam final String teamId) {
        log.info("Received request to delete team");

        if (teamService.delete(teamId)) {
            return new ResponseEntity<>("Page deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Page not found", HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/addmember", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> addMember(@RequestParam final String teamId, @RequestParam final String userId) {
        log.info("Received request to add member to team");
        String result = teamService.addMemberToTeam(teamId, userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
