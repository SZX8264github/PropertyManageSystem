package com.yjq.programmer.service;

import com.yjq.programmer.dto.AnnounceDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-08 21:12
 */
public interface IAnnounceService {

    // 分页获取公告数据
    ResponseDTO<PageDTO<AnnounceDTO>> getAnnounceList(PageDTO<AnnounceDTO> pageDTO);

    // 保存公告信息
    ResponseDTO<Boolean> saveAnnounce(AnnounceDTO announceDTO);

    // 删除公告信息
    ResponseDTO<Boolean> deleteAnnounce(AnnounceDTO announceDTO);
}
