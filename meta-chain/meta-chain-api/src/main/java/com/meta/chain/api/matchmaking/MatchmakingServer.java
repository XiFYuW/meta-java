package com.meta.chain.api.matchmaking;

import cn.hutool.core.date.DateUtil;
import com.meta.chain.api.matchmaking.dto.MatchmakingDealDTO;
import com.meta.chain.mama.entity.*;
import com.meta.chain.mama.service.IMatchmakingByDealOrderService;
import com.meta.chain.mama.service.IMatchmakingDealOrderService;
import com.meta.chain.mama.service.IMatchmakingPendBuyService;
import com.meta.chain.mama.service.IMatchmakingPendSaleService;
import com.meta.module.common.exception.HintRuntimeException;
import com.meta.module.common.result.ResponseResult;
import com.meta.module.common.result.ResponseResultUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MatchmakingServer {

    private final MatchmakingAutoServer matchmakingAutoServer;
    private final MatchmakingMoneyServer matchmakingMoneyServer;
    private final IMatchmakingDealOrderService iMatchmakingDealOrderService;
    private final IMatchmakingByDealOrderService iMatchmakingByDealOrderService;
    private final IMatchmakingPendBuyService iMatchmakingPendBuyService;
    private final IMatchmakingPendSaleService iMatchmakingPendSaleService;
    private final MatchmakingPriceServer matchmakingPriceServer;

    /**
     * 买入动作(扣除买入对:获得卖出对 : GALA/U:卖出对/买入对)：
     *  1.检查买入总金额
     *  2.获取当前最新价
     *  3.限价
     *      3.1.获取成交对列表
     *          3.1.1.成交
     *                  3.1.1.1.交易方一次性扣除买入对
     *                  3.1.1.2.生成订单
     *                          3.1.1.2.1.生产交易方订单(买入订单)
     *                                      3.1.1.2.1.1.获取MatchmakingMoneyServer.ServiceCharge对象。参数：1.买入对 2.成交差值数量
     *                                      3.1.1.2.1.2.归纳交易方手续费(扣除数量)
     *                                      3.1.1.2.1.3.交易方获得卖出对(数量)
     *                                      3.1.1.2.1.4.生成交易方订单
     *                          3.1.1.2.2.生成被交易方订单(卖出订单)
     *                                      3.1.1.2.2.1.生成被交易方用户信息
     *                                      3.1.1.2.2.2.获取MatchmakingMoneyServer.ServiceCharge对象。参数：1.卖出对 2.成交差值数量
     *                                      3.1.1.2.2.3.归纳被交易方手续费(扣除金额)
     *                                      3.1.1.2.2.4.挂单被完全成交
     *                                      3.1.1.2.2.5.挂单被部分成交
     *                                      3.1.1.2.2.6.被交易方扣除冻结(卖出对)
     *                                      3.1.1.2.2.7.被交易方获得买入对(金额)
     *                                      3.1.1.2.2.8.生成被交易方订单
     *                                      3.1.1.2.2.9.更新最新价
     *                                      3.1.1.2.2.10.剩余未成交去挂单
     *          3.2.2.挂单
     *                 3.2.2.1.交易方冻结买入对
     *                 3.2.2.2.生成买入挂单记录
     *  4.市价
     *      4.1.获取成交对列表
     *      4.2.交易方一次性扣除买入对
     *      4.3.生成订单(3.1.1.2.生成订单)
     *      4.4.更新最新价
     *      4.5.剩余未成交去挂单
     * @param matchmakingDealDTO 搓合交易参数
     * @param userId 用户信息
     * @param matchmakingConfig 搓合交易配置
     * @return ResponseResult
     */
    @Transactional
    public ResponseResult buildBuy(MatchmakingDealDTO matchmakingDealDTO, String userId, MatchmakingConfig matchmakingConfig) {
        Date time = DateUtil.date();
        String orderNo = "123456789";
        /*检查买入总金额*/
        //BigDecimal userMoney = FcStrategyEnum.getCyBerUserServiceByInstruct(matchmakingConfig.getBuyInstruct()).getUserMoney(userId);
        BigDecimal userMoney = BigDecimal.ZERO;
        BigDecimal money = matchmakingDealDTO.getMoney().multiply(matchmakingDealDTO.getNumber()).setScale(8, RoundingMode.DOWN);
        if (userMoney.compareTo(money) < 0) {
            throw new HintRuntimeException("所需数量不足。");
        }
        /*获取当前最新价*/
        MatchmakingPriceServer.P marketPrice = matchmakingPriceServer.getMarketPrice(matchmakingConfig);
        if (matchmakingDealDTO.getMode() == 0) {/*限价*/
            /*获取成交对列表*/
            MatchmakingAutoServer.Z z = matchmakingAutoServer.autoLimited(matchmakingDealDTO.getNumber(), matchmakingDealDTO.getWay(), matchmakingDealDTO.getMoney(), marketPrice);
            List<MatchmakingPend> matchmakingPendList = z.getList();
            if (!CollectionUtils.isEmpty(matchmakingPendList)) /*成交*/{

                /*交易方一次性扣除买入对*/
//                FcStrategyEnum.getCyBerUserServiceByInstruct(matchmakingConfig.getBuyInstruct()).saveMoney(userId, money, null, 0,
//                        () -> FcStrategyEnum.getCyBerServiceByInstruct(matchmakingConfig.getBuyInstruct()).saveRecord(
//                                subscribers,
//                                "交易扣除[" + matchmakingConfig.getAlias() + "]",
//                                money,
//                                "",
//                                orderNo,
//                                1,
//                                0,
//                                time
//                        ));


                /*生成订单*/
                generateBuyDealOrder(matchmakingPendList, matchmakingConfig, userId, orderNo, time);

                /*更新最新价*/
                matchmakingPriceServer.updateMarketPrice(matchmakingDealDTO.getWay(), matchmakingDealDTO.getMoney(), matchmakingConfig.getAlias());

                /*剩余未成交去挂单*/
                if (z.getMeet().compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal moneyMeet = matchmakingDealDTO.getMoney().multiply(z.getMeet()).setScale(8, RoundingMode.DOWN);
                    matchmakingDealDTO.setNumber(z.getMeet());
                    generateBuyMakeOrder(matchmakingConfig, userId, matchmakingDealDTO, moneyMeet, orderNo, time);
                }
                return ResponseResultUtils.getResponseResultS("交易成功。");
            }
            else /*挂单*/{
                generateBuyMakeOrder(matchmakingConfig, userId, matchmakingDealDTO, money, orderNo, time);
                return ResponseResultUtils.getResponseResultS("挂单成功。");
            }
        }
        /*市价*/
        /*获取成交对列表*/
        MatchmakingAutoServer.Z z = matchmakingAutoServer.autoMarket(matchmakingDealDTO.getNumber(), matchmakingDealDTO.getWay(), marketPrice);
        List<MatchmakingPend> matchmakingPendList = z.getList();
        if (CollectionUtils.isEmpty(matchmakingPendList)) {/*挂单*/
            generateBuyMakeOrder(matchmakingConfig, userId, matchmakingDealDTO, money, orderNo, time);
        }
        /*生成订单*/
        generateBuyDealOrder(matchmakingPendList, matchmakingConfig, userId, orderNo, time);
        /*更新最新价*/
        MatchmakingPend matchmakingPend = matchmakingPendList.get(matchmakingPendList.size() - 1);
        matchmakingPriceServer.updateMarketPrice(matchmakingDealDTO.getWay(), matchmakingPend.getIntentional(), matchmakingConfig.getAlias());
        /*剩余未成交去挂单*/
        if (z.getMeet().compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal moneyMeet = matchmakingDealDTO.getMoney().multiply(z.getMeet()).setScale(8, RoundingMode.DOWN);
            matchmakingDealDTO.setNumber(z.getMeet());
            generateBuyMakeOrder(matchmakingConfig, userId, matchmakingDealDTO, moneyMeet, orderNo, time);
        }
        return ResponseResultUtils.getResponseResultS("交易成功。");
    }

    /**
     * 买入生成挂单
     * @param matchmakingConfig 撮合交易配置
     * @param userId 用户信息
     * @param matchmakingDealDTO 撮合参数
     * @param orderNo 订单号
     * @param time 时间
     */
    private void generateBuyMakeOrder(MatchmakingConfig matchmakingConfig,
                                      String userId,
                                      MatchmakingDealDTO matchmakingDealDTO,
                                      BigDecimal money,
                                      String orderNo,
                                      Date time){
        /*交易方冻结买入对*/
//        FcStrategyEnum.getCyBerUserServiceByInstruct(matchmakingConfig.getBuyInstruct())
//                .saveMoney(subscribers.getUserId(), money, new HashMap<>(){{
//                            put("dMoney", money);
//                        }}, 0,
//                        () -> FcStrategyEnum.getCyBerServiceByInstruct(matchmakingConfig.getBuyInstruct()).saveRecord(
//                                subscribers,
//                                "交易冻结[" + matchmakingConfig.getAlias() + "]",
//                                money,
//                                "",
//                                orderNo,
//                                3,
//                                0,
//                                time
//                        ));

        /*生成买入挂单记录*/
        MatchmakingPendBuy matchmakingPendBuy = new MatchmakingPendBuy();
        matchmakingPendBuy.setUid(userId);
        matchmakingPendBuy.setCreateTime(time);
        matchmakingPendBuy.setMode(0);
        matchmakingPendBuy.setOrderNo(orderNo);
        matchmakingPendBuy.setStatus(0);
        matchmakingPendBuy.setAmount(matchmakingDealDTO.getNumber());
        matchmakingPendBuy.setMoney(money);
        matchmakingPendBuy.setIntentional(matchmakingDealDTO.getMoney());
        matchmakingPendBuy.setFreeze(money);
        iMatchmakingPendBuyService.save(matchmakingPendBuy);
    }

    /**
     * 买入生成交易单
     * @param matchmakingPendList 成交列表
     * @param matchmakingConfig 撮合交易配置
     * @param userId 用户信息
     * @param orderNo 订单号
     * @param time 时间
     */
    private void generateBuyDealOrder(List<MatchmakingPend> matchmakingPendList,
                                      MatchmakingConfig matchmakingConfig,
                                      String userId,
                                      String orderNo,
                                      Date time
    ){
        /*生产交易方订单*/
        iMatchmakingDealOrderService.saveBatch(matchmakingPendList
                .stream()
                .map(x -> {
                    /*具体的交易数量*/
                    MatchmakingMoneyServer.ServiceCharge serviceCharge = matchmakingMoneyServer.getServiceCharge(
                            matchmakingConfig.getBuyDebit(),
                            matchmakingConfig.getBuyCharge(),
                            x.getDifference()
                    );
                    /*归纳交易方手续费(扣除卖出对数量)*/

                    /*交易方获得卖出对(数量)*/
//                    FcStrategyEnum.getCyBerUserServiceByInstruct(matchmakingConfig.getSaleInstruct()).saveMoney(subscribers.getUserId(), serviceCharge.getPractical(), null, 1,
//                            () -> FcStrategyEnum.getCyBerServiceByInstruct(matchmakingConfig.getSaleInstruct()).saveRecord(
//                                    subscribers,
//                                    "交易增加[" + matchmakingConfig.getAlias() + "]",
//                                    serviceCharge.getPractical(),
//                                    "",
//                                    orderNo,
//                                    1,
//                                    1,
//                                    time
//                            ));

                    /*生成交易方订单*/
                    return MatchmakingDealOrder
                            .builder()
                            .orderNo(orderNo)
                            .createTime(time)
                            .turnover(x.getDifference())
                            .charge(serviceCharge.getServiceCharge())
                            .form(0)
                            .uid(userId)
                            .money(x.getDifference().multiply(x.getIntentional()).setScale(8, RoundingMode.DOWN))
                            .price(x.getIntentional())
                            .build();
                })
                .collect(Collectors.toList())
        );

        /*生成被交易方订单*/
        iMatchmakingByDealOrderService.saveBatch(matchmakingPendList
                .stream()
                .map(x -> {
                    /*生成被交易方用户信息*/
//                    Subscribers subscribersSale = new Subscribers();
//                    subscribersSale.setUserId(x.getUid());
//                    subscribersSale.setEmail(x.getMail());
//                    subscribersSale.setMobile(x.getMobile());
                    /*具体的交易数量*/
                    MatchmakingMoneyServer.ServiceCharge serviceCharge = matchmakingMoneyServer.getServiceCharge(
                            matchmakingConfig.getSaleDebit(),
                            matchmakingConfig.getSaleCharge(),
                            x.getDifference()
                    );
                    BigDecimal incomeMoney = serviceCharge.getPracticalMoney(x.getIntentional(), 8, RoundingMode.DOWN);
                    BigDecimal incomeChargeMoney = serviceCharge.getServiceChargeMoney(x.getIntentional(), 8, RoundingMode.DOWN);
                    /*归纳被交易方手续费(金额)*/

                    /*挂单被完全成交*/
                    if (x.getAmount().compareTo(x.getTurnover()) == 0) {
                        iMatchmakingPendSaleService.completely(time, x);
                    }

                    /*挂单被部分成交*/
                    //189  111 78
                    if (x.getTurnover().compareTo(BigDecimal.ZERO) >= 0 && x.getTurnover().compareTo(x.getAmount()) < 0) {
                        iMatchmakingPendSaleService.some(time, x);
                    }
                    if (x.getTurnover().compareTo(BigDecimal.ZERO) >= 0 && x.getTurnover().compareTo(x.getAmount()) > 0) {
                        iMatchmakingPendSaleService.some(time, x);
                    }

                    /*被交易方扣除冻结(卖出对)*/
//                    FcStrategyEnum.getCyBerUserServiceByInstruct(matchmakingConfig.getSaleInstruct())
//                            .saveMoney(subscribersSale.getUserId(), BigDecimal.ZERO, new HashMap<>(){{
//                                        put("dMoney", x.getFreeze().negate());
//                                    }}, 0,
//                                    () -> {});


                    /*被交易方获得买入对(金额)*/
//                    FcStrategyEnum.getCyBerUserServiceByInstruct(matchmakingConfig.getBuyInstruct()).saveMoney(subscribersSale.getUserId(), incomeMoney, null, 1,
//                            () -> FcStrategyEnum.getCyBerServiceByInstruct(matchmakingConfig.getBuyInstruct()).saveRecord(
//                                    subscribersSale,
//                                    "卖出增加[" + matchmakingConfig.getAlias() + "]",
//                                    incomeMoney,
//                                    "",
//                                    orderNo,
//                                    1,
//                                    1,
//                                    time
//                            ));

                    /*生成被交易方订单*/
                    return MatchmakingByDealOrder
                            .builder()
                            .dealOrderNo(orderNo)
                            .createTime(time)
                            .turnover(x.getDifference())
                            .charge(incomeChargeMoney)
                            .uid(x.getUid())
                            .money(x.getDifference().multiply(x.getIntentional()).setScale(8,RoundingMode.DOWN))
                            .price(x.getIntentional())
                            .makeId(x.getId())
                            .build();
                })
                .collect(Collectors.toList())
        );
    }

    /**
     * 卖出动作(扣除卖出对:获得买入对 : GALA/U:卖出对/买入对)：
     *  1.检查买入总金额
     *  2.获取当前最新价
     *  3.限价
     *      3.1.获取成交对列表
     *          3.1.1.成交
     *                  3.1.1.1.交易方一次性扣除卖出对
     *                  3.1.1.2.生成订单
     *                          3.1.1.2.1.生产交易方订单(卖出订单)
     *                                    3.1.1.2.1.1.获取MatchmakingMoneyServer.ServiceCharge对象。参数：1.卖出对 2.成交差值数量
     *                                    3.1.1.2.1.2.归纳交易方手续费(扣除金额)
     *                                    3.1.1.2.1.3.交易方获得买入对(金额)
     *                                    3.1.1.2.1.4.生成交易方订单
     *                          3.1.1.2.2.生成被交易方订单(买入订单)
     *                                    3.1.1.2.2.1.生成被交易方用户信息
     *                                    3.1.1.2.2.2.获取MatchmakingMoneyServer.ServiceCharge对象。参数：1.买入对 2.成交差值数量
     *                                    3.1.1.2.2.3.归纳被交易方手续费(数量)
     *                                    3.1.1.2.2.4.挂单被完全成交
     *                                    3.1.1.2.2.5.挂单被部分成交
     *                                    3.1.1.2.2.6.被交易方扣除冻结(买入对)
     *                                    3.1.1.2.2.7.被交易方获得卖出对(数量)
     *                                    3.1.1.2.2.8.生成被交易方订单
     *                                    3.1.1.2.2.9.更新最新价
     *                                    3.1.1.2.2.10.剩余未成交去挂单
     *          2.2.2.挂单
     *                 2.2.2.1.交易方冻结卖出对
     *                 2.2.2.2.生成卖出挂单记录
     *  4.市价
     *      4.1.获取成交对列表
     *      4.2.交易方一次性扣除卖出对
     *      4.3.生成订单(3.1.1.2.生成订单)
     *      4.4.更新最新价
     *      4.5.剩余未成交去挂单
     * @param matchmakingDealDTO 搓合交易参数
     * @param userId 用户信息
     * @param matchmakingConfig 搓合交易配置
     * @return ResponseResult
     */
    @Transactional
    public ResponseResult buildSale(MatchmakingDealDTO matchmakingDealDTO, String userId, MatchmakingConfig matchmakingConfig) {
        Date time = DateUtil.date();
        String orderNo = "OrderGenContext.orderNo()";
        //BigDecimal userMoney = FcStrategyEnum.getCyBerUserServiceByInstruct(matchmakingConfig.getSaleInstruct()).getUserMoney(subscribers.getUserId());
        BigDecimal userMoney = BigDecimal.ZERO;
        if (userMoney.compareTo(matchmakingDealDTO.getNumber()) < 0) {
            throw new HintRuntimeException("所需数量不足。");
        }
        /*获取当前最新价*/
        MatchmakingPriceServer.P marketPrice = matchmakingPriceServer.getMarketPrice(matchmakingConfig);
        if (matchmakingDealDTO.getMode() == 0) {/*限价*/
            /*获取成交对列表*/
            MatchmakingAutoServer.Z z = matchmakingAutoServer.autoLimited(matchmakingDealDTO.getNumber(), matchmakingDealDTO.getWay(), matchmakingDealDTO.getMoney(), marketPrice);
            List<MatchmakingPend> matchmakingPendList = z.getList();
            if (!CollectionUtils.isEmpty(matchmakingPendList)) /*成交*/{

                /*交易方一次性扣除卖出对*/
//                FcStrategyEnum.getCyBerUserServiceByInstruct(matchmakingConfig.getSaleInstruct()).saveMoney(subscribers.getUserId(), matchmakingDealDTO.getNumber(), null, 0,
//                        () -> FcStrategyEnum.getCyBerServiceByInstruct(matchmakingConfig.getSaleInstruct()).saveRecord(
//                                subscribers,
//                                "交易扣除[" + matchmakingConfig.getAlias() + "]",
//                                matchmakingDealDTO.getNumber(),
//                                "",
//                                orderNo,
//                                1,
//                                0,
//                                time
//                        ));


                /*生成订单*/
                generateSaleDealOrder(matchmakingPendList, matchmakingConfig, userId, orderNo, time);

                /*更新最新价*/
                matchmakingPriceServer.updateMarketPrice(matchmakingDealDTO.getWay(), matchmakingDealDTO.getMoney(), matchmakingConfig.getAlias());

                /*剩余未成交去挂单*/
                if (z.getMeet().compareTo(BigDecimal.ZERO) != 0) {
                    matchmakingDealDTO.setNumber(z.getMeet());
                    generateSaleMakeOrder(matchmakingConfig, userId, matchmakingDealDTO, orderNo, time);
                }
                return ResponseResultUtils.getResponseResultS("交易成功。");
            }
            else /*挂单*/{
                generateSaleMakeOrder(matchmakingConfig, userId, matchmakingDealDTO, orderNo, time);
                return ResponseResultUtils.getResponseResultS("挂单成功。");
            }
        }
        /*市价*/
        /*获取成交对列表*/
        MatchmakingAutoServer.Z z = matchmakingAutoServer.autoMarket(matchmakingDealDTO.getNumber(), matchmakingDealDTO.getWay(), marketPrice);
        List<MatchmakingPend> matchmakingPendList = z.getList();
        if (CollectionUtils.isEmpty(matchmakingPendList)) {/*挂单*/
            generateSaleMakeOrder(matchmakingConfig, userId, matchmakingDealDTO, orderNo, time);
        }
        /*交易方一次性扣除卖出对*/
//        FcStrategyEnum.getCyBerUserServiceByInstruct(matchmakingConfig.getSaleInstruct()).saveMoney(subscribers.getUserId(), matchmakingDealDTO.getNumber(), null, 0,
//                () -> FcStrategyEnum.getCyBerServiceByInstruct(matchmakingConfig.getSaleInstruct()).saveRecord(
//                        subscribers,
//                        "交易扣除[" + matchmakingConfig.getAlias() + "]",
//                        matchmakingDealDTO.getNumber(),
//                        "",
//                        orderNo,
//                        1,
//                        0,
//                        time
//                ));
        /*生成订单*/
        generateSaleDealOrder(matchmakingPendList, matchmakingConfig, userId, orderNo, time);
        /*更新最新价*/
        MatchmakingPend matchmakingPend = matchmakingPendList.get(matchmakingPendList.size() - 1);
        matchmakingPriceServer.updateMarketPrice(matchmakingDealDTO.getWay(), matchmakingPend.getIntentional(), matchmakingConfig.getAlias());
        /*剩余未成交去挂单*/
        if (z.getMeet().compareTo(BigDecimal.ZERO) != 0) {
            matchmakingDealDTO.setNumber(z.getMeet());
            generateSaleMakeOrder(matchmakingConfig, userId, matchmakingDealDTO, orderNo, time);
        }
        return ResponseResultUtils.getResponseResultS("交易成功。");
    }


    /**
     * 卖出生成挂单
     * @param matchmakingConfig 撮合交易配置
     * @param userId 用户信息
     * @param matchmakingDealDTO 撮合参数
     * @param orderNo 订单号
     * @param time 时间
     */
    private void generateSaleMakeOrder(MatchmakingConfig matchmakingConfig,
                                       String userId,
                                       MatchmakingDealDTO matchmakingDealDTO,
                                       String orderNo,
                                       Date time
    ){
        /*交易方冻结卖出对*/
//        FcStrategyEnum.getCyBerUserServiceByInstruct(matchmakingConfig.getSaleInstruct())
//                .saveMoney(subscribers.getUserId(), matchmakingDealDTO.getNumber(), new HashMap<>(){{
//                            put("dMoney", matchmakingDealDTO.getNumber());
//                        }}, 0,
//                        () -> FcStrategyEnum.getCyBerServiceByInstruct(matchmakingConfig.getSaleInstruct()).saveRecord(
//                                subscribers,
//                                "交易冻结[" + matchmakingConfig.getAlias() + "]",
//                                matchmakingDealDTO.getNumber(),
//                                "",
//                                orderNo,
//                                3,
//                                0,
//                                time
//                        ));

        /*生成卖出挂单记录*/
        MatchmakingPendSale matchmakingPendSale = new MatchmakingPendSale();
        matchmakingPendSale.setUid(userId);
        matchmakingPendSale.setCreateTime(time);
        matchmakingPendSale.setMode(matchmakingDealDTO.getMode());
        matchmakingPendSale.setOrderNo(orderNo);
        matchmakingPendSale.setStatus(0);
        matchmakingPendSale.setAmount(matchmakingDealDTO.getNumber());
        matchmakingPendSale.setMoney(matchmakingDealDTO.getNumber().multiply(matchmakingDealDTO.getMoney()).setScale(8, RoundingMode.DOWN));
        matchmakingPendSale.setIntentional(matchmakingDealDTO.getMoney());
        matchmakingPendSale.setFreeze(matchmakingDealDTO.getNumber());
        iMatchmakingPendSaleService.save(matchmakingPendSale);
    }


    /**
     * 卖出生成交易单
     * @param matchmakingPendList 成交列表
     * @param matchmakingConfig 撮合交易配置
     * @param userId 用户信息
     * @param orderNo 订单号
     * @param time 时间
     */
    private void generateSaleDealOrder(List<MatchmakingPend> matchmakingPendList,
                                       MatchmakingConfig matchmakingConfig,
                                       String userId,
                                       String orderNo,
                                       Date time
    ){
        /*生成交易方订单*/
        iMatchmakingDealOrderService.saveBatch(matchmakingPendList
                .stream()
                .map(x -> {
                    /*具体的交易数量*/
                    MatchmakingMoneyServer.ServiceCharge serviceCharge = matchmakingMoneyServer.getServiceCharge(
                            matchmakingConfig.getSaleDebit(),
                            matchmakingConfig.getSaleCharge(),
                            x.getDifference()
                    );
                    /*归纳交易方手续费(扣除金额)*/
                    /*交易方获得买入对(金额)*/
                    BigDecimal incomeMoney = serviceCharge.getPracticalMoney(x.getIntentional(), 8, RoundingMode.DOWN);
                    BigDecimal incomeChargeMoney = serviceCharge.getServiceChargeMoney(x.getIntentional(), 8, RoundingMode.DOWN);
//                    FcStrategyEnum.getCyBerUserServiceByInstruct(matchmakingConfig.getBuyInstruct()).saveMoney(subscribers.getUserId(), incomeMoney, null, 1,
//                            () -> FcStrategyEnum.getCyBerServiceByInstruct(matchmakingConfig.getBuyInstruct()).saveRecord(
//                                    subscribers,
//                                    "交易增加[" + matchmakingConfig.getAlias() + "]",
//                                    incomeMoney,
//                                    "",
//                                    orderNo,
//                                    1,
//                                    1,
//                                    time
//                            ));

                    /*生产交易方订单*/
                    return MatchmakingDealOrder
                            .builder()
                            .orderNo(orderNo)
                            .createTime(time)
                            .turnover(x.getDifference())
                            .charge(incomeChargeMoney)
                            .form(1)
                            .uid(userId)
                            .money(x.getDifference().multiply(x.getIntentional()).setScale(8, RoundingMode.DOWN))
                            .price(x.getIntentional())
                            .build();
                })
                .collect(Collectors.toList())
        );

        /*生成被交易方订单*/
        iMatchmakingByDealOrderService.saveBatch(matchmakingPendList
                .stream()
                .map(x -> {
                    /*生成被交易方用户信息*/
//                    Subscribers subscribersSale = new Subscribers();
//                    subscribersSale.setUserId(x.getUid());
//                    subscribersSale.setEmail(x.getMail());
//                    subscribersSale.setMobile(x.getMobile());
                    /*具体的交易数量*/
                    MatchmakingMoneyServer.ServiceCharge serviceCharge = matchmakingMoneyServer.getServiceCharge(
                            matchmakingConfig.getBuyDebit(),
                            matchmakingConfig.getBuyCharge(),
                            x.getDifference()
                    );
                    /*归纳被交易方手续费(数量)*/

                    /*挂单被完全成交*/
                    if (x.getAmount().compareTo(x.getTurnover()) == 0) {
                        iMatchmakingPendBuyService.completely(time, x);
                    }

                    /*挂单被部分成交*/
                    if (x.getTurnover().compareTo(BigDecimal.ZERO) >= 0 && x.getTurnover().compareTo(x.getAmount()) < 0) {
                        iMatchmakingPendBuyService.some(time, x);
                    }

                    /*被交易方扣除冻结(买入对)*/
//                    FcStrategyEnum.getCyBerUserServiceByInstruct(matchmakingConfig.getBuyInstruct())
//                            .saveMoney(subscribersSale.getUserId(), BigDecimal.ZERO, new HashMap<>(){{
//                                        put("dMoney", x.getFreeze().negate());
//                                    }}, 0,
//                                    () -> {});
                    /*被交易方获得卖出对(数量)*/
//                    FcStrategyEnum.getCyBerUserServiceByInstruct(matchmakingConfig.getSaleInstruct()).saveMoney(subscribersSale.getUserId(), serviceCharge.getPractical(), null, 1,
//                            () -> FcStrategyEnum.getCyBerServiceByInstruct(matchmakingConfig.getSaleInstruct()).saveRecord(
//                                    subscribersSale,
//                                    "卖出增加[" + matchmakingConfig.getAlias() + "]",
//                                    serviceCharge.getPractical(),
//                                    "",
//                                    orderNo,
//                                    1,
//                                    1,
//                                    time
//                            ));

                    /*生成被交易方订单*/
                    return MatchmakingByDealOrder
                            .builder()
                            .dealOrderNo(orderNo)
                            .createTime(time)
                            .turnover(x.getDifference())
                            .charge(serviceCharge.getServiceCharge())
                            .uid(x.getUid())
                            .money(x.getDifference().multiply(x.getIntentional()).setScale(8, RoundingMode.DOWN))
                            .price(x.getIntentional())
                            .makeId(x.getId())
                            .build();
                })
                .collect(Collectors.toList())
        );
    }
}
