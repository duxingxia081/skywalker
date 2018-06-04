package com.skywalker.core.web

import java.util.*


/**
 * 推送单播通知
 * @author
 * @Date 2017年3月2日
 * @Desc
 */
class BaiduPush {

    fun pushNotificationByUser(
        channelId: String,
        deviceType: Int?, message: String, map: Map<String, Any>?
    ): Array<String> {
        val results = arrayOfNulls<String>(3)
        val retCode = "1"
        val retMsg = ""
        try {
            // 1. 设置developer平台的ApiKey/SecretKey，需到百度推送注册
            var apiKey = ""
            var secretKey = ""
            if (deviceType != null && deviceType!!.toInt() == 4) {// ios
                apiKey = Constants.baiduIOSApiKey
                secretKey = Constants.baiduIOSSecretKey
            } else if (deviceType == 3) {//Android
                apiKey = Constants.baiduAndroidApiKey
                secretKey = Constants.baiduAndroidSecretKey
            }
            // 2. 创建PushKeyPair
            val pair = PushKeyPair(apiKey, secretKey)
            //创建BaiduPushClient，访问SDK接口
            val pushClient = BaiduPushClient(
                pair,
                BaiduPushConstants.CHANNEL_REST_URL
            )

            // 3. 注册YunLogHandler，获取本次请求的交互信息
            pushClient.setChannelLogHandler(object : YunLogHandler() {
                fun onHandle(event: YunLogEvent) {
                    System.out.println(event.getMessage())
                }
            })
            val notification = JSONObject()
            if (deviceType == 4) {
                val jsonAPS = JSONObject()
                jsonAPS.put("alert", message)
                jsonAPS.put("sound", "ttt")
                notification.put("aps", jsonAPS)
                notification.put("title", "XXX")
                //                    notification.put("key2", "value2");这里用于用户自定义

                if (map != null && !map!!.isEmpty()) {
                    val customContentMapKeys = map!!.keys
                    val irt = customContentMapKeys.iterator()
                    while (irt.hasNext()) {
                        val key = irt.next() as String
                        val value = map!![key] as String
                        notification.put(key, value)
                    }
                }
            } else if (deviceType == 3) {
                notification.put("title", "XXX")
                notification.put("description", message)
                notification.put("notification_builder_id", 0)
                notification.put("notification_basic_style", 4)
                notification.put("open_type", 1)
                notification.put("url", "http://push.baidu.com")//app上点开推送之后跳转的链接
                val jsonCustormCont = JSONObject()
                jsonCustormCont.put("name", "test") //自定义内容，key-value
                notification.put("custom_content", jsonCustormCont)
                if (map != null && !map!!.isEmpty()) {//map中放的是用户自定义内容
                    val customContentMapKeys = map!!.keys
                    val irt = customContentMapKeys.iterator()
                    while (irt.hasNext()) {
                        val key = irt.next() as String
                        val value = map!![key] as String
                        notification.put(key, value)
                    }
                }
            }
            // 4. 创建请求类对象
            val request = PushMsgToSingleDeviceRequest()
                .addChannelId(channelId)
                .addTopicId(apiKey)
                .addMsgExpires(3600). // message有效时间
                    addMessageType(1).// 1：通知,0:透传消息. 默认为0 注：IOS只有通知.
                    addMessage(notification.toString()).addDeviceType(deviceType)// deviceType => 3:android, 4:ios

            // 5. 调用pushMessage接口
            val response = pushClient.pushMsgToSingleDevice(request)

            // 6. 认证推送成功
            System.out.println(
                "msgId: " + response.getMsgId()
                        + ",sendTime: " + response.getSendTime()
            )
        } catch (e: PushClientException) {
            //ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,
            //'true' 表示抛出, 'false' 表示捕获。
            if (BaiduPushConstants.ERROROPTTYPE) {
                try {
                    throw e
                } catch (e1: PushClientException) {
                    e1.printStackTrace()
                }

            } else {
                e.printStackTrace()
            }
        } catch (e: PushServerException) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                try {
                    throw e
                } catch (e1: PushServerException) {
                    e1.printStackTrace()
                }

            } else {
                println(
                    String.format(
                        "requestId: %d, errorCode: %d, errorMsg: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()
                    )
                )
            }
        } finally {
            results[0] = retCode
            results[1] = retMsg
        }

        return results
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val baiduPush = BaiduPush()
            val map = HashMap<String, Any>()
            baiduPush.pushNotificationByUser("clientChannelId", 3, "今天天气不错", map)
        }
    }

}