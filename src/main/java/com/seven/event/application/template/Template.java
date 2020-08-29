/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * <p>
 * 项目名：	tms
 * 文件名：	template.java
 * 模块说明：
 * 修改历史：
 * 2020/1/12 - seven - 创建。
 */
package com.seven.event.application.template;

import com.searly.aroma.commons.biz.query.QueryEntity;
import com.searly.aroma.commons.biz.query.QueryField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author seven
 */
@Getter
@Setter
@ApiModel("TEMPLATE")
public class Template {

    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "序号")
    private String no;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否启用")
    private boolean enabled = true;
    @ApiModelProperty(value = "拥有者ID")
    private String ownerId;

    @QueryEntity(Template.class)
    public static abstract class Queries {
        private static final String PREFIX = Template.class.getName() + "::";

        @QueryField
        public static final String ID = PREFIX + "id";
        @QueryField
        public static final String NAME = PREFIX + "name";
        @QueryField
        public static final String ENABLED = PREFIX + "enabled";
        @QueryField
        public static final String REMARK = PREFIX + "remark";
        @QueryField
        public static final String OWNER_ID = PREFIX + "ownerId";

    }
}
