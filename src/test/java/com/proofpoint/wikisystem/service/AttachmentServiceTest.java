package com.proofpoint.wikisystem.service;

import com.proofpoint.wikisystem.model.Attachment;
import com.proofpoint.wikisystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static com.proofpoint.wikisystem.util.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class AttachmentServiceTest {

    @InjectMocks
    AttachmentService attachmentService;

    @Mock
    UserService userService;


    @Mock
    TeamService teamService;


    @Mock
    AccessService accessService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    final void testCreateAndRead_EmptyAccessMap() throws Exception {
        attachmentService.create(FILE_NAME, FILE_CONTENT, OWNER, null);
        final Attachment attachment = attachmentService.read(FILE_NAME);
        assertNotNull(attachment);
        assertEquals("Sample.txt", attachment.getFilename());
        assertEquals("Random data not important", attachment.getContents());
        assertEquals("User101", attachment.getOwner().getId());
    }
    

    @Test
    final void testCreateAndRead_TeamAccess() throws Exception {
        ACCESS_MAP.put(TEAM_ID, "READ_ONLY");
        when(userService.read(USER_ID)).thenReturn(null);
        when(teamService.read(TEAM_ID)).thenReturn(TEAM);

        attachmentService.create(FILE_NAME, FILE_CONTENT, OWNER, ACCESS_MAP);
        final Attachment attachment = attachmentService.read(FILE_NAME);
        assertNotNull(attachment);
        assertEquals("Sample.txt", attachment.getFilename());
        assertEquals("Random data not important", attachment.getContents());
    }

}
