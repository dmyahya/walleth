package org.walleth.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import org.ligi.kaxtui.alert
import org.walleth.data.networks.NetworkDefinitionProvider
import org.walleth.data.networks.getNetworkDefinitionByChainID


// TODO - handle cancellation of dialog

fun Context.chainIDAlert(networkDefinitionProvider: NetworkDefinitionProvider,
                         chainId: Long?,
                         continuationWithWrongChainId: () -> Unit = {},
                         continuationWithCorrectOrNullChainId: () -> Unit) {

    if (chainId == null || chainId == networkDefinitionProvider.getCurrent().chain.id) {
        continuationWithCorrectOrNullChainId()
    } else {
        val networkToSwitchTo = getNetworkDefinitionByChainID(chainId)

        if (networkToSwitchTo == null) {
            alert("WallETH is not supporting chain with id $chainId", "", DialogInterface.OnClickListener { _: DialogInterface, _: Int ->
                continuationWithWrongChainId()
            })

            return
        }


        AlertDialog.Builder(this)
                .setMessage("wrong chainID - do you want to switch?")
                .setPositiveButton(android.R.string.yes, { _, _ ->
                    networkDefinitionProvider.setCurrent(networkToSwitchTo)
                    continuationWithCorrectOrNullChainId()
                })
                .setNegativeButton(android.R.string.no, { _, _ ->
                    continuationWithWrongChainId()
                })
                .setCancelable(false)
                .show()

    }
}