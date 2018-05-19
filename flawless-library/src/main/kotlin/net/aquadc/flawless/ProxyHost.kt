package net.aquadc.flawless

import android.support.v4.app.Fragment
import net.aquadc.flawless.androidView.Host


interface SupportFragmentHost : Host {
    val fragment: Fragment
}

class ProxyHost(
        private val host: Host
) : Host by host

class ProxySupportFragmentHost(
        private val host: SupportFragmentHost
) : Host by host, SupportFragmentHost {
    override val fragment: Fragment
        get() = host.fragment
}
