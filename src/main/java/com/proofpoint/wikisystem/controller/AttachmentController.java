package com.proofpoint.wikisystem.controller;

import com.proofpoint.wikisystem.model.Attachment;
import com.proofpoint.wikisystem.payload.CreateAttachmentArgs;
import com.proofpoint.wikisystem.service.AttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/wikisystem/attachment")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @RequestMapping(method = RequestMethod.POST,
            consumes = "application/json")
    public ResponseEntity<String> create(@RequestBody final CreateAttachmentArgs payload) {

        try {
            log.info("Received request to create attachment");
            log.info("Payload:"+payload.toString());
            attachmentService.create(payload.getFilename(), payload.getContents());
            ResponseEntity<String> response = new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            ResponseEntity<String> response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            return response;
        }

    }

    @RequestMapping(method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Attachment> read(@RequestParam String filename) {
        log.info("Received request to read attachment");
        Attachment output = attachmentService.read(filename);
        ResponseEntity<Attachment> response;

        if(output!=null){
            response = new ResponseEntity<>(output, HttpStatus.OK);
        }else{
            response = new ResponseEntity<>(output, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.DELETE,
            produces = "application/json")
    public ResponseEntity<String> delete(@RequestParam String filename) {
        log.info("Received request to delete attachment");
        ResponseEntity<String> response;
       if(attachmentService.delete(filename)){
           response = new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
       }else{
           response = new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
       }
       return response;
    }
}