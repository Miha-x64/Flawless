package net.aquadc.flawless.sampleapp.search

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.Host
import net.aquadc.flawless.screen.StatelessScreen
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.properties.Property


class ListScreen<T, VH : RecyclerView.ViewHolder>(
        private val listProp: Property<List<T>>,
        private val create: (ViewGroup) -> VH,
        private val bind: VH.(T) -> Unit
) : StatelessScreen<ParcelUnit, ParcelUnit, Host, ViewGroup?, View> {

    private val adapter: RecyclerView.Adapter<VH> = object : RecyclerView.Adapter<VH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = create(parent)
        override fun onBindViewHolder(holder: VH, position: Int) {
            val data = listProp.value[position]
            bind(holder, data)
        }
        override fun getItemCount(): Int = listProp.value.size
    }

    private val onChange: (List<T>, List<T>) -> Unit = { _, _ ->
        adapter.notifyDataSetChanged()
    }

    override fun createView(parent: ViewGroup?): View =
            RecyclerView(parent!!.context).apply {
                layoutManager = LinearLayoutManager(parent.context)
                this@apply.adapter = this@ListScreen.adapter
            }

    override fun viewAttached(view: View) {
        listProp.addChangeListener(onChange)
    }

    override fun disposeView() {
        listProp.removeChangeListener(onChange)
    }

    override fun destroy() {
    }

}
