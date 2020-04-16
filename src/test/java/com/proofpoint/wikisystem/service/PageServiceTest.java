package com.proofpoint.wikisystem.service;

import com.proofpoint.wikisystem.model.AccessType;
import com.proofpoint.wikisystem.model.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

import static com.proofpoint.wikisystem.util.TestConstants.*;
import static org.mockito.Mockito.when;

public class PageServiceTest {

    @InjectMocks
    private PageService pageService;

    @Mock
    private AccessService accessService;

    @Mock
    private UserService userService;

    @Mock
    private TeamService teamService;



    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        ACCESS_MAP.put("User101", "READ_ONLY");
        COLLAB_ACCESS_MAP.put(AccessType.READ_ONLY, Collections.singletonList(PARENT_OWNER));
        PARENT_PAGE.setAccessMap(COLLAB_ACCESS_MAP);
    }

    @Test
    public void testCreate_HappyCase() throws Exception {
        when(userService.read(USER_ID)).thenReturn(OWNER);
        when(teamService.read(TEAM_ID)).thenReturn(TEAM);

        pageService.create(PAGE_ID, PARENT_PAGE_ID, OWNER, PAGE_CONTENT, ACCESS_MAP);
        Page page = pageService.read(PAGE_ID);
        assertNotNull(page);
        assertEquals(PAGE_ID, page.getPageID());
        assertEquals(PARENT_PAGE_ID, page.getParentPageID());
        assertEquals(OWNER,page.getOwner());
    }

    @Test
    public void testCreate_InheritedAccess_InheritParentOwner() throws Exception {
        when(userService.read(USER_ID)).thenReturn(OWNER);
        when(teamService.read(TEAM_ID)).thenReturn(TEAM);

        pageService.create(PARENT_PAGE_ID, null, PARENT_OWNER, PAGE_CONTENT, null);
        pageService.create(PAGE_ID, PARENT_PAGE_ID, OWNER, PAGE_CONTENT, null);
        Page page = pageService.read(PAGE_ID);
        assertNotNull(page);
        assertEquals(PAGE_ID, page.getPageID());
        assertEquals(PARENT_PAGE_ID, page.getParentPageID());
        assertEquals(OWNER,page.getOwner());
        assertEquals(PARENT_USER_ID, page.getAccessMap().get(AccessType.READ_WRITE).get(0).getId());
    }


    @Test
    public void testCreate_Failed() throws Exception {
        when(userService.read(USER_ID)).thenReturn(OWNER);
        when(teamService.read(TEAM_ID)).thenReturn(TEAM);

        pageService.create(PAGE_ID, PARENT_PAGE_ID, OWNER, PAGE_CONTENT, ACCESS_MAP);
    }
}
