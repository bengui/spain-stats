package me.benguiman.spainstats.domain.models

interface Operation {
    val key: Int
}

object AdrhOperation : Operation {
    override val key: Int = 353
}

object IpvaOperation : Operation {
    override val key: Int = 432
}