package com.proofpoint.wikisystem.service;

import com.proofpoint.wikisystem.model.Team;
import com.proofpoint.wikisystem.model.User;
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

    public void create(String ID, boolean isAdmin) {
        log.info("Creating team with team:" + ID);
        Team team = Team.Builder
                .newInstance()
                .withID(ID)
                .withIsAdmin(isAdmin)
                .build();
        log.info("Team created:" + team.toString());
        teams.put(ID, team);

    }

    public Team read(String teamID) {
        if (teams.containsKey(teamID)) {
            Team output = teams.get(teamID);
            log.info("Team found:" + output.toString());
            return output;
        } else {
            return null;
        }
    }

    public String delete(String teamId) {
        if (teams.containsKey(teamId)) {
            teams.remove(teamId);
            return "Team deleted successfully";
        } else {
            return "Team not found";
        }
    }

    public String addMembertoTeam(String teamId, String userId){
        if(teams.containsKey(teamId)){
            Team team = teams.get(teamId);
            User user = userService.read(userId);

            if(user!=null){
                return team.addMember(user);
            }else{
                return "User not found";
            }
        }else{
            return "Team not found";
        }
    }
}
