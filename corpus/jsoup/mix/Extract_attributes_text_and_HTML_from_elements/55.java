package com.virjar.sipsoup.function.select;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import com.virjar.sipsoup.model.SIPNode;

/**
 * Created by virjar on 17/6/11.
 */
public class AttrFunction extends AttrBaseFunction {

    @Override
    public void handle(boolean allAttr, String attrKey, Element element, List<SIPNode> ret) {
        if (allAttr) {
            ret.add(SIPNode.t(element.attributes().toString()));
        } else {
            String value = element.attr(attrKey);
            if (StringUtils.isNotBlank(value)) {
                ret.add(SIPNode.t(value));
            }
        }
    }

    @Override
    public String getName() {
        return "@";
    }
}
