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
package com.entdiy.dev.demo.dao.test;

import com.entdiy.core.test.SpringTransactionalTestCase;
import com.entdiy.core.util.MockEntityUtils;
import com.entdiy.dev.demo.dao.DemoProductDao;
import com.entdiy.dev.demo.entity.DemoProduct;
import com.entdiy.sys.entity.AttachmentFile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

public class DemoProductDaoTest extends SpringTransactionalTestCase {

    @Autowired
    private DemoProductDao demoProductDao;

    @PostConstruct
    public void init() {
        MockEntityUtils.buildMockObject(DemoProduct.class, 5, 10).stream().forEach(
                p -> {
                    List<AttachmentFile> introImages = MockEntityUtils.buildMockObject(AttachmentFile.class, 5, 8);
                    introImages.stream().forEach(e -> {
                        e.setSourceId(null);
                        e.setSourceType(DemoProduct.TYPE_ATTACHMENT_FILE);
                    });
                    p.setIntroImages(introImages);
                    demoProductDao.save(p);
                }
        );
    }

    @Test
    public void findIntroImages() {
        demoProductDao.findAll().stream().forEach(
                p -> logger.debug("introImages: {}", p.getIntroImages())
        );
    }
}
