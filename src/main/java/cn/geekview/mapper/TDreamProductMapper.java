package cn.geekview.mapper;

import cn.geekview.domain.TDreamProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
import java.util.List;

public interface TDreamProductMapper {

    String sql_all = "SELECT p.pk_id,p.product_name,p.product_url,p.website,p.original_id,t.growth_money from t_dream_rank_growth t,t_dream_product p " +
            "where t.product_id = p.pk_id and t.update_date = #{updateDate} and p.product_enabled = 1  ORDER BY t.growth_money DESC ";

    String sql_platform = "SELECT p.pk_id,p.product_name,p.product_url,p.website,p.original_id,t.growth_money from t_dream_rank_growth t,t_dream_product p " +
            "where t.product_id = p.pk_id and t.update_date = #{updateDate} and p.product_enabled = 1 AND p.website = #{website}  " +
                    "ORDER BY t.growth_money DESC LIMIT 5 ";

    //查询时间一周前开始众筹的项目增速榜前5名
    String sql_7days_oldprojects = "SELECT t1.original_id,t1.project_name,IFNULL(t1.money_currency,'CNY') as money_currency,t2.curr_money-t1.curr_money as growthMoney " +
            "FROM ${tableName} t1 RIGHT JOIN ( SELECT t.original_id, t.project_name, t.curr_money FROM ${tableName} t " +
            "WHERE t.update_date = #{nowDate} ) t2 ON t1.original_id = t2.original_id " +
            "WHERE t1 .update_date = #{weekBeforeDate} ORDER BY growthMoney DESC limit 5";

    //查询时间一周内新上的项目增速榜前5名
    String sql_7days_newprojects = "SELECT t.original_id, t.project_name, IFNULL(t.money_currency, 'CNY') as money_currency , t.curr_money " +
            "FROM ${tableName} t WHERE t.update_date > #{weekBeforeDate} AND t.status_value = 2 " +
            "AND t.update_date < #{nowDatePlus1} " +
            "AND t.original_id NOT IN " +
            "( SELECT t.original_id FROM ${tableName} t WHERE t.update_date = #{weekBeforeDate}) " +
            "ORDER BY t.curr_money DESC LIMIT 35";
    /**
     * 根据更新时间和平台编号查询平台一周的增速榜前5名
     * @param nowDate  当前查询时间
     * @param weekBeforeDate  一周前的时间
     * @param tableName 表名  表名不需要预编译，故使用$(变量名取值)
     * @return
     */
    @Select(sql_7days_oldprojects)
    @Results({
            @Result(property = "originalId",column = "original_id",jdbcType = JdbcType.INTEGER),
            @Result(property = "productName",column = "project_name",jdbcType = JdbcType.VARCHAR),
            @Result(property = "moneyCurrency",column = "money_currency",jdbcType = JdbcType.VARCHAR),
            @Result(property = "growthMoney",column = "growth_money",jdbcType = JdbcType.DECIMAL),
    })
    List<TDreamProduct> query7DaysOldpeojectsRankTop5(@Param("nowDate") Date nowDate,@Param("weekBeforeDate") Date weekBeforeDate,@Param("tableName") String tableName);

    /**
     * 根据更新时间和平台编号查询平台一周的增速榜前5名
     * @param nowDatePlus1  当前查询时间
     * @param weekBeforeDate  一周前的时间
     * @param tableName 表名  表名不需要预编译，故使用$(变量名取值)
     * @return
     */
    @Select(sql_7days_newprojects)
    @Results({
            @Result(property = "originalId",column = "original_id",jdbcType = JdbcType.INTEGER),
            @Result(property = "productName",column = "project_name",jdbcType = JdbcType.VARCHAR),
            @Result(property = "moneyCurrency",column = "money_currency",jdbcType = JdbcType.VARCHAR),
            @Result(property = "growthMoney",column = "curr_money",jdbcType = JdbcType.DECIMAL),
    })
    List<TDreamProduct> query7DaysNewpeojectsRankTop5(@Param("nowDatePlus1") Date nowDatePlus1,@Param("weekBeforeDate") Date weekBeforeDate,@Param("tableName") String tableName);

    /**
     *  查询所有平台当天的增速排行榜
     * @param updateDate 更新时间
     * @return
     */
    @Select(sql_all)
    @Results({
            @Result(property = "pkId",column = "pk_id",jdbcType = JdbcType.INTEGER),
            @Result(property = "originalId",column = "original_id",jdbcType = JdbcType.INTEGER),
            @Result(property = "website",column = "website",jdbcType = JdbcType.INTEGER),
            @Result(property = "productName",column = "product_name",jdbcType = JdbcType.VARCHAR),
            @Result(property = "productUrl",column = "product_url",jdbcType = JdbcType.VARCHAR),
            @Result(property = "growthMoney",column = "growth_money",jdbcType = JdbcType.DECIMAL),
    })
    List<TDreamProduct> queryAllGrowthSpeedRank(@Param("updateDate") Date updateDate);

    /**
     * 根据更新时间和平台编号查询平台一天的增速榜前5名
     * @param updateDate 查询时间
     * @param website  平台编号
     * @return
     */
    @Select(sql_platform)
    @Results({
            @Result(property = "pkId",column = "pk_id",jdbcType = JdbcType.INTEGER),
            @Result(property = "originalId",column = "original_id",jdbcType = JdbcType.INTEGER),
            @Result(property = "website",column = "website",jdbcType = JdbcType.INTEGER),
            @Result(property = "productName",column = "product_name",jdbcType = JdbcType.VARCHAR),
            @Result(property = "productUrl",column = "product_url",jdbcType = JdbcType.VARCHAR),
            @Result(property = "growthMoney",column = "growth_money",jdbcType = JdbcType.DECIMAL),
    })
    List<TDreamProduct> queryPlatformGrowthSpeedRankTop5(@Param("updateDate") Date updateDate,@Param("website") Integer website);

}
