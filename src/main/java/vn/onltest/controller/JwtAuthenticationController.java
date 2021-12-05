package vn.onltest.controller;

import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import vn.onltest.config.jwt.JwtUtils;
import vn.onltest.exception.movie.CustomMethodArgumentNotValidException;
import vn.onltest.model.projection.UserInfoSummary;
import vn.onltest.model.request.JwtTokenRequest;
import vn.onltest.model.response.success.JwtTokenResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.onltest.service.UserService;
import vn.onltest.util.PathUtil;
import vn.onltest.util.ServerResponseUtil;

import javax.validation.Valid;

@RestController
@RequestMapping(PathUtil.BASE_PATH + "/login")
@AllArgsConstructor
@Api(tags = "Authentication (Admin || Student || Lecturer)")
public class JwtAuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @ApiOperation(value = "Đăng nhập", response = JwtTokenResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = ServerResponseUtil.SUCCEED_CODE, message = ServerResponseUtil.STATUS_200_MESSAGE),
            @ApiResponse(code = ServerResponseUtil.BAD_REQUEST_CODE, message = ServerResponseUtil.STATUS_400_REASON),
            @ApiResponse(code = ServerResponseUtil.UNAUTHORIZED_CODE, message = ServerResponseUtil.STATUS_401_REASON),
            @ApiResponse(code = ServerResponseUtil.NOT_ALLOWED_CODE, message = ServerResponseUtil.STATUS_403_REASON),
            @ApiResponse(code = ServerResponseUtil.NOT_FOUND_DATA_CODE, message = ServerResponseUtil.STATUS_404_REASON),
            @ApiResponse(code = ServerResponseUtil.INTERNAL_SERVER_ERROR_CODE, message = ServerResponseUtil.STATUS_500_REASON)
    })
    @PostMapping("{name}")
    public JwtTokenResponse<?> createAuthenticatedToken(@PathVariable String name,
                                                        @Valid @RequestBody JwtTokenRequest jwtTokenRequest,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomMethodArgumentNotValidException(bindingResult);
        } else {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtTokenRequest.getUsername().trim(),
                            jwtTokenRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserInfoSummary user = userService.getUserInfoWithRole(jwtTokenRequest.getUsername(), name);
            String token = jwtUtils.generateJwtToken(authentication);
            return new JwtTokenResponse<>(token, user);
        }
    }

}
