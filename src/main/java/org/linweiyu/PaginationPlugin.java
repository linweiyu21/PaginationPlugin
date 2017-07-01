package org.linweiyu;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 生成分页查询方法与分页查询语句.暂时仅适用于MySQL
 * 生成的ethod:List<实体类> selectByPage(@Param("offset") Long offset,@Param("limit") Long limit);
 * 生成的sql:SELECT <include refid="Base_Column_List"/> FROM 表名 LIMIT #{offset}, #{limit}
 *
 * @author linweiyu
 */
public class PaginationPlugin extends PluginAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 是否生成分页方法与sql.调用插件时传入的参数.
     */
    private static final String IS_GEN_PAGINATION = "generate.pagination";


    @Override
    public boolean validate(List<String> warnings) {
        logger.info("--- PaginationPlugin validate invoke");
        return true;
    }

    /**
     * 判断参数generate.pagination.是否生成
     */
    private boolean isGeneratePagination(IntrospectedTable introspectedTable) {
        String is_generate_pagination = introspectedTable.getTableConfigurationProperty(IS_GEN_PAGINATION);
        logger.info("--- PaginationPlugin is_generate_pagination=[{}]", is_generate_pagination);
        if (StringUtility.stringHasValue(is_generate_pagination)) {
            return Boolean.valueOf(is_generate_pagination);
        } else {
            return false;
        }
    }

    /**
     * 生成 List<实体类> selectByPage(@Param("offset") Long offset,@Param("limit") Long limit); 方法
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        boolean super_result = super.clientGenerated(interfaze, topLevelClass, introspectedTable);
        logger.info("--- PaginationPlugin clientGenerated invoke");

        if (isGeneratePagination(introspectedTable) && super_result) {
            // 生成方法
            Method newMethod = new Method("selectByPage");
            // 设置方法类型
            newMethod.setVisibility(JavaVisibility.PUBLIC);

            // 设置方法返回值类型
            FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("List<" + introspectedTable.getTableConfiguration().getDomainObjectName() + ">");
            newMethod.setReturnType(returnType);

            // 设置方法参数
            FullyQualifiedJavaType offsetJavaType = new FullyQualifiedJavaType("Long");
            Parameter offsetParameter = new Parameter(offsetJavaType, "offset");
            offsetParameter.addAnnotation("@Param(\"offset\")");
            newMethod.addParameter(0, offsetParameter);

            FullyQualifiedJavaType limitJavaType = new FullyQualifiedJavaType("Long");
            Parameter limitParameter = new Parameter(limitJavaType, "limit");
            limitParameter.addAnnotation("@Param(\"limit\")");
            newMethod.addParameter(1, limitParameter);

            // 添加相应的包
            interfaze.addImportedType(new FullyQualifiedJavaType(("org.apache.ibatis.annotations.Param")));
            interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
            interfaze.addMethod(newMethod);

            return true;
        }
        return super_result;
    }

    /**
     * 生成如下的分页查询sql语句
     * <select id="selectByPage" resultMap="BaseResultMap"> SELECT <include refid="Base_Column_List"/> FROM 表名 LIMIT #{offset}, #{limit} </select>
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        logger.info("--- PaginationPlugin sqlMapDocumentGenerated invoke");

        boolean super_result = super.sqlMapDocumentGenerated(document, introspectedTable);

        if (isGeneratePagination(introspectedTable) && super_result) {
            XmlElement select = new XmlElement("select");
            select.addAttribute(new Attribute("id", "selectByPage"));
            select.addAttribute(new Attribute("resultMap", "BaseResultMap"));
            select.addElement(new TextElement("SELECT <include refid=\"Base_Column_List\" /> FROM " + introspectedTable.getFullyQualifiedTableNameAtRuntime() + " LIMIT #{offset},#{limit}"));
            XmlElement parentElement = document.getRootElement();
            parentElement.addElement(select);
            return true;
        }
        return super_result;
    }

}
