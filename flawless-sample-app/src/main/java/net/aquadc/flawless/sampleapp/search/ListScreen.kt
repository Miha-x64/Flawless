package net.aquadc.flawless.sampleapp.search

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import net.aquadc.flawless.androidView.ActionSupportFragment
import net.aquadc.flawless.androidView.SupportFragment
import net.aquadc.flawless.androidView.startActivity
import net.aquadc.flawless.implementMe.StatelessActionSupportFragScreen
import net.aquadc.flawless.parcel.ParcelUnit
import net.aquadc.flawless.parcel.pureParcelFunction3
import net.aquadc.properties.Property
import org.jetbrains.anko.support.v4.toast
import java.net.URLEncoder


class ListScreen<T, VH : RecyclerView.ViewHolder>(
        private val listProp: Property<List<T>>,
        private val create: (ViewGroup) -> VH,
        private val bind: VH.(T) -> Unit
) : StatelessActionSupportFragScreen {

    private lateinit var host: ActionSupportFragment

    private lateinit var adapter: RecyclerView.Adapter<VH>
    private val onChange: (List<T>, List<T>) -> Unit = { _, _ ->
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(host: SupportFragment<ParcelUnit, ParcelUnit>, arg: ParcelUnit, state: ParcelUnit?) {
        this.host = host
    }

    override fun createView(host: SupportFragment<ParcelUnit, ParcelUnit>, parent: ViewGroup?, arg: ParcelUnit, state: ParcelUnit?): View =
            RecyclerView(host.activity).apply {
                layoutManager = LinearLayoutManager(host.activity)
                object : RecyclerView.Adapter<VH>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = create(parent)
                    override fun onBindViewHolder(holder: VH, position: Int) {
                        val data = listProp.value[position]
                        bind(holder, data)
                        holder.itemView.setOnClickListener { // such onClick impl is not good, just a sample
                            host.exchange.startActivity(
                                    this@ListScreen,
                                    Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("http://www.google.com/?q=" + URLEncoder.encode(data.toString(), "UTF-8"))
                                    ),
                                    1,
                                    pureParcelFunction3(ListScreen<*, *>::onResult)
                            )
                        }
                    }
                    override fun getItemCount(): Int = listProp.value.size
                }.let {
                    this@ListScreen.adapter = it
                    this@apply.adapter = it
                }
            }

    override fun onViewCreated(host: SupportFragment<ParcelUnit, ParcelUnit>, view: View, arg: ParcelUnit, state: ParcelUnit?) {
        listProp.addChangeListener(onChange)
    }

    override fun onViewDestroyed(host: SupportFragment<ParcelUnit, ParcelUnit>) {
        listProp.removeChangeListener(onChange)
    }

    override fun onDestroy(host: SupportFragment<ParcelUnit, ParcelUnit>) {
    }

    // ClassCastException will happen on decorator (SearchScreen) on result delivery, see #19
    internal fun onResult(responseCode: Int, data: Intent?) {
        host.toast("won't happen")
    }

}
