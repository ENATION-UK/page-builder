package com.enation.itbuilder.image2code.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 1.0.0
 **/

/**
 * Represents the error body when an OpenAI request fails
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiError {

    public OpenAiErrorDetails error;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OpenAiErrorDetails {
        /**
         * Human-readable error message
         */
        String message;

        /**
         * OpenAI error type, for example "invalid_request_error"
         * https://platform.openai.com/docs/guides/error-codes/python-library-error-types
         */
        String type;

        String param;

        /**
         * OpenAI error code, for example "invalid_api_key"
         */
        String code;
    }
}
