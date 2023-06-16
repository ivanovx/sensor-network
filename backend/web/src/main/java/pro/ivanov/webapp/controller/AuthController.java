package pro.ivanov.webapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.ivanov.webapp.requestModel.AuthRequest;
import pro.ivanov.webapp.requestModel.CreateUserRequest;
import pro.ivanov.webapp.responseModel.AuthResponse;
import pro.ivanov.webapp.responseModel.UserResponse;
import pro.ivanov.webapp.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody CreateUserRequest request) {
        UserResponse response = this.authService.signUp(request);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody AuthRequest request) {
        AuthResponse response = this.authService.signIn(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request) {
        AuthResponse response = this.authService.refreshToken(request);

        return ResponseEntity.ok(response);
    }
}