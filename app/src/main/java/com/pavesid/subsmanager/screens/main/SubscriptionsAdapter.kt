package com.pavesid.subsmanager.screens.main

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.pavesid.subsmanager.R
import com.pavesid.subsmanager.models.Subscription
import com.pavesid.subsmanager.utils.clearHours
import com.pavesid.subsmanager.utils.setInitialDateTime
import java.util.Calendar

class SubscriptionsAdapter(private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<SubscriptionsAdapter.ItemViewHolder>() {
    private val subscriptions: MutableList<Subscription> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layout = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_TYPE -> SubscriptionsViewHolder(
                layout.inflate(
                    R.layout.subscription_card,
                    parent,
                    false
                )
            )
            else -> HeaderViewHolder(layout.inflate(R.layout.header_card, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (subscriptions[position].type) {
            0 -> ITEM_TYPE
            else -> HEADER_TYPE
        }
    }

    override fun getItemCount(): Int = subscriptions.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(subscriptions[position], position, listener)
    }

    @MainThread
    fun update(data: List<Subscription>) {
        subscriptions.clear()
        subscriptions.addAll(data)
        notifyDataSetChanged()
    }

    companion object {
        private const val ITEM_TYPE = 0
        private const val HEADER_TYPE = 1
    }

    abstract inner class ItemViewHolder(convertView: View) : RecyclerView.ViewHolder(convertView) {

        @MainThread
        abstract fun bind(subscription: Subscription, position: Int, listener: (Int) -> Unit)
    }

    inner class SubscriptionsViewHolder(itemView: View) : ItemViewHolder(itemView) {
        private val titleView = itemView.findViewById<TextView>(R.id.card_title)
        private val priceView = itemView.findViewById<TextView>(R.id.card_price)
        private val startView = itemView.findViewById<TextView>(R.id.card_start)
        private val endView = itemView.findViewById<TextView>(R.id.card_end)
        private val cardView = itemView.findViewById<ConstraintLayout>(R.id.card_view)
        private val daysLeftView = itemView.findViewById<TextView>(R.id.card_days_left)
        private val warningView = itemView.findViewById<ImageView>(R.id.card_warning)
        private val errorView = itemView.findViewById<ImageView>(R.id.card_error)

        private val today = Calendar.getInstance().clearHours().timeInMillis

        @MainThread
        override fun bind(subscription: Subscription, position: Int, listener: (Int) -> Unit) {

            val daysLeft = ((subscription.ended.toLong() - today) / DateUtils.DAY_IN_MILLIS).toInt()

            when {
                daysLeft < 0 -> {
                    errorView.visibility = View.VISIBLE
                    warningView.visibility = View.GONE
                }
                daysLeft < 3 -> {
                    errorView.visibility = View.GONE
                    warningView.visibility = View.VISIBLE
                }
                else -> {
                    errorView.visibility = View.GONE
                    warningView.visibility = View.GONE
                }
            }

            itemView.setOnClickListener {
                listener(position)
            }

            titleView.text = subscription.title
            priceView.text = "${subscription.price} $ "
            startView.setInitialDateTime(itemView.context, subscription.started.toLong())
            endView.setInitialDateTime(itemView.context, subscription.ended.toLong())

            daysLeftView.text = daysLeft.toString()

            cardView.setBackgroundResource(subscription.color)
        }
    }

    inner class HeaderViewHolder(itemView: View) : ItemViewHolder(itemView) {
        private val priceView = itemView.findViewById<TextView>(R.id.card_price)
        private val endView = itemView.findViewById<TextView>(R.id.card_end)
        private val cardView = itemView.findViewById<ConstraintLayout>(R.id.card_view)
        private val daysLeftView = itemView.findViewById<TextView>(R.id.card_days_left)
        private val warningView = itemView.findViewById<ImageView>(R.id.card_warning)
        private val errorView = itemView.findViewById<ImageView>(R.id.card_error)
        private val needView = itemView.findViewById<TextView>(R.id.card_cost)

        private val today = Calendar.getInstance().clearHours().timeInMillis

        @MainThread
        override fun bind(subscription: Subscription, position: Int, listener: (Int) -> Unit) {

            val daysLeft = ((subscription.ended.toLong() - today) / DateUtils.DAY_IN_MILLIS).toInt()

            priceView.text = "${subscription.price} $ "
            needView.text =  "${subscription.needPrice} $ "
            endView.setInitialDateTime(itemView.context, subscription.ended.toLong())

            daysLeftView.text = if (subscription.ended.toLong() != 0L) {
                when {
                    daysLeft < 0 -> {
                        errorView.visibility = View.VISIBLE
                        warningView.visibility = View.GONE
                    }
                    daysLeft < 3 -> {
                        errorView.visibility = View.GONE
                        warningView.visibility = View.VISIBLE
                    }
                    else -> {
                        errorView.visibility = View.GONE
                        warningView.visibility = View.GONE
                    }
                }
                daysLeft.toString()
            } else {
                "0"
            }


            cardView.setBackgroundResource(R.drawable.custom_five)
        }
    }
}