package com.meta.chain.api.matchmaking;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.meta.chain.mama.entity.MatchmakingByDealOrder;
import com.meta.chain.mama.entity.MatchmakingConfig;
import com.meta.chain.mama.entity.MatchmakingDealOrder;
import com.meta.chain.mama.service.IMatchmakingByDealOrderService;
import com.meta.chain.mama.service.IMatchmakingDealOrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class MatchmakingPriceServer {

    private final RedisTemplate<String, Object> redisTemplate;
    private final IMatchmakingByDealOrderService iMatchmakingByDealOrderService;
    private final IMatchmakingDealOrderService iMatchmakingDealOrderService;
    private final static String marketPriceKey = "Matchmaking-Key:";


    public void updateMarketPrice(int direction, BigDecimal price, String alias){
        redisTemplate.opsForValue().set(marketPriceKey + alias, new P(price, alias));
    }

    public BigDecimal getLatestPrice(MatchmakingConfig matchmakingConfig){
        return getMarketPrice(matchmakingConfig).getPrice();
    }

    public P getMarketPrice(MatchmakingConfig matchmakingConfig){
        Object o = redisTemplate.opsForValue().get(marketPriceKey);
        if (o == null) {/*初始化*/
            MatchmakingByDealOrder matchmakingByDealOrder = iMatchmakingByDealOrderService.getOne(new LambdaQueryWrapper<MatchmakingByDealOrder>()
                    .select(MatchmakingByDealOrder::getPrice, MatchmakingByDealOrder::getCreateTime)
                    .orderByDesc(MatchmakingByDealOrder::getId).last("limit 1")
            );
            MatchmakingDealOrder matchmakingDealOrder = iMatchmakingDealOrderService.getOne(new LambdaQueryWrapper<MatchmakingDealOrder>()
                    .select(MatchmakingDealOrder::getPrice, MatchmakingDealOrder::getCreateTime)
                    .orderByDesc(MatchmakingDealOrder::getId).last("limit 1")
            );
            if (matchmakingByDealOrder == null && matchmakingDealOrder == null) {
                return initMarketPrice(matchmakingConfig.getInitPrice(), matchmakingConfig.getAlias());
            } else if (matchmakingByDealOrder != null && matchmakingDealOrder == null) {
                return initMarketPrice(matchmakingByDealOrder.getPrice(), matchmakingConfig.getAlias());
            } else if (matchmakingDealOrder != null && matchmakingByDealOrder == null) {
                return initMarketPrice(matchmakingDealOrder.getPrice(), matchmakingConfig.getAlias());
            } else {
                if (matchmakingByDealOrder.getCreateTime().compareTo(matchmakingDealOrder.getCreateTime()) < 0) {
                    return initMarketPrice(matchmakingDealOrder.getPrice(), matchmakingConfig.getAlias());
                }
                return initMarketPrice(matchmakingByDealOrder.getPrice(), matchmakingConfig.getAlias());
            }
        }
        JSONObject jsonObject = (JSONObject) o;
        return jsonObject.toJavaObject(P.class);
    }

    private P initMarketPrice(BigDecimal price, String alias){
        P p = new P(price, alias);
        redisTemplate.opsForValue().set(marketPriceKey + alias, p);
        return p;
    }

    static class P implements Serializable {
        /*当前价*/
        private final BigDecimal price;

        /*别名(卖出对名称/买入对名称)*/
        private final String alias;

        public P(BigDecimal price, String alias) {
            this.price = price;
            this.alias = alias;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public String getAlias() {
            return alias;
        }
    }
}
