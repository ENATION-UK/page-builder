package com.enation.itbuilder.image2code.api;

import lombok.Builder;
import lombok.Data;

/**
 * websocket 响应
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 1.0.0
 **/
@Data
@Builder
public class WsResponse {
    private String status;
    private String body;
}
