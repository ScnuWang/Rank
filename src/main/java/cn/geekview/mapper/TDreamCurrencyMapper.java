package cn.geekview.mapper;

import cn.geekview.domain.TDreamCurrency;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface TDreamCurrencyMapper {
    String sql_all = "SELECT t.currency_nick,t.currency_exchange FROM t_dream_currency t";

    @Select(sql_all)
    @Results({
            @Result(property = "currencyNick",column = "currency_nick",jdbcType = JdbcType.INTEGER),
            @Result(property = "currencyExchange",column = "currency_exchange",jdbcType = JdbcType.INTEGER),
    })
    List<TDreamCurrency> queryCurrency();
}
