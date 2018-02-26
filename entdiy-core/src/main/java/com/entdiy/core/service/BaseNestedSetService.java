/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
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
package com.entdiy.core.service;

import com.entdiy.core.entity.BaseNativeNestedSetEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

/**
 * 基于Nested Set Model数据模型，对递归树形数据提供节点新增、删除、移动、排序等操作支持接口
 *
 * @see "https://en.wikipedia.org/wiki/Nested_set_model"
 */
public abstract class BaseNestedSetService<T extends BaseNativeNestedSetEntity, ID extends Serializable> extends BaseService<T, ID> {

    private final Logger logger = LoggerFactory.getLogger(BaseNestedSetService.class);

    private T queryEntity(Long id) {
        Query query = entityManager.createQuery("from " + entityName + " where id=:id");
        query.setParameter("id", id);
        return (T) query.getSingleResult();
    }

    private void moveToRight(T entity, int newRight) {
        //记录移动对象的左右值
        Integer left = entity.getLft();
        Integer right = entity.getRgt();
        int width = entity.getRgt() - entity.getLft() + 1;

        //"临时"移除节点，把移动节点及子节点，全部置为负数
        Query update = entityManager.createQuery("update " + entityName + " set rgt = 0-rgt, lft=0-lft where lft >= :curLft and rgt <= :curRgt");
        update.setParameter("curLft", left);
        update.setParameter("curRgt", right);
        int cnt = update.executeUpdate();

        //移动区间节点调整
        Query update1 = entityManager.createQuery("update " + entityName + " set rgt = rgt - :width where rgt > :curRight and rgt < :newRight");
        Query update2 = entityManager.createQuery("update " + entityName + " set lft = lft - :width where lft > :curRight and lft < :newRight");
        update1.setParameter("curRight", right);
        update2.setParameter("curRight", right);
        update1.setParameter("newRight", newRight);
        update2.setParameter("newRight", newRight);
        update1.setParameter("width", width);
        update2.setParameter("width", width);
        int cnt1 = update1.executeUpdate();
        int cnt2 = update2.executeUpdate();

        //调整移动节点及子节点
        int diff = newRight - 1 - entity.getRgt();
        entity.setLft(left + diff);
        entity.setRgt(right + diff);

        //批量移动子节点
        if (entity.hasChildren()) {
            Query update3 = entityManager.createQuery("update " + entityName + " set rgt = rgt - :diff, lft=lft - :diff where lft < 0 and rgt < 0");
            update3.setParameter("diff", diff);
            int cnt3 = update3.executeUpdate();

            //恢复移动节点及子节点
            Query update4 = entityManager.createQuery("update " + entityName + " set rgt = 0-rgt, lft=0-lft where lft < 0 and rgt < 0");
            int cnt4 = update4.executeUpdate();
        }
    }

    private void moveToLeft(T entity, int newLeft) {
        //记录移动对象的左右值
        Integer left = entity.getLft();
        Integer right = entity.getRgt();
        int width = entity.getRgt() - entity.getLft() + 1;

        //"临时"移除节点，把移动节点及子节点，全部置为负数
        Query update = entityManager.createQuery("update " + entityName + " set rgt = 0-rgt, lft=0-lft where lft >= :curLft and rgt <= :curRgt");
        update.setParameter("curLft", left);
        update.setParameter("curRgt", right);
        int cnt = update.executeUpdate();

        // 移动区间节点调整
        Query update1 = entityManager.createQuery("update " + entityName + " set rgt = rgt + :width where rgt >= :newLeft and rgt < :curRight");
        Query update2 = entityManager.createQuery("update " + entityName + " set lft = lft + :width where lft >= :newLeft and lft < :curRight");
        update1.setParameter("curRight", right);
        update2.setParameter("curRight", right);
        update1.setParameter("newLeft", newLeft);
        update2.setParameter("newLeft", newLeft);
        update1.setParameter("width", width);
        update2.setParameter("width", width);
        int cnt1 = update1.executeUpdate();
        int cnt2 = update2.executeUpdate();

        //调整移动节点及子节点
        int diff = left - newLeft;
        entity.setLft(left - diff);
        entity.setRgt(right - diff);

        //批量移动子节点
        if (entity.hasChildren()) {
            Query update3 = entityManager.createQuery("update " + entityName + " set rgt = rgt + :diff, lft=lft + :diff where lft < 0 and rgt < 0");
            update3.setParameter("diff", diff);
            int cnt3 = update3.executeUpdate();

            //恢复移动节点及子节点
            Query update4 = entityManager.createQuery("update " + entityName + " set rgt = 0-rgt, lft=0-lft where lft < 0 and rgt < 0");
            int cnt4 = update4.executeUpdate();
        }
    }


    @Transactional
    @Override
    public T save(T entity) {
        //非根节点处理
        if (entity.getParent() != null && entity.getParent().getId() != null) {
            // 注意：直接SQL批量更新方式，会导致与Hibernate缓存数据不一致，但是这些不一致的属性都是用于Nested Set Model模型处理的，对其他业务属性不影响，因此可以容忍。
            // 因此以下数据处理凡是涉及模型属性的全部采用Query语法进行，以获取最新批处理结果数据

            Long parentId = entity.getParent().getId();
            //新增节点处理
            if (entity.isNew()) {
                T parent = queryEntity(parentId);

                //LAST位置或空白节点追加新增节点
                entity.setLft(parent.getRgt());
                entity.setRgt(entity.getLft() + 1);
                entity.setDepth(parent.getDepth() + 1);
                entity.setParent(parent);

                //更新待插入新位置之前的树结构数据
                Query update1 = entityManager.createQuery("update " + entityName + " set rgt = rgt+2 where rgt >= :newLft");
                Query update2 = entityManager.createQuery("update " + entityName + " set lft = lft+2 where lft > :newLft");
                update1.setParameter("newLft", entity.getLft());
                update2.setParameter("newLft", entity.getLft());
                int cnt1 = update1.executeUpdate();
                int cnt2 = update2.executeUpdate();
            } else if (entity.getNewParentId() != null || entity.getSortPrevId() != null) {
                //记录移动对象的左右值
                Integer right = entity.getRgt();

                //如果parent做了修改，则先处理原先节点移除，在处理移动新增节点
                if (entity.getNewParentId() != null && !entity.getNewParentId().equals(parentId)) {
                    //移动新位置父节点对象
                    T newParent;
                    if (entity.getNewParentId() > 0) {
                        newParent = queryEntity(entity.getNewParentId());
                    } else {
                        newParent = findRoot();
                    }
                    Integer newParentRgt = newParent.getRgt();

                    if (newParentRgt > right) { //向右移动
                        moveToRight(entity, newParentRgt);
                    } else { //向左移动
                        moveToLeft(entity, newParentRgt);
                    }

                    //设置新的父节点对象关联
                    entity.setParent(newParent);
                    entity.setDepth(newParent.getDepth() + 1);
                } else if (entity.getSortPrevId() != null) {
                    //同级排序调整
                    Long sortPrevId = entity.getSortPrevId();
                    //移动到同级顶部
                    if (sortPrevId <= -1) {
                        //移动节点置顶，取父节点的rgt作为基准值
                        T parent = queryEntity(parentId);
                        moveToRight(entity, parent.getRgt());
                    } else {
                        //查询移动新位置向上相邻节点
                        T prevEntity = queryEntity(sortPrevId);
                        if (prevEntity.getLft() - entity.getLft() > 2) { //向上移动至少一格
                            moveToRight(entity, prevEntity.getLft());
                        } else if (prevEntity.getLft() - entity.getLft() < 0) { //向下移动
                            moveToLeft(entity, prevEntity.getLft());
                        }
                    }
                }
            }

            //清除缓存
            entityManager.getEntityManagerFactory().getCache().evict(entityClass);
            entityManager.flush();
            entityManager.clear();
        } else {
            //根节点处理
            entity.makeRoot();
        }

        jpaRepository.save(entity);

        return entity;
    }

    /**
     * Finds root of the tree
     *
     * @return Node
     */
    public T findRoot() {
        Query query = entityManager.createQuery("FROM " + entityName + " WHERE parent is null");
        return (T) query.getResultList().get(0);
    }

    public List<T> findChildren(T node) {
        Query query = entityManager.createQuery("FROM " + entityName + " WHERE parent.id = :parentId");
        query.setParameter("parentId", node.getId());
        return query.getResultList();
    }

    /**
     * 数据删除操作
     *
     * @param entity 待操作数据
     */
    @Transactional
    @Override
    public void delete(T entity) {
        Validation.isTrue(!entity.hasChildren(), "只允许删除子节点");

        //记录删除对象的左右值
        Integer left = entity.getLft();
        Integer right = entity.getRgt();

        //先删除当前对象
        jpaRepository.delete(entity);
        entityManager.flush();

        //删除节点后，移动右侧节点。注意此算法需要确保已递归删除所有子节点，如果出现孤儿节点情况则会有问题。
        int width = right - left + 1;
        Query update1 = entityManager.createQuery("update " + entityName + " set rgt = rgt - :width where rgt > :rightParam");
        Query update2 = entityManager.createQuery("update " + entityName + " set lft = lft - :width where lft > :rightParam");
        update1.setParameter("rightParam", right);
        update2.setParameter("rightParam", right);
        update1.setParameter("width", width);
        update2.setParameter("width", width);
        int cnt1 = update1.executeUpdate();
        int cnt2 = update2.executeUpdate();
    }
}
