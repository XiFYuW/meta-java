package com.meta.chain.api.matchmaking;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class MatchmakingMoneyServer {

    public ServiceCharge getServiceCharge(int debit, BigDecimal charge, BigDecimal number){
        if (debit == 0) {
            return new ServiceCharge(
                    number.subtract(charge).setScale(8, RoundingMode.DOWN),
                    charge.setScale(8, RoundingMode.DOWN)
            );
        } else {
            BigDecimal sxf = number.multiply(charge).setScale(8, RoundingMode.DOWN);
            return new ServiceCharge(
                    number.subtract(sxf).setScale(8, RoundingMode.DOWN),
                    sxf
            );
        }
    }

    static class ServiceCharge {
        private final BigDecimal practical;/*实际数量*/
        private final BigDecimal serviceCharge;/*手续费数量*/

        public ServiceCharge(BigDecimal practical, BigDecimal serviceCharge) {
            this.practical = practical;
            this.serviceCharge = serviceCharge;
        }

        public BigDecimal getPractical() {
            return practical;
        }

        public BigDecimal getServiceCharge() {
            return serviceCharge;
        }

        /*实际金额*/
        public BigDecimal getPracticalMoney(BigDecimal intentional, int newScale, RoundingMode roundingMode){
            return practical.multiply(intentional).setScale(newScale, roundingMode);
        }

        /*手续费金额*/
        public BigDecimal getServiceChargeMoney(BigDecimal intentional, int newScale, RoundingMode roundingMode){
            return serviceCharge.multiply(intentional).setScale(newScale, roundingMode);
        }
    }

}
