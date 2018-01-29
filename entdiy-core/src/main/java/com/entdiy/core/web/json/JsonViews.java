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
package com.entdiy.core.web.json;

public class JsonViews {

    public interface View {
    }

    public interface Public extends View {
    }

    public interface App extends Public {
    }

    public interface List {
    }

    public interface Detail {
    }

    public interface AppList extends App, List {
    }

    public interface AppDetail extends App, Detail {
    }

    public interface Admin extends App, List {
    }

    public interface Tree extends Admin, App, List {
    }
}
