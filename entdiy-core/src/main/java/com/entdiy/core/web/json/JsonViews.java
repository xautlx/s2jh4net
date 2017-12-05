package com.entdiy.core.web.json;

public class JsonViews {

    public interface Public {
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
}
