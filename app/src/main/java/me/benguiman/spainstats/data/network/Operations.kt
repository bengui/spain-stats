package me.benguiman.spainstats.data.network

interface Operation {
    val key: Int
}

object AdrhOperation : Operation {
    override val key: Int = 353
}

object IpvaOperation : Operation {
    override val key: Int = 432
}