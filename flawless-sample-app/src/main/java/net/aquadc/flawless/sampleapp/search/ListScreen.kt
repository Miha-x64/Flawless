package net.aquadc.flawless.sampleapp.search

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.implementMe.StatelessScreen
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.properties.Property


class ListScreen<T, VH : RecyclerView.ViewHolder>(
        private val listProp: Property<List<T>>,
        private val create: (ViewGroup) -> VH,
        private val bind: VH.(T) -> Unit
) : StatelessScreen<ParcelUnit, ParcelUnit, Host, ViewGroup?, View> {

    private lateinit var host: Host

    private lateinit var adapter: RecyclerView.Adapter<VH>
    private val onChange: (List<T>, List<T>) -> Unit = { _, _ ->
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(host: Host, arg: ParcelUnit, state: ParcelUnit?) {
        this.host = host
    }

    override fun createView(host: Host, parent: ViewGroup?, arg: ParcelUnit, state: ParcelUnit?): View =
            RecyclerView(parent!!.context).apply {
                layoutManager = LinearLayoutManager(parent.context)
                object : RecyclerView.Adapter<VH>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = create(parent)
                    override fun onBindViewHolder(holder: VH, position: Int) {
                        val data = listProp.value[position]
                        bind(holder, data)
                    }
                    override fun getItemCount(): Int = listProp.value.size
                }.let {
                    this@ListScreen.adapter = it
                    this@apply.adapter = it
                }
            }

    override fun onViewCreated(host: Host, view: View, arg: ParcelUnit, state: ParcelUnit?) {
        listProp.addChangeListener(onChange)
    }

    override fun onViewDestroyed(host: Host) {
        listProp.removeChangeListener(onChange)
    }

    override fun onDestroy(host: Host) {
    }

}
