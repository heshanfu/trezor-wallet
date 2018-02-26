package cz.skala.trezorwallet.ui.transactions

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.SupportFragmentInjector
import cz.skala.trezorwallet.R
import cz.skala.trezorwallet.data.entity.TransactionWithInOut
import kotlinx.android.synthetic.main.fragment_transactions.*


/**
 * A fragment for transactions list.
 */
class TransactionsFragment : Fragment(), SupportFragmentInjector {
    companion object {
        val ARG_ACCOUNT_ID = "account_id"
    }

    override val injector = KodeinInjector()
    private val viewModel: TransactionsViewModel by injector.instance()

    private val adapter = TransactionsAdapter()

    override fun provideOverridingModule() = Kodein.Module {
        bind<TransactionsViewModel>() with provider {
            val factory = TransactionsViewModel.Factory(instance(), instance())
            ViewModelProviders.of(this@TransactionsFragment, factory)[TransactionsViewModel::class.java]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeInjector()

        val args = arguments ?: return
        viewModel.start(args.getString(ARG_ACCOUNT_ID))

        viewModel.transactions.observe(this, Observer {
            if (it != null) {
                showTransactions(it)
            }
        })

        viewModel.refreshing.observe(this, Observer {
            swipeRefreshLayout.isRefreshing = (it == true)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchTransactions()
        }
    }

    override fun onDestroy() {
        destroyInjector()
        super.onDestroy()
    }

    private fun showTransactions(transactions: List<TransactionWithInOut>) {
        Log.d("TransactionsFragment", "showTransactions " + transactions.size)
        adapter.transactions = transactions
        adapter.notifyDataSetChanged()
    }
}