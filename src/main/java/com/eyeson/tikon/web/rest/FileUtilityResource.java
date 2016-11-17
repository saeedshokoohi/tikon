package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.PersistentToken;
import com.eyeson.tikon.domain.User;
import com.eyeson.tikon.repository.PersistentTokenRepository;
import com.eyeson.tikon.repository.UserRepository;
import com.eyeson.tikon.security.SecurityUtils;
import com.eyeson.tikon.service.MailService;
import com.eyeson.tikon.service.UserService;
import com.eyeson.tikon.web.rest.dto.KeyAndPasswordDTO;
import com.eyeson.tikon.web.rest.dto.ManagedUserDTO;
import com.eyeson.tikon.web.rest.dto.UserDTO;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class FileUtilityResource {

    private final Logger log = LoggerFactory.getLogger(FileUtilityResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private MailService mailService;

    /**
     * POST /uploadFile -> receive and locally save a file.
     *
     * @param uploadfile The uploaded file as Multipart file parameter in the
     * HTTP request. The RequestParam name must be the same of the attribute
     * "name" in the input tag with type file.
     *
     * @return An http OK status in case of success, an http 4xx status in case
     * of errors.
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(
        @RequestParam("uploadfile") MultipartFile uploadfile) {

        try {
            // Get the filename and build the local file path (be sure that the
            // application have write permissions on such directory)
            String filename = uploadfile.getOriginalFilename();
            String directory = "/var/netgloo_blog/uploads";
            String filepath = Paths.get(directory, filename).toString();

            // Save the file locally
            BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(uploadfile.getBytes());
            stream.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    } // method uploadFile
}
