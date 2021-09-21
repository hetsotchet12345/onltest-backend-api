package vn.onltest.controller.user;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.onltest.entity.Group;
import vn.onltest.exception.movie.CustomMethodArgumentNotValidException;
import vn.onltest.model.request.GroupModel;
import vn.onltest.model.response.success.AbstractResultResponse;
import vn.onltest.model.response.success.BaseResultResponse;
import vn.onltest.model.response.success.PageInfo;
import vn.onltest.model.response.success.PagingResultResponse;
import vn.onltest.service.GroupService;
import vn.onltest.util.PathUtil;
import vn.onltest.util.SwaggerUtil;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(PathUtil.BASE_PATH + "/groups")
@PreAuthorize("hasRole('ADMIN') or hasRole('LECTURER')")
@AllArgsConstructor
@Api(tags = "Group")
public class GroupController {
    private final GroupService groupService;

    @ApiOperation(value = "Get list group test created by lecturer", response = PagingResultResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerUtil.STATUS_200_MESSAGE),
            @ApiResponse(code = 401, message = SwaggerUtil.STATUS_401_REASON),
            @ApiResponse(code = 403, message = SwaggerUtil.STATUS_403_REASON),
            @ApiResponse(code = 404, message = SwaggerUtil.STATUS_404_REASON),
            @ApiResponse(code = 500, message = SwaggerUtil.STATUS_500_REASON)
    })
    @GetMapping("list/{lecturerUsername}")
    public AbstractResultResponse<List<Group>> getGroupsIsExistedByLecturerId(
            @PathVariable String lecturerUsername,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "25") int size,
            @RequestParam(name = "query", required = false) String query
    ) {
        Page<Group> resultPage;
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        if (query == null) {
            resultPage = groupService.getGroupsIsExistedByLecturerIdWithPagination(lecturerUsername, pageable);
        } else {
            resultPage = groupService.getGroupsIsExistedByLecturerIdWithQueryAndPagination(lecturerUsername, query, pageable);
        }

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

    @ApiOperation(value = "Create a group", response = BaseResultResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SwaggerUtil.STATUS_200_MESSAGE),
            @ApiResponse(code = 401, message = SwaggerUtil.STATUS_401_REASON),
            @ApiResponse(code = 403, message = SwaggerUtil.STATUS_403_REASON),
            @ApiResponse(code = 404, message = SwaggerUtil.STATUS_404_REASON),
            @ApiResponse(code = 500, message = SwaggerUtil.STATUS_500_REASON)
    })
    @PostMapping("create")
    public AbstractResultResponse<Group> createGroupForDoingExam(@Valid @RequestBody GroupModel groupModel,
                                                   BindingResult bindingResult) throws CustomMethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            throw new CustomMethodArgumentNotValidException(bindingResult);
        } else {
            Group createdGroup = groupService.createGroup(groupModel);
            return new BaseResultResponse<>(HttpStatus.OK.value(), createdGroup);
        }
    }
}
