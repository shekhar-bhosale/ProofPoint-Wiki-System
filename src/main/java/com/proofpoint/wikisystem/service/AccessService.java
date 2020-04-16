package com.proofpoint.wikisystem.service;

import com.proofpoint.wikisystem.exceptions.AccessDeniedException;
import com.proofpoint.wikisystem.model.AccessType;
import com.proofpoint.wikisystem.model.Collaborator;
import com.proofpoint.wikisystem.model.Component;
import com.proofpoint.wikisystem.payload.CreateAccessDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Scope("singleton")
@Slf4j
public class AccessService {

    @Autowired
    private PageService pageService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    public void assignAccess(final CreateAccessDto createAccessDto) {
        log.info("createAccessDto received:" + createAccessDto.toString());

        Component component;
        Collaborator collaborator;

        if (createAccessDto.isPage()) {
            component = pageService.read(createAccessDto.getComponentId());
        } else {
            component = attachmentService.read(createAccessDto.getComponentId());
        }

        if (createAccessDto.isIndividualUser()) {
            collaborator = userService.read(createAccessDto.getCollaboratorId());
        } else {
            collaborator = teamService.read(createAccessDto.getCollaboratorId());
        }

        if (component == null || collaborator == null) {
            throw new AccessDeniedException("Given Entities does not exist in system.");
        }

        assignAccess(component, AccessType.valueOf(createAccessDto.getAccessType()), collaborator);
    }

    public void assignAccess(final Component component, final AccessType accessType, final Collaborator collaborator) {
        final Map<AccessType, List<Collaborator>> accessMap = component.getAccessMap();
        if (accessMap.containsKey(accessType)) {
            accessMap.get(accessType).add(collaborator);
        } else {
            List<Collaborator> list = new ArrayList<>();
            list.add(collaborator);
            accessMap.put(accessType, list);
        }
    }
}
