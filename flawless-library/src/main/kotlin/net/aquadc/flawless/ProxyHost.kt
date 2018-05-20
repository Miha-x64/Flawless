package net.aquadc.flawless

import android.support.v4.app.Fragment
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.screen.Screen


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
