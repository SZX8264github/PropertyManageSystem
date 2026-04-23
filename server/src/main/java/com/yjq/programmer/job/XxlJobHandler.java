package com.yjq.programmer.job;

import com.xiaogang.xxljobadminsdk.service.XxlJobService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yjq.programmer.dao.FeeMapper;
import com.yjq.programmer.domain.Fee;
import com.yjq.programmer.domain.FeeExample;
import com.yjq.programmer.enums.FeeStateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-06 21:37
 */
@Component
public class XxlJobHandler {

    @Resource
    private FeeMapper feeMapper;

    @Resource
    private XxlJobService xxlJobService;


    private static final Logger logger = LoggerFactory.getLogger(XxlJobHandler.class);

    // 缴费逾期罚金处理任务
    @XxlJob("feeHandler")
    public void feeHandler() {
        // 获取任务参数
        String param = XxlJobHelper.getJobParam();
        logger.info("【缴费逾期罚金处理任务】接收到参数：{}", param);
        long jobId = XxlJobHelper.getJobId();
        logger.info("【缴费逾期罚金处理任务】任务id：{}", jobId);
        Fee feeDB = feeMapper.selectByPrimaryKey(param);
        if(FeeStateEnum.PAYED.getCode().equals(feeDB.getState())) {
            xxlJobService.remove((int) jobId);
        } else if (new Date().getTime() > feeDB.getDeadTime().getTime()) {
            int diffDay = (int) ((new Date().getTime() - feeDB.getDeadTime().getTime()) / (24 * 60 * 60 * 1000));
            BigDecimal addPrice = new BigDecimal(diffDay).multiply(feeDB.getDeadPrice());
            feeDB.setAddPrice(addPrice);
            feeDB.setState(FeeStateEnum.OUT.getCode());
            feeMapper.updateByPrimaryKeySelective(feeDB);
        }
        logger.info("【缴费逾期罚金处理任务】处理完成");
    }

    @PostConstruct
    public void initServer() {
        logger.info("【服务初始化启动】准备执行缴费逾期罚金处理任务");
        FeeExample feeExample = new FeeExample();
        feeExample.createCriteria().andStateNotEqualTo(FeeStateEnum.PAYED.getCode()).andDeadTimeLessThan(new Date());
        List<Fee> feeList = feeMapper.selectByExample(feeExample);
        for(Fee fee : feeList) {
            int diffDay = (int) ((new Date().getTime() - fee.getDeadTime().getTime()) / (24 * 60 * 60 * 1000));
            BigDecimal addPrice = new BigDecimal(diffDay).multiply(fee.getDeadPrice());
            fee.setAddPrice(addPrice);
            fee.setState(FeeStateEnum.OUT.getCode());
            feeMapper.updateByPrimaryKeySelective(fee);
        }
        logger.info("【服务初始化启动】缴费逾期罚金处理任务完成");
    }

}
