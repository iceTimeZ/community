package edu.tongji.community.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class DiscussPost {

    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    private int commentCount;
    private double score;
}
