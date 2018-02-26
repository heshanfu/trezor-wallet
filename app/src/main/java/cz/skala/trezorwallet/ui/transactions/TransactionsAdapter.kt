package cz.skala.trezorwallet.ui.transactions

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.skala.trezorwallet.R
import cz.skala.trezorwallet.data.entity.Transaction
import cz.skala.trezorwallet.data.entity.TransactionWithInOut
import kotlinx.android.synthetic.main.item_transaction.view.*

/**
 * Transactions list adapter.
 */
class TransactionsAdapter : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {
    var transactions: List<TransactionWithInOut> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(transaction: TransactionWithInOut) = with(itemView) {
            txtDateTime.text = transaction.tx.getBlockTimeFormatted(resources)
            txtLabel.text = transaction.tx.txid
            val sign = if (transaction.tx.type == Transaction.Type.RECV) "+" else "−"
            val value = if (transaction.tx.type == Transaction.Type.RECV) transaction.tx.value
                else transaction.tx.value + transaction.tx.fee
            var valueFormatted = formatValue(value)
            txtValueBtc.text = sign + valueFormatted + " BTC"
            val colorRes = when (transaction.tx.type) {
                Transaction.Type.RECV -> R.color.colorPrimary
                else -> R.color.colorRed
            }
            txtValueBtc.setTextColor(resources.getColor(colorRes))
            txtValueUsd.visibility = View.GONE
        }

        private fun formatValue(value: Double): String {
            var str = java.lang.String.format("%.8f", value)
            var endIndex = str.length
            val dotIndex = str.indexOf(".")
            while (str[endIndex - 1] == '0' && endIndex - 1 - dotIndex > 2) {
                endIndex--
            }
            return str.substring(0, endIndex)
        }
    }
}
