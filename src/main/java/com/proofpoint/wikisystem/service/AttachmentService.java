package com.proofpoint.wikisystem.service;

import com.proofpoint.wikisystem.exceptions.AccessDeniedException;
import com.proofpoint.wikisystem.exceptions.AttachmentNotFoundException;
import com.proofpoint.wikisystem.model.*;
import com.proofpoint.wikisystem.payload.UpdateComponentDto;
import com.proofpoint.wikisystem.util.Action;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.proofpoint.wikisystem.util.Constants.authorizedActionsMap;

@Service
@Slf4j
@Scope("singleton")
public class AttachmentService {
    private Map<String, Attachment> attachments = new HashMap<>();

    @Autowired
    private AccessService accessService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    public void create(final String filename, final String contents, final User owner, final Map<String, String> accessMap)
            throws Exception {
        final Attachment attachment = Attachment
                    .Builder
                    .newInstance()
                    .withFilename(filename)
                    .withContents(contents)
                    .withOwner(owner)
                    .build();

        if (accessMap != null) {
            log.info("Assigning access rights to component");
            for (String collaboratorId : accessMap.keySet()) {
                Collaborator collaborator;
                collaborator = userService.read(collaboratorId);
                if (collaborator == null) {
                    collaborator = teamService.read(collaboratorId);
                    if (collaborator == null) {
                        log.error("User does not exist");
                        throw new Exception("Given user in access map does not exist");
                    }
                }
                accessService.assignAccess(attachment, AccessType.valueOf(accessMap.get(collaboratorId)), collaborator);
            }
        }
        log.info("Attachment created:" + attachment.toString());
        attachments.put(filename, attachment);
    }

    public Attachment read(final String filename) {
        if (attachments.containsKey(filename)) {
            Attachment output = attachments.get(filename);
            log.info("Attachment found:" + output.toString());
            return output;
        } else {
            throw new AttachmentNotFoundException("Attachment not found");
        }
    }

    public Attachment accessAttachment(final String filename, final String requesterId, Boolean isIndividualUser) {
        if (isAuthorizedToPerformAction(Action.READ, filename, requesterId, isIndividualUser)) {
            return read(filename);
        } else {
            throw new AccessDeniedException("Not authorized");
        }
    }

    public String update(final String filename, final UpdateComponentDto updateArgs, final  String requesterId) {
        if (isAuthorizedToPerformAction(Action.UPDATE, filename, requesterId, Boolean.parseBoolean(updateArgs.getIsIndividualUser()))) {
            if (attachments.containsKey(filename)) {
                Attachment attachment = attachments.get(filename);
                if (updateArgs.getContents() != null) {
                    attachment.setContents(updateArgs.getContents());
                }

                if (updateArgs.getOwnerId() != null) {
                    if (isRequesterIsOwner(attachment, requesterId)) {
                        log.info("Transferring ownership of file");
                        User owner = userService.read(updateArgs.getOwnerId());
                        attachment.setOwner(owner);
                    }
                }
                return "Successfully updated attachment";
            } else {
                return "Attachment not found";
            }
        } else {
            throw new AccessDeniedException("Not authorized");
        }
    }

    private boolean isRequesterIsOwner(final Attachment attachment, final String requesterId) {
        return attachment.getOwner().getId().equals(requesterId);
    }

    public boolean delete(final String filename, final String requesterId, final Boolean isIndividualUser) {
        if (isAuthorizedToPerformAction(Action.DELETE, filename, requesterId, isIndividualUser)) {
            if (attachments.containsKey(filename)) {
                attachments.remove(filename);
                return true;
            } else {
                return false;
            }
        } else {
            throw new AccessDeniedException("Not authorized");
        }
    }

    private boolean isAuthorizedToPerformAction(final Action action, final String filename, final String requesterId, final boolean isIndividualUser) {

        Attachment attachment = read(filename);

        if (isRequesterIsOwner(attachment, requesterId)) {
            return true;
        }

        Collaborator collaborator;
        if (isIndividualUser) {
            collaborator = userService.read(requesterId);
        } else {
            Team team = teamService.read(requesterId);
            if (team!= null && team.isAdmin()) {
                return true;
            }
            collaborator = team;
        }

        List<AccessType> allowedAccessTypes = authorizedActionsMap.get(action);

        Map<AccessType, List<Collaborator>> accessMap = attachment.getAccessMap();

        for (AccessType allowedAccessType : allowedAccessTypes) {
            if (accessMap != null && accessMap.get(allowedAccessType) != null && accessMap.get(allowedAccessType).contains(collaborator)) {
                return true;
            }
        }

        return false;
    }

}
