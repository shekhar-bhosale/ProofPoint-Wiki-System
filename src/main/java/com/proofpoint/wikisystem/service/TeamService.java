package com.proofpoint.wikisystem.service;

import com.proofpoint.wikisystem.model.Team;
import com.proofpoint.wikisystem.model.User;
import com.proofpoint.wikisystem.payload.UpdateTeamDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Scope("singleton")
public class TeamService {

    @Autowired
    private UserService userService;

    private Map<String, Team> teams = new HashMap<>();

    public void create(final String ID, final boolean isAdmin) {
        log.info("Creating team with team:" + ID);
        Team team = Team.Builder
                .newInstance()
                .withID(ID)
                .withIsAdmin(isAdmin)
                .build();
        log.info("Team created:" + team.toString());
        teams.put(ID, team);

    }

    public Team read(final String teamID) {
        if (teams.containsKey(teamID)) {
            Team output = teams.get(teamID);
            log.info("Team found:" + output.toString());
            return output;
        } else {
            return null;
        }
    }

    public boolean delete(String teamId) {
        if (teams.containsKey(teamId)) {
            teams.remove(teamId);
            return true;
        } else {
            return false;
        }
    }

    public String update(final String teamId, final UpdateTeamDto updateArgs) {
        if (teams.containsKey(teamId)) {
            Team team = teams.get(teamId);
            if (!updateArgs.getIsAdmin().isEmpty()) {
                team.setAdmin(Boolean.parseBoolean(updateArgs.getIsAdmin()));
            }
            return "Successfully updated team";
        } else {
            return "Team not found";
        }
    }

    public String addMemberToTeam(final String teamId, final String userId) {
        if (teams.containsKey(teamId)) {
            Team team = teams.get(teamId);
            User user = userService.read(userId);

            if (user != null) {
                return team.addMember(user);
            } else {
                return "User not found";
            }
        } else {
            return "Team not found";
        }
    }
}
