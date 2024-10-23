package org.test.teamproject_back.dto.response;

import lombok.Builder;
import lombok.Data;
import org.test.teamproject_back.entity.Cart;
import org.test.teamproject_back.entity.Order;

import java.util.List;

@Builder
@Data
public class RespOrderDto {
    private Long userId;
    private String name;
    private String email;
    private Long point;
    private Long addressId;
    private String address;
    private String detailAddress;
    private String title;
    private String thumbnailImg;
}
