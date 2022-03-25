package com.meta.chain.api.matchmaking;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.meta.chain.mama.entity.MatchmakingPend;
import com.meta.chain.mama.entity.MatchmakingPendBuy;
import com.meta.chain.mama.entity.MatchmakingPendSale;
import com.meta.chain.mama.service.IMatchmakingPendBuyService;
import com.meta.chain.mama.service.IMatchmakingPendSaleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class MatchmakingAutoServer {

    private final IMatchmakingPendSaleService iMatchmakingPendSaleService;
    private final IMatchmakingPendBuyService iMatchmakingPendBuyService;

    /**
     * 市价撮合交易
     * @param number 委托量
     * @param direction 0.买入 1.卖出
     * @param marketPrice 价格对象
     * @return Z List<MatchmakingPend> !=null:交易成功 ==null:挂单 meet:!=BigDecimal.ZERO:挂单
     */
    public Z autoMarket(BigDecimal number, Integer direction, MatchmakingPriceServer.P marketPrice){
        BigDecimal latestPrice = marketPrice.getPrice();
        BigDecimal meet = checkMarketMeet(number, direction, latestPrice);
        return new Z(
                direction == 0 ? autoMarketSale(meet == null ? number : meet, latestPrice) : autoMarketBuy(meet == null ? number : meet, latestPrice),
                meet == null ? BigDecimal.ZERO : meet
        );
    }

    /**
     * 检查当前交易对待撮合是否可以满足本次交易(市价)
     * @param number 交易数量
     * @param direction 方式 0.买入 1.卖出
     * @param latestPrice 最新价
     * @return 可以全部成交返回null 否则返回未成交数量
     */
    private BigDecimal checkMarketMeet(BigDecimal number, Integer direction, BigDecimal latestPrice){
        return direction == 0 ? iMatchmakingPendSaleService.meetMarket(number, latestPrice) : iMatchmakingPendBuyService.meetMarket(number, latestPrice);
    }

    /**
     * 市价撮合卖出
     * @param number 数量
     * @param latestPrice 最新价
     * @return 成交列表
     */
    private List<MatchmakingPend> autoMarketSale(BigDecimal number, BigDecimal latestPrice){
        List<MatchmakingPendSale> data = iMatchmakingPendSaleService.market(latestPrice);
        if (CollectionUtils.isEmpty(data)) {
            return null;/*挂单*/
        }
        AtomicReference<BigDecimal> done = new AtomicReference<>(number);
        return data
                .stream()
                .filter(x -> autoMarketDeal(done, x))
                .collect(Collectors.toList());
    }

    /**
     * 市价撮合买入
     * @param number 数量
     * @param latestPrice 最新价
     * @return 成交列表
     */
    private List<MatchmakingPend> autoMarketBuy(BigDecimal number, BigDecimal latestPrice){
        List<MatchmakingPendBuy> data = iMatchmakingPendBuyService.market(latestPrice);
        if (CollectionUtils.isEmpty(data)) {
            return null;/*挂单*/
        }
        AtomicReference<BigDecimal> done = new AtomicReference<>(number);
        return data
                .stream()
                .filter(x -> autoMarketDeal(done, x))
                .collect(Collectors.toList());
    }

    /**
     * 市价撮合规则
     * @param done 待撮合数量
     * @param x 撮合对象
     * @return true.被撮合 false.未被撮合
     */
    private boolean autoMarketDeal(AtomicReference<BigDecimal> done, MatchmakingPend x){
        BigDecimal old = done.get();
        if (old.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        if (old.compareTo(x.getAmount()) >= 0) {/*可以完全成交*/
            done.set(old.subtract(x.getAmount()));
            x.setTurnover(x.getAmount());
            x.setDifference(x.getAmount());
            return true;
        }
        /*可以部分成交*/
        BigDecimal difference = x.getAmount().subtract(x.getTurnover());
        done.set(old.subtract(difference));
        x.setTurnover(x.getTurnover().add(difference));
        x.setDifference(difference);
        return true;
    }


    /**
     * 限价撮合交易
     * @param number 委托量
     * @param direction 0.买入 1.卖出
     * @param price 意向价
     * @param marketPrice 价格对象
     * @return Z: List<MatchmakingPend> !=null:交易成功 ==bull:挂单 meet:!=BigDecimal.ZERO:挂单
     */
    public Z autoLimited(BigDecimal number, Integer direction, BigDecimal price, MatchmakingPriceServer.P marketPrice) {
        BigDecimal latestPrice = marketPrice.getPrice();
        AtomicReference<BigDecimal> init = new AtomicReference<>(number);
        BigDecimal meet = checkLimitedMeet(number, direction, latestPrice);
        return new Z(
                direction == 0 ? autoLimitedSale(meet == null ? number : meet, price, latestPrice, init) : autoLimitedBuy(meet == null ? number : meet, price, latestPrice, init),
                meet == null ? BigDecimal.ZERO : meet
        );
    }

    /**
     * 检查当前交易对待撮合是否可以满足本次交易(限价)
     * @param number 交易数量
     * @param direction 方式 0.买入 1.卖出
     * @param latestPrice 最新价
     * @return 可以全部成交返回null 否则返回未成交数量
     */
    private BigDecimal checkLimitedMeet(BigDecimal number, Integer direction, BigDecimal latestPrice){
        return direction == 0 ? iMatchmakingPendSaleService.meetLimited(number, latestPrice) : iMatchmakingPendBuyService.meetLimited(number, latestPrice);
    }

    /**
     * 限价撮合卖出
     * @param number 数量
     * @param price 价格
     * @param latestPrice 最新价
     * @param init 待撮合数量
     * @return 成交列表
     */
    private List<MatchmakingPend> autoLimitedSale(BigDecimal number, BigDecimal price, BigDecimal latestPrice, AtomicReference<BigDecimal> init){
        if (iMatchmakingPendSaleService.count(new LambdaQueryWrapper<MatchmakingPendSale>()
                .eq(MatchmakingPendSale::getStatus, 0)
                .gt(MatchmakingPendSale::getIntentional, price)
                .lt(MatchmakingPendSale::getIntentional, latestPrice)
                .last("limit 1")
        ) != 0) {
            return null;/*挂单*/
        }
        List<MatchmakingPendSale> data = iMatchmakingPendSaleService.limited(number, price);
        if (CollectionUtils.isEmpty(data)) {
            return null;/*挂单*/
        }
        return data.stream().filter(x -> autoLimitedDeal(init, x)).collect(Collectors.toList());
    }

    /**
     * 限价撮合卖买入
     * @param number 数量
     * @param price 价格
     * @param latestPrice 最新价
     * @param init 待撮合数量
     * @return 成交列表
     */
    private List<MatchmakingPend> autoLimitedBuy(BigDecimal number, BigDecimal price, BigDecimal latestPrice, AtomicReference<BigDecimal> init){
        if (iMatchmakingPendBuyService.count(new LambdaQueryWrapper<MatchmakingPendBuy>()
                .eq(MatchmakingPendBuy::getStatus, 0)
                .gt(MatchmakingPendBuy::getIntentional, latestPrice)
                .lt(MatchmakingPendBuy::getIntentional, price)
                .last("limit 1")
        ) != 0) {
            return null;/*挂单*/
        }
        List<MatchmakingPendBuy> data = iMatchmakingPendBuyService.limited(number, price);
        if (CollectionUtils.isEmpty(data)) {
            return null;/*挂单*/
        }
        return data.stream().filter(x -> autoLimitedDeal(init, x)).collect(Collectors.toList());
    }

    /**
     * 限价撮合规则
     * @param init 待撮合数量
     * @param x 撮合对象
     * @return true.被撮合 false.未被撮合
     */
    private boolean autoLimitedDeal(AtomicReference<BigDecimal> init, MatchmakingPend x){
        BigDecimal in = init.get();
        /*剩余交易量 == 0*/
        if (in.compareTo(BigDecimal.ZERO) == 0) {
            return false;
        }
        /*挂单差值 = 挂单量 - 挂单已成交量*/
        BigDecimal difference = x.getAmount().subtract(x.getTurnover());
        /*挂单量 - 挂单已成交量 <= 剩余交易量*/
        if (difference.compareTo(in) <= 0) {
            init.set(in.subtract(difference));
            x.setTurnover(x.getTurnover().add(difference));
            x.setDifference(difference);
            return true;
        }
        /*挂单量 > 剩余交易量*/
        if (x.getAmount().compareTo(in) > 0) {
            init.set(BigDecimal.ZERO);
            x.setTurnover(x.getTurnover().add(in));
            x.setDifference(in);
            return true;
        }
        return false;
    }

    static class Z{
        /*成交列表*/
        private final List<MatchmakingPend> list;
        /*剩余待撮合数量 !=0:剩余待撮合数量 ==null:完全成交*/
        private final BigDecimal meet;

        Z(List<MatchmakingPend> list, BigDecimal meet) {
            this.list = list;
            this.meet = meet;
        }

        public List<MatchmakingPend> getList() {
            return list;
        }

        public BigDecimal getMeet() {
            return meet;
        }
    }
}
