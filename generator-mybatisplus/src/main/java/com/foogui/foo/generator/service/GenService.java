package com.foogui.foo.generator.service;

import com.foogui.foo.generator.domain.GenDTO;

public interface GenService {


    /**
     * 通过ddl创建代码
     *
     * @param dto dto
     * @return {@link byte[]}
     */
    byte[] doCreateCodeByDDL(GenDTO dto);


    /**
     * 批量生成代码
     *
     * @param dto dto
     * @return {@link byte[]}
     */
    public byte[] doCreateCodeBatch(GenDTO dto);

}
