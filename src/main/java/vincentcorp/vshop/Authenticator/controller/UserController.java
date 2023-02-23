package vincentcorp.vshop.Authenticator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import vincentcorp.vshop.Authenticator.http.HttpResponseThrowers;
import vincentcorp.vshop.Authenticator.model.User;
import vincentcorp.vshop.Authenticator.service.JwtService;
import vincentcorp.vshop.Authenticator.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController 
{
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public User getUser(@RequestHeader(required = false, value = "Authorization") String jwt1, @RequestBody(required = false) String jwt2)
    {
        if(jwt1 != null && !jwt1.isEmpty() && !jwt1.isBlank())
            return this.jwtService.getUser(jwt1);

        if(jwt2 != null && !jwt2.isEmpty() && !jwt2.isBlank())
            return this.jwtService.getUser(jwt2);

        return (User) HttpResponseThrowers.throwBadRequest("Invalid or missing jwt token");
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getById(@PathVariable("id") int id)
    {
        try
        {
            User user = userService.getById(id);

            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch(ErrorResponseException ex)
        {
            throw ex;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> create(@RequestBody User user)
    {
        try
        {
            User savedUser = userService.createUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        }
        catch(ErrorResponseException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }
    
    @PutMapping("{id}")
    public ResponseEntity<User> update(@PathVariable("id") int id, @RequestBody User user)
    {
        try
        {
            user = this.userService.modifyUser(id, user);

            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch(ErrorResponseException ex)
        {
            throw ex;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<User> patch(@PathVariable("id") int id, @RequestBody User user)
    {
        try
        {
            user = this.userService.patchUser(id, user);

            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch(ErrorResponseException ex)
        {
            throw ex;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id)
    {
        try
        {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(ErrorResponseException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}
