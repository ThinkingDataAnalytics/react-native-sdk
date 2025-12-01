/*
 * Copyright (C) 2025 ThinkingData
 */
package cn.thinking;

import org.json.JSONObject;

public class TDViewProperties {
    public String elementContent;
    public JSONObject params;
    public boolean isIgnore;

    public TDViewProperties(String elementContent, JSONObject params) {
        this.elementContent = elementContent;
        this.params = params;
        if (params.has("TDIgnoreViewClick")) {
            isIgnore = params.optBoolean("TDIgnoreViewClick");
            params.remove("TDIgnoreViewClick");
        }
    }
}
