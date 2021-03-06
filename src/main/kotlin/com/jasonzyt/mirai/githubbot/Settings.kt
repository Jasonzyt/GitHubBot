package com.jasonzyt.mirai.githubbot

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Settings : ReadOnlyPluginConfig("settings") {
    @Serializable
    class RegexSettings {
        val regex: String = ""
        val keys: Map<String, Int> = mapOf()
    }
    @Serializable
    class ReplySettings {
        val enabled: Boolean = false
        val message: String? = null
        val matches: List<RegexSettings> = listOf()
    }
    @Serializable
    class ForwardSettings {
        val enabled: Boolean = false
        val message: String? = null
    }
    @Serializable
    class GroupSettings {
        val enabled: Boolean = false
        val defaultRepo: String? = null
        val webhookRepos: List<String>? = null
        val ignoresMembers: List<Long>? = null
        val forward: Map<String, ForwardSettings>? = null
        val reply: Map<String, ReplySettings>? = null
        val dailySummary: Boolean = true
    }
    @ValueDescription("群聊消息设置")
    val groups: Map<String, GroupSettings> by value()
    @ValueDescription("PAT设置(在这里生成: https://github.com/settings/tokens)")
    val restApiToken: String by value("user:personal-access-token")
    @ValueDescription("Webhook钩子服务器监听端口")
    val webhookPort: Int by value(23333)
    @ValueDescription("Webhook服务器请求路径")
    val webhookPath: String by value("/hooks")
    @ValueDescription("Webhook签名密钥(空字符串代表不使用签名)")
    val webhookSecret: String by value("")
    @ValueDescription("全局默认仓库")
    val defaultRepo: String by value("")
    @ValueDescription("全局Webhook事件转发设置")
    val forward: Map<String, ForwardSettings> by value()
    @ValueDescription("全局回复设置")
    val reply: Map<String, ReplySettings> by value()
    @ValueDescription("全局忽略成员列表(这些成员的发言将不会触发回复)")
    val ignoresMembers: List<Long> by value()
    @ValueDescription("是否启用截图功能(需要前置插件)")
    val enableScreenshot: Boolean by value(true)

    fun getGroupSettings(groupId: Long): GroupSettings? {
        return groups[groupId.toString()]
    }

    fun getGroupReplySettings(groupId: Long, event: String): ReplySettings? {
        val groupSettings = getGroupSettings(groupId) ?: return reply[event]
        if (groupSettings.reply == null) {
            return reply[event]
        }
        return groupSettings.reply?.get(event)
    }

    fun getGroupDefaultRepo(groupId: Long): String? {
        val groupSettings = getGroupSettings(groupId) ?: return null
        return if (groupSettings.defaultRepo == null) defaultRepo
               else groupSettings.defaultRepo
    }

    fun isGroupEnabled(groupId: Long): Boolean {
        val groupSettings = getGroupSettings(groupId)
        return groupSettings?.enabled ?: false
    }

}