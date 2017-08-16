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

    String sql_all = "SELECT p.pk_id,p.product_name,p.product_url,p.website,t.growth_money from t_dream_rank_growth t,t_dream_product p " +
            "where t.product_id = p.pk_id and t.update_date = #{updateDate} and p.product_enabled = 1  ORDER BY t.growth_money DESC ";

    String sal_platform = "SELECT p.pk_id,p.product_name,p.product_url,p.website,t.growth_money from t_dream_rank_growth t,t_dream_product p " +
            "where t.product_id = p.pk_id and t.update_date = #{updateDate} and p.product_enabled = 1 AND p.website = #{website}  " +
                    "ORDER BY t.growth_money DESC LIMIT 5 ";

    /**
     *  查询所有平台当天的增速排行榜
     * @param updateDate 更新时间
     * @return
     */
    @Select(sql_all)
    @Results({
            @Result(property = "pkId",column = "pk_id",jdbcType = JdbcType.INTEGER),
            @Result(property = "website",column = "website",jdbcType = JdbcType.INTEGER),
            @Result(property = "productName",column = "product_name",jdbcType = JdbcType.VARCHAR),
            @Result(property = "productUrl",column = "product_url",jdbcType = JdbcType.VARCHAR),
            @Result(property = "growthMoney",column = "growth_money",jdbcType = JdbcType.DECIMAL),
    })
    List<TDreamProduct> queryAllGrowthSpeedRank(@Param("updateDate") Date updateDate);

    /**
     * 根据更新时间和平台编号查询平台的增速榜前5名
     * @param updateDate 更新时间
     * @param website  平台编号
     * @return
     */
    @Select(sal_platform)
    @Results({
            @Result(property = "pkId",column = "pk_id",jdbcType = JdbcType.INTEGER),
            @Result(property = "website",column = "website",jdbcType = JdbcType.INTEGER),
            @Result(property = "productName",column = "product_name",jdbcType = JdbcType.VARCHAR),
            @Result(property = "productUrl",column = "product_url",jdbcType = JdbcType.VARCHAR),
            @Result(property = "growthMoney",column = "growth_money",jdbcType = JdbcType.DECIMAL),
    })
    List<TDreamProduct> queryPlatformGrowthSpeedRankTop5(@Param("updateDate") Date updateDate,@Param("website") Integer website);

}
