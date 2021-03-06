package com.zh;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 讲一个列表中的对象，按一个属性分组，另外一个属性求和，最终输出位一个列表
 */
public class TestGroupBy {

    @Test
    public void test1() {
        List<Holder> list = new ArrayList<>();
        list.add(new Holder("1", new BigDecimal(10)));
        list.add(new Holder("2", new BigDecimal(11)));
        list.add(new Holder("1", new BigDecimal(12)));
        List<Holder> results = list.stream()
                .collect(Collectors.groupingBy(Holder::getRivalid))
                .entrySet()
                .stream()
                .map(item -> {
                    String key = item.getKey();
                    List<Holder> values = item.getValue();
                    BigDecimal sum = values.stream()
                            .map(Holder::getUsedCreditQuota)
                            .reduce(new BigDecimal(0), (before, after) -> before.add(after));
                    Holder holder = new Holder(key, sum);
                    return holder;
                })
                .collect(Collectors.toList());
        System.out.println(results);
    }

    /**
     * 根据某个字段的一部分排序
     */
    @Test
    public void test2() {
        List<Holder> list = new ArrayList<>();
        list.add(new Holder("101T00", new BigDecimal(10)));
        list.add(new Holder("10T00", new BigDecimal(11)));
        list.add(new Holder("110T00", new BigDecimal(12)));
        list.add(new Holder("112T00", new BigDecimal(13)));
        Map<String, List<Holder>> result = list.stream()
                .collect(Collectors.groupingBy(holder -> holder.getRivalid().substring(0, 2)));
        System.out.println(result);
    }

    /**
     * 根据对象的一个字段排重，根据另外一个字段排序并取最大值
     */
    @Test
    public void test3() {
        List<Holder> list = new ArrayList<>();
        list.add(new Holder("10T00", new BigDecimal(10)));
        list.add(new Holder("10T00", new BigDecimal(11)));

        list.add(new Holder("112T00", new BigDecimal(12)));
        list.add(new Holder("112T00", new BigDecimal(13)));

        List<Holder> result = list.stream()
                .collect(Collectors.groupingBy(Holder::getRivalid))
                .values()
                .stream()
                .map(holders -> holders.stream()
                            .max(Comparator.comparing(Holder::getUsedCreditQuota)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        System.out.println(result);
    }
}

class Holder{
    private String rivalid;
    private BigDecimal usedCreditQuota;

    public Holder(String rivalid, BigDecimal usedCreditQuota) {
        this.rivalid = rivalid;
        this.usedCreditQuota = usedCreditQuota;
    }

    public String getRivalid() {
        return rivalid;
    }

    public void setRivalid(String rivalid) {
        this.rivalid = rivalid;
    }

    public BigDecimal getUsedCreditQuota() {
        return usedCreditQuota;
    }

    public void setUsedCreditQuota(BigDecimal usedCreditQuota) {
        this.usedCreditQuota = usedCreditQuota;
    }

    public String toString() {
        return "[ rivalid=" + rivalid + ", " + "usedCreditQuota=" + this.usedCreditQuota + " ]";
    }
}
