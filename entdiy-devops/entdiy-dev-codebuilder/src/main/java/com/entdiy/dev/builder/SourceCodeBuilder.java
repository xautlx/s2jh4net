/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.dev.builder;

import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.AbstractPersistableEntity;
import com.entdiy.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.envers.RevisionEntity;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 基本CRUD框架代码生成工具类，直接main方法执行或Maven方式运行工程的pom.xml调用生成代码
 * 生成的代码在：target/generated-codes目录下，其中standalone是一个Entity一个目录，用于偶尔重复生成拷贝某一个Entity相关代码之用，integrate是整合到一起的目录结构
 * 模板文件位置：src\main\resources\builder\freemarker，可自行根据项目需要调整模板定义格式
 */
public class SourceCodeBuilder {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) throws Exception {
        debug("Invoking " + SourceCodeBuilder.class);

        Configuration cfg = new Configuration();
        // 设置FreeMarker的模版文件位置
        cfg.setClassForTemplateLoading(SourceCodeBuilder.class, "/com/entdiy/dev/builder/template");
        cfg.setDefaultEncoding("UTF-8");

        Set<String> entityNames = new HashSet<String>();

        //扫码所有@Entity注解实体类
        debug("Scanning Entity list...");

        Set<BeanDefinition> beanDefinitions = null;
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        scanner.addExcludeFilter(new AnnotationTypeFilter(RevisionEntity.class));
        String basePackages = System.getProperty("basePackages");
        if (StringUtils.isBlank(basePackages)) {
            beanDefinitions = scanner.findCandidateComponents("com.entdiy");
        } else {
            debug("Using basePackages=" + basePackages);
            beanDefinitions = Sets.newHashSet();
            String[] packages = StringUtils.split(basePackages,
                    ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String pkg : packages) {
                beanDefinitions.addAll(scanner.findCandidateComponents(pkg));
            }
        }
        for (BeanDefinition beanDefinition : beanDefinitions) {
            debug(" - " + beanDefinition.getBeanClassName());
            entityNames.add(beanDefinition.getBeanClassName());
        }

        String integrateRoot = "." + File.separator + "target" + File.separator + "generated-codes" + File.separator + "integrate" + File.separator;
        String standaloneRoot = "." + File.separator + "target" + File.separator + "generated-codes" + File.separator + "standalone" + File.separator;
        new File(integrateRoot).mkdirs();
        new File(standaloneRoot).mkdirs();

        for (String entityName : entityNames) {

            String integrateRootPath = integrateRoot;
            String standaloneRootPath = standaloneRoot;

            String rootPackage = StringUtils.substringBeforeLast(StringUtils.substringBefore(entityName, ".entity"), ".");
            String rootPackagePath = StringUtils.replace(rootPackage, ".", File.separator);

            String className = StringUtils.substringAfterLast(entityName, ".");
            String classFullName = entityName;

            String modelName = StringUtils.substringBetween(entityName, rootPackage + ".", ".entity");
            String modelPath = StringUtils.replace(modelName, ".", "/");
            modelPath = "/" + modelPath;
            String modelPackagePath = StringUtils.replace(modelName, ".", File.separator);
            modelPackagePath = File.separator + modelPackagePath;

            Map<String, Object> root = new HashMap<String, Object>();
            String nameField = StringUtils.uncapitalize(className);
            root.put("model_name", modelName);
            root.put("model_path", modelPath);
            root.put("entity_name", className);
            root.put("entity_name_uncapitalize", StringUtils.uncapitalize(className));
            root.put("entity_name_field", nameField);
            root.put("entity_name_field_line", propertyToField(nameField).toLowerCase());
            root.put("full_entity_name_field", modelPath.replaceAll("/", "-").substring(1, modelPath.length()) + "-"
                    + propertyToField(nameField).toLowerCase());
            root.put("root_package", rootPackage + "." + modelName);
            root.put("action_package", rootPackage);
            root.put("table_name", "tbl_TODO_" + className.toUpperCase());

            Class entityClass = Class.forName(classFullName);
            root.put("id_type", entityClass.getMethod("getId").getReturnType().getSimpleName());
            boolean entityEditable = true;
            MetaData classEntityComment = (MetaData) entityClass.getAnnotation(MetaData.class);
            if (classEntityComment != null) {
                root.put("model_title", classEntityComment.value());
                entityEditable = classEntityComment.editable();
            } else {
                root.put("model_title", entityName);
            }
            root.put("model_editable", entityEditable);
            debug("Entity Data Map=" + root);

            Set<Field> fields = Sets.newLinkedHashSet();

            Field[] curfields = entityClass.getDeclaredFields();
            for (Field field : curfields) {
                fields.add(field);
            }

            Class superClass = entityClass.getSuperclass();
            while (superClass != null && !superClass.equals(BaseEntity.class)) {
                Field[] superfields = superClass.getDeclaredFields();
                for (Field field : superfields) {
                    fields.add(field);
                }
                superClass = superClass.getSuperclass();
            }

            //计算主界面OR查询字段列表
            Map<String, String> searchOrFields = Maps.newLinkedHashMap();
            //定义用于OneToOne关联对象的Fetch参数
            Map<String, String> fetchJoinFields = Maps.newLinkedHashMap();
            List<EntityCodeField> entityFields = new ArrayList<EntityCodeField>();
            int cnt = 1;
            for (Field field : fields) {
                if ((field.getModifiers() & Modifier.FINAL) != 0 || "id".equals(field.getName())) {
                    continue;
                }
                debug(" - Field=" + field);
                Class fieldType = field.getType();

                EntityCodeField entityCodeField = null;
                if (fieldType.isEnum()) {
                    entityCodeField = new EntityCodeField();
                    entityCodeField.setListFixed(true);
                    entityCodeField.setListWidth(80);
                    entityCodeField.setFieldType(StringUtils.uncapitalize(fieldType.getName()));
                    entityCodeField.setListAlign("center");
                } else if (fieldType == Boolean.class) {
                    entityCodeField = new EntityCodeField();
                    entityCodeField.setListFixed(true);
                    entityCodeField.setListAlign("center");
                } else if (AbstractPersistableEntity.class.isAssignableFrom(fieldType)) {
                    entityCodeField = new EntityCodeField();
                    entityCodeField.setFieldType("Entity");

                } else if (Number.class.isAssignableFrom(fieldType)) {
                    entityCodeField = new EntityCodeField();
                    entityCodeField.setListFixed(true);
                    entityCodeField.setListWidth(60);
                    entityCodeField.setListAlign("right");
                } else if (fieldType == String.class) {
                    entityCodeField = new EntityCodeField();

                    //根据Hibernate注解的字符串类型和长度设定是否列表显示
                    Column fieldColumn = field.getAnnotation(Column.class);
                    if (fieldColumn != null) {
                        int length = fieldColumn.length();
                        if (length > 300) {
                            length = 200;
                            entityCodeField.setList(false);
                        } else {
                            if (searchOrFields.size() < 3) {
                                MetaData fieldMetaData = field.getAnnotation(MetaData.class);
                                if (fieldMetaData != null) {
                                    searchOrFields.put(field.getName(), fieldMetaData.value());
                                } else {
                                    searchOrFields.put(field.getName(), field.getName());
                                }
                            }
                        }
                        entityCodeField.setListWidth(length);
                    }
                    Lob fieldLob = field.getAnnotation(Lob.class);
                    if (fieldLob != null) {
                        entityCodeField.setList(false);
                        entityCodeField.setListWidth(200);
                    }
                } else if (fieldType == LocalDate.class) {
                    entityCodeField = new EntityCodeField();
                    entityCodeField.setListFixed(true);

                    //根据Json注解设定合理的列宽
                    entityCodeField.setListWidth(90);
                    entityCodeField.setFieldType("LocalDate");
                    entityCodeField.setListAlign("center");
                } else if (fieldType == LocalDateTime.class) {
                    entityCodeField = new EntityCodeField();
                    entityCodeField.setListFixed(true);

                    //根据Json注解设定合理的列宽
                    entityCodeField.setListWidth(150);
                    entityCodeField.setFieldType("LocalDateTime");
                    entityCodeField.setListAlign("center");
                } else if (fieldType == Date.class) {
                    throw new RuntimeException("Not support, please use LocalDate type.");
                }

                if (entityCodeField != null) {
                    if (fieldType.isEnum()) {
                        entityCodeField.setEnumField(true);
                    }
                    if (StringUtils.isBlank(entityCodeField.getFieldType())) {
                        entityCodeField.setFieldType(fieldType.getSimpleName());
                    } else {
                        entityCodeField.setFieldType(String.class.getSimpleName());
                    }
                    entityCodeField.setFieldName(field.getName());
                    entityCodeField.setTitle(field.getName());
                    entityCodeField.setOrder(cnt++);

                    entityCodeField.setEdit(entityEditable);
                    MetaData entityMetaData = field.getAnnotation(MetaData.class);
                    if (entityMetaData != null) {
                        entityCodeField.setTitle(entityMetaData.value());
                        if (entityEditable) {
                            entityCodeField.setEdit(entityMetaData.editable());
                        }
                    }

                    JsonProperty fieldJsonProperty = field.getAnnotation(JsonProperty.class);
                    if (fieldJsonProperty != null) {
                        entityCodeField.setList(true);
                    }

                    if (entityCodeField.getList() || entityCodeField.getListHidden()) {
                        JoinColumn fieldJoinColumn = field.getAnnotation(JoinColumn.class);
                        if (fieldJoinColumn != null) {
                            if (fieldJoinColumn.nullable() == false) {
                                fetchJoinFields.put(field.getName(), "INNER");
                            } else {
                                fetchJoinFields.put(field.getName(), "LEFT");
                            }
                        }
                    }

                    entityFields.add(entityCodeField);
                }

            }
            Collections.sort(entityFields);
            root.put("entityFields", entityFields);
            if (fetchJoinFields.size() > 0) {
                root.put("fetchJoinFields", fetchJoinFields);
            }
            root.put("searchOrFieldNames", StringUtils.join(searchOrFields.keySet(), "_OR_"));
            root.put("searchOrFieldPlaceholders", StringUtils.join(searchOrFields.values(), " , "));

            integrateRootPath = integrateRootPath + rootPackagePath + modelPackagePath;
            process(cfg.getTemplate("Dao.ftl"), root, integrateRootPath + File.separator + "dao" + File.separator, className + "Dao.java");
            process(cfg.getTemplate("Service.ftl"), root, integrateRootPath + File.separator + "service" + File.separator, className + "Service.java");
            process(cfg.getTemplate("Controller.ftl"), root, integrateRootPath + File.separator + "web" + File.separator + "action" + File.separator,
                    className + "Controller.java");
            process(cfg.getTemplate("Test.ftl"), root, integrateRootPath + File.separator + "test" + File.separator + "service" + File.separator,
                    className + "ServiceTest.java");
            process(cfg.getTemplate("JSP_Index.ftl"), root, integrateRootPath + File.separator + "jsp" + File.separator, nameField + "-index.jsp");
            process(cfg.getTemplate("JSP_Input_Tabs.ftl"), root, integrateRootPath + File.separator + "jsp" + File.separator, nameField
                    + "-inputTabs.jsp");
            process(cfg.getTemplate("JSP_Input_Basic.ftl"), root, integrateRootPath + File.separator + "jsp" + File.separator, nameField
                    + "-inputBasic.jsp");

            standaloneRootPath = standaloneRootPath + rootPackagePath + modelPackagePath + File.separator + className;
            process(cfg.getTemplate("Dao.ftl"), root, standaloneRootPath + File.separator + "dao" + File.separator, className + "Dao.java");
            process(cfg.getTemplate("Service.ftl"), root, standaloneRootPath + File.separator + "service" + File.separator, className
                    + "Service.java");
            process(cfg.getTemplate("Controller.ftl"), root, standaloneRootPath + File.separator + "web" + File.separator + "admin" + File.separator,
                    className + "Controller.java");
            process(cfg.getTemplate("Test.ftl"), root, standaloneRootPath + File.separator + "test" + File.separator + "service" + File.separator,
                    className + "ServiceTest.java");
            process(cfg.getTemplate("JSP_Index.ftl"), root, standaloneRootPath + File.separator + "jsp" + File.separator, nameField + "-index.jsp");
            process(cfg.getTemplate("JSP_Input_Tabs.ftl"), root, standaloneRootPath + File.separator + "jsp" + File.separator, nameField
                    + "-inputTabs.jsp");
            process(cfg.getTemplate("JSP_Input_Basic.ftl"), root, standaloneRootPath + File.separator + "jsp" + File.separator, nameField
                    + "-inputBasic.jsp");
        }

        debug("Source Code Builder Done. Processed entities size: " + entityNames.size());
    }

    private static void debug(String message) {
        System.out.println(message);
    }

    private static void process(Template template, Map<String, Object> root, String dir, String fileName) throws Exception, FileNotFoundException {
        if ((dir + fileName).length() > 300) {
            throw new IllegalArgumentException("Dir path too long.");
        }
        File newsDir = new File(dir);
        if (!newsDir.exists()) {
            newsDir.mkdirs();
        }
        debug("Write to file: " + dir + fileName);
        Writer out = new OutputStreamWriter(new FileOutputStream(dir + fileName), "UTF-8");
        template.process(root, out);
    }

    /**
     * 对象属性转换为字段 例如：userName to user_name
     *
     * @param property 字段名
     * @return
     */
    public static String propertyToField(String property) {
        if (null == property) {
            return "";
        }
        char[] chars = property.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (char c : chars) {
            if (CharUtils.isAsciiAlphaUpper(c)) {
                sb.append("-" + StringUtils.lowerCase(CharUtils.toString(c)));
            } else {
                sb.append(c);
            }
        }
        return sb.toString().toUpperCase();
    }

    public static class DemoEntity {

        private TestEnum testEnum;

        public enum TestEnum {
            Abc, Xyz
        }

        public TestEnum getTestEnum() {
            return testEnum;
        }

        public void setTestEnum(TestEnum testEnum) {
            this.testEnum = testEnum;
        }
    }

    /**
     * 用于代码生成处理的辅助对象
     */
    public static class EntityCodeField implements Comparable<EntityCodeField> {
        /** 属性标题 */
        private String title;
        /** Java属性名称 */
        private String fieldName;
        /** 属性描述 */
        private String description;
        /** 属性在列表jqGrid中定义的宽度 */
        private Integer listWidth = 200;
        /** 在生成代码中属性的相对顺序 */
        private Integer order = Integer.MAX_VALUE;
        /** 属性在列表jqGrid中定义的对齐方式：left，right，center */
        private String listAlign = "center";
        /** 属性在列表jqGrid中定义的宽度固定模式 */
        private boolean listFixed = false;
        /** 属性在列表jqGrid中定义的默认不显示模式 */
        private boolean listHidden = false;
        /** 属性在编辑界面生成表单元素 */
        private boolean edit = true;
        /** 属性在jqGrid列表中生成column定义 */
        private boolean list = true;
        /** 标识属性是否枚举类型，根据Java属性反射获取 */
        private Boolean enumField = false;
        /** 属性类型，根据Java属性反射获取 */
        private String fieldType;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean getListFixed() {
            return listFixed;
        }

        public void setListFixed(boolean listFixed) {
            this.listFixed = listFixed;
        }

        public boolean getListHidden() {
            return listHidden;
        }

        public void setListHidden(boolean listHidden) {
            this.listHidden = listHidden;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        @Override
        public int compareTo(EntityCodeField o) {
            return order.compareTo(o.getOrder());
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

        public String getListAlign() {
            return listAlign;
        }

        public void setListAlign(String listAlign) {
            this.listAlign = listAlign;
        }

        public Integer getListWidth() {
            return listWidth;
        }

        public void setListWidth(Integer listWidth) {
            this.listWidth = listWidth;
        }

        public String getFieldType() {
            return fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }

        public Boolean getEnumField() {
            return enumField;
        }

        public void setEnumField(Boolean enumField) {
            this.enumField = enumField;
        }

        public String getUncapitalizeFieldType() {
            return StringUtils.uncapitalize(fieldType);
        }

        public boolean isEdit() {
            return edit;
        }

        public void setEdit(boolean edit) {
            this.edit = edit;
        }

        public boolean getList() {
            return list;
        }

        public void setList(boolean list) {
            this.list = list;
        }

    }

}
