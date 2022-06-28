package com.nowcoder.community.util;

public interface CommunityConstant {

    /*
    激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /*
    重复激活
     */
    int ACTIVATION_REPEATE = 1;

    /*
    激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /*
    默认状态的登录凭证超时时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /*
    记住我状态下的登录凭证超时时间
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 12 * 30;

    /*
    实体类型——帖子——1
     */
    int ENTITY_TYPE_POST = 1;
    /*
    实体类型——评论——2
     */
    int ENTIRY_TYPE_COMMENT = 2;
}
