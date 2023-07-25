package com.kob.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer user_id;
    private Integer rating;
    private Integer waiting_time; //等待时间
}
