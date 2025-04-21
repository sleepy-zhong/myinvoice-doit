package com.example.myinvoice.server.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myinvoice.Entity.*;
import com.example.myinvoice.Entity.DTO.UserModifyRequest;
import com.example.myinvoice.exception.BusinessException;
import com.example.myinvoice.exception.enums.ErrorCodeEnum;
import com.example.myinvoice.mapper.*;
import com.example.myinvoice.server.Invoice.TicketService;
import jakarta.annotation.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {

    @Resource
    private TrainTicketMapper trainTicketMapper;

    @Resource
    private InvoiceMapper invoiceMapper;

    @Resource
    private AirItineraryMapper airItineraryMapper;
    @Resource
    private UserMapper userMapper;

    @Resource
    private TicketOperationLogMapper logMapper;
    @Resource
    private TicketMapper ticketMapper;
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Override//仅匹配invoice
    public List<Map<String, Object>> getFullTicketsByUserId(Long userId) {

        // 1. 参数基础校验
        if (userId == null) {
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR, "用户ID不能为空");
        }

        // 2. 校验用户存在性
        User user = userMapper.selectById(userId);
        if (user == null) {
            logger.warn("查询票据失败，用户不存在 - 用户ID: {}", userId);
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }

        // 查询主表记录
        List<Ticket> tickets = lambdaQuery()
                .eq(Ticket::getUserId, userId)//仅查询当前用户的票据
                .eq(Ticket::getType, "invoice")// 仅查询invoice类型，如果查其他类型删除这一行
                .list();

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Ticket ticket : tickets) {
            String type = ticket.getType();  // train / flight / vat
            Long ticketId = ticket.getId();

            Map<String, Object> fullTicket = new HashMap<>();
            fullTicket.put("ticket", ticket.data_findbyuserid());  // 主表信息

            // 查询子表数据
            Map<String, Object> data = null;
            switch (type) {
                case "train_ticket":
                    TrainTicket train = trainTicketMapper.selectById(ticketId);
                    data = train != null ? train.data_findbyuserid() : null;
                    break;
                case "invoice":
                    Invoices vat = invoiceMapper.selectById(ticketId);
                    data = vat != null ? vat.data_findbyuserid() : null;
                    break;
                case "air_itinerary":
                    AirItinerary flight = airItineraryMapper.selectById(ticketId);
                    data = flight != null ? flight.data_findbyuserid() : null;
                    break;
            }

            fullTicket.put("data", data);
            resultList.add(fullTicket);
        }

        return resultList;

    }


    @Override
    @Transactional(rollbackFor = {Exception.class}) // 所有异常均触发回滚
    public boolean reviewTicket(Long ticketId, Long reviewerId, String result, String reason) {
        // 1. 校验用户角色
        User reviewer = userMapper.selectById(reviewerId);
        if (reviewer == null || !"finance".equals(reviewer.getRole())) {
            throw new BusinessException(ErrorCodeEnum.REVIEWER_INVALID);
        }

        // 2. 更新票据状态
        Ticket ticket = getById(ticketId);
        if (ticket == null || !"pending".equals(ticket.getApprovalStatus())) {
            return false;
        }

        String status = switch (result.toLowerCase()) {
            case "approved" -> "approved";
            case "rejected" -> "rejected";
            default -> throw new BusinessException(ErrorCodeEnum.REVIEW_STATUS_INVALID);
        };

        if (status == null) return false;

        ticket.setApprovalStatus(status);
        updateById(ticket);

        // 3. 插入审核日志
        TicketOperationLog log = new TicketOperationLog();
        log.setTicketId(ticketId);
        log.setOperationId(reviewerId);
//        log.setReviewTime((java.sql.Date) new Date());
        log.setOperationTime(new java.sql.Date(System.currentTimeMillis()));
//        log.setResult("approved".equals(status) ? "通过" : "不通过");
        log.setOperation("Financial audits："+status);
        log.setReason(reason);

//        logMapper.insert(log);
// 修改后的插入审核日志代码
        try {
            logMapper.insert(log);
        } catch (DataAccessException e) {
            logger.error("审核日志插入失败，ticketId: {}, reviewerId: {}", ticketId, reviewerId, e);
            throw new BusinessException(ErrorCodeEnum.LOG_INSERT_FAILED, "审核日志记录失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetTicketToPending(Long userId, Long ticketId) {
        // 1. 校验用户权限
        User operator = userMapper.selectById(userId);
        if (operator == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
//        if (!List.of("finance", "admin").contains(operator.getRole().toLowerCase())) {
//            throw new BusinessException(ErrorCodeEnum.PERMISSION_DENIED);
//        }

        // 2. 获取票据并校验
        Ticket ticket = getById(ticketId);
        if (ticket == null) {
            throw new BusinessException(ErrorCodeEnum.TICKET_NOT_FOUND);
        }
        if ("pending".equals(ticket.getApprovalStatus())) {
            return true; // 已经是pending状态无需处理
        }

        // 3. 更新状态
        ticket.setApprovalStatus("pending");
        ticket.setUpdatedAt(LocalDateTime.now());
        updateById(ticket);

        // 4. 记录操作日志
        TicketOperationLog log = new TicketOperationLog();
        log.setTicketId(ticketId);
        log.setOperationId(userId);
        log.setOperationTime(new java.sql.Date(System.currentTimeMillis()));
        log.setOperation("Status Reset: pending");
        log.setReason("用户重新提交");
        logMapper.insert(log);

        return true;
    }
    // 在TicketServiceImpl.java中实现
    @Override
    public List<Map<String, Object>> getInvoicesByDepartment(String department) {
        // 1. 获取部门用户列表
        QueryWrapper<User> userQuery = new QueryWrapper<>();
        userQuery.select("id").eq("department", department);
        List<Long> userIds = userMapper.selectList(userQuery)
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());

        if (userIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 查询这些用户的发票票据
        QueryWrapper<Ticket> ticketQuery = new QueryWrapper<>();
        ticketQuery.in("user_id", userIds)
                .eq("type", "invoice")
                .orderByDesc("created_at");

        List<Ticket> tickets = ticketMapper.selectList(ticketQuery);

        // 3. 组装完整数据
        return tickets.stream().map(ticket -> {
            Map<String, Object> result = new HashMap<>();
            result.put("ticket", ticket.data_findbyfinance());

            Invoices invoice = invoiceMapper.selectById(ticket.getId());
            result.put("data", invoice != null ? invoice.data_findbyfinance() : null);

            return result;
        }).collect(Collectors.toList());
    }
    @Override
    public List<Map<String, Object>> getTicketsforapprove(Long userId) {

        // 1. 参数基础校验
        if (userId == null) {
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR, "用户ID不能为空");
        }

        // 2. 校验用户存在性且用户角色为finance
        User user = userMapper.selectById(userId);
        if (user == null) {
            logger.warn("查询票据失败，用户不存在 - 用户ID: {}", userId);
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        } if (!"finance".equalsIgnoreCase(user.getRole())) {
            logger.warn("非法访问: 用户[{}]非财务角色尝试访问发票数据", userId);
            throw new BusinessException(ErrorCodeEnum.PERMISSION_DENIED, "仅限财务人员访问");
        }

        // 查询主表记录
        List<Ticket> tickets = lambdaQuery()
                .select() // 查询所有字段
                .list();

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Ticket ticket : tickets) {
            String type = ticket.getType();  // train / flight / vat
            Long ticketId = ticket.getId();

            Map<String, Object> fullTicket = new HashMap<>();
            fullTicket.put("ticket", ticket.data_findbyfinance());  // 主表信息

            // 查询子表数据
            Map<String, Object> data = null;
            switch (type) {
                case "train_ticket":
                    TrainTicket train = trainTicketMapper.selectById(ticketId);
                    data = train != null ? train.data_findbyuserid() : null;
                    break;
                case "invoice":
                    Invoices vat = invoiceMapper.selectById(ticketId);
                    data = vat != null ? vat.data_findbyuserid() : null;
                    break;
                case "air_itinerary":
                    AirItinerary flight = airItineraryMapper.selectById(ticketId);
                    data = flight != null ? flight.data_findbyuserid() : null;
                    break;
            }

            fullTicket.put("data", data);
            resultList.add(fullTicket);
        }

        return resultList;

    }
    @Override// 获取发票待审核列表，只有发票类型，finance和admin可以访问
    public List<Map<String, Object>> getInvoiceforapprove(Long userId) {

        // 1. 参数基础校验
        if (userId == null) {
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR, "用户ID不能为空");
        }

        // 2. 校验用户存在性且用户角色为finance
        User user = userMapper.selectById(userId);
        if (user == null) {
            logger.warn("查询票据失败，用户不存在 - 用户ID: {}", userId);
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        } if ("user".equalsIgnoreCase(user.getRole())) //普通用户访问失败
        {
            logger.warn("非法访问: 用户[{}]无权限角色尝试访问发票数据", userId);
            throw new BusinessException(ErrorCodeEnum.PERMISSION_DENIED, "仅限权限人员访问");
        }

        // 查询主表记录
        List<Ticket> tickets = lambdaQuery()
                .eq(Ticket::getType, "invoice")  // 新增类型过滤条件
                .list();
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Ticket ticket : tickets) {
            String type = ticket.getType();  // train / flight / vat
            Long ticketId = ticket.getId();

            Map<String, Object> fullTicket = new HashMap<>();
            fullTicket.put("ticket", ticket.data_findbyfinance());  // 主表信息

            // 查询子表数据
            // 由于已过滤类型，可以简化switch逻辑
            if ("invoice".equals(type)) {
                Invoices vat = invoiceMapper.selectById(ticketId);
                fullTicket.put("data", vat != null ? vat.data_findbyfinance() : null);
            }

            resultList.add(fullTicket);
        }

        return resultList;

    }

    @Override
    public Object getOcrDataByTicketId(Long userId, Long ticketId) {
        // 1. 参数基础校验
        if (userId == null || ticketId == null) {
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR, "参数不能为空");
        }

        // 2. 验证用户权限
        User user = userMapper.selectById(userId);
        if (user == null) {
            logger.warn("用户不存在 - 用户ID: {}", userId);
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        if (!List.of("finance", "admin").contains(user.getRole().toLowerCase())) {
            logger.warn("非法访问尝试 - 用户[{}]非财务/管理员角色", userId);
            throw new BusinessException(ErrorCodeEnum.PERMISSION_DENIED, "无权访问该资源");
        }

        // 3. 查询票据信息
        Ticket ticket = this.getById(ticketId);
        if (ticket == null) {
            logger.warn("票据不存在 - 票据ID: {}", ticketId);
            throw new BusinessException(ErrorCodeEnum.RESOURCE_NOT_FOUND, "票据不存在");
        }

//        // 4. 返回OCR数据
//        try {
//            return ticket.getOcrJson();
//        } catch (Exception e) {
//            logger.error("解析OCR数据失败 - 票据ID: {}", ticketId, e);
//            throw new BusinessException(ErrorCodeEnum.DATA_PROCESS_ERROR, "数据解析失败");
//        }
        // 4. 返回OCR数据的data部分
        try {
            Map<String, Object> ocrJson = (Map<String, Object>) ticket.getOcrJson();
            if (ocrJson == null) {
                logger.warn("OCR数据为空 - 票据ID: {}", ticketId);
                return Collections.emptyMap();
            }

            // 新结构解析逻辑
            return extractNestedData(ocrJson, ticketId);
        } catch (Exception e) {
            logger.error("解析失败 TicketID:{}", ticketId, e);
            throw new BusinessException(ErrorCodeEnum.DATA_PROCESS_ERROR, "数据解析失败");
        }

    }
    private Object extractNestedData(Map<String, Object> ocrJson, Long ticketId) {
        // 1. 检查subMsgs字段
        if (!ocrJson.containsKey("subMsgs")) {
            logger.warn("OCR缺少subMsgs字段 - TicketID:{}", ticketId);
            throw new BusinessException(ErrorCodeEnum.DATA_PROCESS_ERROR);
        }

        List<Map<String, Object>> subMsgs = (List<Map<String, Object>>) ocrJson.get("subMsgs");
        if (subMsgs == null || subMsgs.isEmpty()) {
            logger.warn("subMsgs为空列表 - TicketID:{}", ticketId);
            throw new BusinessException(ErrorCodeEnum.DATA_PROCESS_ERROR);
        }

        // 2. 获取第一个subMsg
        Map<String, Object> firstSubMsg = subMsgs.get(0);
        if (!firstSubMsg.containsKey("result")) {
            logger.warn("subMsg缺少result字段 - TicketID:{}", ticketId);
            throw new BusinessException(ErrorCodeEnum.DATA_PROCESS_ERROR);
        }

        // 3. 提取result中的data
        Map<String, Object> result = (Map<String, Object>) firstSubMsg.get("result");
        if (!result.containsKey("data")) {
            logger.warn("result缺少data字段 - TicketID:{}", ticketId);
            throw new BusinessException(ErrorCodeEnum.DATA_PROCESS_ERROR);
        }

        return result.get("data");
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean userModifyTicket(Long userId, UserModifyRequest request) {
        // 0. 获取用户角色
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        String role = user.getRole();
        // 1. 验证用户权限和票据存在性
        Ticket ticket = ticketMapper.selectById(request.getTicketId());
        if (ticket == null) {
            throw new BusinessException(ErrorCodeEnum.TICKET_NOT_FOUND);
        }
        if(ticket.getApprovalStatus().equals("approved")){
            throw new BusinessException(ErrorCodeEnum.TICKET_ALREADY_APPROVED);
        }
//        if ("user".equals(role) && !ticket.getUserId().equals(userId)) {
//            throw new BusinessException(ErrorCodeEnum.PERMISSION_DENIED);
//        }

        // 2. 根据票据类型更新对应分表
        boolean updateSuccess = switch (ticket.getType()) {
            case "train_ticket" -> updateTrainTicket(request.getTicketId(), request.getUpdateFields());
            case "air_itinerary" -> updateAirItinerary(request.getTicketId(), request.getUpdateFields());
            case "invoice" -> updateInvoice(request.getTicketId(), request.getUpdateFields());
            default -> throw new BusinessException(ErrorCodeEnum.INVALID_TICKET_TYPE);
        };

        if (!updateSuccess) {
            return false;
        }
        // 新增：更新主表 updated_at 字段
        ticket.setUpdatedAt(java.time.LocalDateTime.now());
        ticketMapper.updateById(ticket);

        // 3. 记录操作日志
        TicketOperationLog log = new TicketOperationLog();
        log.setTicketId(request.getTicketId());
        log.setOperationId(userId);
        log.setOperationTime(new java.sql.Date(System.currentTimeMillis()));
        log.setOperation(request.getOperation());
        log.setReason(request.getReason());

        try {
            logMapper.insert(log);
        } catch (DataAccessException e) {
            throw new BusinessException(ErrorCodeEnum.LOG_INSERT_FAILED, "操作记录失败");
        }

        return true;
    }

    public Map<String, Object> getInvoiceDetail(Long userId, Long ticketId) {
        // 1. 参数基础校验
        if (userId == null || ticketId == null) {
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR, "参数不能为空");
        }

        // 2. 校验用户存在性
        User user = userMapper.selectById(userId);
        if (user == null) {
            logger.warn("查询票据失败，用户不存在 - 用户ID: {}", userId);
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }

        // 3. 校验用户角色
        String userRole = user.getRole();
        if (!"finance".equalsIgnoreCase(userRole) && !"admin".equalsIgnoreCase(userRole)) {
            logger.warn("非法访问: 用户[{}]（角色: {}）尝试访问受限资源", userId, userRole);
            throw new BusinessException(ErrorCodeEnum.PERMISSION_DENIED, "仅限财务人员和管理员访问");
        }

        // 4. 获取票据信息
        Ticket ticket = ticketMapper.selectById(ticketId);
        if (ticket == null) {
            logger.warn("票据不存在 - 票据ID: {}", ticketId);
            throw new BusinessException(ErrorCodeEnum.RESOURCE_NOT_FOUND, "票据不存在");
        }

        // 5. 构建返回结果
        Map<String, Object> ticketDetail = new HashMap<>();
        ticketDetail.put("ticket", ticket.data_findbyfinance());

        // 6. 根据票据类型获取子表信息
        String type = ticket.getType();
        if ("invoice".equalsIgnoreCase(type)) {
            Invoices vat = invoiceMapper.selectById(ticketId);
            if (vat != null) {
                ticketDetail.put("data", vat.data_findbyfinance());
            } else {
                logger.warn("发票子表数据不存在 - 票据ID: {}", ticketId);
                ticketDetail.put("data", null);
            }
        }

        return ticketDetail;
    }

    @Override
    public List<Map<String, Object>> getTicketOperationDetails(Long ticketId) {
        // 1. 参数校验
        if (ticketId == null) {
            throw new BusinessException(ErrorCodeEnum.PARAM_ERROR, "票据ID不能为空");
        }

        // 2. 查询操作记录
        QueryWrapper<TicketOperationLog> logQuery = new QueryWrapper<>();
        logQuery.eq("ticket_id", ticketId)
                .orderByDesc("operation_time");
        List<TicketOperationLog> logs = logMapper.selectList(logQuery);

        if (logs.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 提取操作人ID集合
        Set<Long> userIds = logs.stream()
                .map(TicketOperationLog::getOperationId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 4. 批量查询用户信息
        Map<Long, User> userMap = userMapper.selectBatchIds(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        // 5. 组装结果
        return logs.stream().map(log -> {
            Map<String, Object> detail = new LinkedHashMap<>();
            detail.put("operationTime", log.getOperationTime());
            detail.put("operation", log.getOperation());
            detail.put("reason", log.getReason());
            detail.put("operatorId", log.getOperationId());

            User operator = userMap.get(log.getOperationId());
            if (operator != null) {
                detail.put("operationUsername", operator.getUsername());
                detail.put("employeeId", operator.getEmployeeId());
            } else {
                detail.put("operationUsername", "N/A");
                detail.put("employeeId", "N/A");
            }

            return detail;
        }).collect(Collectors.toList());
    }
    // 更新火车票分表


    private boolean updateTrainTicket(Long ticketId, Map<String, Object> fields) {
        TrainTicket trainTicket = trainTicketMapper.selectById(ticketId);
        if (trainTicket == null) return false;

        applyFieldUpdates(trainTicket, fields);
        return trainTicketMapper.updateById(trainTicket) > 0;
    }

    // 更新行程单分表
    private boolean updateAirItinerary(Long ticketId, Map<String, Object> fields) {
        AirItinerary airItinerary = airItineraryMapper.selectById(ticketId);
        if (airItinerary == null) return false;

        applyFieldUpdates(airItinerary, fields);
        return airItineraryMapper.updateById(airItinerary) > 0;
    }

    // 更新发票分表
    private boolean updateInvoice(Long ticketId, Map<String, Object> fields) {
        Invoices invoice = invoiceMapper.selectById(ticketId);
        if (invoice == null) return false;

        applyFieldUpdates(invoice, fields);
        return invoiceMapper.updateById(invoice) > 0;
    }

    // 动态应用字段修改（反射实现）
    private void applyFieldUpdates(Object entity, Map<String, Object> fields) {
        fields.forEach((key, value) -> {
            try {
                Field field = entity.getClass().getDeclaredField(key);
                field.setAccessible(true);
                Object convertedValue = convertValueToType(value, field.getType());
                field.set(entity, convertedValue);
            } catch (Exception e) {
                throw new BusinessException(ErrorCodeEnum.INVALID_FIELD, "字段 " + key + " 更新失败");
            }
        });
    }
    private Object convertValueToType(Object value, Class<?> targetType) {
        if (value == null) return null;

        // 类型匹配直接返回
        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }

        // 常见类型转换
        try {
            if (targetType == String.class) {
                return value.toString();
            } else if (targetType == Integer.class || targetType == int.class) {
                return Integer.parseInt(value.toString());
            } else if (targetType == Double.class || targetType == double.class) {
                return Double.parseDouble(value.toString());
            } else if (targetType == BigDecimal.class) {
                return new BigDecimal(value.toString());
            }
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCodeEnum.INVALID_FIELD_VALUE, "字段值格式错误: " + value);
        }

        throw new BusinessException(ErrorCodeEnum.TYPE_MISMATCH, "无法将值转换为 " + targetType.getSimpleName());
    }
}