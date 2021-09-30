package vn.onltest.controller.student;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.onltest.entity.Test;
import vn.onltest.model.response.AbstractResponse;
import vn.onltest.model.response.success.PageInfo;
import vn.onltest.model.response.success.PagingResultResponse;
import vn.onltest.service.TestService;
import vn.onltest.util.PathUtil;
import vn.onltest.util.SwaggerUtil;

@RestController
@RequestMapping(PathUtil.BASE_PATH + "/students/{studentId}")
@PreAuthorize("hasRole('STUDENT')")
@AllArgsConstructor
@Api(tags = "Test")
public class TestController {
    private final TestService testService;

    @ApiOperation(value = "Get list current exams", response = PagingResultResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerUtil.STATUS_200_MESSAGE),
            @ApiResponse(code = 401, message = SwaggerUtil.STATUS_401_REASON),
            @ApiResponse(code = 403, message = SwaggerUtil.STATUS_403_REASON),
            @ApiResponse(code = 404, message = SwaggerUtil.STATUS_404_REASON),
            @ApiResponse(code = 500, message = SwaggerUtil.STATUS_500_REASON)
    })
    @GetMapping("tests")
    public AbstractResponse getTestsByStudentId(@PathVariable String studentId,
                                                @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                @RequestParam(name = "size", required = false, defaultValue = "12") int size,
                                                @RequestParam(name = "query", required = false, defaultValue = "done") String query) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        Page<Test> resultPage = testService.getTestsWithStatus(query.trim(), pageable);

        return new PagingResultResponse<>(
                HttpStatus.OK.value(),
                resultPage.getContent(),
                new PageInfo(
                        page,
                        size,
                        (int) resultPage.getTotalElements(),
                        resultPage.getTotalPages()
                )
        );

    }
}
