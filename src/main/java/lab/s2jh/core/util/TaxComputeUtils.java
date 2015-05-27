package lab.s2jh.core.util;

import java.math.BigDecimal;

/**
 * 缴税金额计算器工具
 * 
 * @author Administrator
 *
 */
public class TaxComputeUtils {

    public static BigDecimal POINT_2 = new BigDecimal(0.2);
    public static BigDecimal POINT_3 = new BigDecimal(0.3);
    public static BigDecimal POINT_4 = new BigDecimal(0.4);
    public static BigDecimal POINT_8 = new BigDecimal(0.8);
    public static BigDecimal HUNDRED_8 = new BigDecimal(800);
    public static BigDecimal THOUSAND_4 = new BigDecimal(4000);
    public static BigDecimal THOUSAND_2 = new BigDecimal(2000);
    public static BigDecimal THOUSAND_7 = new BigDecimal(7000);
    public static BigDecimal THOUSAND_20 = new BigDecimal(20000);
    public static BigDecimal THOUSAND_50 = new BigDecimal(50000);

    /**
     * 劳务报酬个税计算
     * 
     * @param totalAmountBeforeTaxMonthly 当月本次之前已结算税前总金额
     * @param amountBeforeTax 本次应结算金额[税前]
     * @return
     */
    public static BigDecimal remunerationTax(BigDecimal totalAmountBeforeTaxMonthly, BigDecimal amountBeforeTax) {

        //本次实际结算金额
        BigDecimal amountAfterTax = BigDecimal.ZERO;

        //本次应缴税金额
        BigDecimal amountTaxThisTime = BigDecimal.ZERO;

        //1.获取本月税前应结报酬金额 [:= 当月本次之前已结税前金额 + 本次结算税前金额 ]
        totalAmountBeforeTaxMonthly = NumberUtils.add(totalAmountBeforeTaxMonthly, amountBeforeTax);

        //2.判断当月累计报酬金额是否大于4000
        if (NumberUtils.compare(NumberUtils.sub(totalAmountBeforeTaxMonthly, THOUSAND_4), BigDecimal.ZERO) > 0) {
            //2.1)大于4000
            BigDecimal amountNeedTaxThisTime = BigDecimal.ZERO;
            //计算本次应纳所得税金额 [:= 当月报酬总金额 * (1-0.2) ]
            amountNeedTaxThisTime = NumberUtils.mul(totalAmountBeforeTaxMonthly, POINT_8);

            //3.判断本次应纳所得税额档次,并 计算本次应纳税额
            if (NumberUtils.compare(new BigDecimal(20000), amountNeedTaxThisTime) >= 0) {
                //3.1 第一档:应纳所得税额 <= 20000,缴税金额计算 [:= 应纳所得税金额 *0.2]
                amountTaxThisTime = NumberUtils.mul(amountNeedTaxThisTime, POINT_2);

            } else if (NumberUtils.compare(amountNeedTaxThisTime, THOUSAND_20) > 0 && NumberUtils.compare(THOUSAND_50, amountNeedTaxThisTime) > 0) {
                //3.2 第二档: 20000 < 应纳所得税额 > 50000 , 缴税金额计算 [:= 应交税费金额*0.3-2000]
                amountTaxThisTime = NumberUtils.sub(NumberUtils.mul(amountNeedTaxThisTime, POINT_3), THOUSAND_2);

            } else {
                //3.3 第三档: 应纳所得税额 > 50000, 缴税金额计算[:= 应交税费金额*0.4-7000]
                amountTaxThisTime = NumberUtils.sub(NumberUtils.mul(amountNeedTaxThisTime, POINT_4), THOUSAND_7);
            }

        } else {

            //判断是否可进入纳税流程 [:= 计算本月报酬总计 - 最低纳税金额 > 800 ? 进入：不进入]
            BigDecimal taxLimitDiff = NumberUtils.sub(totalAmountBeforeTaxMonthly, HUNDRED_8);

            //2.2)小于4000  纳税金额计算公式: 纳税金额 = (本月累计总额 - 800) * 20%
            if (NumberUtils.compare(totalAmountBeforeTaxMonthly, HUNDRED_8) > 0) {
                //2.2.1) 总计报酬金大于800，进入纳税流程
                amountTaxThisTime = NumberUtils.mul(taxLimitDiff, POINT_2);
            }

        }

        return amountTaxThisTime;
    }

    public static void main(String[] args) {
        BigDecimal one = new BigDecimal(80000);
        System.out.println(one.toString() + "工资，需要税费金额:" + remunerationTax(new BigDecimal(0), one));
    }
}
