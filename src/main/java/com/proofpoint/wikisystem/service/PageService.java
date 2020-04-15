package com.proofpoint.wikisystem.service;

import com.proofpoint.wikisystem.model.*;
import com.proofpoint.wikisystem.payload.UpdateComponentArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public void create(String pageID, String parentPageID, User owner, String content, Map<String, String> accessMap) throws Exception {
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

    public Page read(String pageID) {
        return pages.getOrDefault(pageID, null);
    }

    public String update(String pageId, UpdateComponentArgs updateArgs, String requesterId){
        if(pages.containsKey(pageId)){
            Page page = pages.get(pageId);
            if(updateArgs.getContents()!=null){
                page.setContent(updateArgs.getContents());
            }

            if(updateArgs.getOwnerId() != null){
                if(isAuthorized(page, requesterId)){
                    log.info("Transferring ownership of page");
                    User owner = userService.read(updateArgs.getOwnerId());
                    page.setOwner(owner);
                }
            }
            return "Successfully updated page";
        }else{
            return "Page not found";
        }
    }

    private boolean isAuthorized(Page page, String requesterId){
        return page.getOwner().getUsername().equals(requesterId);
    }

    public boolean delete(String pageID) {
        if (pages.containsKey(pageID)) {
            pages.remove(pageID);
            return true;
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
        if (parentPage.getPageID() != null) {
            // Add parent owner to RW
            readWriteGroup.add(parentPage.getOwner());

            //Add Inherited Access
            if (parentPage.getAccessMap() != null) {
                readWriteGroup.addAll(parentPage.getAccessMap().get(AccessType.READ_WRITE));
                readGroup.addAll(parentPage.getAccessMap().get(AccessType.READ_ONLY));
                noAccessGroup.addAll(parentPage.getAccessMap().get(AccessType.NO_ACCESS));
            }
        }

        accessMap.put(AccessType.READ_WRITE, readWriteGroup);
        accessMap.put(AccessType.READ_ONLY, readGroup);
        accessMap.put(AccessType.NO_ACCESS, noAccessGroup);

        return accessMap;
    }


}
