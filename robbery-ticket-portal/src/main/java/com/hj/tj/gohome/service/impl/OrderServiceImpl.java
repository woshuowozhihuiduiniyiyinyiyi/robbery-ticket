package com.hj.tj.gohome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hj.tj.gohome.config.handler.ServiceException;
import com.hj.tj.gohome.config.handler.ServiceExceptionEnum;
import com.hj.tj.gohome.entity.*;
import com.hj.tj.gohome.enums.BaseStatusEnum;
import com.hj.tj.gohome.enums.OrderStatusEnum;
import com.hj.tj.gohome.mapper.ExpectDateQueryMapper;
import com.hj.tj.gohome.mapper.OrderExpectDateQueryMapper;
import com.hj.tj.gohome.mapper.OrderMapper;
import com.hj.tj.gohome.mapper.PortalUserMapper;
import com.hj.tj.gohome.service.*;
import com.hj.tj.gohome.utils.DateUtil;
import com.hj.tj.gohome.vo.requestVo.*;
import com.hj.tj.gohome.vo.responseVO.OrderResObj;
import com.hj.tj.gohome.vo.responseVO.OrderStatisticDataResObj;
import com.hj.tj.gohome.vo.responseVO.PassengerResObj;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OwnerService ownerService;

    @Resource
    private RobbingTicketUserService robbingTicketUserService;

    @Resource
    private PassengerService passengerService;

    @Resource
    private RelPassengerOrderService relPassengerOrderService;

    @Resource
    private PortalUserMapper portalUserMapper;

    @Resource
    private ExpectDateQueryMapper expectDateQueryMapper;

    @Resource
    private OrderExpectDateQueryMapper orderExpectDateQueryMapper;

    @Override
    public PageInfo<OrderResObj> listOrder(OrderReqObj orderReqObj) {
        List<Integer> queryIdList = new ArrayList<>();
        if (!StringUtils.isEmpty(orderReqObj.getOwnerWxNickName()) || !StringUtils.isEmpty(orderReqObj.getOwnerWxAccount())) {
            getOrderIdsByOwner(orderReqObj, queryIdList);
        }

        if (!StringUtils.isEmpty(orderReqObj.getRobbingUserName())) {
            getIdListByRobbingName(orderReqObj, queryIdList);
        }

        if (!StringUtils.isEmpty(orderReqObj.getPassengerIdCard()) || !StringUtils.isEmpty(orderReqObj.getPassengerName())) {
            getIdListByPassengerInfo(orderReqObj, queryIdList);
        }

        if (Objects.nonNull(orderReqObj.getExpectDateMin()) || Objects.nonNull(orderReqObj.getExpectDateMax())) {
            getIdListByDepartureDate(orderReqObj, queryIdList);
        }

        if (!CollectionUtils.isEmpty(queryIdList)) {
            orderReqObj.setIdList(queryIdList);
        }

        QueryWrapper<Order> orderQueryWrapper = genQueryMapper(orderReqObj);
        Page page = orderReqObj.getPage();
        PageHelper.startPage(page.getPage(), page.getSize(), "id desc");

        List<Order> orders = orderMapper.selectList(orderQueryWrapper);
        PageInfo<Order> pageInfo = new PageInfo<>(orders);

        if (CollectionUtils.isEmpty(orders)) {
            return new PageInfo<>();
        }

        List<Integer> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());

        // 业主信息map
        List<Integer> ownerIds = orders.stream().map(Order::getOwnerId).collect(Collectors.toList());
        Map<Integer, Owner> ownerInfoMap = getOwnerInfoMap(ownerIds);

        // 抢票人员map
        List<Integer> robbingTicketUserIds = orders.stream().map(Order::getRobbingTicketUserId).collect(Collectors.toList());
        Map<Integer, RobbingTicketUser> robbingInfoMap = getRobbingInfoMap(robbingTicketUserIds);

        // 旅客map
        Map<Integer, List<Passenger>> passengerInfoMap = getPassengerInfoMap(orderIds);

        List<OrderResObj> orderResObjList = new ArrayList<>(orders.size());
        for (Order order : orders) {
            OrderResObj orderResObj = convertToResObj(order, ownerInfoMap, robbingInfoMap, passengerInfoMap);

            orderResObjList.add(orderResObj);
        }

        PageInfo<OrderResObj> resultPageInfo = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, resultPageInfo);
        resultPageInfo.setList(orderResObjList);
        return resultPageInfo;
    }

    private void getIdListByDepartureDate(OrderReqObj orderReqObj, List<Integer> queryIdList) {
        QueryWrapper<ExpectDateQuery> queryWrapper = new QueryWrapper<>();

        if (Objects.nonNull(orderReqObj.getExpectDateMax()) && Objects.nonNull(orderReqObj.getExpectDateMin())) {
            queryWrapper.between("expect_date", orderReqObj.getExpectDateMin(), orderReqObj.getExpectDateMax());
        } else if (Objects.nonNull(orderReqObj.getExpectDateMin())) {
            queryWrapper.ge("expect_date", orderReqObj.getExpectDateMin());
        } else if (Objects.nonNull(orderReqObj.getExpectDateMax())) {
            queryWrapper.le("expect_date", orderReqObj.getExpectDateMax());
        }

        List<ExpectDateQuery> expectDateQueries = expectDateQueryMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(expectDateQueries)) {
            queryIdList.add(-1);
            return;
        }

        List<Integer> expectDateQueryIdList = expectDateQueries.stream().map(ExpectDateQuery::getId).collect(Collectors.toList());
        QueryWrapper<OrderExpectDateQuery> orderExpectDateQueryQueryWrapper = new QueryWrapper<>();
        orderExpectDateQueryQueryWrapper.in("expect_date_query_id", expectDateQueryIdList).eq("status", BaseStatusEnum.UN_DELETE.getValue());

        List<OrderExpectDateQuery> orderExpectDateQueries = orderExpectDateQueryMapper.selectList(orderExpectDateQueryQueryWrapper);
        if (CollectionUtils.isEmpty(orderExpectDateQueries)) {
            queryIdList.add(-1);
            return;
        }

        List<Integer> orderIds = orderExpectDateQueries.stream().map(OrderExpectDateQuery::getOrderId).collect(Collectors.toList());
        queryIdList.addAll(orderIds);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Integer saveOrder(OrderInsertReqObj orderInsertReqObj) {
        Order order = genOrder(orderInsertReqObj);
        orderMapper.updateById(order);
        Integer orderId = orderInsertReqObj.getId();

        List<Integer> expectDateQueryIdList = insertToExpectDate(orderInsertReqObj.getExpectDate());
        insertToOrderExpectDateQuery(orderId, expectDateQueryIdList);

        List<Integer> passengerIdList = passengerService.batchSavePassenger(orderInsertReqObj.getPassengerList());
        if (CollectionUtils.isEmpty(passengerIdList)) {
            log.error("[action = `saveOrder`, save passenger error.passengerIdList:{}]", passengerIdList);
            throw new ServiceException(ServiceExceptionEnum.SYS_ERROR);
        }

        // 插入订单旅客关联表
        relPassengerOrderService.saveRelPassengerOrder(passengerIdList, orderId);

        return orderId;
    }

    /**
     * 插入日期查询关联表
     *
     * @param orderId               订单id
     * @param expectDateQueryIdList 日期表id
     */
    private void insertToOrderExpectDateQuery(Integer orderId, List<Integer> expectDateQueryIdList) {
        QueryWrapper<OrderExpectDateQuery> queryQueryWrapper = new QueryWrapper<>();
        queryQueryWrapper.eq("order_id", orderId);

        OrderExpectDateQuery updateRecord = new OrderExpectDateQuery();
        updateRecord.setStatus(BaseStatusEnum.DELETE.getValue());

        orderExpectDateQueryMapper.update(updateRecord, queryQueryWrapper);

        for (Integer expectDateId : expectDateQueryIdList) {
            OrderExpectDateQuery insertRecord = new OrderExpectDateQuery();
            insertRecord.setStatus(BaseStatusEnum.UN_DELETE.getValue());
            insertRecord.setOrderId(orderId);
            insertRecord.setExpectDateQueryId(expectDateId);

            orderExpectDateQueryMapper.insert(insertRecord);
        }
    }

    /**
     * 插入日期表
     *
     * @param expectDateString 用户期待日期字符串
     * @return 所有用户期待日期字符串对应日期表的id
     */
    private List<Integer> insertToExpectDate(String expectDateString) {
        List<Date> expectDateList = new ArrayList<>();
        String[] expectDateStrArr = expectDateString.split("、");
        for (String expectDateStr : expectDateStrArr) {
            Date expectDate = DateUtil.formatStrToDate(expectDateStr, DateUtil.NORMAL_DATE_FORMAT);
            expectDateList.add(expectDate);
        }

        QueryWrapper<ExpectDateQuery> expectDateQueryQueryWrapper = new QueryWrapper<>();
        expectDateQueryQueryWrapper.in("expect_date", expectDateList);

        List<ExpectDateQuery> expectDateQueryList = expectDateQueryMapper.selectList(expectDateQueryQueryWrapper);
        Map<Date, ExpectDateQuery> expectDateQueryMap = expectDateQueryList.stream().collect(Collectors.toMap(ExpectDateQuery::getExpectDate, e -> e));

        List<Integer> expectDateQueryIdList = new ArrayList<>();
        for (Date expectDate : expectDateList) {
            ExpectDateQuery expectDateQuery = expectDateQueryMap.get(expectDate);
            if (Objects.isNull(expectDateQuery)) {
                expectDateQuery = new ExpectDateQuery();
                expectDateQuery.setExpectDate(expectDate);

                expectDateQueryMapper.insert(expectDateQuery);
            }

            expectDateQueryIdList.add(expectDateQuery.getId());
        }

        return expectDateQueryIdList;
    }

    @Override
    public OrderResObj getOrderDetail(Integer id) {
        Order order = orderMapper.selectById(id);
        if (Objects.isNull(order) || Objects.equals(order.getStatus(), OrderStatusEnum.DELETE.getValue())) {
            throw new ServiceException(ServiceExceptionEnum.ORDER_NOT_EXIST);
        }

        // 业主信息map
        List<Integer> ownerIds = Arrays.asList(order.getOwnerId());
        Map<Integer, Owner> ownerInfoMap = getOwnerInfoMap(ownerIds);

        // 抢票人员map
        List<Integer> robbingTicketUserIds = Arrays.asList(order.getRobbingTicketUserId());
        Map<Integer, RobbingTicketUser> robbingInfoMap = getRobbingInfoMap(robbingTicketUserIds);

        // 旅客map
        List<Integer> orderIds = Arrays.asList(id);
        Map<Integer, List<Passenger>> passengerInfoMap = getPassengerInfoMap(orderIds);

        OrderResObj orderResObj = convertToResObj(order, ownerInfoMap, robbingInfoMap, passengerInfoMap);

        orderResObj.setPortalUserName("");
        PortalUser portalUser = portalUserMapper.selectById(order.getPortalUserId());
        if (Objects.nonNull(portalUser)) {
            orderResObj.setPortalUserName(portalUser.getName());
        }

        return orderResObj;
    }

    @Override
    public void deleteOrder(List<Integer> orderIdList) {
        Order order = new Order();
        order.setStatus(OrderStatusEnum.DELETE.getValue());
        order.setUpdatedAt(new Date());

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.in("id", orderIdList);

        orderMapper.update(order, orderQueryWrapper);
    }

    @Override
    public OrderStatisticDataResObj statisticData() {
        OrderStatisticDataResObj orderStatisticDataResObj = new OrderStatisticDataResObj();

        List<Integer> statusList = Arrays.asList(OrderStatusEnum.CLOSED.getValue(), OrderStatusEnum.SUCCESS.getValue(),
                OrderStatusEnum.ROBBING.getValue());

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.in("status", statusList);
        int orderCount = orderMapper.selectCount(orderQueryWrapper);

        orderStatisticDataResObj.setTotalOrderCount(orderCount);

        orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("status", OrderStatusEnum.ROBBING.getValue());
        int robbingCount = orderMapper.selectCount(orderQueryWrapper);

        orderStatisticDataResObj.setRobbingCount(robbingCount);

        orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("status", OrderStatusEnum.SUCCESS.getValue());
        int successCount = orderMapper.selectCount(orderQueryWrapper);

        orderStatisticDataResObj.setSuccessCount(successCount);

        Integer totalProfit = orderMapper.getTotalProfit(statusList);

        if (Objects.isNull(totalProfit)) {
            orderStatisticDataResObj.setTotalProfit(0.0);
        } else {
            orderStatisticDataResObj.setTotalProfit(totalProfit / 100.0);
        }

        return orderStatisticDataResObj;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void refreshQueryDate() {
        List<Order> orders = orderMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }

        for (Order order : orders) {
            if (StringUtils.isEmpty(order.getExpectDate()) && Objects.nonNull(order.getDepartureDate())) {
                Order updateRecord = new Order();
                updateRecord.setId(order.getId());
                updateRecord.setExpectDate(DateUtil.formatDateToString(order.getDepartureDate(), DateUtil.NORMAL_DATE_FORMAT));
                order.setExpectDate(DateUtil.formatDateToString(order.getDepartureDate(), DateUtil.NORMAL_DATE_FORMAT));

                orderMapper.updateById(updateRecord);
            } else if (StringUtils.isEmpty(order.getExpectDate())) {
                continue;
            }

            List<Integer> expectDateIdList = insertToExpectDate(order.getExpectDate());
            insertToOrderExpectDateQuery(order.getId(), expectDateIdList);
        }
    }

    /**
     * 构造订单信息
     *
     * @param orderInsertReqObj 插入订单请求参数
     * @return 订单实体
     */
    private Order genOrder(OrderInsertReqObj orderInsertReqObj) {
        Order order = new Order();
        BeanUtils.copyProperties(orderInsertReqObj, order);

        order.setPrice(0);
        order.setProfit(0);
        if (Objects.nonNull(orderInsertReqObj.getPrice())) {
            order.setPrice((int) (orderInsertReqObj.getPrice().doubleValue() * 100));
            if (Objects.nonNull(orderInsertReqObj.getRobbingPrice())) {
                double profit = orderInsertReqObj.getPrice() - orderInsertReqObj.getRobbingPrice();
                order.setProfit((int) (profit * 100));
            }
        }

        order.setStatus(BaseStatusEnum.UN_DELETE.getValue());
        if (Objects.nonNull(orderInsertReqObj.getStatus())) {
            order.setStatus(orderInsertReqObj.getStatus());
        }

        order.setUpdatedAt(new Date());

        order.setOwnerId(orderInsertReqObj.getOwnerId());

        return order;
    }

    /**
     * 根据订单id 列表来获取对应订单抢票人员信息
     *
     * @param robbingTicketUserIds 抢票人员id 列表
     * @return key：抢票人员Id ， value：抢票人信息
     */
    private Map<Integer, RobbingTicketUser> getRobbingInfoMap(List<Integer> robbingTicketUserIds) {
        if (CollectionUtils.isEmpty(robbingTicketUserIds)) {
            return new HashMap<>();
        }

        RobbingTicketUserReqObj robbingTicketUserReqObj = new RobbingTicketUserReqObj();
        robbingTicketUserReqObj.setRobbingIdList(robbingTicketUserIds);

        List<RobbingTicketUser> robbingTicketUsers = robbingTicketUserService.listRobbingTicketUser(robbingTicketUserReqObj);
        if (CollectionUtils.isEmpty(robbingTicketUsers)) {
            return new HashMap<>();
        }

        return robbingTicketUsers.stream().collect(Collectors.toMap(RobbingTicketUser::getId, r -> r, (m1, m2) -> m1));
    }

    private Map<Integer, List<Passenger>> getPassengerInfoMap(List<Integer> orderIds) {
        RelPassengerOrderReqObj relPassengerOrderReqObj = new RelPassengerOrderReqObj();
        relPassengerOrderReqObj.setOrderIdList(orderIds);

        List<RelPassengerOrder> relPassengerOrders = relPassengerOrderService.listRelPassengerOrder(relPassengerOrderReqObj);
        if (CollectionUtils.isEmpty(relPassengerOrders)) {
            return new HashMap<>();
        }

        List<Integer> passengerIds = relPassengerOrders.stream().map(RelPassengerOrder::getPassengerId).collect(Collectors.toList());

        PassengerReqObj passengerReqObj = new PassengerReqObj();
        passengerReqObj.setIdList(passengerIds);
        List<Passenger> passengers = passengerService.listPassenger(passengerReqObj);

        if (CollectionUtils.isEmpty(passengers)) {
            return new HashMap<>();
        }

        Map<Integer, List<RelPassengerOrder>> relPassengerOrderMap = relPassengerOrders.stream()
                .collect(Collectors.groupingBy(RelPassengerOrder::getOrderId));

        Map<Integer, Passenger> passengerMap = passengers.stream().collect(Collectors.toMap(Passenger::getId, p -> p, (m1, m2) -> m1));

        Map<Integer, List<Passenger>> resultMap = new HashMap<>();

        for (Integer orderId : orderIds) {
            List<RelPassengerOrder> relPassengerOrderList = relPassengerOrderMap.get(orderId);
            if (!CollectionUtils.isEmpty(relPassengerOrderList)) {
                List<Passenger> passengerList = resultMap.get(orderId);
                if (Objects.isNull(passengerList)) {
                    passengerList = new ArrayList<>();
                    resultMap.put(orderId, passengerList);
                }

                for (RelPassengerOrder tempRelPassengerOrder : relPassengerOrderList) {
                    Passenger passenger = passengerMap.get(tempRelPassengerOrder.getPassengerId());

                    if (Objects.nonNull(passenger)) {
                        passengerList.add(passenger);
                    }
                }
            }
        }

        return resultMap;
    }


    /**
     * 根据订单id 列表,来获取对应的业主信息Map
     *
     * @param ownerIds 订单id 列表
     * @return key:订单Id,value:业主信息
     */
    private Map<Integer, Owner> getOwnerInfoMap(List<Integer> ownerIds) {
        OwnerReqObj ownerReqObj = new OwnerReqObj();
        ownerReqObj.setIdList(ownerIds);
        List<Owner> owners = ownerService.listOwner(ownerReqObj);

        if (CollectionUtils.isEmpty(owners)) {
            return new HashMap<>();
        }

        return owners.stream().collect(Collectors.toMap(Owner::getId, o -> o, (m1, m2) -> m1));
    }

    /**
     * 根据抢票用户名来获取该抢票用户的订单Id 列表
     *
     * @param orderReqObj 根据抢票人查询订单Id 列表
     * @param queryIdList 查询成功的订单Id 列表
     */
    private void getIdListByRobbingName(OrderReqObj orderReqObj, List<Integer> queryIdList) {
        RobbingTicketUserReqObj robbingTicketUserReqObj = new RobbingTicketUserReqObj();
        robbingTicketUserReqObj.setName(orderReqObj.getRobbingUserName());

        List<RobbingTicketUser> robbingTicketUsers = robbingTicketUserService.listRobbingTicketUser(robbingTicketUserReqObj);
        if (CollectionUtils.isEmpty(robbingTicketUsers)) {
            queryIdList.add(-1);
            return;
        }

        List<Integer> robbingTicketUserIds = robbingTicketUsers.stream().map(RobbingTicketUser::getId).collect(Collectors.toList());

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.in("robbing_ticket_user_id", robbingTicketUserIds);

        List<Order> orders = orderMapper.selectList(orderQueryWrapper);
        if (CollectionUtils.isEmpty(orders)) {
            queryIdList.add(-1);
            return;
        }

        List<Integer> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());

        queryIdList.addAll(orderIds);
    }

    /**
     * 根据业主用户来获取该业主对应的订单Id 列表
     *
     * @param orderReqObj
     * @param queryIdList
     */
    private void getOrderIdsByOwner(OrderReqObj orderReqObj, List<Integer> queryIdList) {
        OwnerReqObj ownerReqObj = new OwnerReqObj();
        ownerReqObj.setWxAccount(orderReqObj.getOwnerWxAccount());
        ownerReqObj.setWxNickName(orderReqObj.getOwnerWxNickName());
        List<Owner> owners = ownerService.listOwner(ownerReqObj);

        if (CollectionUtils.isEmpty(owners)) {
            queryIdList.add(-1);
            return;
        }

        List<Integer> ownerIds = owners.stream().map(Owner::getId).collect(Collectors.toList());

        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.in("owner_id", ownerIds);

        List<Order> orders = orderMapper.selectList(orderQueryWrapper);
        if (CollectionUtils.isEmpty(orders)) {
            queryIdList.add(-1);
            return;
        }

        List<Integer> orderIds = orders.stream().map(Order::getId).collect(Collectors.toList());

        queryIdList.addAll(orderIds);
    }

    private void getIdListByPassengerInfo(OrderReqObj orderReqObj, List<Integer> queryIdList) {
        List<RelPassengerOrder> relPassengerOrder = getRelPassengerOrder(orderReqObj);

        if (CollectionUtils.isEmpty(relPassengerOrder)) {
            queryIdList.add(-1);
        } else {
            List<Integer> orderIds = relPassengerOrder.stream().map(RelPassengerOrder::getOrderId).collect(Collectors.toList());
            queryIdList.addAll(orderIds);
        }
    }

    private OrderResObj convertToResObj(Order order, Map<Integer, Owner> ownerMap, Map<Integer, RobbingTicketUser> robbingTicketUserMap,
                                        Map<Integer, List<Passenger>> passengerInfoMap) {
        OrderResObj orderResObj = new OrderResObj();
        BeanUtils.copyProperties(order, orderResObj);

        orderResObj.setCreatedAtStr(DateUtil.formatDateNormal(order.getCreatedAt()));
        orderResObj.setDepartureDateStr(DateUtil.formatDateNormal(order.getDepartureDate()));
        orderResObj.setPrice(order.getPrice() / 100.0);
        orderResObj.setProfit(order.getProfit() / 100.0);
        orderResObj.setServicePrice((order.getPrice() - order.getProfit()) / 100.0);
        orderResObj.setRobbingTicketUserId(order.getRobbingTicketUserId());
        orderResObj.setPortalUserId(order.getPortalUserId());
        orderResObj.setExpectDate(order.getExpectDate());

        if (!Objects.equals(order.getProfit(), 0)) {
            orderResObj.setRobbingPrice((order.getPrice() - order.getProfit()) / 100.0);
        }

        orderResObj.setStatus(order.getStatus().intValue());
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getOrderStatusEnumByValue(order.getStatus());
        if (Objects.nonNull(orderStatusEnum)) {
            orderResObj.setStatusStr(orderStatusEnum.getDescription());
        }

        orderResObj.setPassengerList(new ArrayList<>());
        if (Objects.nonNull(passengerInfoMap.get(order.getId()))) {
            List<Passenger> passengerList = passengerInfoMap.get(order.getId());
            List<PassengerResObj> passengerResObjs = new ArrayList<>();
            for (Passenger passenger : passengerList) {
                PassengerResObj passengerResObj = new PassengerResObj();
                passengerResObj.setIdCard(passenger.getIdCard());
                passengerResObj.setName(passenger.getName());
                passengerResObjs.add(passengerResObj);
            }

            orderResObj.setPassengerList(passengerResObjs);
        }


        OwnerResObj ownerResObj = new OwnerResObj();
        ownerResObj.setWxAccount("");
        ownerResObj.setWxNickname("");
        ownerResObj.setPhone(order.getPhone());
        if (Objects.nonNull(ownerMap.get(order.getOwnerId()))) {
            Owner owner = ownerMap.get(order.getOwnerId());
            ownerResObj.setWxAccount(owner.getWxAccount());
            ownerResObj.setWxNickname(owner.getWxNickname());
            ownerResObj.setPhone(owner.getPhone());
            ownerResObj.setId(owner.getId());
        }
        if (!StringUtils.isEmpty(order.getPhone())) {
            ownerResObj.setPhone(order.getPhone());
        }

        orderResObj.setOwnerInfo(ownerResObj);

        orderResObj.setRobbingTicketUserName("");
        if (Objects.nonNull(robbingTicketUserMap.get(order.getRobbingTicketUserId()))) {
            RobbingTicketUser robbingTicketUser = robbingTicketUserMap.get(order.getRobbingTicketUserId());
            orderResObj.setRobbingTicketUserName(robbingTicketUser.getName());
        }

        return orderResObj;
    }

    private QueryWrapper<Order> genQueryMapper(OrderReqObj orderReqObj) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();

        if (Objects.nonNull(orderReqObj.getId())) {
            queryWrapper.eq("id", orderReqObj.getId());
        }

        if (!CollectionUtils.isEmpty(orderReqObj.getIdList())) {
            queryWrapper.in("id", orderReqObj.getIdList());
        }

        if (Objects.nonNull(orderReqObj.getCreatedAtMax()) && Objects.nonNull(orderReqObj.getCreatedAtMin())) {
            queryWrapper.between("created_at", orderReqObj.getCreatedAtMin(), orderReqObj.getCreatedAtMax());
        } else if (Objects.nonNull(orderReqObj.getCreatedAtMin())) {
            queryWrapper.ge("created_at", orderReqObj.getCreatedAtMin());
        } else if (Objects.nonNull(orderReqObj.getCreatedAtMax())) {
            queryWrapper.le("created_at", orderReqObj.getCreatedAtMax());
        }


        if (Objects.nonNull(orderReqObj.getStatus())) {
            queryWrapper.eq("status", orderReqObj.getStatus());
        } else {
            queryWrapper.in("status", Arrays.asList(OrderStatusEnum.ROBBING.getValue(), OrderStatusEnum.SUCCESS.getValue(), OrderStatusEnum.CLOSED.getValue()));
        }

        return queryWrapper;
    }

    private List<RelPassengerOrder> getRelPassengerOrder(OrderReqObj orderReqObj) {
        PassengerReqObj passengerReqObj = new PassengerReqObj();
        passengerReqObj.setIdCard(orderReqObj.getPassengerIdCard());
        passengerReqObj.setName(orderReqObj.getPassengerName());

        List<Passenger> passengers = passengerService.listPassenger(passengerReqObj);

        if (CollectionUtils.isEmpty(passengers)) {
            return new ArrayList<>();
        }

        List<Integer> passengerIdList = passengers.stream().map(Passenger::getId).collect(Collectors.toList());

        RelPassengerOrderReqObj relPassengerOrderReqObj = new RelPassengerOrderReqObj();
        relPassengerOrderReqObj.setPassengerIdList(passengerIdList);

        return relPassengerOrderService.listRelPassengerOrder(relPassengerOrderReqObj);
    }
}
