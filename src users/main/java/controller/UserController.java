package controller;

import model.LoginRequest;
import model.Role;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import service.UserService;
import config.JwtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static model.Role.*;

@RestController
@RequestMapping("/users")
//@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    private static User userSecurity;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User user) {
        LoginRequest loginRequest = new LoginRequest();
        try {
            loginRequest.setPassword(user.getPassword());
            loginRequest.setUsername(user.getName());
            // Log input for debugging
            System.out.println("Login attempt for username: " + loginRequest.getUsername());

            // Authenticate user credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Log authentication success
            System.out.println("Authentication successful for username: " + loginRequest.getUsername());

            // Set the authenticated user context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get the authenticated user details
            User authenticatedUser = (User) authentication.getPrincipal();

            // Generate JWT token
            String token = jwtUtil.generateToken(authenticatedUser.getName());

            // Prepare response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", String.valueOf(authenticatedUser.getId()));
            response.put("role", authenticatedUser.getRole().toString());

            // Redirect user based on their role
            if (authenticatedUser.getRole() == Role.ADMIN) {
                response.put("url", "/admin/dashboard");
            } else {
                response.put("url", "/client/dashboard");
            }

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (BadCredentialsException e) {
            // Log the exception for debugging
            System.out.println("Invalid credentials for username: " + loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminPage() {
        if (userSecurity.getRole() == ADMIN) {
            return ResponseEntity.ok("Admin page");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }
    }

    @GetMapping("/client")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> getClientPage() {
        if (userSecurity.getRole() != ADMIN) {
            return ResponseEntity.ok("Client page");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.findById(Long.valueOf(userId)) != null);
    }
}
