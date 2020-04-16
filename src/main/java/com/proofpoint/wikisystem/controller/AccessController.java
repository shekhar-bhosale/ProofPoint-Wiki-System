package com.proofpoint.wikisystem.controller;

import com.proofpoint.wikisystem.payload.CreateAccessDto;
import com.proofpoint.wikisystem.service.AccessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.proofpoint.wikisystem.util.Constants.STATUS_FAILED_WITH_MESSAGE;
import static com.proofpoint.wikisystem.util.Constants.STATUS_SUCCESS;

@Slf4j
@RestController
@RequestMapping("/wikisystem/access")
public class AccessController {

    @Autowired
    private AccessService accessService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> create(@RequestBody final CreateAccessDto payload) {

        try {
            log.info("Received request to create access");
            accessService.assignAccess(payload);
            return new ResponseEntity<>(STATUS_SUCCESS, HttpStatus.CREATED);

        } catch (final Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(STATUS_FAILED_WITH_MESSAGE + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}
