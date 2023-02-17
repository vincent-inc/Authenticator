package vincentcorp.vshop.Authenticator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import vincentcorp.vshop.Authenticator.model.User;
import vincentcorp.vshop.Authenticator.service.JwtService;
import vincentcorp.vshop.Authenticator.service.UserService;

@RestController
@RequestMapping("auth")
public class AuthenticationController 
{
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<String> login(@RequestBody User user)
    {
        user = this.userService.login(user);
        String jwt = this.jwtService.generateJwtToken(user);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("Authorization", String.format("Bearer %s", jwt));

        jwt = String.format("{\"jwt\":\"%s\"}", jwt);
        return new ResponseEntity<String>(jwt, map, 201);
    }

    @PostMapping("/any_authority")
    public ResponseEntity<String> hasAnyAuthority(@RequestHeader("Authorization") String jwt, @RequestBody List<String> roles)
    {
        User user = this.jwtService.getUser(jwt);
        boolean hasAuthority = this.userService.hasAnyAuthority(user, roles);
        String body = String.format("{\"hasAuthority\":%s}", hasAuthority);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
    }

    @PostMapping("/all_authority")
    public ResponseEntity<String> hasAllAuthority(@RequestHeader("Authorization") String jwt, @RequestBody List<String> roles)
    {
        User user = this.jwtService.getUser(jwt);
        boolean hasAuthority = this.userService.hasAllAuthority(user, roles);
        String body = String.format("{\"hasAuthority\":%s}", hasAuthority);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
    }
}
