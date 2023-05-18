package pro.ivanov.webapp.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pro.ivanov.webapp.ApiRequestException;
import pro.ivanov.webapp.model.User;
import pro.ivanov.webapp.repository.UserRepository;
import pro.ivanov.webapp.requestModel.AuthRequest;
import pro.ivanov.webapp.requestModel.CreateUserRequest;
import pro.ivanov.webapp.responseModel.AuthResponse;

@Service
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtService jwtService, AuthenticationManager authManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse signUp(CreateUserRequest request) {
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(this.passwordEncoder.encode(request.getPassword()));

        User savedUser = this.userRepository.save(user);
        String jwtToken = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        return AuthResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse signIn(AuthRequest request) {
        this.authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = this.userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ApiRequestException("User with %s email not found".formatted(request.getEmail())));

        String jwtToken = this.jwtService.generateToken(user);
        String refreshToken = this.jwtService.generateRefreshToken(user);

        return AuthResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // THROW
        }

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail == null) {
            // THROW
        }

        User user = this.userRepository.findByEmail(userEmail).orElseThrow();
        String accessToken = this.jwtService.generateToken(user);

        return AuthResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}