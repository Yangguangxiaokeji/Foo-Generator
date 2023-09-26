package com.foogui.foo.generator.service;

import com.foogui.generator.common.model.request.GenRequest;

public interface GenService {


    /**
     * 通过ddl创建代码
     *
     * @param dto dto
     * @return {@link byte[]}
     */
    byte[] doCreateCodeByDDL(GenRequest dto);


    /**
     * 批量生成代码
     *
     * @param dto dto
     * @return {@link byte[]}
     */
    byte[] doCreateCodeBatch(GenRequest dto);

}
