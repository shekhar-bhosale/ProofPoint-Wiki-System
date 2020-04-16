package com.proofpoint.wikisystem.controller;

import com.proofpoint.wikisystem.model.Page;
import com.proofpoint.wikisystem.model.User;
import com.proofpoint.wikisystem.payload.CreatePageDto;
import com.proofpoint.wikisystem.payload.DeleteComponentDto;
import com.proofpoint.wikisystem.payload.UpdateComponentDto;
import com.proofpoint.wikisystem.service.PageService;
import com.proofpoint.wikisystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.proofpoint.wikisystem.util.Constants.STATUS_FAILED_WITH_MESSAGE;
import static com.proofpoint.wikisystem.util.Constants.STATUS_SUCCESS;

@Slf4j
@RestController
@RequestMapping("/wikisystem/page")
public class PageController {

    @Autowired
    private PageService pageService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> create(@RequestBody final CreatePageDto payload) {

        try {
            log.info("Received request to create page");
            log.info("Payload:" + payload.toString());

            User owner = userService.read(payload.getOwnerId());
            pageService.create(payload.getPageId(), payload.getParentPageId(), owner, payload.getContent(), payload.getAccessMap());

            return new ResponseEntity<>(STATUS_SUCCESS, HttpStatus.CREATED);

        } catch (final Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(STATUS_FAILED_WITH_MESSAGE + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Page> read(@RequestParam final String pageId, @RequestParam final String requesterId, @RequestParam final String isIndividualUser) {
        log.info("Received request to read page");
        Page output = pageService.accessPage(pageId, requesterId, Boolean.parseBoolean(isIndividualUser));

        if (output != null) {
            return new ResponseEntity<>(output, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(output, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<String> update(@RequestParam final String pageId, @RequestBody final UpdateComponentDto payload) {
        log.info("Received request to update page");
        String output = pageService.update(pageId, payload, payload.getRequesterId());

        ResponseEntity<String> response;
        if (output != null) {
            response = new ResponseEntity<>(output, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(output, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<String> delete(@RequestParam final String pageId, @RequestBody final DeleteComponentDto payload) {
        log.info("Received request to delete page");

        if (pageService.delete(pageId, payload.getRequesterId(), Boolean.parseBoolean(payload.getIsIndividualUser()))) {
            return new ResponseEntity<>("Page deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Page not found", HttpStatus.NOT_FOUND);
        }

    }
}
