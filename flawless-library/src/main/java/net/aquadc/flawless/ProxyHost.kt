package net.aquadc.flawless

import net.aquadc.flawless.androidView.Host


class ProxyHost(
        private val host: Host
) : Host by host
