package cz.skala.trezorwallet.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import cz.skala.trezorwallet.data.entity.Transaction
import cz.skala.trezorwallet.data.entity.TransactionWithInOut

/**
 * A transaction DAO.
 */
@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE account = :account")
    fun getByAccount(account: Int): List<TransactionWithInOut>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(transaction: Transaction)
}