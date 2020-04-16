package com.proofpoint.wikisystem.service;

import com.proofpoint.wikisystem.model.*;
import com.proofpoint.wikisystem.payload.UpdateComponentDto;
import com.proofpoint.wikisystem.util.Action;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.proofpoint.wikisystem.util.Constants.authorizedActionsMap;

@Service
@Slf4j
@Scope("singleton")
public class PageService {

    private Map<String, Page> pages = new HashMap<>();

    @Autowired
    private AccessService accessService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    public void create(final String pageID, final String parentPageID, final User owner, final String content,
                       final Map<String, String> accessMap) throws Exception {
        log.info("Creating page with pageId:" + pageID);
        Page page = Page.Builder
                .newInstance()
                .withPageID(pageID)
                .withParentPageID(parentPageID)
                .withOwner(owner)
                .withContent(content)
                .build();

        if (accessMap != null) {
            log.info("Assigning access rights to component");
            for (final String collaboratorId : accessMap.keySet()) {
                Collaborator collaborator;
                collaborator = userService.read(collaboratorId);
                if (collaborator == null) {
                    collaborator = teamService.read(collaboratorId);
                    if (collaborator == null) {
                        log.error("User does not exist");
                        throw new Exception("Given user in access map does not exist");
                    }
                }
                accessService.assignAccess(page, AccessType.valueOf(accessMap.get(collaboratorId)), collaborator);
            }
        } else {
            log.info("Inheriting access from parent hierarchy");
            page.setAccessMap(inheritAccess(page));
        }
        log.info("Page created:" + page.toString());
        pages.put(pageID, page);

    }

    public Page read(final String pageID) {
        return pages.getOrDefault(pageID, null);
    }

    public String update(final String pageId, final UpdateComponentDto updateArgs, final String requesterId) {
        if (isAuthorizedToPerformAction(Action.UPDATE, pageId, requesterId, Boolean.parseBoolean(updateArgs.getIsIndividualUser()))) {
            if (pages.containsKey(pageId)) {
                Page page = pages.get(pageId);
                if (updateArgs.getContents() != null) {
                    page.setContent(updateArgs.getContents());
                }

                if (updateArgs.getOwnerId() != null) {
                    if (isRequesterIsOwner(page, requesterId)) {
                        log.info("Transferring ownership of page");
                        User owner = userService.read(updateArgs.getOwnerId());
                        page.setOwner(owner);
                    }
                }
                return "Successfully updated page";
            } else {
                return "Page not found";
            }
        } else {
            return "Not authorized to perform action on given component";
        }

    }

    public Page accessPage(final String pageID, final String requesterId, final boolean isIndividualUser) {
        if (isAuthorizedToPerformAction(Action.READ, pageID, requesterId, isIndividualUser)) {
            return read(pageID);
        } else {
            return null;
        }
    }

    private boolean isRequesterIsOwner(final Page page, final String requesterId) {
        return page.getOwner().getUsername().equals(requesterId);
    }

    private boolean isAuthorizedToPerformAction(final Action action, final String pageID, final String requesterId,
                                                final boolean isIndividualUser) {

        final Page page = read(pageID);

        if (isRequesterIsOwner(page, requesterId)) {
            return true;
        }

        Collaborator collaborator;
        if (isIndividualUser) {
            collaborator = userService.read(requesterId);
        } else {
            Team team = teamService.read(requesterId);
            if (team.isAdmin()) {
                return true;
            }
            collaborator = team;
        }

        List<AccessType> allowedAccessTypes = authorizedActionsMap.get(action);

        Map<AccessType, List<Collaborator>> accessMap = page.getAccessMap();

        for (AccessType allowedAccessType : allowedAccessTypes) {
            if (accessMap.get(allowedAccessType).contains(collaborator)) {
                return true;
            }
        }

        return false;
    }

    public boolean delete(final String pageID, final String requesterId, final boolean isIndividualUser) {
        if (isAuthorizedToPerformAction(Action.DELETE, pageID, requesterId, isIndividualUser)) {
            if (pages.containsKey(pageID)) {
                pages.remove(pageID);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    private Map<AccessType, List<Collaborator>> inheritAccess(final Page page) {
        Map<AccessType, List<Collaborator>> accessMap = new HashMap<>();

        List<Collaborator> readWriteGroup = new ArrayList<>();
        List<Collaborator> readGroup = new ArrayList<>();
        List<Collaborator> noAccessGroup = new ArrayList<>();

        Page parentPage = read(page.getParentPageID());
        if (parentPage != null && parentPage.getPageID() != null) {
            // Add parent owner to RW
            readWriteGroup.add(parentPage.getOwner());

            //Add Inherited Access
            if (parentPage.getAccessMap() != null) {
                if (parentPage.getAccessMap().get(AccessType.READ_WRITE) != null)
                    readWriteGroup.addAll(parentPage.getAccessMap().get(AccessType.READ_WRITE));

                if (parentPage.getAccessMap().get(AccessType.READ_ONLY) != null)
                    readGroup.addAll(parentPage.getAccessMap().get(AccessType.READ_ONLY));

                if (parentPage.getAccessMap().get(AccessType.NO_ACCESS) != null)
                    noAccessGroup.addAll(parentPage.getAccessMap().get(AccessType.NO_ACCESS));
            }
        }

        accessMap.put(AccessType.READ_WRITE, readWriteGroup);
        accessMap.put(AccessType.READ_ONLY, readGroup);
        accessMap.put(AccessType.NO_ACCESS, noAccessGroup);

        return accessMap;
    }


}
