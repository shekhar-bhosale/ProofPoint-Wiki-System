package com.proofpoint.wikisystem.util;

import com.proofpoint.wikisystem.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestConstants {
    public final static String FILE_NAME = "Sample.txt";
    public final static String REQUESTER_ID = "foo";
    public final static String INDV_USER_TRUE = "True";
    public final static String FILE_CONTENT = "Random data not important";
    public final static String USER_ID = "User101";
    public final static String ADDITIONAL_USER_ID = "User101";
    public final static String PAGE_ID = "Page101";
    public final static String PARENT_USER_ID = "ParentUser101";
    public final static String PARENT_PAGE_ID = "Page501";
    public final static String PAGE_CONTENT = "Dummy content for page";
    public final static String TEAM_ID = "Team101";
    public final static Map<String, String> ACCESS_MAP = new HashMap<>();
    public final static Map<AccessType, List<Collaborator>> COLLAB_ACCESS_MAP = new HashMap<>();

    public static User OWNER = User.Builder
            .newInstance()
            .withID(USER_ID)
            .build();

    public static User PARENT_OWNER = User.Builder
            .newInstance()
            .withID(PARENT_USER_ID)
            .build();

    public static User ADDITIONAL_OWNER = User.Builder
            .newInstance()
            .withID(ADDITIONAL_USER_ID)
            .build();

    public static Attachment ATTACHMENT = Attachment.Builder
            .newInstance()
            .withFilename(FILE_NAME)
            .withContents(FILE_CONTENT)
            .build();

    public static Team TEAM = Team.Builder
            .newInstance()
            .withID(TEAM_ID)
            .withIsAdmin(false)
            .build();

    public static Page PARENT_PAGE = Page.Builder
            .newInstance()
            .withOwner(PARENT_OWNER)
            .withPageID(PARENT_PAGE_ID)
            .build();

}
