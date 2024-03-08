package com.enation.itbuilder.image2code.api;

import lombok.Getter;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
@Getter
public enum CodeType {

    bootstrap("Bootstrap"),
    vue_elementui("Vue + Element UI"),
    vue_uniapp("Vue + UniApp"),
    vue_tailwind("Vue + Tailwind"),
    react_tailwind("React + Tailwind");

    private final String displayText;

    CodeType(String displayText) {
        this.displayText = displayText;
    }

}
