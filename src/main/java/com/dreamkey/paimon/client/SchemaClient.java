package com.dreamkey.paimon.client;

import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.application.PaimonSchema;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.common.annotation.Property;
import com.dreamkey.paimon.model.bean.*;
import com.dreamkey.paimon.model.query.BaseQuery;
import com.dreamkey.paimon.util.AssertUtil;
import com.dreamkey.paimon.util.PaimonUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作资产类型 API
 *
 * @author WangHaoquan
 * @date 2022/3/22
 */
public class SchemaClient {

    private final PaimonConfig config;

    public SchemaClient(PaimonConfig config) {
        this.config = config;
    }

    /**
     * 添加资产类型，通过类获取 Schema 相关信息添加资产类型
     *
     * @param clazz 类信息，用于创建资产类型
     * @return
     * @throws IOException
     */
    public <T> String addSchema(Class<T> clazz) throws IOException {
        // 获取类名
        String name = PaimonUtil.getSchemaName(clazz);
        // 反射获取类字段信息
        List<SchemaProperty> properties = this.getSchemaProperties(clazz);
        // 创建资产类型
        Schema schema = new Schema(name, properties);
        return addSchema(schema);
    }

    /**
     * 添加资产类型
     * @param schema
     * @return
     * @throws IOException
     */
    public String addSchema(Schema schema) throws IOException{
        Session session = PaimonUtil.getSession(config);
        PaimonSchema paimonSchema = new PaimonSchema(config, session);
        // 创建资产类型
        paimonSchema.addSchema(schema);
        return schema.getName();
    }

    /**
     * 分页查询资产类型
     *
     * @param page 当前页码
     * @param size 每页记录数量
     * @return 返回 data 是资产类型名称集合
     * @throws IOException
     */
    public PageInfo<String> querySchemas(Integer page, Integer size) throws IOException {
        AssertUtil.notNull(page, AssertUtil.notNullTemplate("page"));
        AssertUtil.notNull(size, AssertUtil.notNullTemplate("size"));
        AssertUtil.isTrue(page > 0 && size > 0, "'page' and 'size' must be greater than 0");

        Session session = PaimonUtil.getSession(config);
        PaimonSchema paimonSchema = new PaimonSchema(config, session);
        BaseQuery query = new BaseQuery();
        query.setOffset((page - 1) * size);
        query.setLimit(size);
        ResponseEntity response = paimonSchema.querySchemas(query);

        String data = response.getData();
        JSONObject dataJson = JSONObject.parseObject(data);

        return new PageInfo<>(dataJson.getInteger("total"),page,size,
                dataJson.getJSONArray("schemas").toJavaList(String.class));
    }

    /**
     * 查询资产类型信息
     *
     * @param clazz 类信息，用于分析要查看的资产类型是哪一个
     * @return
     * @throws IOException
     */
    public <T> Schema getSchema(Class<T> clazz) throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonSchema paimonSchema = new PaimonSchema(config, session);

        String name = PaimonUtil.getSchemaName(clazz);
        ResponseEntity response = paimonSchema.getSchema(name);

        String data = response.getData();
        return JSONObject.parseObject(data, Schema.class);
    }

    /**
     * 更新资产类型，通过类获取 Schema 相关信息添加资产类型
     *
     * @param clazz 类信息，根据类属性上的注解来更新资产类型
     * @throws IOException
     */
    public <T> void updateSchema(Class<T> clazz) throws IOException {
        String name = clazz.getSimpleName();
        List<SchemaProperty> properties = this.getSchemaProperties(clazz);

        Schema schema = new Schema(name, properties);
        updateSchema(schema);
    }


    /**
     * 更新资产类型
     * @param schema
     * @throws IOException
     */
    public void updateSchema(Schema schema) throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonSchema paimonSchema = new PaimonSchema(config, session);
        paimonSchema.updateSchema(schema);
    }

    /**
     * 删除资产类型（删除资产类型将清除归属该类型的所有资产，谨慎调用，区块中会保留相关记录）
     *
     * @param clazz 类信息，用于分析资产类型是哪一个
     * @throws IOException
     */
    public <T> void deleteSchema(Class<T> clazz) throws IOException {
        String name = clazz.getSimpleName();
        deleteSchema(name);
    }


    /**
     * 删除资产类型（删除资产类型将清除归属该类型的所有资产，谨慎调用，区块中会保留相关记录）
     *
     * @param schemaName
     * @throws IOException
     */
    public <T> void deleteSchema(String schemaName) throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonSchema paimonSchema = new PaimonSchema(config, session);
        paimonSchema.deleteSchema(schemaName);
    }

    /**
     * 重建文档类型字段索引（使用 update 方法更新字段后，如果改动字段索引状态需要调用此方法使索引重建生效）
     *
     * @param clazz 类信息，用于分析资产类型是哪一个
     * @throws IOException
     */
    public <T> void rebuildIndex(Class<T> clazz) throws IOException {
        String name = PaimonUtil.getSchemaName(clazz);
        rebuildIndex(name);
    }

    /**
     * 重建文档类型字段索引（使用 update 方法更新字段后，如果改动字段索引状态需要调用此方法使索引重建生效）
     *
     * @param schemaName
     * @throws IOException
     */
    public void rebuildIndex(String schemaName) throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonSchema paimonSchema = new PaimonSchema(config, session);
        paimonSchema.rebuildIndex(schemaName);
    }

    /**
     * 反射获取类字段信息
     *
     * @param clazz
     * @return
     */
    private List<SchemaProperty> getSchemaProperties(Class<?> clazz) {
        List<SchemaProperty> properties = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            // 通过注解来分析字段信息（在 paimon 中的类型、是否索引）
            Property property = field.getAnnotation(Property.class);
            if (property == null) {
                continue;
            }
            // 创建资产类型
            SchemaProperty schemaProperty = SchemaProperty.builder()
                    .name(field.getName())
                    .type(property.type().getType())
                    .indexed(property.indexed())
                    .build();
            properties.add(schemaProperty);
        }
        return properties;
    }

}
