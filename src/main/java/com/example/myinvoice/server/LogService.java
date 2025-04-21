package com.example.myinvoice.server;

import com.example.myinvoice.Entity.DTO.ApiResult;
import com.example.myinvoice.Entity.DTO.OperationLogDetailDTO;
import com.example.myinvoice.Entity.TicketOperationLog;
import com.example.myinvoice.Entity.User;
import com.example.myinvoice.exception.BusinessException;
import com.example.myinvoice.exception.enums.ErrorCodeEnum;
import com.example.myinvoice.mapper.TicketOperationLogMapper;
import com.example.myinvoice.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private final UserMapper userMapper;
    private final TicketOperationLogMapper operationLogMapper;

    public ApiResult<List<OperationLogDetailDTO>> getOperationLogs(Long requestUserId) {
        // 1. 验证请求用户是否为管理员
        User requestUser = userMapper.selectById(requestUserId);
        if (requestUser == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        if (!"admin".equals(requestUser.getRole())) {
            throw new BusinessException(ErrorCodeEnum.PERMISSION_DENIED);
        }

        // 2. 查询所有操作日志
        List<TicketOperationLog> logs = operationLogMapper.selectList(null);
        if (logs.isEmpty()) {
            return ApiResult.success(Collections.emptyList());
        }

        // 3. 关联操作用户信息
        List<OperationLogDetailDTO> result = logs.stream()
            .map(log -> {
                OperationLogDetailDTO dto = new OperationLogDetailDTO();
                dto.setLogId(log.getId());
                dto.setTicketId(log.getTicketId());
                dto.setOperation(log.getOperation());
                dto.setOperationTime(log.getOperationTime());
                dto.setReason(log.getReason());

                // 获取操作用户详情
                User operator = userMapper.selectById(log.getOperationId());
                if (operator != null) {
                    OperationLogDetailDTO.UserInfo userInfo = new OperationLogDetailDTO.UserInfo();
                    userInfo.setUsername(operator.getUsername());
                    userInfo.setEmployeeId(operator.getEmployeeId());
                    userInfo.setDepartment(operator.getDepartment());
                    userInfo.setRole(operator.getRole());
                    userInfo.setPhone(operator.getPhone());


                    dto.setUserInfo(userInfo);
                }
                return dto;
            })
            .collect(Collectors.toList());

        return ApiResult.success(result);
    }
}