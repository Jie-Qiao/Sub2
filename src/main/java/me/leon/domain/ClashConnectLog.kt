package me.leon.domain

data class ClashConnectLog(
    val proxies: LinkedHashMap<String, Group>
)


data class Group(
    val all: List<String> = mutableListOf(),
    val history: List<History> = mutableListOf(),
    val name: String?,
    val now: String?,
    val type: String?
) {
    val isNode
        get() = type !in listOf("Selector", "URLTest", "Reject", "Direct")
}

data class History(
    val time: String = "",
    val delay: Int = 0
)




